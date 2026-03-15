package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietDonLuanChuyen;

@Repository
public interface ChiTietDonLuanChuyenRepository extends JpaRepository<ChiTietDonLuanChuyen, Long> {
    List<ChiTietDonLuanChuyen> findByDonLuanChuyenId(Long donLuanChuyenId);
}
