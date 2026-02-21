package com.vn.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietGioHang;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.GioHang;
import com.vn.shopping.domain.id.ChiTietGioHangId;

@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, ChiTietGioHangId> {
    List<ChiTietGioHang> findByGioHang(GioHang gioHang);

    Optional<ChiTietGioHang> findByGioHangAndChiTietSanPham(GioHang gioHang, ChiTietSanPham chiTietSanPham);
}
