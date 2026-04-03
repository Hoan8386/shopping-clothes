package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;
import com.vn.shopping.repository.KhuyenMaiTheoHoaDonRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class KhuyenMaiTheoHoaDonService {

    private final KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository;

    public KhuyenMaiTheoHoaDonService(KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository) {
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
    }

    private void validateKhuyenMai(KhuyenMaiTheoHoaDon khuyenMai) throws IdInvalidException {
        if (khuyenMai.getPhanTramGiam() == null || khuyenMai.getPhanTramGiam() < 0
                || khuyenMai.getPhanTramGiam() > 100) {
            throw new IdInvalidException("Phần trăm giảm phải nằm trong khoảng 0 đến 100");
        }
        if (khuyenMai.getGiamToiDa() == null || khuyenMai.getGiamToiDa() < 0) {
            throw new IdInvalidException("Giảm tối đa không được âm");
        }
        if (khuyenMai.getHoaDonToiThieu() == null || khuyenMai.getHoaDonToiThieu() < 0) {
            throw new IdInvalidException("Hóa đơn tối thiểu không được âm");
        }
        if (khuyenMai.getSoLuong() == null || khuyenMai.getSoLuong() < 0) {
            throw new IdInvalidException("Số lượng không được âm");
        }
        if (khuyenMai.getThoiGianBatDau() != null && khuyenMai.getThoiGianKetThuc() != null
                && !khuyenMai.getThoiGianBatDau().isBefore(khuyenMai.getThoiGianKetThuc())) {
            throw new IdInvalidException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }
    }

    public KhuyenMaiTheoHoaDon create(KhuyenMaiTheoHoaDon khuyenMai) throws IdInvalidException {
        validateKhuyenMai(khuyenMai);
        return khuyenMaiTheoHoaDonRepository.save(khuyenMai);
    }

    public KhuyenMaiTheoHoaDon update(KhuyenMaiTheoHoaDon khuyenMai) throws IdInvalidException {
        validateKhuyenMai(khuyenMai);
        KhuyenMaiTheoHoaDon existing = khuyenMaiTheoHoaDonRepository.findById(khuyenMai.getId()).orElse(null);
        if (existing == null) {
            throw new IdInvalidException("Không tìm thấy khuyến mãi theo hóa đơn: " + khuyenMai.getId());
        }
        existing.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
        existing.setGiamToiDa(khuyenMai.getGiamToiDa());
        existing.setHoaDonToiThieu(khuyenMai.getHoaDonToiThieu());
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
