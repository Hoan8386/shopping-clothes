package com.vn.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.LoaiDonLuanChuyen;

@Repository
public interface LoaiDonLuanChuyenRepository extends JpaRepository<LoaiDonLuanChuyen, Long> {
    Optional<LoaiDonLuanChuyen> findFirstByTenLoaiContainingIgnoreCase(String tenLoai);
}
