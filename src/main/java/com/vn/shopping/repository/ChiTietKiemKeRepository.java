package com.vn.shopping.repository;

import com.vn.shopping.domain.ChiTietKiemKe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietKiemKeRepository extends JpaRepository<ChiTietKiemKe, Long> {
    List<ChiTietKiemKe> findByKiemKeHangHoaId(Long kiemKeId);
}
