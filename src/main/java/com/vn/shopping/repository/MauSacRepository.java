package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.MauSac;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {
}
