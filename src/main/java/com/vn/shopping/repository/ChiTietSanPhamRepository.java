package com.vn.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietSanPham;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, String> {
}
