package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.BoSuuTap;

@Repository
public interface BoSuuTapRepository extends JpaRepository<BoSuuTap, Long> {
}
