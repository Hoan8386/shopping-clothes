package com.vn.shopping.service;

import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.Role;
import com.vn.shopping.domain.response.ResNhanVienDTO;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.repository.NhanVienRepository;
import com.vn.shopping.repository.RoleRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class NhanVienService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern GMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");

    private final NhanVienRepository nhanVienRepository;
    private final RoleRepository roleRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;

    public NhanVienService(NhanVienRepository nhanVienRepository, RoleRepository roleRepository,
            KhachHangRepository khachHangRepository, PasswordEncoder passwordEncoder) {
        this.nhanVienRepository = nhanVienRepository;
        this.roleRepository = roleRepository;
        this.khachHangRepository = khachHangRepository;
        this.passwordEncoder = passwordEncoder;
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

    public NhanVien handleCreateNhanVien(NhanVien nhanVien) throws IdInvalidException {
        validateNhanVienInput(nhanVien, true, null);

        nhanVien.setEmail(nhanVien.getEmail().trim().toLowerCase(Locale.ROOT));
        nhanVien.setSoDienThoai(nhanVien.getSoDienThoai().trim());
        nhanVien.setMatKhau(passwordEncoder.encode(nhanVien.getMatKhau().trim()));

        // Tự động gán role NHAN_VIEN (id=3) nếu chưa có role
        if (nhanVien.getRole() == null) {
            nhanVien.setRole(getDefaultNhanVienRole());
        }
        return nhanVienRepository.save(nhanVien);
    }

    public NhanVien update(NhanVien nhanVien) throws IdInvalidException {
        NhanVien existing = nhanVienRepository.findById(nhanVien.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên: " + nhanVien.getId()));

        validateNhanVienInput(nhanVien, false, existing.getId());

        existing.setTenNhanVien(nhanVien.getTenNhanVien());
        existing.setEmail(nhanVien.getEmail().trim().toLowerCase(Locale.ROOT));
        existing.setSoDienThoai(nhanVien.getSoDienThoai().trim());
        if (nhanVien.getMatKhau() != null && !nhanVien.getMatKhau().isBlank()) {
            existing.setMatKhau(passwordEncoder.encode(nhanVien.getMatKhau().trim()));
        }
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

    private void validateNhanVienInput(NhanVien nhanVien, boolean isCreate, Long currentNhanVienId)
            throws IdInvalidException {
        String tenNhanVien = nhanVien.getTenNhanVien() == null ? "" : nhanVien.getTenNhanVien().trim();
        String email = nhanVien.getEmail() == null ? "" : nhanVien.getEmail().trim().toLowerCase(Locale.ROOT);
        String soDienThoai = nhanVien.getSoDienThoai() == null ? "" : nhanVien.getSoDienThoai().trim();
        String matKhau = nhanVien.getMatKhau() == null ? "" : nhanVien.getMatKhau().trim();

        if (tenNhanVien.isEmpty()) {
            throw new IdInvalidException("Tên nhân viên không được để trống");
        }
        if (email.isEmpty()) {
            throw new IdInvalidException("Email không được để trống");
        }
        if (!GMAIL_PATTERN.matcher(email).matches()) {
            throw new IdInvalidException("Email phải có định dạng @gmail.com");
        }
        if (soDienThoai.isEmpty()) {
            throw new IdInvalidException("Số điện thoại không được để trống");
        }
        if (!PHONE_PATTERN.matcher(soDienThoai).matches()) {
            throw new IdInvalidException("Số điện thoại phải gồm đúng 10 chữ số");
        }

        if (isCreate) {
            if (matKhau.isEmpty()) {
                throw new IdInvalidException("Mật khẩu không được để trống");
            }
            if (matKhau.length() < 6) {
                throw new IdInvalidException("Mật khẩu phải có ít nhất 6 ký tự");
            }
        } else if (!matKhau.isEmpty() && matKhau.length() < 6) {
            throw new IdInvalidException("Mật khẩu phải có ít nhất 6 ký tự");
        }

        NhanVien nhanVienByEmail = nhanVienRepository.findByEmail(email).orElse(null);
        if (nhanVienByEmail != null && !nhanVienByEmail.getId().equals(currentNhanVienId)) {
            throw new IdInvalidException("Email nhân viên đã tồn tại");
        }

        KhachHang khachHangByEmail = khachHangRepository.findByEmail(email).orElse(null);
        if (khachHangByEmail != null) {
            throw new IdInvalidException("Email đã tồn tại ở tài khoản khách hàng");
        }

        boolean isPhoneUsedByAnotherNhanVien = nhanVienRepository.findAll().stream()
                .anyMatch(item -> item.getSoDienThoai() != null
                        && soDienThoai.equals(item.getSoDienThoai().trim())
                        && !item.getId().equals(currentNhanVienId));
        if (isPhoneUsedByAnotherNhanVien) {
            throw new IdInvalidException("Số điện thoại nhân viên đã tồn tại");
        }

        KhachHang khachHangByPhone = khachHangRepository.findBySdt(soDienThoai).orElse(null);
        if (khachHangByPhone != null) {
            throw new IdInvalidException("Số điện thoại đã tồn tại ở tài khoản khách hàng");
        }
    }
}
