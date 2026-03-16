package com.vn.shopping.repository;

import com.vn.shopping.domain.KiemKeHangHoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KiemKeHangHoaRepository extends JpaRepository<KiemKeHangHoa, Long> {
    List<KiemKeHangHoa> findByCuaHangId(Long cuaHangId);

    List<KiemKeHangHoa> findByNhanVienTaoId(Long nhanVienTaoId);
}
