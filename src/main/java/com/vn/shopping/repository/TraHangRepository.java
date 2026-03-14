package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.TraHang;

@Repository
public interface TraHangRepository extends JpaRepository<TraHang, Long>, JpaSpecificationExecutor<TraHang> {
    List<TraHang> findByDonHangId(Long donHangId);
}
