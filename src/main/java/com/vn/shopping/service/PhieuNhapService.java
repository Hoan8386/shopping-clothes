package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.repository.PhieuNhapRepository;

@Service
public class PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
    }

    public PhieuNhap create(PhieuNhap phieuNhap) {
        return phieuNhapRepository.save(phieuNhap);
    }

    public PhieuNhap create(String tenPhieuNhap, Long cuaHangId, Long nhaCungCapId,
            Integer trangThai) {
        PhieuNhap pn = new PhieuNhap();
        pn.setTenPhieuNhap(tenPhieuNhap);
        pn.setTrangThai(trangThai);

        if (cuaHangId != null) {
            CuaHang ch = new CuaHang();
            ch.setId(cuaHangId);
            pn.setCuaHang(ch);
        }
        if (nhaCungCapId != null) {
            NhaCungCap ncc = new NhaCungCap();
            ncc.setId(nhaCungCapId);
            pn.setNhaCungCap(ncc);
        }

        return phieuNhapRepository.save(pn);
    }

    public PhieuNhap update(PhieuNhap phieuNhap) {
        PhieuNhap existing = phieuNhapRepository.findById(phieuNhap.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập: " + phieuNhap.getId()));
        existing.setTenPhieuNhap(phieuNhap.getTenPhieuNhap());
        existing.setCuaHang(phieuNhap.getCuaHang());
        existing.setNhaCungCap(phieuNhap.getNhaCungCap());
        existing.setTrangThai(phieuNhap.getTrangThai());
        existing.setNgayGiaoHang(phieuNhap.getNgayGiaoHang());
        existing.setNgayNhanHang(phieuNhap.getNgayNhanHang());
        return phieuNhapRepository.save(existing);
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
}
