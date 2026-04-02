package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietDonHang;
import com.vn.shopping.domain.DonHang;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {
    List<ChiTietDonHang> findByDonHangId(Long donHangId);

    List<ChiTietDonHang> findByDonHang(DonHang donHang);
}
