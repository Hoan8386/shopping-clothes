package com.vn.shopping.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.vn.shopping.domain.DanhGiaSanPham;

@Repository
public interface DanhGiaSanPhamRepository
        extends JpaRepository<DanhGiaSanPham, Long>, JpaSpecificationExecutor<DanhGiaSanPham> {
    List<DanhGiaSanPham> findByKhachHangId(Long khachHangId);

    List<DanhGiaSanPham> findByChiTietDonHangId(Long chiTietDonHangId);

    boolean existsByKhachHangIdAndChiTietDonHangId(Long khachHangId, Long chiTietDonHangId);

    @Query("SELECT d FROM DanhGiaSanPham d " +
            "WHERE d.chiTietDonHang.chiTietSanPham.sanPham.id = :sanPhamId")
    List<DanhGiaSanPham> findBySanPhamId(@Param("sanPhamId") Long sanPhamId);
}
