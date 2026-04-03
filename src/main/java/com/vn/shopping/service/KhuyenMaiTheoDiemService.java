package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhuyenMaiTheoDiem;
import com.vn.shopping.repository.KhuyenMaiTheoDiemRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class KhuyenMaiTheoDiemService {

    private final KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository;

    public KhuyenMaiTheoDiemService(KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository) {
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
    }

    private void validateKhuyenMai(KhuyenMaiTheoDiem khuyenMai) throws IdInvalidException {
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
        if (khuyenMai.getDiemToiThieu() == null || khuyenMai.getDiemToiThieu() < 0) {
            throw new IdInvalidException("Điểm tối thiểu không được âm");
        }
        if (khuyenMai.getSoLuong() == null || khuyenMai.getSoLuong() < 0) {
            throw new IdInvalidException("Số lượng không được âm");
        }
        if (khuyenMai.getThoiGianBatDau() != null && khuyenMai.getThoiGianKetThuc() != null
                && !khuyenMai.getThoiGianBatDau().isBefore(khuyenMai.getThoiGianKetThuc())) {
            throw new IdInvalidException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }
    }

    public KhuyenMaiTheoDiem create(KhuyenMaiTheoDiem khuyenMai) throws IdInvalidException {
        validateKhuyenMai(khuyenMai);
        return khuyenMaiTheoDiemRepository.save(khuyenMai);
    }

    public KhuyenMaiTheoDiem update(KhuyenMaiTheoDiem khuyenMai) throws IdInvalidException {
        validateKhuyenMai(khuyenMai);
        KhuyenMaiTheoDiem existing = khuyenMaiTheoDiemRepository.findById(khuyenMai.getId()).orElse(null);
        if (existing == null) {
            throw new IdInvalidException("Không tìm thấy khuyến mãi theo điểm: " + khuyenMai.getId());
        }
        existing.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
        existing.setGiamToiDa(khuyenMai.getGiamToiDa());
        existing.setHoaDonToiThieu(khuyenMai.getHoaDonToiThieu());
        existing.setPhanTramGiam(khuyenMai.getPhanTramGiam());
        existing.setHinhThuc(khuyenMai.getHinhThuc());
        existing.setThoiGianBatDau(khuyenMai.getThoiGianBatDau());
        existing.setThoiGianKetThuc(khuyenMai.getThoiGianKetThuc());
        existing.setDiemToiThieu(khuyenMai.getDiemToiThieu());
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
