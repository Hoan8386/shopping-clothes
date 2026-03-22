package com.vn.shopping.service;

import com.vn.shopping.domain.LoiPhatSinh;
import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.repository.LoiPhatSinhRepository;
import com.vn.shopping.repository.LichLamViecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoiPhatSinhService {

    private final LoiPhatSinhRepository loiPhatSinhRepository;
    private final LichLamViecRepository lichLamViecRepository;

    public LoiPhatSinhService(LoiPhatSinhRepository loiPhatSinhRepository,
            LichLamViecRepository lichLamViecRepository) {
        this.loiPhatSinhRepository = loiPhatSinhRepository;
        this.lichLamViecRepository = lichLamViecRepository;
    }

    public LoiPhatSinh create(LoiPhatSinh loiPhatSinh) {
        return loiPhatSinhRepository.save(loiPhatSinh);
    }

    public LoiPhatSinh update(LoiPhatSinh loiPhatSinh) {
        LoiPhatSinh existing = loiPhatSinhRepository.findById(loiPhatSinh.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lỗi phát sinh: " + loiPhatSinh.getId()));
        existing.setLichLamViec(loiPhatSinh.getLichLamViec());
        existing.setChiTietLichLam(loiPhatSinh.getChiTietLichLam());
        existing.setTenLoiPhatSinh(loiPhatSinh.getTenLoiPhatSinh());
        existing.setSoTienTru(loiPhatSinh.getSoTienTru());
        existing.setTrangThai(loiPhatSinh.getTrangThai());
        existing.setJson(loiPhatSinh.getJson());
        return loiPhatSinhRepository.save(existing);
    }

    public void delete(long id) {
        loiPhatSinhRepository.deleteById(id);
    }

    public LoiPhatSinh findById(long id) {
        return loiPhatSinhRepository.findById(id).orElse(null);
    }

    public List<LoiPhatSinh> findAll() {
        return loiPhatSinhRepository.findAll();
    }

    public List<LoiPhatSinh> findByLichLamViecId(Long lichLamViecId) {
        LichLamViec lichLamViec = lichLamViecRepository.findById(lichLamViecId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch làm việc: " + lichLamViecId));
        return loiPhatSinhRepository.findByLichLamViec(lichLamViec);
    }
}
