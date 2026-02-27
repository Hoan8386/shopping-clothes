package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.ThuongHieu;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {
}
