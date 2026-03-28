package com.vn.shopping.repository;

import com.vn.shopping.domain.LoiPhatSinh;
import com.vn.shopping.domain.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface LoiPhatSinhRepository extends JpaRepository<LoiPhatSinh, Long> {

    List<LoiPhatSinh> findByLichLamViec(LichLamViec lichLamViec);

    @Query("SELECT l FROM LoiPhatSinh l WHERE l.lichLamViec.nhanVien.cuaHang.id = :cuaHangId " +
            "AND (:year IS NULL OR YEAR(l.ngayTao) = :year) " +
            "AND (:month IS NULL OR MONTH(l.ngayTao) = :month)")
    List<LoiPhatSinh> findByCuaHangId(@Param("cuaHangId") Long cuaHangId, @Param("year") Integer year,
            @Param("month") Integer month);
}
