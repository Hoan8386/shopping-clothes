package com.vn.shopping.service;

import com.vn.shopping.domain.CaLamViec;
import com.vn.shopping.repository.CaLamViecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaLamViecService {

    private final CaLamViecRepository caLamViecRepository;

    public CaLamViecService(CaLamViecRepository caLamViecRepository) {
        this.caLamViecRepository = caLamViecRepository;
    }

    public CaLamViec create(CaLamViec caLamViec) {
        return caLamViecRepository.save(caLamViec);
    }

    public CaLamViec update(CaLamViec caLamViec) {
        CaLamViec existing = caLamViecRepository.findById(caLamViec.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc: " + caLamViec.getId()));
        existing.setTenCaLam(caLamViec.getTenCaLam());
        existing.setGioBatDau(caLamViec.getGioBatDau());
        existing.setGioKetThuc(caLamViec.getGioKetThuc());
        existing.setTrangThai(caLamViec.getTrangThai());
        existing.setJson(caLamViec.getJson());
        return caLamViecRepository.save(existing);
    }

    public void delete(long id) {
        caLamViecRepository.deleteById(id);
    }

    public CaLamViec findById(long id) {
        return caLamViecRepository.findById(id).orElse(null);
    }

    public List<CaLamViec> findAll() {
        return caLamViecRepository.findAll();
    }
}
