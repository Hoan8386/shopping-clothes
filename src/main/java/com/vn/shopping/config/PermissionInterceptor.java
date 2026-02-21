package com.vn.shopping.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.Permission;
import com.vn.shopping.domain.Role;
import com.vn.shopping.service.KhachHangService;
import com.vn.shopping.service.NhanVienService;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.error.PermissionException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    NhanVienService nhanVienService;

    @Autowired
    KhachHangService khachHangService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        if (email != null && !email.isEmpty()) {
            Role role = null;

            NhanVien nhanVien = nhanVienService.findByEmail(email);
            if (nhanVien != null) {
                role = nhanVien.getRole();
            } else {
                KhachHang khachHang = khachHangService.findByEmail(email);
                if (khachHang != null) {
                    role = khachHang.getRole();
                }
            }

            if (role != null) {
                List<Permission> permissions = role.getPermissions();
                boolean isAllow = permissions.stream()
                        .anyMatch(item -> item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                if (!isAllow) {
                    throw new PermissionException("Bạn không có quyền truy cập endpoint này.");
                }
            } else {
                throw new PermissionException("Bạn không có quyền truy cập endpoint này.");
            }
        }
        return true;
    }
}
