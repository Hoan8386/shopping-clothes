package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.SanPham;
import com.vn.shopping.repository.SanPhamRepository;

@Service
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    public SanPham create(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    public SanPham update(SanPham sanPham) {
        SanPham existing = sanPhamRepository.findById(sanPham.getMaSanPham())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + sanPham.getMaSanPham()));

        existing.setTenSanPham(sanPham.getTenSanPham());
        existing.setGiaVon(sanPham.getGiaVon());
        existing.setGiaBan(sanPham.getGiaBan());
        existing.setKieuSanPham(sanPham.getKieuSanPham());
        existing.setNhaCungCap(sanPham.getNhaCungCap());
        existing.setBoSuuTap(sanPham.getBoSuuTap());
        existing.setThuongHieu(sanPham.getThuongHieu());
        existing.setTrangThai(sanPham.getTrangThai());

        return sanPhamRepository.save(existing);
    }

    public void delete(String maSanPham) {
        sanPhamRepository.deleteById(maSanPham);
    }

    public SanPham findById(String maSanPham) {
        return sanPhamRepository.findById(maSanPham).orElse(null);
    }

    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }
}
