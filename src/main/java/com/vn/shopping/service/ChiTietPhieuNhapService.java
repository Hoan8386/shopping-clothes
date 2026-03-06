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
}
