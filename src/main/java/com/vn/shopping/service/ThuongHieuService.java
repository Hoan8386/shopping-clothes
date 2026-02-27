package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ThuongHieu;
import com.vn.shopping.repository.ThuongHieuRepository;

@Service
public class ThuongHieuService {

    private final ThuongHieuRepository thuongHieuRepository;

    public ThuongHieuService(ThuongHieuRepository thuongHieuRepository) {
        this.thuongHieuRepository = thuongHieuRepository;
    }

    public ThuongHieu create(ThuongHieu thuongHieu) {
        return thuongHieuRepository.save(thuongHieu);
    }

    public ThuongHieu update(ThuongHieu thuongHieu) {
        ThuongHieu existing = thuongHieuRepository.findById(thuongHieu.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu: " + thuongHieu.getId()));
        existing.setTenThuongHieu(thuongHieu.getTenThuongHieu());
        existing.setTrangThaiHoatDong(thuongHieu.getTrangThaiHoatDong());
        existing.setTrangThaiHienThi(thuongHieu.getTrangThaiHienThi());
        return thuongHieuRepository.save(existing);
    }

    public void delete(long id) {
        thuongHieuRepository.deleteById(id);
    }

    public ThuongHieu findById(long id) {
        return thuongHieuRepository.findById(id).orElse(null);
    }

    public List<ThuongHieu> findAll() {
        return thuongHieuRepository.findAll();
    }
}
