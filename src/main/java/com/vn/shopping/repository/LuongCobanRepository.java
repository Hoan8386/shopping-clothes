package com.vn.shopping.repository;

import com.vn.shopping.domain.LuongCoban;
import com.vn.shopping.domain.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuongCobanRepository extends JpaRepository<LuongCoban, Long> {

    List<LuongCoban> findByNhanVien(NhanVien nhanVien);
}
