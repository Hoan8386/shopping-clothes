package com.vn.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ChiTietSanPham;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long>,
        JpaSpecificationExecutor<ChiTietSanPham> {

    List<ChiTietSanPham> findBySanPhamId(long sanPhamId);

    Optional<ChiTietSanPham> findFirstBySanPhamIdAndMauSacIdAndKichThuocIdAndMaCuaHang(
            Long sanPhamId,
            Long mauSacId,
            Long kichThuocId,
            Long maCuaHang);
}
