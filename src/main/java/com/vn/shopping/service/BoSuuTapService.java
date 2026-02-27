package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.BoSuuTap;
import com.vn.shopping.repository.BoSuuTapRepository;

@Service
public class BoSuuTapService {

    private final BoSuuTapRepository boSuuTapRepository;

    public BoSuuTapService(BoSuuTapRepository boSuuTapRepository) {
        this.boSuuTapRepository = boSuuTapRepository;
    }

    public BoSuuTap create(BoSuuTap boSuuTap) {
        return boSuuTapRepository.save(boSuuTap);
    }

    public BoSuuTap update(BoSuuTap boSuuTap) {
        BoSuuTap existing = boSuuTapRepository.findById(boSuuTap.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ sưu tập: " + boSuuTap.getId()));
        existing.setTenSuuTap(boSuuTap.getTenSuuTap());
        existing.setMoTa(boSuuTap.getMoTa());
        return boSuuTapRepository.save(existing);
    }

    public void delete(long id) {
        boSuuTapRepository.deleteById(id);
    }

    public BoSuuTap findById(long id) {
        return boSuuTapRepository.findById(id).orElse(null);
    }

    public List<BoSuuTap> findAll() {
        return boSuuTapRepository.findAll();
    }
}
