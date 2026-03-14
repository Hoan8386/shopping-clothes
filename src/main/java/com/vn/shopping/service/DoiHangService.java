package com.vn.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqDoiHangDTO;
import com.vn.shopping.domain.response.ResDoiHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class DoiHangService {

    private final DoiHangRepository doiHangRepository;
    private final ChiTietDoiHangRepository chiTietDoiHangRepository;
    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final KhachHangRepository khachHangRepository;

    public DoiHangService(DoiHangRepository doiHangRepository,
            ChiTietDoiHangRepository chiTietDoiHangRepository,
            DonHangRepository donHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            KhachHangRepository khachHangRepository) {
        this.doiHangRepository = doiHangRepository;
        this.chiTietDoiHangRepository = chiTietDoiHangRepository;
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.khachHangRepository = khachHangRepository;
    }

    /**
     * Tạo phiếu đổi hàng:
     * - Đơn hàng phải ở trạng thái 5 (Đã nhận hàng)
     * - Tính tongTien = chênh lệch giá giữa sản phẩm đổi và sản phẩm trả + giá
     * đơn hàng cũ
     * - Trạng thái phiếu đổi: 0 = Chờ xử lý
     */
    @Transactional
    public DoiHang taoPhieuDoiHang(ReqDoiHangDTO req) throws IdInvalidException {
        // 1. Validate đơn hàng
        DonHang donHang = donHangRepository.findById(req.getDonHangId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + req.getDonHangId()));

        if (donHang.getTrangThai() != 5) {
            throw new IdInvalidException("Chỉ có thể đổi hàng khi đơn hàng ở trạng thái Đã nhận hàng");
        }

        // 2. Kiểm tra quyền: chỉ khách hàng sở hữu đơn hàng mới được đổi
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email).orElse(null);
        if (khachHang != null && donHang.getKhachHang() != null
                && !donHang.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Bạn không có quyền đổi hàng cho đơn hàng này");
        }

        // 3. Validate chi tiết đổi hàng
        if (req.getChiTietDoiHangs() == null || req.getChiTietDoiHangs().isEmpty()) {
            throw new IdInvalidException("Phải chọn ít nhất một sản phẩm để đổi");
        }

        // 4. Tạo phiếu đổi hàng
        DoiHang doiHang = new DoiHang();
        doiHang.setDonHang(donHang);
        doiHang.setGhiTru(req.getGhiTru());
        doiHang.setTrangThai(0); // 0 = Chờ xử lý

        // 5. Tạo chi tiết đổi hàng và tính tổng chênh lệch
        List<ChiTietDoiHang> chiTietList = new ArrayList<>();
        double tongChenhLech = 0;

        for (ReqDoiHangDTO.ChiTietDoiHangItem item : req.getChiTietDoiHangs()) {
            // Validate sản phẩm trả (chi tiết đơn hàng)
            ChiTietDonHang ctdh = chiTietDonHangRepository.findById(item.getChiTietDonHangId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết đơn hàng: " + item.getChiTietDonHangId()));

            if (!ctdh.getDonHang().getId().equals(donHang.getId())) {
                throw new IdInvalidException("Chi tiết đơn hàng không thuộc đơn hàng này");
            }

            // Validate sản phẩm đổi (chi tiết sản phẩm mới)
            ChiTietSanPham ctspDoi = chiTietSanPhamRepository.findById(item.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy sản phẩm đổi: " + item.getChiTietSanPhamId()));

            if (ctspDoi.getSoLuong() == null || ctspDoi.getSoLuong() < ctdh.getSoLuong()) {
                throw new IdInvalidException("Sản phẩm đổi không đủ số lượng trong kho");
            }

            ChiTietDoiHang chiTiet = new ChiTietDoiHang();
            chiTiet.setDoiHang(doiHang);
            chiTiet.setSanPhamTra(ctdh);
            chiTiet.setSanPhamDoi(ctspDoi);
            chiTiet.setGhiTru(item.getGhiTru());
            chiTiet.setTrangThai(0); // 0 = Chờ xử lý
            chiTietList.add(chiTiet);

            // Tính chênh lệch: giá sản phẩm đổi - giá sản phẩm trả (theo số lượng)
            double giaTra = ctdh.getGiaSanPham() * ctdh.getSoLuong();
            double giaDoi = ctspDoi.getSanPham().getGiaBan() * ctdh.getSoLuong();
            tongChenhLech += (giaDoi - giaTra);
        }

        doiHang.setChiTietDoiHangs(chiTietList);

        // 6. Tổng tiền = chênh lệch giá + giá đơn hàng cũ (tongTienTra)
        int tongTienDonHang = donHang.getTongTienTra() != null ? donHang.getTongTienTra() : donHang.getTongTien();
        double tongTien = tongTienDonHang + tongChenhLech;
        doiHang.setTongTien(tongTien);

        return doiHangRepository.save(doiHang);
    }

    /**
     * Cập nhật trạng thái phiếu đổi hàng
     * 0 = Chờ xử lý → 1 = Đã duyệt | 2 = Từ chối
     */
    @Transactional
    public DoiHang capNhatTrangThai(Long id, Integer trangThai) throws IdInvalidException {
        DoiHang doiHang = doiHangRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu đổi hàng: " + id));

        if (doiHang.getTrangThai() != 0) {
            throw new IdInvalidException("Chỉ có thể cập nhật phiếu đổi hàng đang chờ xử lý");
        }

        if (trangThai != 1 && trangThai != 2) {
            throw new IdInvalidException("Trạng thái không hợp lệ. 1 = Đã duyệt, 2 = Từ chối");
        }

        doiHang.setTrangThai(trangThai);

        // Cập nhật trạng thái chi tiết đổi hàng
        if (doiHang.getChiTietDoiHangs() != null) {
            for (ChiTietDoiHang ct : doiHang.getChiTietDoiHangs()) {
                ct.setTrangThai(trangThai);

                // Khi duyệt: cập nhật tồn kho
                if (trangThai == 1) {
                    // Tăng số lượng sản phẩm trả (khách trả lại → nhập kho)
                    ChiTietDonHang ctdh = ct.getSanPhamTra();
                    if (ctdh != null && ctdh.getChiTietSanPham() != null) {
                        ChiTietSanPham ctspTra = ctdh.getChiTietSanPham();
                        int slTra = ctdh.getSoLuong() != null ? ctdh.getSoLuong() : 0;
                        int slHienTaiTra = ctspTra.getSoLuong() != null ? ctspTra.getSoLuong() : 0;
                        ctspTra.setSoLuong(slHienTaiTra + slTra);
                        chiTietSanPhamRepository.save(ctspTra);
                    }

                    // Giảm số lượng sản phẩm đổi (khách nhận mới → xuất kho)
                    ChiTietSanPham ctspDoi = ct.getSanPhamDoi();
                    if (ctspDoi != null && ctdh != null) {
                        int slDoi = ctdh.getSoLuong() != null ? ctdh.getSoLuong() : 0;
                        int slHienTaiDoi = ctspDoi.getSoLuong() != null ? ctspDoi.getSoLuong() : 0;
                        ctspDoi.setSoLuong(slHienTaiDoi - slDoi);
                        chiTietSanPhamRepository.save(ctspDoi);
                    }
                }
            }
        }

        return doiHangRepository.save(doiHang);
    }

    public DoiHang findById(Long id) {
        return doiHangRepository.findById(id).orElse(null);
    }

    public List<DoiHang> findByDonHangId(Long donHangId) {
        return doiHangRepository.findByDonHangId(donHangId);
    }

    public ResultPaginationDTO findAll(Pageable pageable) {
        Page<DoiHang> page = doiHangRepository.findAll(pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);

        List<ResDoiHangDTO> dtos = new ArrayList<>();
        for (DoiHang dh : page.getContent()) {
            dtos.add(convertToDTO(dh));
        }
        result.setResult(dtos);
        return result;
    }

    public ResDoiHangDTO convertToDTO(DoiHang doiHang) {
        ResDoiHangDTO dto = new ResDoiHangDTO();
        dto.setId(doiHang.getId());
        dto.setDonHangId(doiHang.getDonHang() != null ? doiHang.getDonHang().getId() : null);
        dto.setGhiTru(doiHang.getGhiTru());
        dto.setTrangThai(mapTrangThai(doiHang.getTrangThai()));
        dto.setTongTien(doiHang.getTongTien());
        dto.setNgayTao(doiHang.getNgayTao());
        dto.setNgayCapNhat(doiHang.getNgayCapNhat());

        if (doiHang.getChiTietDoiHangs() != null) {
            List<ResDoiHangDTO.ChiTietDoiHangDTO> chiTietDTOs = new ArrayList<>();
            for (ChiTietDoiHang ct : doiHang.getChiTietDoiHangs()) {
                ResDoiHangDTO.ChiTietDoiHangDTO ctDto = new ResDoiHangDTO.ChiTietDoiHangDTO();
                ctDto.setId(ct.getId());
                ctDto.setGhiTru(ct.getGhiTru());
                ctDto.setTrangThai(mapTrangThai(ct.getTrangThai()));

                // Sản phẩm trả (từ chi tiết đơn hàng)
                ChiTietDonHang ctdh = ct.getSanPhamTra();
                if (ctdh != null) {
                    ctDto.setChiTietDonHangId(ctdh.getId());
                    ctDto.setGiaSanPhamTra(ctdh.getGiaSanPham());
                    ctDto.setSoLuongTra(ctdh.getSoLuong());

                    ChiTietSanPham ctspTra = ctdh.getChiTietSanPham();
                    if (ctspTra != null) {
                        SanPham spTra = ctspTra.getSanPham();
                        if (spTra != null) {
                            ctDto.setTenSanPhamTra(spTra.getTenSanPham());
                            ctDto.setHinhAnhSanPhamTra(spTra.getHinhAnhChinh());
                        }
                        if (ctspTra.getMauSac() != null) {
                            ctDto.setMauSacTra(ctspTra.getMauSac().getTenMauSac());
                        }
                        if (ctspTra.getKichThuoc() != null) {
                            ctDto.setKichThuocTra(ctspTra.getKichThuoc().getTenKichThuoc());
                        }
                    }
                }

                // Sản phẩm đổi (sản phẩm mới)
                ChiTietSanPham ctspDoi = ct.getSanPhamDoi();
                if (ctspDoi != null) {
                    ctDto.setChiTietSanPhamId(ctspDoi.getId());
                    SanPham spDoi = ctspDoi.getSanPham();
                    if (spDoi != null) {
                        ctDto.setTenSanPhamDoi(spDoi.getTenSanPham());
                        ctDto.setHinhAnhSanPhamDoi(spDoi.getHinhAnhChinh());
                        ctDto.setGiaSanPhamDoi(spDoi.getGiaBan());
                    }
                    if (ctspDoi.getMauSac() != null) {
                        ctDto.setMauSacDoi(ctspDoi.getMauSac().getTenMauSac());
                    }
                    if (ctspDoi.getKichThuoc() != null) {
                        ctDto.setKichThuocDoi(ctspDoi.getKichThuoc().getTenKichThuoc());
                    }

                    // Chênh lệch giá
                    if (ctdh != null && spDoi != null) {
                        double giaTra = ctdh.getGiaSanPham() * ctdh.getSoLuong();
                        double giaDoi = spDoi.getGiaBan() * ctdh.getSoLuong();
                        ctDto.setChenhLechGia(giaDoi - giaTra);
                    }
                }

                chiTietDTOs.add(ctDto);
            }
            dto.setChiTietDoiHangs(chiTietDTOs);
        }

        return dto;
    }

    private String mapTrangThai(Integer trangThai) {
        if (trangThai == null)
            return "Không xác định";
        return switch (trangThai) {
            case 0 -> "Chờ xử lý";
            case 1 -> "Đã duyệt";
            case 2 -> "Từ chối";
            default -> "Không xác định";
        };
    }
}
