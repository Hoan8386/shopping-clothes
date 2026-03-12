package com.vn.shopping.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.response.ResCuaHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.CuaHangRepository;

@Service
public class CuaHangService {

    private final CuaHangRepository cuaHangRepository;

    public CuaHangService(CuaHangRepository cuaHangRepository) {
        this.cuaHangRepository = cuaHangRepository;
    }

    public CuaHang create(CuaHang cuaHang) {
        return cuaHangRepository.save(cuaHang);
    }

    public CuaHang update(CuaHang cuaHang) {
        CuaHang existing = cuaHangRepository.findById(cuaHang.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng: " + cuaHang.getId()));
        existing.setTenCuaHang(cuaHang.getTenCuaHang());
        existing.setDiaChi(cuaHang.getDiaChi());
        existing.setViTri(cuaHang.getViTri());
        existing.setSoDienThoai(cuaHang.getSoDienThoai());
        existing.setEmail(cuaHang.getEmail());
        existing.setTrangThai(cuaHang.getTrangThai());
        return cuaHangRepository.save(existing);
    }

    public void delete(long id) {
        cuaHangRepository.deleteById(id);
    }

    public CuaHang findById(long id) {
        return cuaHangRepository.findById(id).orElse(null);
    }

    public List<CuaHang> findAll() {
        return cuaHangRepository.findAll();
    }

    public ResultPaginationDTO filter(String name, String address, Integer status, Pageable pageable) {
        Page<CuaHang> page = cuaHangRepository.filter(name, address, status, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(page.getContent().stream().map(this::toDTO).toList());
        return result;
    }

    public ResCuaHangDTO toDTO(CuaHang cuaHang) {
        ResCuaHangDTO dto = new ResCuaHangDTO();
        dto.setId(cuaHang.getId());
        dto.setTenCuaHang(cuaHang.getTenCuaHang());
        dto.setDiaChi(cuaHang.getDiaChi());
        dto.setViTri(cuaHang.getViTri());
        dto.setSoDienThoai(cuaHang.getSoDienThoai());
        dto.setEmail(cuaHang.getEmail());
        dto.setTrangThai(cuaHang.getTrangThai());
        if (cuaHang.getViTri() != null && cuaHang.getViTri().contains(",")) {
            String[] parts = cuaHang.getViTri().split(",");
            try {
                dto.setLatitude(Double.parseDouble(parts[0].trim()));
                dto.setLongitude(Double.parseDouble(parts[1].trim()));
            } catch (NumberFormatException e) {
                dto.setLatitude(null);
                dto.setLongitude(null);
            }
        }
        return dto;
    }
}
