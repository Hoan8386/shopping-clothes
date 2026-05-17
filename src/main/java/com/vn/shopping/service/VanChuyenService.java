package com.vn.shopping.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.VanChuyen;
import com.vn.shopping.repository.VanChuyenRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class VanChuyenService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    private final VanChuyenRepository vanChuyenRepository;

    public VanChuyenService(VanChuyenRepository vanChuyenRepository) {
        this.vanChuyenRepository = vanChuyenRepository;
    }

    public List<VanChuyen> findAll() {
        return vanChuyenRepository.findAll();
    }

    public VanChuyen findById(long id) {
        return vanChuyenRepository.findById(id).orElse(null);
    }

    public VanChuyen create(VanChuyen vanChuyen) throws IdInvalidException {
        validateInput(vanChuyen);
        vanChuyen.setSoDienThoai(vanChuyen.getSoDienThoai().trim());
        return vanChuyenRepository.save(vanChuyen);
    }

    public VanChuyen update(VanChuyen vanChuyen) throws IdInvalidException {
        validateInput(vanChuyen);
        VanChuyen existing = vanChuyenRepository.findById(vanChuyen.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy bên vận chuyển: " + vanChuyen.getId()));
        existing.setTenVanChuyen(vanChuyen.getTenVanChuyen());
        existing.setSoDienThoai(vanChuyen.getSoDienThoai().trim());
        existing.setWebsite(vanChuyen.getWebsite());
        existing.setGhiTru(vanChuyen.getGhiTru());
        existing.setTrangThai(vanChuyen.getTrangThai());
        return vanChuyenRepository.save(existing);
    }

    public void delete(long id) {
        vanChuyenRepository.deleteById(id);
    }

    private void validateInput(VanChuyen vanChuyen) throws IdInvalidException {
        if (vanChuyen == null) {
            throw new IdInvalidException("Thông tin bên vận chuyển không hợp lệ");
        }
        String ten = vanChuyen.getTenVanChuyen() == null ? "" : vanChuyen.getTenVanChuyen().trim();
        if (ten.isEmpty()) {
            throw new IdInvalidException("Tên bên vận chuyển không được để trống");
        }
        String phone = vanChuyen.getSoDienThoai() == null ? "" : vanChuyen.getSoDienThoai().trim();
        if (phone.isEmpty()) {
            throw new IdInvalidException("Số điện thoại không được để trống");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IdInvalidException("Số điện thoại phải gồm đúng 10 chữ số");
        }
    }
}