package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.repository.ChiTietSanPhamRepository;

@Service
public class ChiTietSanPhamService {

    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public ChiTietSanPhamService(ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public ChiTietSanPham create(ChiTietSanPham chiTietSanPham) {
        return chiTietSanPhamRepository.save(chiTietSanPham);
    }

    public ChiTietSanPham update(ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham existing = chiTietSanPhamRepository.findById(chiTietSanPham.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm: " + chiTietSanPham.getId()));
        existing.setSanPham(chiTietSanPham.getSanPham());
        existing.setKichThuoc(chiTietSanPham.getKichThuoc());
        existing.setMauSac(chiTietSanPham.getMauSac());
        existing.setSku(chiTietSanPham.getSku());
        return chiTietSanPhamRepository.save(existing);
    }

    public void delete(long id) {
        chiTietSanPhamRepository.deleteById(id);
    }

    public ChiTietSanPham findById(long id) {
        return chiTietSanPhamRepository.findById(id).orElse(null);
    }

    public List<ChiTietSanPham> findAll() {
        return chiTietSanPhamRepository.findAll();
    }

    public List<ChiTietSanPham> findBySanPhamId(long sanPhamId) {
        return chiTietSanPhamRepository.findBySanPhamId(sanPhamId);
    }
}
