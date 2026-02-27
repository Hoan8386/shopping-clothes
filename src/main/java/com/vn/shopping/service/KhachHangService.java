package com.vn.shopping.service;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.Role;
import com.vn.shopping.domain.response.ResCreateUserDTO;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.repository.RoleRepository;

@Service
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final RoleRepository roleRepository;

    public KhachHangService(KhachHangRepository khachHangRepository, RoleRepository roleRepository) {
        this.khachHangRepository = khachHangRepository;
        this.roleRepository = roleRepository;
    }

    public KhachHang findByEmail(String email) {
        return khachHangRepository.findByEmail(email).orElse(null);
    }

    public boolean isEmailExist(String email) {
        return khachHangRepository.existsByEmail(email);
    }

    public boolean isSdtExist(String sdt) {
        return khachHangRepository.existsBySdt(sdt);
    }

    public KhachHang handleCreateUser(KhachHang khachHang) {
        // Tự động gán role KHACH_HANG (id=4) nếu chưa có role
        if (khachHang.getRole() == null) {
            Role khachHangRole = roleRepository.findById(4L).orElse(null);
            khachHang.setRole(khachHangRole);
        }
        return khachHangRepository.save(khachHang);
    }

    public void updateUserToken(String token, String email) {
        KhachHang khachHang = this.findByEmail(email);
        if (khachHang != null) {
            khachHang.setRefreshToken(token);
            khachHangRepository.save(khachHang);
        }
    }

    public KhachHang getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return khachHangRepository.findByRefreshTokenAndEmail(refreshToken, email).orElse(null);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(KhachHang khachHang) {
        ResCreateUserDTO dto = new ResCreateUserDTO();
        dto.setId(khachHang.getId());
        dto.setTenKhachHang(khachHang.getTenKhachHang());
        dto.setEmail(khachHang.getEmail());
        dto.setSdt(khachHang.getSdt());
        return dto;
    }
}
