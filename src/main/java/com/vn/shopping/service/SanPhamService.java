package com.vn.shopping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.response.ResSanPhamDTO;
import com.vn.shopping.repository.SanPhamRepository;

import jakarta.persistence.EntityManager;

@Service
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final EntityManager entityManager;

    public SanPhamService(SanPhamRepository sanPhamRepository, EntityManager entityManager) {
        this.sanPhamRepository = sanPhamRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public SanPham create(SanPham sanPham) {
        SanPham saved = sanPhamRepository.save(sanPham);
        // Flush + clear cache để re-fetch load đầy đủ LAZY relations
        entityManager.flush();
        entityManager.clear();
        return sanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    @Transactional
    public SanPham update(SanPham sanPham) {
        SanPham existing = sanPhamRepository.findById(sanPham.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + sanPham.getId()));

        existing.setTenSanPham(sanPham.getTenSanPham());
        existing.setGiaVon(sanPham.getGiaVon());
        existing.setGiaBan(sanPham.getGiaBan());
        existing.setTrangThai(sanPham.getTrangThai());

        SanPham saved = sanPhamRepository.save(existing);
        entityManager.flush();
        entityManager.clear();
        return sanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    public void delete(long id) {
        sanPhamRepository.deleteById(id);
    }

    public SanPham findById(long id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }

    // === Convert to DTO ===

    @Transactional(readOnly = true)
    public ResSanPhamDTO convertToDTO(SanPham sp) {
        if (sp == null)
            return null;

        ResSanPhamDTO dto = new ResSanPhamDTO();
        dto.setId(sp.getId());
        dto.setTenSanPham(sp.getTenSanPham());
        dto.setGiaVon(sp.getGiaVon());
        dto.setGiaBan(sp.getGiaBan());
        dto.setGiaGiam(sp.getGiaGiam());
        dto.setHinhAnhChinh(sp.getHinhAnhChinh());
        dto.setMoTa(sp.getMoTa());
        dto.setTrangThai(sp.getTrangThai());

        if (sp.getKieuSanPham() != null) {
            dto.setTenKieuSanPham(sp.getKieuSanPham().getTenKieuSanPham());
        }
        if (sp.getBoSuuTap() != null) {
            dto.setTenBoSuuTap(sp.getBoSuuTap().getTenSuuTap());
        }
        if (sp.getThuongHieu() != null) {
            dto.setTenThuongHieu(sp.getThuongHieu().getTenThuongHieu());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ResSanPhamDTO> convertToListDTO(List<SanPham> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResSanPhamDTO findByIdDTO(long id) {
        SanPham sp = sanPhamRepository.findById(id).orElse(null);
        return convertToDTO(sp);
    }

    @Transactional(readOnly = true)
    public List<ResSanPhamDTO> findAllDTO() {
        return convertToListDTO(sanPhamRepository.findAll());
    }
}
