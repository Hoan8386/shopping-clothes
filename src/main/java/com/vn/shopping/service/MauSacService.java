package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.MauSac;
import com.vn.shopping.repository.MauSacRepository;

@Service
public class MauSacService {

    private final MauSacRepository mauSacRepository;

    public MauSacService(MauSacRepository mauSacRepository) {
        this.mauSacRepository = mauSacRepository;
    }

    public MauSac create(MauSac mauSac) {
        return mauSacRepository.save(mauSac);
    }

    public MauSac update(MauSac mauSac) {
        MauSac existing = mauSacRepository.findById(mauSac.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc: " + mauSac.getId()));
        existing.setTenMauSac(mauSac.getTenMauSac());
        return mauSacRepository.save(existing);
    }

    public void delete(long id) {
        mauSacRepository.deleteById(id);
    }

    public MauSac findById(long id) {
        return mauSacRepository.findById(id).orElse(null);
    }

    public List<MauSac> findAll() {
        return mauSacRepository.findAll();
    }
}
