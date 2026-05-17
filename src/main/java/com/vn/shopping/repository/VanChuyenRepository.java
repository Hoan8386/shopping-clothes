package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.VanChuyen;

@Repository
public interface VanChuyenRepository extends JpaRepository<VanChuyen, Long> {
}