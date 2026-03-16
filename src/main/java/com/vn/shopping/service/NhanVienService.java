package com.vn.shopping.service;

import java.util.List;
import java.util.Collections;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.Role;
import com.vn.shopping.domain.response.ResNhanVienDTO;
import com.vn.shopping.repository.NhanVienRepository;
import com.vn.shopping.repository.RoleRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class NhanVienService {

    private final NhanVienRepository nhanVienRepository;
    private final RoleRepository roleRepository;

    public NhanVienService(NhanVienRepository nhanVienRepository, RoleRepository roleRepository) {
        this.nhanVienRepository = nhanVienRepository;
        this.roleRepository = roleRepository;
    }

    public List<NhanVien> findAll() {
        return nhanVienRepository.findAll();
    }

    public List<ResNhanVienDTO> findAllVisibleByCurrentUser() throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        NhanVien currentNhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên với email: " + email));

        List<NhanVien> visibleNhanViens;
        if (currentNhanVien.getRole() != null
                && "ADMIN".equalsIgnoreCase(currentNhanVien.getRole().getName())) {
            visibleNhanViens = nhanVienRepository.findAll();
        } else if (currentNhanVien.getCuaHang() != null && currentNhanVien.getCuaHang().getId() != null) {
            visibleNhanViens = nhanVienRepository.findByCuaHang_Id(currentNhanVien.getCuaHang().getId());
        } else {
            visibleNhanViens = Collections.emptyList();
        }

        return visibleNhanViens.stream().map(this::toDTO).toList();
    }

    public NhanVien findById(long id) {
        return nhanVienRepository.findById(id).orElse(null);
    }

    public NhanVien handleCreateNhanVien(NhanVien nhanVien) {
        // Tự động gán role NHAN_VIEN (id=3) nếu chưa có role
        if (nhanVien.getRole() == null) {
            nhanVien.setRole(getDefaultNhanVienRole());
        }
        return nhanVienRepository.save(nhanVien);
    }

    public NhanVien update(NhanVien nhanVien) throws IdInvalidException {
        NhanVien existing = nhanVienRepository.findById(nhanVien.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên: " + nhanVien.getId()));

        existing.setTenNhanVien(nhanVien.getTenNhanVien());
        existing.setEmail(nhanVien.getEmail());
        existing.setSoDienThoai(nhanVien.getSoDienThoai());
        existing.setMatKhau(nhanVien.getMatKhau());
        existing.setNgayBatDauLam(nhanVien.getNgayBatDauLam());
        existing.setNgayKetThucLam(nhanVien.getNgayKetThucLam());
        existing.setTrangThai(nhanVien.getTrangThai());
        existing.setCuaHang(nhanVien.getCuaHang());

        if (nhanVien.getRole() != null) {
            existing.setRole(nhanVien.getRole());
        }

        return nhanVienRepository.save(existing);
    }

    public void delete(long id) {
        nhanVienRepository.deleteById(id);
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

    public NhanVien updatePasswordByEmail(String email, String encodedPassword) throws IdInvalidException {
        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên với email: " + email));
        nhanVien.setMatKhau(encodedPassword);
        return nhanVienRepository.save(nhanVien);
    }

    public NhanVien updateProfileByEmail(String email, String tenNhanVien, String soDienThoai, String avatarUrl)
            throws IdInvalidException {
        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên với email: " + email));

        if (tenNhanVien != null && !tenNhanVien.isBlank()) {
            nhanVien.setTenNhanVien(tenNhanVien.trim());
        }
        if (soDienThoai != null && !soDienThoai.isBlank()) {
            nhanVien.setSoDienThoai(soDienThoai.trim());
        }
        if (avatarUrl != null && !avatarUrl.isBlank()) {
            nhanVien.setAvatar(avatarUrl);
        }

        return nhanVienRepository.save(nhanVien);
    }

    public ResNhanVienDTO toDTO(NhanVien nhanVien) {
        ResNhanVienDTO dto = new ResNhanVienDTO();
        dto.setId(nhanVien.getId());
        dto.setTenNhanVien(nhanVien.getTenNhanVien());
        dto.setEmail(nhanVien.getEmail());
        dto.setSoDienThoai(nhanVien.getSoDienThoai());
        dto.setNgayBatDauLam(nhanVien.getNgayBatDauLam());
        dto.setNgayKetThucLam(nhanVien.getNgayKetThucLam());
        dto.setTrangThai(nhanVien.getTrangThai());

        if (nhanVien.getCuaHang() != null) {
            ResNhanVienDTO.CuaHangInfo cuaHangInfo = new ResNhanVienDTO.CuaHangInfo();
            cuaHangInfo.setId(nhanVien.getCuaHang().getId());
            cuaHangInfo.setTenCuaHang(nhanVien.getCuaHang().getTenCuaHang());
            cuaHangInfo.setDiaChi(nhanVien.getCuaHang().getDiaChi());
            cuaHangInfo.setSoDienThoai(nhanVien.getCuaHang().getSoDienThoai());
            cuaHangInfo.setEmail(nhanVien.getCuaHang().getEmail());
            cuaHangInfo.setTrangThai(nhanVien.getCuaHang().getTrangThai());
            dto.setCuaHang(cuaHangInfo);
        }

        if (nhanVien.getRole() != null) {
            ResNhanVienDTO.RoleInfo roleInfo = new ResNhanVienDTO.RoleInfo();
            roleInfo.setId(nhanVien.getRole().getId());
            roleInfo.setName(nhanVien.getRole().getName());
            roleInfo.setDescription(nhanVien.getRole().getDescription());
            roleInfo.setActive(nhanVien.getRole().isActive());
            dto.setRole(roleInfo);
        }

        return dto;
    }

    private Role getDefaultNhanVienRole() {
        return roleRepository.findById(3L).orElse(null);
    }
}
