package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;
import com.vn.shopping.repository.KhuyenMaiTheoHoaDonRepository;

@Service
public class KhuyenMaiTheoHoaDonService {

    private final KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository;

    public KhuyenMaiTheoHoaDonService(KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository) {
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
    }

    public KhuyenMaiTheoHoaDon create(KhuyenMaiTheoHoaDon khuyenMai) {
        return khuyenMaiTheoHoaDonRepository.save(khuyenMai);
    }

    public KhuyenMaiTheoHoaDon update(KhuyenMaiTheoHoaDon khuyenMai) {
        KhuyenMaiTheoHoaDon existing = khuyenMaiTheoHoaDonRepository.findById(khuyenMai.getId())
                .orElseThrow(
                        () -> new RuntimeException("Không tìm thấy khuyến mãi theo hóa đơn: " + khuyenMai.getId()));
        existing.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
        existing.setGiamToiDa(khuyenMai.getGiamToiDa());
        existing.setHoaDonToiDa(khuyenMai.getHoaDonToiDa());
        existing.setPhanTramGiam(khuyenMai.getPhanTramGiam());
        existing.setHinhThuc(khuyenMai.getHinhThuc());
        existing.setThoiGianBatDau(khuyenMai.getThoiGianBatDau());
        existing.setThoiGianKetThuc(khuyenMai.getThoiGianKetThuc());
        existing.setSoLuong(khuyenMai.getSoLuong());
        existing.setTrangThai(khuyenMai.getTrangThai());
        return khuyenMaiTheoHoaDonRepository.save(existing);
    }

    public void delete(long id) {
        khuyenMaiTheoHoaDonRepository.deleteById(id);
    }

    public KhuyenMaiTheoHoaDon findById(long id) {
        return khuyenMaiTheoHoaDonRepository.findById(id).orElse(null);
    }

    public List<KhuyenMaiTheoHoaDon> findAll() {
        return khuyenMaiTheoHoaDonRepository.findAll();
    }
}
