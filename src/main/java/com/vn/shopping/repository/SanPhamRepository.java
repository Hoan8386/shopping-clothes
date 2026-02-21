package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.SanPham;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, String> {
}
