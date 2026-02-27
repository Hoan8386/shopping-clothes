package com.vn.shopping.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.service.KhachHangService;
import com.vn.shopping.service.NhanVienService;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {

    private final NhanVienService nhanVienService;
    private final KhachHangService khachHangService;

    public UserDetailsCustom(NhanVienService nhanVienService, KhachHangService khachHangService) {
        this.nhanVienService = nhanVienService;
        this.khachHangService = khachHangService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NhanVien nhanVien = nhanVienService.findByEmail(username);
        if (nhanVien != null) {
            return new User(
                    nhanVien.getEmail(),
                    nhanVien.getMatKhau(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        KhachHang khachHang = khachHangService.findByEmail(username);
        if (khachHang != null) {
            return new User(
                    khachHang.getEmail(),
                    khachHang.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + username);
    }

}