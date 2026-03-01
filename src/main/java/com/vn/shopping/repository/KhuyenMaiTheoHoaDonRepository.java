package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;

@Repository
public interface KhuyenMaiTheoHoaDonRepository extends JpaRepository<KhuyenMaiTheoHoaDon, Long> {
}
