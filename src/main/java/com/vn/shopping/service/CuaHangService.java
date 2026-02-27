package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.repository.CuaHangRepository;

@Service
public class CuaHangService {

    private final CuaHangRepository cuaHangRepository;

    public CuaHangService(CuaHangRepository cuaHangRepository) {
        this.cuaHangRepository = cuaHangRepository;
    }

    public CuaHang create(CuaHang cuaHang) {
        return cuaHangRepository.save(cuaHang);
    }

    public CuaHang update(CuaHang cuaHang) {
        CuaHang existing = cuaHangRepository.findById(cuaHang.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng: " + cuaHang.getId()));
        existing.setTenCuaHang(cuaHang.getTenCuaHang());
        existing.setDiaChi(cuaHang.getDiaChi());
        existing.setViTri(cuaHang.getViTri());
        existing.setSoDienThoai(cuaHang.getSoDienThoai());
        existing.setEmail(cuaHang.getEmail());
        existing.setTrangThai(cuaHang.getTrangThai());
        return cuaHangRepository.save(existing);
    }

    public void delete(long id) {
        cuaHangRepository.deleteById(id);
    }

    public CuaHang findById(long id) {
        return cuaHangRepository.findById(id).orElse(null);
    }

    public List<CuaHang> findAll() {
        return cuaHangRepository.findAll();
    }
}
