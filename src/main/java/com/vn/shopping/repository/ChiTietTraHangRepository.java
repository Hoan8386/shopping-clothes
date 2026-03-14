package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietTraHang;

@Repository
public interface ChiTietTraHangRepository extends JpaRepository<ChiTietTraHang, Long> {
    List<ChiTietTraHang> findByTraHangId(Long traHangId);
}
