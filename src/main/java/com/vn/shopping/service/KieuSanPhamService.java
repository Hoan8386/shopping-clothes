package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KieuSanPham;
import com.vn.shopping.repository.KieuSanPhamRepository;

@Service
public class KieuSanPhamService {

    private final KieuSanPhamRepository kieuSanPhamRepository;

    public KieuSanPhamService(KieuSanPhamRepository kieuSanPhamRepository) {
        this.kieuSanPhamRepository = kieuSanPhamRepository;
    }

    public KieuSanPham create(KieuSanPham kieuSanPham) {
        return kieuSanPhamRepository.save(kieuSanPham);
    }

    public KieuSanPham update(KieuSanPham kieuSanPham) {
        KieuSanPham existing = kieuSanPhamRepository.findById(kieuSanPham.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kiểu sản phẩm: " + kieuSanPham.getId()));
        existing.setTenKieuSanPham(kieuSanPham.getTenKieuSanPham());
        return kieuSanPhamRepository.save(existing);
    }

    public void delete(long id) {
        kieuSanPhamRepository.deleteById(id);
    }

    public KieuSanPham findById(long id) {
        return kieuSanPhamRepository.findById(id).orElse(null);
    }

    public List<KieuSanPham> findAll() {
        return kieuSanPhamRepository.findAll();
    }
}
