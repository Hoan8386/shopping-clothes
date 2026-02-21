package com.vn.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.GioHang;
import com.vn.shopping.domain.KhachHang;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Long> {
    Optional<GioHang> findByKhachHang(KhachHang khachHang);
}
