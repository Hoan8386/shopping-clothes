package com.vn.shopping.repository;

import com.vn.shopping.domain.LuongThuong;
import com.vn.shopping.domain.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuongThuongRepository extends JpaRepository<LuongThuong, Long> {

    List<LuongThuong> findByNhanVien(NhanVien nhanVien);
}
