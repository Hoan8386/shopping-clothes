package com.vn.shopping.service;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.Role;
import com.vn.shopping.repository.NhanVienRepository;
import com.vn.shopping.repository.RoleRepository;

@Service
public class NhanVienService {

    private final NhanVienRepository nhanVienRepository;
    private final RoleRepository roleRepository;

    public NhanVienService(NhanVienRepository nhanVienRepository, RoleRepository roleRepository) {
        this.nhanVienRepository = nhanVienRepository;
        this.roleRepository = roleRepository;
    }

    public NhanVien handleCreateNhanVien(NhanVien nhanVien) {
        // Tự động gán role NHAN_VIEN (id=3) nếu chưa có role
        if (nhanVien.getRole() == null) {
            Role nhanVienRole = roleRepository.findById(3L).orElse(null);
            nhanVien.setRole(nhanVienRole);
        }
        return nhanVienRepository.save(nhanVien);
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
