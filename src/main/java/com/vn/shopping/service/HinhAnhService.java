package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.HinhAnh;
import com.vn.shopping.repository.HinhAnhRepository;

@Service
public class HinhAnhService {

    private final HinhAnhRepository hinhAnhRepository;

    public HinhAnhService(HinhAnhRepository hinhAnhRepository) {
        this.hinhAnhRepository = hinhAnhRepository;
    }

    public HinhAnh create(HinhAnh hinhAnh) {
        return hinhAnhRepository.save(hinhAnh);
    }

    public HinhAnh update(HinhAnh hinhAnh) {
        HinhAnh existing = hinhAnhRepository.findById(hinhAnh.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hình ảnh: " + hinhAnh.getId()));
        existing.setTenHinhAnh(hinhAnh.getTenHinhAnh());
        existing.setChiTietSanPham(hinhAnh.getChiTietSanPham());
        return hinhAnhRepository.save(existing);
    }

    public void delete(long id) {
        hinhAnhRepository.deleteById(id);
    }

    public HinhAnh findById(long id) {
        return hinhAnhRepository.findById(id).orElse(null);
    }

    public List<HinhAnh> findAll() {
        return hinhAnhRepository.findAll();
    }

    public List<HinhAnh> findByChiTietSanPhamId(long chiTietSanPhamId) {
        return hinhAnhRepository.findByChiTietSanPhamId(chiTietSanPhamId);
    }
}
