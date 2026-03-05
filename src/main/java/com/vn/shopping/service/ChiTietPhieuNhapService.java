package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.PhieuNhap;
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

    public ChiTietPhieuNhap create(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapRepository.save(chiTietPhieuNhap);
    }

    public ChiTietPhieuNhap create(Long phieuNhapId, Long chiTietSanPhamId, Integer soLuong,
            String ghiTru, String ghiTruKiemHang, Integer trangThai) throws IdInvalidException {
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
        ct.setSoLuong(soLuong);
        ct.setGhiTru(ghiTru);
        ct.setGhiTruKiemHang(ghiTruKiemHang);
        ct.setTrangThai(trangThai);

        if (phieuNhapId != null) {
            PhieuNhap pn = phieuNhapRepository.findById(phieuNhapId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + phieuNhapId));
            ct.setPhieuNhap(pn);
        }
        if (chiTietSanPhamId != null) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + chiTietSanPhamId));
            ct.setChiTietSanPham(ctsp);
        }

        return chiTietPhieuNhapRepository.save(ct);
    }

    public ChiTietPhieuNhap update(ChiTietPhieuNhap chiTietPhieuNhap) throws IdInvalidException {
        ChiTietPhieuNhap existing = chiTietPhieuNhapRepository.findById(chiTietPhieuNhap.getId())
                .orElseThrow(() -> new IdInvalidException(
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
