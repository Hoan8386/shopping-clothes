package com.vn.shopping.service;

import com.vn.shopping.domain.LoaiKiemKe;
import com.vn.shopping.domain.request.ReqLoaiKiemKeDTO;
import com.vn.shopping.repository.LoaiKiemKeRepository;
import com.vn.shopping.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiKiemKeService {

    private final LoaiKiemKeRepository loaiKiemKeRepository;

    public LoaiKiemKeService(LoaiKiemKeRepository loaiKiemKeRepository) {
        this.loaiKiemKeRepository = loaiKiemKeRepository;
    }

    public LoaiKiemKe create(ReqLoaiKiemKeDTO dto) {
        LoaiKiemKe loaiKiemKe = new LoaiKiemKe();
        loaiKiemKe.setTenLoaiKiemKe(dto.getTenLoaiKiemKe());
        loaiKiemKe.setMoTa(dto.getMoTa());
        loaiKiemKe.setTrangThai(dto.getTrangThai());
        return loaiKiemKeRepository.save(loaiKiemKe);
    }

    public LoaiKiemKe update(ReqLoaiKiemKeDTO dto) throws IdInvalidException {
        LoaiKiemKe existing = loaiKiemKeRepository.findById(dto.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy loại kiểm kê: " + dto.getId()));

        existing.setTenLoaiKiemKe(dto.getTenLoaiKiemKe());
        existing.setMoTa(dto.getMoTa());
        existing.setTrangThai(dto.getTrangThai());
        return loaiKiemKeRepository.save(existing);
    }

    public void delete(Long id) {
        loaiKiemKeRepository.deleteById(id);
    }

    public LoaiKiemKe findById(Long id) {
        return loaiKiemKeRepository.findById(id).orElse(null);
    }

    public List<LoaiKiemKe> findAll() {
        return loaiKiemKeRepository.findAll();
    }
}
