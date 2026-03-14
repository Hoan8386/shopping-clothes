package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.DoiHang;

@Repository
public interface DoiHangRepository extends JpaRepository<DoiHang, Long>, JpaSpecificationExecutor<DoiHang> {
    List<DoiHang> findByDonHangId(Long donHangId);
}
