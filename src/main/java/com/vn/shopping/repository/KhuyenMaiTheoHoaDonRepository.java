package com.vn.shopping.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;

@Repository
public interface KhuyenMaiTheoHoaDonRepository extends JpaRepository<KhuyenMaiTheoHoaDon, Long> {

        /**
         * Tìm các khuyến mãi theo hóa đơn đang hoạt động mà giỏ hàng đủ điều kiện:
         * - Trạng thái = 1 (đang hoạt động)
         * - Còn trong thời gian áp dụng
         * - Còn số lượng
         * - Tổng tiền giỏ hàng đạt mức hoaDonToiThieu (nếu có)
         */
        @Query("SELECT k FROM KhuyenMaiTheoHoaDon k WHERE k.trangThai = 1 " +
                        "AND k.thoiGianBatDau <= :now AND k.thoiGianKetThuc >= :now " +
                        "AND (k.soLuong IS NULL OR k.soLuong > 0) " +
                        "AND (k.hoaDonToiThieu IS NULL OR k.hoaDonToiThieu = 0 OR :tongTien >= k.hoaDonToiThieu) " +
                        "ORDER BY k.phanTramGiam DESC")
        List<KhuyenMaiTheoHoaDon> findKhuyenMaiHopLe(@Param("tongTien") Integer tongTien,
                        @Param("now") LocalDateTime now);
}
