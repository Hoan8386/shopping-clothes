package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.LoaiDonLuanChuyen;
import com.vn.shopping.repository.LoaiDonLuanChuyenRepository;

@Service
public class LoaiDonLuanChuyenService {

    private final LoaiDonLuanChuyenRepository loaiDonLuanChuyenRepository;

    public LoaiDonLuanChuyenService(LoaiDonLuanChuyenRepository loaiDonLuanChuyenRepository) {
        this.loaiDonLuanChuyenRepository = loaiDonLuanChuyenRepository;
    }

    public LoaiDonLuanChuyen create(LoaiDonLuanChuyen loaiDonLuanChuyen) {
        return loaiDonLuanChuyenRepository.save(loaiDonLuanChuyen);
    }

    public LoaiDonLuanChuyen update(LoaiDonLuanChuyen loaiDonLuanChuyen) {
        LoaiDonLuanChuyen existing = loaiDonLuanChuyenRepository.findById(loaiDonLuanChuyen.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại đơn luân chuyển: " + loaiDonLuanChuyen.getId()));
        existing.setTenLoai(loaiDonLuanChuyen.getTenLoai());
        existing.setMoTa(loaiDonLuanChuyen.getMoTa());
        return loaiDonLuanChuyenRepository.save(existing);
    }

    public void delete(long id) {
        loaiDonLuanChuyenRepository.deleteById(id);
    }

    public LoaiDonLuanChuyen findById(long id) {
        return loaiDonLuanChuyenRepository.findById(id).orElse(null);
    }

    public List<LoaiDonLuanChuyen> findAll() {
        return loaiDonLuanChuyenRepository.findAll();
    }
}
