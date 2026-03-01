package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhuyenMaiTheoDiem;
import com.vn.shopping.repository.KhuyenMaiTheoDiemRepository;

@Service
public class KhuyenMaiTheoDiemService {

    private final KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository;

    public KhuyenMaiTheoDiemService(KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository) {
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
    }

    public KhuyenMaiTheoDiem create(KhuyenMaiTheoDiem khuyenMai) {
        return khuyenMaiTheoDiemRepository.save(khuyenMai);
    }

    public KhuyenMaiTheoDiem update(KhuyenMaiTheoDiem khuyenMai) {
        KhuyenMaiTheoDiem existing = khuyenMaiTheoDiemRepository.findById(khuyenMai.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi theo điểm: " + khuyenMai.getId()));
        existing.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
        existing.setGiamToiDa(khuyenMai.getGiamToiDa());
        existing.setHoaDonToiDa(khuyenMai.getHoaDonToiDa());
        existing.setPhanTramGiam(khuyenMai.getPhanTramGiam());
        existing.setHinhThuc(khuyenMai.getHinhThuc());
        existing.setThoiGianBatDau(khuyenMai.getThoiGianBatDau());
        existing.setThoiGianKetThuc(khuyenMai.getThoiGianKetThuc());
        existing.setSoLuong(khuyenMai.getSoLuong());
        existing.setTrangThai(khuyenMai.getTrangThai());
        return khuyenMaiTheoDiemRepository.save(existing);
    }

    public void delete(long id) {
        khuyenMaiTheoDiemRepository.deleteById(id);
    }

    public KhuyenMaiTheoDiem findById(long id) {
        return khuyenMaiTheoDiemRepository.findById(id).orElse(null);
    }

    public List<KhuyenMaiTheoDiem> findAll() {
        return khuyenMaiTheoDiemRepository.findAll();
    }
}
