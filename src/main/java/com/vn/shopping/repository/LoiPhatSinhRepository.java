package com.vn.shopping.repository;

import com.vn.shopping.domain.LoiPhatSinh;
import com.vn.shopping.domain.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoiPhatSinhRepository extends JpaRepository<LoiPhatSinh, Long> {

    List<LoiPhatSinh> findByLichLamViec(LichLamViec lichLamViec);
}
