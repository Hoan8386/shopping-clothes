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
        SanPham existing = sanPhamRepository.findById(sanPham.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + sanPham.getId()));

        existing.setTenSanPham(sanPham.getTenSanPham());
        existing.setGiaVon(sanPham.getGiaVon());
        existing.setGiaBan(sanPham.getGiaBan());
        existing.setTrangThai(sanPham.getTrangThai());

        return sanPhamRepository.save(existing);
    }

    public void delete(long id) {
        sanPhamRepository.deleteById(id);
    }

    public SanPham findById(long id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }
}
