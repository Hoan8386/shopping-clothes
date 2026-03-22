package com.vn.shopping.repository;

import com.vn.shopping.domain.DoiCa;
import com.vn.shopping.domain.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoiCaRepository extends JpaRepository<DoiCa, Long> {

    List<DoiCa> findByLichLamViec(LichLamViec lichLamViec);
}
