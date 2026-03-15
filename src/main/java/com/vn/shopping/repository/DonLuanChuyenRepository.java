package com.vn.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.DonLuanChuyen;

@Repository
public interface DonLuanChuyenRepository extends JpaRepository<DonLuanChuyen, Long>, JpaSpecificationExecutor<DonLuanChuyen> {
    List<DonLuanChuyen> findByCuaHangDatId(Long cuaHangId);
    List<DonLuanChuyen> findByCuaHangGuiId(Long cuaHangId);
}
