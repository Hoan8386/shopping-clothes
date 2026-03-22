package com.vn.shopping.service;

import com.vn.shopping.domain.LuongCoban;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.repository.LuongCobanRepository;
import com.vn.shopping.repository.NhanVienRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LuongCobanService {

    private final LuongCobanRepository luongCobanRepository;
    private final NhanVienRepository nhanVienRepository;

    public LuongCobanService(LuongCobanRepository luongCobanRepository,
            NhanVienRepository nhanVienRepository) {
        this.luongCobanRepository = luongCobanRepository;
        this.nhanVienRepository = nhanVienRepository;
    }

    public LuongCoban create(LuongCoban luongCoban) {
        return luongCobanRepository.save(luongCoban);
    }

    public LuongCoban update(LuongCoban luongCoban) {
        LuongCoban existing = luongCobanRepository.findById(luongCoban.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lương cơ bản: " + luongCoban.getId()));
        existing.setNhanVien(luongCoban.getNhanVien());
        existing.setLuongCoBan(luongCoban.getLuongCoBan());
        existing.setNgayApDung(luongCoban.getNgayApDung());
        existing.setTrangThai(luongCoban.getTrangThai());
        existing.setJson(luongCoban.getJson());
        return luongCobanRepository.save(existing);
    }

    public void delete(long id) {
        luongCobanRepository.deleteById(id);
    }

    public LuongCoban findById(long id) {
        return luongCobanRepository.findById(id).orElse(null);
    }

    public List<LuongCoban> findAll() {
        return luongCobanRepository.findAll();
    }

    public List<LuongCoban> findByNhanVienId(Long nhanVienId) {
        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên: " + nhanVienId));
        return luongCobanRepository.findByNhanVien(nhanVien);
    }
}
