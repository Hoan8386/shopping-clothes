package com.vn.shopping.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.vn.shopping.domain.DanhGiaSanPham;

@Repository
public interface DanhGiaSanPhamRepository
        extends JpaRepository<DanhGiaSanPham, Long>, JpaSpecificationExecutor<DanhGiaSanPham> {
    List<DanhGiaSanPham> findBySanPhamId(Long sanPhamId);

    List<DanhGiaSanPham> findByKhachHangId(Long khachHangId);

    List<DanhGiaSanPham> findByDonHangId(Long donHangId);

    boolean existsByKhachHangIdAndSanPhamIdAndDonHangId(Long khachHangId, Long sanPhamId, Long donHangId);
}
