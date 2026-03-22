package com.vn.shopping.service;

import com.vn.shopping.domain.DoiCa;
import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.repository.DoiCaRepository;
import com.vn.shopping.repository.LichLamViecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoiCaService {

    private final DoiCaRepository doiCaRepository;
    private final LichLamViecRepository lichLamViecRepository;

    public DoiCaService(DoiCaRepository doiCaRepository,
            LichLamViecRepository lichLamViecRepository) {
        this.doiCaRepository = doiCaRepository;
        this.lichLamViecRepository = lichLamViecRepository;
    }

    public DoiCa create(DoiCa doiCa) {
        return doiCaRepository.save(doiCa);
    }

    public DoiCa update(DoiCa doiCa) {
        DoiCa existing = doiCaRepository.findById(doiCa.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đổi ca: " + doiCa.getId()));
        existing.setLichLamViec(doiCa.getLichLamViec());
        existing.setChiTietLichLam(doiCa.getChiTietLichLam());
        existing.setNhanVienNhanCa(doiCa.getNhanVienNhanCa());
        existing.setTrangThai(doiCa.getTrangThai());
        existing.setJson(doiCa.getJson());
        return doiCaRepository.save(existing);
    }

    public void delete(long id) {
        doiCaRepository.deleteById(id);
    }

    public DoiCa findById(long id) {
        return doiCaRepository.findById(id).orElse(null);
    }

    public List<DoiCa> findAll() {
        return doiCaRepository.findAll();
    }

    public List<DoiCa> findByLichLamViecId(Long lichLamViecId) {
        LichLamViec lichLamViec = lichLamViecRepository.findById(lichLamViecId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch làm việc: " + lichLamViecId));
        return doiCaRepository.findByLichLamViec(lichLamViec);
    }
}
