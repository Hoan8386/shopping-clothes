package com.vn.shopping.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.VerificationToken;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final KhachHangRepository khachHangRepository;

    public VerificationTokenService(VerificationTokenRepository tokenRepository,
            KhachHangRepository khachHangRepository) {
        this.tokenRepository = tokenRepository;
        this.khachHangRepository = khachHangRepository;
    }

    public String createVerificationToken(KhachHang khachHang) {
        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setKhachHang(khachHang);
        vt.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(vt);
        return token;
    }

    @Transactional
    public boolean validateVerificationToken(String token) {
        Optional<VerificationToken> opt = tokenRepository.findByToken(token);
        if (opt.isEmpty()) {
            return false;
        }
        VerificationToken vt = opt.get();
        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(vt);
            return false;
        }
        KhachHang kh = vt.getKhachHang();
        kh.setEnabled(1);
        khachHangRepository.save(kh);
        tokenRepository.delete(vt);
        return true;
    }
}
