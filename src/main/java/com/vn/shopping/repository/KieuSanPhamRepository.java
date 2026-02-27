package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.KieuSanPham;

@Repository
public interface KieuSanPhamRepository extends JpaRepository<KieuSanPham, Long> {
}
