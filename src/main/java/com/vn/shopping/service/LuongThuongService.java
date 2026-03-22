package com.vn.shopping.service;

import com.vn.shopping.domain.LuongThuong;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.repository.LuongThuongRepository;
import com.vn.shopping.repository.NhanVienRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LuongThuongService {

    private final LuongThuongRepository luongThuongRepository;
    private final NhanVienRepository nhanVienRepository;

    public LuongThuongService(LuongThuongRepository luongThuongRepository,
            NhanVienRepository nhanVienRepository) {
        this.luongThuongRepository = luongThuongRepository;
        this.nhanVienRepository = nhanVienRepository;
    }

    public LuongThuong create(LuongThuong luongThuong) {
        return luongThuongRepository.save(luongThuong);
    }

    public LuongThuong update(LuongThuong luongThuong) {
        LuongThuong existing = luongThuongRepository.findById(luongThuong.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lương thưởng: " + luongThuong.getId()));
        existing.setNhanVien(luongThuong.getNhanVien());
        existing.setTienThuong(luongThuong.getTienThuong());
        existing.setNgayBatDau(luongThuong.getNgayBatDau());
        existing.setNgayKetThuc(luongThuong.getNgayKetThuc());
        existing.setTrangThai(luongThuong.getTrangThai());
        existing.setJson(luongThuong.getJson());
        return luongThuongRepository.save(existing);
    }

    public void delete(long id) {
        luongThuongRepository.deleteById(id);
    }

    public LuongThuong findById(long id) {
        return luongThuongRepository.findById(id).orElse(null);
    }

    public List<LuongThuong> findAll() {
        return luongThuongRepository.findAll();
    }

    public List<LuongThuong> findByNhanVienId(Long nhanVienId) {
        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên: " + nhanVienId));
        return luongThuongRepository.findByNhanVien(nhanVien);
    }
}
