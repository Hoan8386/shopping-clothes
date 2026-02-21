package com.vn.shopping.service;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhachHang;
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
}
