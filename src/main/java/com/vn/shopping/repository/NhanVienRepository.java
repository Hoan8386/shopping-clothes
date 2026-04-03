package com.vn.shopping.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.NhanVien;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {
    Optional<NhanVien> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

    List<NhanVien> findByCuaHang_Id(Long cuaHangId);

    Optional<NhanVien> findByRefreshTokenAndEmail(String refreshToken, String email);
}
