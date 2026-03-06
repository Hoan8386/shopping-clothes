package com.vn.shopping.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.request.ReqPhieuNhapDTO;
import com.vn.shopping.domain.response.ResPhieuNhapDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.ChiTietPhieuNhapRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.CuaHangRepository;
import com.vn.shopping.repository.NhaCungCapRepository;
import com.vn.shopping.repository.PhieuNhapRepository;
import com.vn.shopping.repository.SanPhamRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class PhieuNhapService {

    private static final int TRANG_THAI_DA_DAT = 0;
    private static final int TRANG_THAI_DA_NHAN = 1;
    private static final int TRANG_THAI_CHAM_GIAO = 2;
    private static final int TRANG_THAI_HUY = 3;
    private static final int TRANG_THAI_THIEU_HANG = 4;
    private static final int TRANG_THAI_HOAN_THANH = 5;

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CuaHangRepository cuaHangRepository;
    private final NhaCungCapRepository nhaCungCapRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository,
            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            SanPhamRepository sanPhamRepository,
            CuaHangRepository cuaHangRepository,
            NhaCungCapRepository nhaCungCapRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    public PhieuNhap create(ReqPhieuNhapDTO dto) throws IdInvalidException {
        PhieuNhap pn = new PhieuNhap();
        pn.setTenPhieuNhap(dto.getTenPhieuNhap());
        pn.setTrangThai(TRANG_THAI_DA_DAT);
        pn.setNgayDatHang(LocalDateTime.now());

        if (dto.getCuaHangId() != null) {
            CuaHang ch = cuaHangRepository.findById(dto.getCuaHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + dto.getCuaHangId()));
            pn.setCuaHang(ch);
        }
        if (dto.getNhaCungCapId() != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(dto.getNhaCungCapId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhà cung cấp: " + dto.getNhaCungCapId()));
            pn.setNhaCungCap(ncc);
        }

        return phieuNhapRepository.save(pn);
    }

    @Transactional
    public PhieuNhap update(ReqPhieuNhapDTO dto) throws IdInvalidException {
        PhieuNhap existing = phieuNhapRepository.findById(dto.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + dto.getId()));

        Integer oldTrangThai = existing.getTrangThai();
        Integer newTrangThai = dto.getTrangThai();

        // Không cho phép cập nhật đơn đã hủy
        if (oldTrangThai != null && oldTrangThai == TRANG_THAI_HUY) {
            throw new IdInvalidException("Phiếu nhập đã hủy, không thể cập nhật");
        }

        // Không cho phép thay đổi trạng thái khi đã nhận hàng
        if (oldTrangThai != null && (oldTrangThai == TRANG_THAI_DA_NHAN
                || oldTrangThai == TRANG_THAI_HOAN_THANH
                || oldTrangThai == TRANG_THAI_THIEU_HANG)) {
            throw new IdInvalidException("Phiếu nhập đã nhận hàng, không thể thay đổi trạng thái");
        }

        // Validate trạng thái hợp lệ (0, 1, 2, 3, 4, 5)
        if (newTrangThai != null && (newTrangThai < 0 || newTrangThai > 5)) {
            throw new IdInvalidException(
                    "Trạng thái không hợp lệ. 0: Đã đặt, 1: Đã nhận, 2: Chậm giao, 3: Hủy, 4: Thiếu hàng, 5: Hoàn thành");
        }

        existing.setTenPhieuNhap(dto.getTenPhieuNhap());
        existing.setTrangThai(newTrangThai);

        if (dto.getCuaHangId() != null) {
            CuaHang ch = cuaHangRepository.findById(dto.getCuaHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + dto.getCuaHangId()));
            existing.setCuaHang(ch);
        }
        if (dto.getNhaCungCapId() != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(dto.getNhaCungCapId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhà cung cấp: " + dto.getNhaCungCapId()));
            existing.setNhaCungCap(ncc);
        }

        PhieuNhap saved = phieuNhapRepository.save(existing);

        // Khi trạng thái chuyển sang "đã nhận" (1) => chỉ đánh dấu nhận hàng, chưa kiểm
        // kê
        boolean isNhanHang = (newTrangThai != null
                && newTrangThai == TRANG_THAI_DA_NHAN
                && (oldTrangThai == null || oldTrangThai != TRANG_THAI_DA_NHAN));

        if (isNhanHang) {
            existing.setNgayNhanHang(LocalDateTime.now());
            phieuNhapRepository.save(existing);
        }

        return saved;
    }

    /**
     * Kiểm kê phiếu nhập: chỉ gọi khi phiếu đang ở trạng thái "Đã nhận" (1).
     * - Duyệt từng chi tiết phiếu nhập: trangThai 0 = đủ, 1 = thiếu
     * - Nếu thiếu: số lượng thực nhập = soLuong - soLuongThieu
     * - Nếu đủ: số lượng thực nhập = soLuong
     * - Cập nhật stock vào ChiTietSanPham + tổng SanPham
     * - Nếu có ít nhất 1 chi tiết thiếu → phiếu nhập = 4 (Thiếu hàng)
     * - Nếu tất cả đủ → phiếu nhập = 5 (Hoàn thành)
     */
    @Transactional
    public PhieuNhap kiemKe(Long phieuNhapId) throws IdInvalidException {
        PhieuNhap phieuNhap = phieuNhapRepository.findById(phieuNhapId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + phieuNhapId));

        if (phieuNhap.getTrangThai() == null || phieuNhap.getTrangThai() != TRANG_THAI_DA_NHAN) {
            throw new IdInvalidException("Chỉ có thể kiểm kê phiếu nhập ở trạng thái 'Đã nhận'");
        }

        boolean coThieuHang = capNhatSoLuongSauNhapHang(phieuNhapId);
        if (coThieuHang) {
            phieuNhap.setTrangThai(TRANG_THAI_THIEU_HANG);
        } else {
            phieuNhap.setTrangThai(TRANG_THAI_HOAN_THANH);
        }

        return phieuNhapRepository.save(phieuNhap);
    }

    /**
     * Cập nhật số lượng ChiTietSanPham và tổng SanPham sau khi nhập hàng.
     * - Kiểm tra trạng thái từng chi tiết phiếu nhập: 0 = đủ, 1 = thiếu
     * - Nếu thiếu: số lượng thực nhập = soLuong - soLuongThieu
     * - Nếu đủ: số lượng thực nhập = soLuong
     * - Trả về true nếu có ít nhất 1 chi tiết bị thiếu hàng
     */
    private boolean capNhatSoLuongSauNhapHang(Long phieuNhapId) {
        PhieuNhap phieuNhap = phieuNhapRepository.findById(phieuNhapId).orElse(null);
        if (phieuNhap == null)
            return false;

        boolean coThieuHang = false;
        List<ChiTietPhieuNhap> danhSachChiTiet = chiTietPhieuNhapRepository.findByPhieuNhapId(phieuNhapId);

        for (ChiTietPhieuNhap ctpn : danhSachChiTiet) {
            if (ctpn.getChiTietSanPham() == null || ctpn.getSoLuong() == null || ctpn.getSoLuong() <= 0) {
                continue;
            }

            // Cập nhật số lượng chi tiết sản phẩm
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(ctpn.getChiTietSanPham().getId())
                    .orElse(null);
            if (ctsp == null) {
                continue;
            }

            int soLuongDat = ctpn.getSoLuong();
            int soLuongThucNhap = soLuongDat;

            // Nếu chi tiết phiếu nhập bị thiếu hàng (trangThai = 1)
            if (ctpn.getTrangThai() != null && ctpn.getTrangThai() == 1) {
                coThieuHang = true;
                int soLuongThieu = ctpn.getSoLuongThieu() != null ? ctpn.getSoLuongThieu() : 0;
                soLuongThucNhap = soLuongDat - soLuongThieu;
                if (soLuongThucNhap < 0) {
                    soLuongThucNhap = 0;
                }
            }

            int soLuongHienTai = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            ctsp.setSoLuong(soLuongHienTai + soLuongThucNhap);

            // Cập nhật mã phiếu nhập và mã cửa hàng
            ctsp.setMaPhieuNhap(phieuNhapId);
            if (phieuNhap.getCuaHang() != null) {
                ctsp.setMaCuaHang(phieuNhap.getCuaHang().getId());
            }

            chiTietSanPhamRepository.save(ctsp);

            // Tính lại tổng số lượng sản phẩm
            if (ctsp.getSanPham() != null) {
                capNhatTongSoLuongSanPham(ctsp.getSanPham().getId());
            }
        }
        return coThieuHang;
    }

    /**
     * Tính lại tổng số lượng sản phẩm = tổng soLuong của tất cả ChiTietSanPham
     * thuộc sản phẩm đó
     */
    private void capNhatTongSoLuongSanPham(Long sanPhamId) {
        SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
        if (sanPham == null) {
            return;
        }

        List<ChiTietSanPham> danhSachChiTiet = chiTietSanPhamRepository.findBySanPhamId(sanPhamId);
        int tongSoLuong = danhSachChiTiet.stream()
                .mapToInt(ct -> ct.getSoLuong() != null ? ct.getSoLuong() : 0)
                .sum();

        sanPham.setSoLuong(tongSoLuong);
        sanPhamRepository.save(sanPham);
    }

    public void delete(long id) {
        phieuNhapRepository.deleteById(id);
    }

    public PhieuNhap findById(long id) {
        return phieuNhapRepository.findById(id).orElse(null);
    }

    public List<PhieuNhap> findAll() {
        return phieuNhapRepository.findAll();
    }

    public ResultPaginationDTO filterPhieuNhap(
            String tenPhieuNhap, Integer trangThai,
            String tenCuaHang, String tenNhaCungCap,
            LocalDateTime ngayTaoTu, LocalDateTime ngayTaoDen,
            LocalDateTime ngayDatHangTu, LocalDateTime ngayDatHangDen,
            LocalDateTime ngayNhanHangTu, LocalDateTime ngayNhanHangDen,
            Pageable pageable) {

        Specification<PhieuNhap> spec = PhieuNhapSpecification.filter(
                tenPhieuNhap, trangThai, tenCuaHang, tenNhaCungCap,
                ngayTaoTu, ngayTaoDen,
                ngayDatHangTu, ngayDatHangDen, ngayNhanHangTu, ngayNhanHangDen);

        Page<PhieuNhap> page = phieuNhapRepository.findAll(spec, pageable);

        List<ResPhieuNhapDTO> dtoList = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(dtoList);
        return result;
    }

    // ===== DTO Conversion =====

    public ResPhieuNhapDTO convertToDTO(PhieuNhap pn) {
        ResPhieuNhapDTO dto = new ResPhieuNhapDTO();
        dto.setId(pn.getId());
        dto.setTenPhieuNhap(pn.getTenPhieuNhap());
        dto.setTrangThai(pn.getTrangThai());
        dto.setTrangThaiText(getTrangThaiText(pn.getTrangThai()));
        dto.setNgayDatHang(pn.getNgayDatHang());
        dto.setNgayNhanHang(pn.getNgayNhanHang());
        dto.setNgayTao(pn.getNgayTao());
        dto.setNgayCapNhat(pn.getNgayCapNhat());

        if (pn.getCuaHang() != null) {
            CuaHang ch = pn.getCuaHang();
            dto.setCuaHang(new ResPhieuNhapDTO.CuaHangDTO(
                    ch.getId(), ch.getTenCuaHang(), ch.getDiaChi(), ch.getSoDienThoai()));
        }

        if (pn.getNhaCungCap() != null) {
            NhaCungCap ncc = pn.getNhaCungCap();
            dto.setNhaCungCap(new ResPhieuNhapDTO.NhaCungCapDTO(
                    ncc.getId(), ncc.getTenNhaCungCap(), ncc.getSoDienThoai(), ncc.getEmail()));
        }

        if (pn.getChiTietPhieuNhaps() != null) {
            List<ResPhieuNhapDTO.ChiTietPhieuNhapDTO> chiTietDTOs = pn.getChiTietPhieuNhaps().stream()
                    .map(this::convertChiTietToDTO)
                    .collect(Collectors.toList());
            dto.setChiTietPhieuNhaps(chiTietDTOs);
        }

        return dto;
    }

    private ResPhieuNhapDTO.ChiTietPhieuNhapDTO convertChiTietToDTO(ChiTietPhieuNhap ctpn) {
        ResPhieuNhapDTO.ChiTietPhieuNhapDTO dto = new ResPhieuNhapDTO.ChiTietPhieuNhapDTO();
        dto.setId(ctpn.getId());
        dto.setSoLuong(ctpn.getSoLuong());
        dto.setSoLuongThieu(ctpn.getSoLuongThieu());
        dto.setGhiTru(ctpn.getGhiTru());
        dto.setGhiTruKiemHang(ctpn.getGhiTruKiemHang());
        dto.setTrangThai(ctpn.getTrangThai());
        if (ctpn.getTrangThai() != null) {
            dto.setTrangThaiText(ctpn.getTrangThai() == 0 ? "Đủ" : "Thiếu");
        }

        if (ctpn.getChiTietSanPham() != null) {
            ChiTietSanPham ctsp = ctpn.getChiTietSanPham();
            dto.setChiTietSanPhamId(ctsp.getId());
            if (ctsp.getSanPham() != null) {
                dto.setTenSanPham(ctsp.getSanPham().getTenSanPham());
            }
            if (ctsp.getMauSac() != null) {
                dto.setTenMauSac(ctsp.getMauSac().getTenMauSac());
            }
            if (ctsp.getKichThuoc() != null) {
                dto.setTenKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
            }
        }

        return dto;
    }

    private String getTrangThaiText(Integer trangThai) {
        if (trangThai == null)
            return null;
        switch (trangThai) {
            case TRANG_THAI_DA_DAT:
                return "Đã đặt";
            case TRANG_THAI_DA_NHAN:
                return "Đã nhận";
            case TRANG_THAI_CHAM_GIAO:
                return "Chậm giao";
            case TRANG_THAI_HUY:
                return "Hủy";
            case TRANG_THAI_THIEU_HANG:
                return "Thiếu hàng";
            case TRANG_THAI_HOAN_THANH:
                return "Hoàn thành";
            default:
                return "Không xác định";
        }
    }
}
