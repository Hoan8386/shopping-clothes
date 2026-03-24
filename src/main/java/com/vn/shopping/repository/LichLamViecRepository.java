package com.vn.shopping.repository;

import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.domain.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LichLamViecRepository extends JpaRepository<LichLamViec, Long> {

    @Query("SELECT l FROM LichLamViec l WHERE l.nhanVien.cuaHang.id = :cuaHangId")
    List<LichLamViec> findByCuaHangId(@Param("cuaHangId") Long cuaHangId);

    List<LichLamViec> findByNhanVien(NhanVien nhanVien);
    List<LichLamViec> findByNhanVienAndNgayLamViec(NhanVien nhanVien, LocalDate ngayLamViec);

    List<LichLamViec> findByNgayLamViec(LocalDate ngayLamViec);

    @Query("SELECT l FROM LichLamViec l WHERE l.nhanVien.cuaHang.id = :cuaHangId AND l.ngayLamViec BETWEEN :tuNgay AND :denNgay")
    List<LichLamViec> findByCuaHangIdAndDateRange(@Param("cuaHangId") Long cuaHangId, @Param("tuNgay") LocalDate tuNgay, @Param("denNgay") LocalDate denNgay);
}
