package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.HinhAnh;

@Repository
public interface HinhAnhRepository extends JpaRepository<HinhAnh, Long> {
    List<HinhAnh> findByChiTietSanPhamId(Long chiTietSanPhamId);
}
