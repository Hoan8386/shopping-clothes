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
        pn.setNgayGiaoHang(dto.getNgayGiaoHang());
        pn.setNgayNhanHang(dto.getNgayNhanHang());

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
        if (oldTrangThai != null && oldTrangThai == TRANG_THAI_DA_NHAN) {
            throw new IdInvalidException("Phiếu nhập đã nhận hàng, không thể thay đổi trạng thái");
        }

        // Validate trạng thái hợp lệ (0, 1, 2, 3)
        if (newTrangThai != null && (newTrangThai < 0 || newTrangThai > 3)) {
            throw new IdInvalidException("Trạng thái không hợp lệ. 0: Đã đặt, 1: Đã nhận, 2: Chậm giao, 3: Hủy");
        }

        existing.setTenPhieuNhap(dto.getTenPhieuNhap());
        existing.setTrangThai(newTrangThai);
        existing.setNgayGiaoHang(dto.getNgayGiaoHang());
        existing.setNgayNhanHang(dto.getNgayNhanHang());

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

        // Khi trạng thái chuyển sang "đã nhận" (1) => cập nhật số lượng
        boolean isNewSuccess = (newTrangThai != null
                && newTrangThai == TRANG_THAI_DA_NHAN
                && (oldTrangThai == null || oldTrangThai != TRANG_THAI_DA_NHAN));

        if (isNewSuccess) {
            capNhatSoLuongSauNhapHang(saved.getId());
        }

        return saved;
    }

    /**
     * Cập nhật số lượng ChiTietSanPham và tổng SanPham sau khi nhập hàng thành
     * công.
     * - Cộng soLuong từ ChiTietPhieuNhap vào ChiTietSanPham tương ứng
     * - Tính lại tổng soLuong trên SanPham = tổng soLuong của tất cả ChiTietSanPham
     * thuộc sản phẩm đó
     */
    private void capNhatSoLuongSauNhapHang(Long phieuNhapId) {
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

            int soLuongHienTai = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            ctsp.setSoLuong(soLuongHienTai + ctpn.getSoLuong());
            chiTietSanPhamRepository.save(ctsp);

            // Tính lại tổng số lượng sản phẩm
            if (ctsp.getSanPham() != null) {
                capNhatTongSoLuongSanPham(ctsp.getSanPham().getId());
            }
        }
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
            LocalDateTime ngayGiaoHangTu, LocalDateTime ngayGiaoHangDen,
            LocalDateTime ngayNhanHangTu, LocalDateTime ngayNhanHangDen,
            Pageable pageable) {

        Specification<PhieuNhap> spec = PhieuNhapSpecification.filter(
                tenPhieuNhap, trangThai, tenCuaHang, tenNhaCungCap,
                ngayTaoTu, ngayTaoDen, ngayGiaoHangTu, ngayGiaoHangDen,
                ngayNhanHangTu, ngayNhanHangDen);

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
        dto.setNgayGiaoHang(pn.getNgayGiaoHang());
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
        dto.setGhiTru(ctpn.getGhiTru());
        dto.setGhiTruKiemHang(ctpn.getGhiTruKiemHang());
        dto.setTrangThai(ctpn.getTrangThai());

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
            default:
                return "Không xác định";
        }
    }
}
