package com.vn.shopping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.response.ResChiTietSanPhamDTO;
import com.vn.shopping.repository.ChiTietSanPhamRepository;

import jakarta.persistence.EntityManager;

@Service
public class ChiTietSanPhamService {

    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final EntityManager entityManager;

    public ChiTietSanPhamService(ChiTietSanPhamRepository chiTietSanPhamRepository, EntityManager entityManager) {
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public ChiTietSanPham create(ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham saved = chiTietSanPhamRepository.save(chiTietSanPham);
        entityManager.flush();
        entityManager.clear();
        return chiTietSanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    @Transactional
    public ChiTietSanPham update(ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham existing = chiTietSanPhamRepository.findById(chiTietSanPham.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm: " + chiTietSanPham.getId()));
        existing.setSanPham(chiTietSanPham.getSanPham());
        existing.setKichThuoc(chiTietSanPham.getKichThuoc());
        existing.setMauSac(chiTietSanPham.getMauSac());
        existing.setTrangThai(chiTietSanPham.getTrangThai());
        existing.setMoTa(chiTietSanPham.getMoTa());
        existing.setGhiTru(chiTietSanPham.getGhiTru());
        ChiTietSanPham saved = chiTietSanPhamRepository.save(existing);
        entityManager.flush();
        entityManager.clear();
        return chiTietSanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    public void delete(long id) {
        chiTietSanPhamRepository.deleteById(id);
    }

    public ChiTietSanPham findById(long id) {
        return chiTietSanPhamRepository.findById(id).orElse(null);
    }

    public List<ChiTietSanPham> findAll() {
        return chiTietSanPhamRepository.findAll();
    }

    public List<ChiTietSanPham> findBySanPhamId(long sanPhamId) {
        return chiTietSanPhamRepository.findBySanPhamId(sanPhamId);
    }

    // === Convert to DTO ===

    @Transactional(readOnly = true)
    public ResChiTietSanPhamDTO convertToDTO(ChiTietSanPham ct) {
        if (ct == null)
            return null;

        ResChiTietSanPhamDTO dto = new ResChiTietSanPhamDTO();
        dto.setId(ct.getId());
        dto.setMaPhieuNhap(ct.getMaPhieuNhap());
        dto.setMaCuaHang(ct.getMaCuaHang());
        dto.setTrangThai(ct.getTrangThai());
        dto.setMoTa(ct.getMoTa());
        dto.setGhiTru(ct.getGhiTru());

        if (ct.getSanPham() != null) {
            dto.setTenSanPham(ct.getSanPham().getTenSanPham());
        }
        if (ct.getMauSac() != null) {
            dto.setTenMauSac(ct.getMauSac().getTenMauSac());
        }
        if (ct.getKichThuoc() != null) {
            dto.setTenKichThuoc(ct.getKichThuoc().getTenKichThuoc());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ResChiTietSanPhamDTO> convertToListDTO(List<ChiTietSanPham> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResChiTietSanPhamDTO findByIdDTO(long id) {
        ChiTietSanPham ct = chiTietSanPhamRepository.findById(id).orElse(null);
        return convertToDTO(ct);
    }

    @Transactional(readOnly = true)
    public List<ResChiTietSanPhamDTO> findAllDTO() {
        return convertToListDTO(chiTietSanPhamRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ResChiTietSanPhamDTO> findBySanPhamIdDTO(long sanPhamId) {
        return convertToListDTO(chiTietSanPhamRepository.findBySanPhamId(sanPhamId));
    }
}
