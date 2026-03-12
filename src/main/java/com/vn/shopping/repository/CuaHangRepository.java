package com.vn.shopping.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.CuaHang;

@Repository
public interface CuaHangRepository extends JpaRepository<CuaHang, Long> {

    @Query("SELECT c FROM CuaHang c WHERE " +
            "(:name IS NULL OR LOWER(c.tenCuaHang) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:address IS NULL OR LOWER(c.diaChi) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
            "(:status IS NULL OR c.trangThai = :status)")
    Page<CuaHang> filter(
            @Param("name") String name,
            @Param("address") String address,
            @Param("status") Integer status,
            Pageable pageable);
}
