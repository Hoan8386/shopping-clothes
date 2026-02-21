package com.vn.shopping.service;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.repository.NhanVienRepository;

@Service
public class NhanVienService {

    private final NhanVienRepository nhanVienRepository;

    public NhanVienService(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }

    public NhanVien findByEmail(String email) {
        return nhanVienRepository.findByEmail(email).orElse(null);
    }
}
