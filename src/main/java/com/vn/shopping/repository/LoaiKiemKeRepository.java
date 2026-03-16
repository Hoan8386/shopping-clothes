package com.vn.shopping.repository;

import com.vn.shopping.domain.LoaiKiemKe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiKiemKeRepository extends JpaRepository<LoaiKiemKe, Long> {
}
