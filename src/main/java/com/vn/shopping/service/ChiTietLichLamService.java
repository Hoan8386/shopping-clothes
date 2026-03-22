package com.vn.shopping.service;

import com.vn.shopping.domain.ChiTietLichLam;
import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.repository.ChiTietLichLamRepository;
import com.vn.shopping.repository.LichLamViecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietLichLamService {

    private final ChiTietLichLamRepository chiTietLichLamRepository;
    private final LichLamViecRepository lichLamViecRepository;

    public ChiTietLichLamService(ChiTietLichLamRepository chiTietLichLamRepository,
            LichLamViecRepository lichLamViecRepository) {
        this.chiTietLichLamRepository = chiTietLichLamRepository;
        this.lichLamViecRepository = lichLamViecRepository;
    }

    public ChiTietLichLam create(ChiTietLichLam chiTietLichLam) {
        return chiTietLichLamRepository.save(chiTietLichLam);
    }

    public ChiTietLichLam update(ChiTietLichLam chiTietLichLam) {
        ChiTietLichLam existing = chiTietLichLamRepository.findById(chiTietLichLam.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết lịch làm: " + chiTietLichLam.getId()));
        existing.setLichLamViec(chiTietLichLam.getLichLamViec());
        existing.setCaLamViec(chiTietLichLam.getCaLamViec());
        existing.setTrangThai(chiTietLichLam.getTrangThai());
        return chiTietLichLamRepository.save(existing);
    }

    public void delete(long id) {
        chiTietLichLamRepository.deleteById(id);
    }

    public ChiTietLichLam findById(long id) {
        return chiTietLichLamRepository.findById(id).orElse(null);
    }

    public List<ChiTietLichLam> findAll() {
        return chiTietLichLamRepository.findAll();
    }

    public List<ChiTietLichLam> findByLichLamViecId(Long lichLamViecId) {
        LichLamViec lichLamViec = lichLamViecRepository.findById(lichLamViecId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch làm việc: " + lichLamViecId));
        return chiTietLichLamRepository.findByLichLamViec(lichLamViec);
    }
}
