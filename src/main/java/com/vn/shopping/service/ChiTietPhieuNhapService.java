package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.repository.ChiTietPhieuNhapRepository;

@Service
public class ChiTietPhieuNhapService {

    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;

    public ChiTietPhieuNhapService(ChiTietPhieuNhapRepository chiTietPhieuNhapRepository) {
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
    }

    public ChiTietPhieuNhap create(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapRepository.save(chiTietPhieuNhap);
    }

    public ChiTietPhieuNhap create(Long phieuNhapId, Long chiTietSanPhamId, Integer soLuong,
            String ghiTru, String ghiTruKiemHang, Integer trangThai) {
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
        ct.setSoLuong(soLuong);
        ct.setGhiTru(ghiTru);
        ct.setGhiTruKiemHang(ghiTruKiemHang);
        ct.setTrangThai(trangThai);

        if (phieuNhapId != null) {
            PhieuNhap pn = new PhieuNhap();
            pn.setId(phieuNhapId);
            ct.setPhieuNhap(pn);
        }
        if (chiTietSanPhamId != null) {
            ChiTietSanPham ctsp = new ChiTietSanPham();
            ctsp.setId(chiTietSanPhamId);
            ct.setChiTietSanPham(ctsp);
        }

        return chiTietPhieuNhapRepository.save(ct);
    }

    public ChiTietPhieuNhap update(ChiTietPhieuNhap chiTietPhieuNhap) {
        ChiTietPhieuNhap existing = chiTietPhieuNhapRepository.findById(chiTietPhieuNhap.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy chi tiết phiếu nhập: " + chiTietPhieuNhap.getId()));
        existing.setPhieuNhap(chiTietPhieuNhap.getPhieuNhap());
        existing.setChiTietSanPham(chiTietPhieuNhap.getChiTietSanPham());
        existing.setSoLuong(chiTietPhieuNhap.getSoLuong());
        existing.setGhiTru(chiTietPhieuNhap.getGhiTru());
        existing.setGhiTruKiemHang(chiTietPhieuNhap.getGhiTruKiemHang());
        existing.setTrangThai(chiTietPhieuNhap.getTrangThai());
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
}
