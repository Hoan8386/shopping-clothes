package com.vn.shopping.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.KhuyenMaiTheoDiem;

@Repository
public interface KhuyenMaiTheoDiemRepository extends JpaRepository<KhuyenMaiTheoDiem, Long> {

    /**
     * Tìm các khuyến mãi theo điểm đang hoạt động mà khách hàng đủ điều kiện:
     * - Trạng thái = 1 (đang hoạt động)
     * - Còn trong thời gian áp dụng
     * - Còn số lượng
     * - Điểm tích lũy của khách >= điểm tối thiểu
     */
    @Query("SELECT k FROM KhuyenMaiTheoDiem k WHERE k.trangThai = 1 " +
            "AND k.thoiGianBatDau <= :now AND k.thoiGianKetThuc >= :now " +
            "AND (k.soLuong IS NULL OR k.soLuong > 0) " +
            "AND (k.diemToiThieu IS NULL OR k.diemToiThieu <= :diem) " +
            "ORDER BY k.phanTramGiam DESC")
    List<KhuyenMaiTheoDiem> findKhuyenMaiHopLe(@Param("diem") Integer diem, @Param("now") LocalDateTime now);
}
