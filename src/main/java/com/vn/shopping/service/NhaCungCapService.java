package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.repository.NhaCungCapRepository;

@Service
public class NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    public NhaCungCapService(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    public NhaCungCap create(NhaCungCap nhaCungCap) {
        return nhaCungCapRepository.save(nhaCungCap);
    }

    public NhaCungCap update(NhaCungCap nhaCungCap) {
        NhaCungCap existing = nhaCungCapRepository.findById(nhaCungCap.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp: " + nhaCungCap.getId()));
        existing.setTenNhaCungCap(nhaCungCap.getTenNhaCungCap());
        existing.setSoDienThoai(nhaCungCap.getSoDienThoai());
        existing.setEmail(nhaCungCap.getEmail());
        existing.setDiaChi(nhaCungCap.getDiaChi());
        existing.setGhiTru(nhaCungCap.getGhiTru());
        existing.setTrangThai(nhaCungCap.getTrangThai());
        return nhaCungCapRepository.save(existing);
    }

    public void delete(long id) {
        nhaCungCapRepository.deleteById(id);
    }

    public NhaCungCap findById(long id) {
        return nhaCungCapRepository.findById(id).orElse(null);
    }

    public List<NhaCungCap> findAll() {
        return nhaCungCapRepository.findAll();
    }
}
