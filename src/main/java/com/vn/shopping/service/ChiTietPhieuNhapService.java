package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.domain.request.ReqChiTietPhieuNhapDTO;
import com.vn.shopping.domain.response.ResChiTietPhieuNhapDTO;
import com.vn.shopping.repository.ChiTietPhieuNhapRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.PhieuNhapRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class ChiTietPhieuNhapService {

    private static final int TRANG_THAI_DA_DAT = 0;
    private static final int TRANG_THAI_DA_NHAN = 1;
    private static final int TRANG_THAI_CHAM_GIAO = 2;
    private static final int TRANG_THAI_HUY = 3;
    private static final int TRANG_THAI_THIEU_HANG = 4;
    private static final int TRANG_THAI_HOAN_THANH = 5;

    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public ChiTietPhieuNhapService(ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
            PhieuNhapRepository phieuNhapRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public ChiTietPhieuNhap create(ReqChiTietPhieuNhapDTO dto) throws IdInvalidException {
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
        ct.setSoLuong(dto.getSoLuong());
        ct.setSoLuongThieu(dto.getSoLuongThieu());
        ct.setGhiTru(dto.getGhiTru());
        ct.setGhiTruKiemHang(dto.getGhiTruKiemHang());
        ct.setTrangThai(dto.getTrangThai());

        if (dto.getPhieuNhapId() != null) {
            PhieuNhap pn = phieuNhapRepository.findById(dto.getPhieuNhapId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + dto.getPhieuNhapId()));
            // Chỉ được thêm chi tiết khi phiếu chưa nhận hoặc đang chậm giao
            Integer tt = pn.getTrangThai();
            if (tt != null && (tt == TRANG_THAI_HUY || tt == TRANG_THAI_DA_NHAN
                    || tt == TRANG_THAI_THIEU_HANG || tt == TRANG_THAI_HOAN_THANH)) {
                throw new IdInvalidException(
                        "Không thể thêm chi tiết cho phiếu nhập ở trạng thái: " + getTrangThaiText(tt));
            }
            ct.setPhieuNhap(pn);
        }
        if (dto.getChiTietSanPhamId() != null) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(dto.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + dto.getChiTietSanPhamId()));
            ct.setChiTietSanPham(ctsp);
        }

        return chiTietPhieuNhapRepository.save(ct);
    }

    public ChiTietPhieuNhap update(ReqChiTietPhieuNhapDTO dto) throws IdInvalidException {
        ChiTietPhieuNhap existing = chiTietPhieuNhapRepository.findById(dto.getId())
                .orElseThrow(() -> new IdInvalidException(
                        "Không tìm thấy chi tiết phiếu nhập: " + dto.getId()));

        // Kiểm tra trạng thái phiếu nhập cha tương ứng
        PhieuNhap phieuNhap = existing.getPhieuNhap();
        if (phieuNhap != null) {
            Integer tt = phieuNhap.getTrangThai();
            if (tt != null && (tt == TRANG_THAI_HOAN_THANH || tt == TRANG_THAI_HUY)) {
                throw new IdInvalidException(
                        "Không thể cập nhật chi tiết phiếu nhập khi phiếu đã ở trạng thái: "
                                + getTrangThaiText(tt));
            }
        }

        existing.setSoLuong(dto.getSoLuong());
        existing.setSoLuongThieu(dto.getSoLuongThieu());
        existing.setGhiTru(dto.getGhiTru());
        existing.setGhiTruKiemHang(dto.getGhiTruKiemHang());
        existing.setTrangThai(dto.getTrangThai());

        if (dto.getPhieuNhapId() != null) {
            PhieuNhap pn = phieuNhapRepository.findById(dto.getPhieuNhapId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + dto.getPhieuNhapId()));
            existing.setPhieuNhap(pn);
        }
        if (dto.getChiTietSanPhamId() != null) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(dto.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + dto.getChiTietSanPhamId()));
            existing.setChiTietSanPham(ctsp);
        }

        return chiTietPhieuNhapRepository.save(existing);
    }

    public void delete(long id) {
        chiTietPhieuNhapRepository.deleteById(id);
    }

    public ChiTietPhieuNhap findById(long id) {
        return chiTietPhieuNhapRepository.findById(id).orElse(null);
    }

    public List<ChiTietPhieuNhap> findAll() {
        return chiTietPhieuNhapRepository.findAll();
    }

    public List<ChiTietPhieuNhap> findByPhieuNhapId(long phieuNhapId) {
        return chiTietPhieuNhapRepository.findByPhieuNhapId(phieuNhapId);
    }

    public ResChiTietPhieuNhapDTO convertToDTO(ChiTietPhieuNhap ct) {
        ResChiTietPhieuNhapDTO dto = new ResChiTietPhieuNhapDTO();
        dto.setId(ct.getId());
        dto.setSoLuong(ct.getSoLuong());
        dto.setSoLuongThieu(ct.getSoLuongThieu());
        dto.setSoLuongDaNhap(ct.getSoLuongDaNhap());
        dto.setGhiTru(ct.getGhiTru());
        dto.setGhiTruKiemHang(ct.getGhiTruKiemHang());
        dto.setTrangThai(ct.getTrangThai());
        if (ct.getTrangThai() != null) {
            dto.setTrangThaiText(ct.getTrangThai() == 0 ? "Đủ" : "Thiếu");
        }
        dto.setNgayTao(ct.getNgayTao());
        dto.setNgayCapNhat(ct.getNgayCapNhat());

        if (ct.getPhieuNhap() != null) {
            dto.setPhieuNhapId(ct.getPhieuNhap().getId());
            dto.setTenPhieuNhap(ct.getPhieuNhap().getTenPhieuNhap());
        }

        if (ct.getChiTietSanPham() != null) {
            ChiTietSanPham ctsp = ct.getChiTietSanPham();
            ResChiTietPhieuNhapDTO.ChiTietSanPhamDTO ctspDTO = new ResChiTietPhieuNhapDTO.ChiTietSanPhamDTO();
            ctspDTO.setId(ctsp.getId());
            ctspDTO.setSoLuong(ctsp.getSoLuong());
            if (ctsp.getSanPham() != null) {
                ctspDTO.setTenSanPham(ctsp.getSanPham().getTenSanPham());
            }
            if (ctsp.getMauSac() != null) {
                ctspDTO.setTenMauSac(ctsp.getMauSac().getTenMauSac());
            }
            if (ctsp.getKichThuoc() != null) {
                ctspDTO.setTenKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
            }
            dto.setChiTietSanPham(ctspDTO);
        }

        return dto;
    }

    private String getTrangThaiText(Integer trangThai) {
        if (trangThai == null)
            return "Không xác định";
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
