package com.vn.shopping.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Verification_Token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khachhang_id")
    private KhachHang khachHang;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    public VerificationToken() {
    }

    public VerificationToken(String token, KhachHang khachHang, LocalDateTime expiryDate) {
        this.token = token;
        this.khachHang = khachHang;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
