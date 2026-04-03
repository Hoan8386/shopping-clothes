package com.vn.shopping.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.repository.NhaCungCapRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class NhaCungCapService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    private final NhaCungCapRepository nhaCungCapRepository;

    public NhaCungCapService(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    public NhaCungCap create(NhaCungCap nhaCungCap) throws IdInvalidException {
        validateSupplierInput(nhaCungCap);
        nhaCungCap.setSoDienThoai(nhaCungCap.getSoDienThoai().trim());
        return nhaCungCapRepository.save(nhaCungCap);
    }

    public NhaCungCap update(NhaCungCap nhaCungCap) throws IdInvalidException {
        validateSupplierInput(nhaCungCap);
        NhaCungCap existing = nhaCungCapRepository.findById(nhaCungCap.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp: " + nhaCungCap.getId()));
        existing.setTenNhaCungCap(nhaCungCap.getTenNhaCungCap());
        existing.setSoDienThoai(nhaCungCap.getSoDienThoai().trim());
        existing.setEmail(nhaCungCap.getEmail());
        existing.setDiaChi(nhaCungCap.getDiaChi());
        existing.setGhiTru(nhaCungCap.getGhiTru());
        existing.setTrangThai(nhaCungCap.getTrangThai());
        return nhaCungCapRepository.save(existing);
    }

    public void delete(long id) {
        nhaCungCapRepository.deleteById(id);
    }

    public NhaCungCap findById(long id) {
        return nhaCungCapRepository.findById(id).orElse(null);
    }

    public List<NhaCungCap> findAll() {
        return nhaCungCapRepository.findAll();
    }

    private void validateSupplierInput(NhaCungCap nhaCungCap) throws IdInvalidException {
        String phone = nhaCungCap.getSoDienThoai() == null ? "" : nhaCungCap.getSoDienThoai().trim();
        if (phone.isEmpty()) {
            throw new IdInvalidException("Số điện thoại không được để trống");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IdInvalidException("Số điện thoại phải gồm đúng 10 chữ số");
        }
    }
}
