package com.vn.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqTraHangDTO;
import com.vn.shopping.domain.response.ResTraHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class TraHangService {

    private final TraHangRepository traHangRepository;
    private final ChiTietTraHangRepository chiTietTraHangRepository;
    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final KhachHangRepository khachHangRepository;

    public TraHangService(TraHangRepository traHangRepository,
            ChiTietTraHangRepository chiTietTraHangRepository,
            DonHangRepository donHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            KhachHangRepository khachHangRepository) {
        this.traHangRepository = traHangRepository;
        this.chiTietTraHangRepository = chiTietTraHangRepository;
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.khachHangRepository = khachHangRepository;
    }

    /**
     * Tạo phiếu trả hàng:
     * - Đơn hàng phải ở trạng thái 5 (Đã nhận hàng)
     * - Tính tongTien = tổng tiền đơn hàng (tongTienTra) - tổng giá gốc sản phẩm
     * trả
     * - Trạng thái phiếu trả: 0 = Chờ xử lý
     */
    @Transactional
    public TraHang taoPhieuTraHang(ReqTraHangDTO req) throws IdInvalidException {
        // 1. Validate đơn hàng
        DonHang donHang = donHangRepository.findById(req.getDonHangId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + req.getDonHangId()));

        if (donHang.getTrangThai() != 5) {
            throw new IdInvalidException("Chỉ có thể trả hàng khi đơn hàng ở trạng thái Đã nhận hàng");
        }

        // 2. Kiểm tra quyền: chỉ khách hàng sở hữu đơn hàng mới được trả
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email).orElse(null);
        if (khachHang != null && donHang.getKhachHang() != null
                && !donHang.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Bạn không có quyền trả hàng cho đơn hàng này");
        }

        // 3. Validate chi tiết trả hàng
        if (req.getChiTietTraHangs() == null || req.getChiTietTraHangs().isEmpty()) {
            throw new IdInvalidException("Phải chọn ít nhất một sản phẩm để trả");
        }

        // 4. Tạo phiếu trả hàng
        TraHang traHang = new TraHang();
        traHang.setDonHang(donHang);
        traHang.setLyDoTraHang(req.getLyDoTraHang());
        traHang.setTrangThai(0); // 0 = Chờ xử lý

        // 5. Tạo chi tiết trả hàng và tính tổng giá gốc sản phẩm trả
        List<ChiTietTraHang> chiTietList = new ArrayList<>();
        double tongGiaGocSanPhamTra = 0;

        for (ReqTraHangDTO.ChiTietTraHangItem item : req.getChiTietTraHangs()) {
            ChiTietDonHang ctdh = chiTietDonHangRepository.findById(item.getChiTietDonHangId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết đơn hàng: " + item.getChiTietDonHangId()));

            // Kiểm tra chi tiết đơn hàng thuộc đơn hàng đang trả
            if (!ctdh.getDonHang().getId().equals(donHang.getId())) {
                throw new IdInvalidException("Chi tiết đơn hàng không thuộc đơn hàng này");
            }

            ChiTietTraHang chiTiet = new ChiTietTraHang();
            chiTiet.setTraHang(traHang);
            chiTiet.setSanPhamTra(ctdh);
            chiTiet.setGhiTru(item.getGhiTru());
            chiTiet.setTrangThai(0); // 0 = Chờ xử lý
            chiTietList.add(chiTiet);

            // Giá gốc sản phẩm trả = giaSanPham * soLuong (giá trước giảm)
            tongGiaGocSanPhamTra += ctdh.getGiaSanPham() * ctdh.getSoLuong();
        }

        traHang.setChiTietTraHangs(chiTietList);

        // 6. Tổng tiền trả = tổng tiền đơn hàng đã thanh toán - giá gốc sản phẩm trả
        int tongTienDonHang = donHang.getTongTienTra() != null ? donHang.getTongTienTra() : donHang.getTongTien();
        double tongTienTra = tongTienDonHang - tongGiaGocSanPhamTra;
        traHang.setTongTien(tongTienTra);

        return traHangRepository.save(traHang);
    }

    /**
     * Cập nhật trạng thái phiếu trả hàng
     * 0 = Chờ xử lý → 1 = Đã duyệt | 2 = Từ chối
     */
    @Transactional
    public TraHang capNhatTrangThai(Long id, Integer trangThai) throws IdInvalidException {
        TraHang traHang = traHangRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu trả hàng: " + id));

        if (traHang.getTrangThai() != 0) {
            throw new IdInvalidException("Chỉ có thể cập nhật phiếu trả hàng đang chờ xử lý");
        }

        if (trangThai != 1 && trangThai != 2) {
            throw new IdInvalidException("Trạng thái không hợp lệ. 1 = Đã duyệt, 2 = Từ chối");
        }

        traHang.setTrangThai(trangThai);

        // Cập nhật trạng thái chi tiết trả hàng
        if (traHang.getChiTietTraHangs() != null) {
            for (ChiTietTraHang ct : traHang.getChiTietTraHangs()) {
                ct.setTrangThai(trangThai);

                // Khi duyệt: tăng số lượng sản phẩm trả lại kho
                if (trangThai == 1) {
                    ChiTietDonHang ctdh = ct.getSanPhamTra();
                    if (ctdh != null && ctdh.getChiTietSanPham() != null) {
                        ChiTietSanPham ctsp = ctdh.getChiTietSanPham();
                        int slTra = ctdh.getSoLuong() != null ? ctdh.getSoLuong() : 0;
                        int slHienTai = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
                        ctsp.setSoLuong(slHienTai + slTra);
                        chiTietSanPhamRepository.save(ctsp);
                    }
                }
            }
        }

        return traHangRepository.save(traHang);
    }

    public TraHang findById(Long id) {
        return traHangRepository.findById(id).orElse(null);
    }

    public List<TraHang> findByDonHangId(Long donHangId) {
        return traHangRepository.findByDonHangId(donHangId);
    }

    public ResultPaginationDTO findAll(Pageable pageable) {
        Page<TraHang> page = traHangRepository.findAll(pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);

        List<ResTraHangDTO> dtos = new ArrayList<>();
        for (TraHang th : page.getContent()) {
            dtos.add(convertToDTO(th));
        }
        result.setResult(dtos);
        return result;
    }

    public ResTraHangDTO convertToDTO(TraHang traHang) {
        ResTraHangDTO dto = new ResTraHangDTO();
        dto.setId(traHang.getId());
        dto.setDonHangId(traHang.getDonHang() != null ? traHang.getDonHang().getId() : null);
        dto.setLyDoTraHang(traHang.getLyDoTraHang());
        dto.setTrangThai(mapTrangThai(traHang.getTrangThai()));
        dto.setTongTien(traHang.getTongTien());
        dto.setNgayTao(traHang.getNgayTao());
        dto.setNgayCapNhat(traHang.getNgayCapNhat());

        if (traHang.getChiTietTraHangs() != null) {
            List<ResTraHangDTO.ChiTietTraHangDTO> chiTietDTOs = new ArrayList<>();
            for (ChiTietTraHang ct : traHang.getChiTietTraHangs()) {
                ResTraHangDTO.ChiTietTraHangDTO ctDto = new ResTraHangDTO.ChiTietTraHangDTO();
                ctDto.setId(ct.getId());
                ctDto.setGhiTru(ct.getGhiTru());
                ctDto.setTrangThai(mapTrangThai(ct.getTrangThai()));

                ChiTietDonHang ctdh = ct.getSanPhamTra();
                if (ctdh != null) {
                    ctDto.setChiTietDonHangId(ctdh.getId());
                    ctDto.setGiaSanPham(ctdh.getGiaSanPham());
                    ctDto.setSoLuong(ctdh.getSoLuong());
                    ctDto.setThanhTien(ctdh.getThanhTien());

                    ChiTietSanPham ctsp = ctdh.getChiTietSanPham();
                    if (ctsp != null) {
                        SanPham sp = ctsp.getSanPham();
                        if (sp != null) {
                            ctDto.setTenSanPham(sp.getTenSanPham());
                            ctDto.setHinhAnhChinh(sp.getHinhAnhChinh());
                        }
                        if (ctsp.getMauSac() != null) {
                            ctDto.setTenMauSac(ctsp.getMauSac().getTenMauSac());
                        }
                        if (ctsp.getKichThuoc() != null) {
                            ctDto.setTenKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
                        }
                    }
                }
                chiTietDTOs.add(ctDto);
            }
            dto.setChiTietTraHangs(chiTietDTOs);
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
