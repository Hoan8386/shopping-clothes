package com.vn.shopping.service;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.response.ResCreateUserDTO;
import com.vn.shopping.repository.KhachHangRepository;

@Service
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    public KhachHangService(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }

    public KhachHang findByEmail(String email) {
        return khachHangRepository.findByEmail(email).orElse(null);
    }

    public boolean isEmailExist(String email) {
        return khachHangRepository.existsByEmail(email);
    }

    public KhachHang handleCreateUser(KhachHang khachHang) {
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
