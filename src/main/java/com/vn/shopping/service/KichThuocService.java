package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KichThuoc;
import com.vn.shopping.repository.KichThuocRepository;

@Service
public class KichThuocService {

    private final KichThuocRepository kichThuocRepository;

    public KichThuocService(KichThuocRepository kichThuocRepository) {
        this.kichThuocRepository = kichThuocRepository;
    }

    public KichThuoc create(KichThuoc kichThuoc) {
        return kichThuocRepository.save(kichThuoc);
    }

    public KichThuoc update(KichThuoc kichThuoc) {
        KichThuoc existing = kichThuocRepository.findById(kichThuoc.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước: " + kichThuoc.getId()));
        existing.setTenKichThuoc(kichThuoc.getTenKichThuoc());
        return kichThuocRepository.save(existing);
    }

    public void delete(long id) {
        kichThuocRepository.deleteById(id);
    }

    public KichThuoc findById(long id) {
        return kichThuocRepository.findById(id).orElse(null);
    }

    public List<KichThuoc> findAll() {
        return kichThuocRepository.findAll();
    }
}
