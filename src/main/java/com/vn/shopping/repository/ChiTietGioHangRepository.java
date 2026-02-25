package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietGioHang;

import java.util.Optional;

@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Long> {

    Optional<ChiTietGioHang> findByGioHangMaGioHangAndChiTietSanPhamId(Long maGioHang, Long chiTietSanPhamId);
}
