package com.vn.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.GioHangNhanVien;

@Repository
public interface GioHangNhanVienRepository extends JpaRepository<GioHangNhanVien, Long> {
    Optional<GioHangNhanVien> findFirstByNhanVienIdAndTrangThaiOrderByNgayCapNhatDesc(Long nhanVienId,
            Integer trangThai);

    java.util.List<GioHangNhanVien> findByNhanVienIdAndTrangThaiOrderByNgayCapNhatDesc(Long nhanVienId,
            Integer trangThai);

    Optional<GioHangNhanVien> findByIdAndNhanVienId(Long id, Long nhanVienId);
}
