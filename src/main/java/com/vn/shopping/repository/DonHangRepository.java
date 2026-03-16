package com.vn.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.DonHang;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Long>, JpaSpecificationExecutor<DonHang> {
    Optional<DonHang> findByPaymentRef(String paymentRef);
}
