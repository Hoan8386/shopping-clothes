package com.vn.shopping.repository;

import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.domain.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LichLamViecRepository extends JpaRepository<LichLamViec, Long> {

    List<LichLamViec> findByNhanVien(NhanVien nhanVien);

    List<LichLamViec> findByNgayLamViec(LocalDate ngayLamViec);

    List<LichLamViec> findByNhanVienAndNgayLamViec(NhanVien nhanVien, LocalDate ngayLamViec);
}
