package com.vn.shopping.repository;

import com.vn.shopping.domain.ChiTietLichLam;
import com.vn.shopping.domain.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietLichLamRepository extends JpaRepository<ChiTietLichLam, Long> {

    List<ChiTietLichLam> findByLichLamViec(LichLamViec lichLamViec);
}
