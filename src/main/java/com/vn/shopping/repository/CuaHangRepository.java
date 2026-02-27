package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.CuaHang;

@Repository
public interface CuaHangRepository extends JpaRepository<CuaHang, Long> {
}
