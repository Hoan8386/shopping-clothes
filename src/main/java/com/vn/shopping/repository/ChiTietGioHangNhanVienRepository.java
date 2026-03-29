package com.vn.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietGioHangNhanVien;

@Repository
public interface ChiTietGioHangNhanVienRepository extends JpaRepository<ChiTietGioHangNhanVien, Long> {
    Optional<ChiTietGioHangNhanVien> findByGioHangNhanVienIdAndChiTietSanPhamId(Long gioHangNhanVienId,
            Long chiTietSanPhamId);
}
