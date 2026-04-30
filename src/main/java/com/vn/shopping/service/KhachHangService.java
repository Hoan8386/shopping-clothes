package com.vn.shopping.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.Role;
import com.vn.shopping.domain.response.ResCreateUserDTO;
import com.vn.shopping.domain.response.ResKhachHangLookupDTO;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.repository.RoleRepository;
import com.vn.shopping.util.error.IdInvalidException;

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

    public KhachHang findBySdt(String sdt) {
        if (sdt == null || sdt.isBlank()) {
            return null;
        }
        return khachHangRepository.findBySdt(sdt.trim()).orElse(null);
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
            Role khachHangRole = roleRepository.findById(3L).orElse(null);
            khachHang.setRole(khachHangRole);
        }
        // Ensure new user is not enabled until email verification
        khachHang.setEnabled(0);
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

    public KhachHang updatePasswordByEmail(String email, String encodedPassword) throws IdInvalidException {
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));
        khachHang.setPassword(encodedPassword);
        return khachHangRepository.save(khachHang);
    }

    public KhachHang updateProfileByEmail(String email, String tenKhachHang, String sdt, String avatarUrl)
            throws IdInvalidException {
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        if (tenKhachHang != null && !tenKhachHang.isBlank()) {
            khachHang.setTenKhachHang(tenKhachHang.trim());
        }
        if (sdt != null && !sdt.isBlank()) {
            khachHang.setSdt(sdt.trim());
        }
        if (avatarUrl != null && !avatarUrl.isBlank()) {
            khachHang.setAvatar(avatarUrl);
        }

        return khachHangRepository.save(khachHang);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(KhachHang khachHang) {
        ResCreateUserDTO dto = new ResCreateUserDTO();
        dto.setId(khachHang.getId());
        dto.setTenKhachHang(khachHang.getTenKhachHang());
        dto.setEmail(khachHang.getEmail());
        dto.setSdt(khachHang.getSdt());
        return dto;
    }

    public List<ResKhachHangLookupDTO> findAllLookupDTO() {
        return khachHangRepository.findAll().stream()
                .sorted(Comparator.comparing(KhachHang::getId).reversed())
                .map(item -> new ResKhachHangLookupDTO(
                        item.getId(),
                        item.getTenKhachHang(),
                        item.getSdt(),
                        item.getEmail(),
                        item.getDiemTichLuy()))
                .toList();
    }
}
