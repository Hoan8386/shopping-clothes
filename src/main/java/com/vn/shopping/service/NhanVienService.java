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

    public void updateUserToken(String token, String email) {
        NhanVien nhanVien = this.findByEmail(email);
        if (nhanVien != null) {
            nhanVien.setRefreshToken(token);
            nhanVienRepository.save(nhanVien);
        }
    }

    public NhanVien getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return nhanVienRepository.findByRefreshTokenAndEmail(refreshToken, email).orElse(null);
    }
}
