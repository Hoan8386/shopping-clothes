package com.vn.shopping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.BoSuuTap;
import com.vn.shopping.domain.KieuSanPham;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.ThuongHieu;
import com.vn.shopping.domain.response.ResSanPhamDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.SanPhamRepository;

import jakarta.persistence.EntityManager;

@Service
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final EntityManager entityManager;
    private final MinioStorageService minioStorageService;

    public SanPhamService(SanPhamRepository sanPhamRepository, EntityManager entityManager,
            MinioStorageService minioStorageService) {
        this.sanPhamRepository = sanPhamRepository;
        this.entityManager = entityManager;
        this.minioStorageService = minioStorageService;
    }

    @Transactional
    public SanPham create(SanPham sanPham) {
        SanPham saved = sanPhamRepository.save(sanPham);
        // Flush + clear cache để re-fetch load đầy đủ LAZY relations
        entityManager.flush();
        entityManager.clear();
        return sanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * Tạo sản phẩm từ từng trường riêng lẻ + upload ảnh lên MinIO
     */
    @Transactional
    public SanPham createSanPham(String tenSanPham, Double giaVon, Double giaBan, Integer giaGiam,
            String moTa, Integer trangThai, Long kieuSanPhamId, Long boSuuTapId,
            Long thuongHieuId, MultipartFile file) {

        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(tenSanPham);
        sanPham.setGiaVon(giaVon);
        sanPham.setGiaBan(giaBan);
        sanPham.setGiaGiam(giaGiam);
        sanPham.setMoTa(moTa);
        sanPham.setTrangThai(trangThai);

        if (kieuSanPhamId != null) {
            KieuSanPham ksp = new KieuSanPham();
            ksp.setId(kieuSanPhamId);
            sanPham.setKieuSanPham(ksp);
        }
        if (boSuuTapId != null) {
            BoSuuTap bst = new BoSuuTap();
            bst.setId(boSuuTapId);
            sanPham.setBoSuuTap(bst);
        }
        if (thuongHieuId != null) {
            ThuongHieu th = new ThuongHieu();
            th.setId(thuongHieuId);
            sanPham.setThuongHieu(th);
        }

        // Upload ảnh lên MinIO nếu có
        if (file != null && !file.isEmpty()) {
            String imageUrl = minioStorageService.uploadSingleFile(file);
            sanPham.setHinhAnhChinh(imageUrl);
        }
        System.out.println(file.getName());
        return create(sanPham);
    }

    @Transactional
    public SanPham update(SanPham sanPham) {
        SanPham existing = sanPhamRepository.findById(sanPham.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + sanPham.getId()));

        existing.setTenSanPham(sanPham.getTenSanPham());
        existing.setGiaVon(sanPham.getGiaVon());
        existing.setGiaBan(sanPham.getGiaBan());
        existing.setGiaGiam(sanPham.getGiaGiam());
        existing.setMoTa(sanPham.getMoTa());
        existing.setTrangThai(sanPham.getTrangThai());

        if (sanPham.getKieuSanPham() != null) {
            existing.setKieuSanPham(sanPham.getKieuSanPham());
        }
        if (sanPham.getBoSuuTap() != null) {
            existing.setBoSuuTap(sanPham.getBoSuuTap());
        }
        if (sanPham.getThuongHieu() != null) {
            existing.setThuongHieu(sanPham.getThuongHieu());
        }

        // Cập nhật hình ảnh chính nếu có giá trị mới
        if (sanPham.getHinhAnhChinh() != null) {
            existing.setHinhAnhChinh(sanPham.getHinhAnhChinh());
        }

        SanPham saved = sanPhamRepository.save(existing);
        entityManager.flush();
        entityManager.clear();
        return sanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * Cập nhật sản phẩm từ từng trường riêng lẻ + upload ảnh mới lên MinIO
     */
    @Transactional
    public SanPham updateSanPham(Long id, String tenSanPham, Double giaVon, Double giaBan, Integer giaGiam,
            String moTa, Integer trangThai, Long kieuSanPhamId, Long boSuuTapId,
            Long thuongHieuId, MultipartFile file) {

        SanPham sanPham = new SanPham();
        sanPham.setId(id);
        sanPham.setTenSanPham(tenSanPham);
        sanPham.setGiaVon(giaVon);
        sanPham.setGiaBan(giaBan);
        sanPham.setGiaGiam(giaGiam);
        sanPham.setMoTa(moTa);
        sanPham.setTrangThai(trangThai);

        if (kieuSanPhamId != null) {
            KieuSanPham ksp = new KieuSanPham();
            ksp.setId(kieuSanPhamId);
            sanPham.setKieuSanPham(ksp);
        }
        if (boSuuTapId != null) {
            BoSuuTap bst = new BoSuuTap();
            bst.setId(boSuuTapId);
            sanPham.setBoSuuTap(bst);
        }
        if (thuongHieuId != null) {
            ThuongHieu th = new ThuongHieu();
            th.setId(thuongHieuId);
            sanPham.setThuongHieu(th);
        }

        // Upload ảnh mới lên MinIO nếu có
        if (file != null && !file.isEmpty()) {
            String imageUrl = minioStorageService.uploadSingleFile(file);
            sanPham.setHinhAnhChinh(imageUrl);
        }

        return update(sanPham);
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

    /**
     * Lọc sản phẩm theo nhiều tiêu chí + phân trang
     */
    @Transactional(readOnly = true)
    public ResultPaginationDTO filterSanPham(
            String tenSanPham,
            Long kieuSanPhamId,
            Long boSuuTapId,
            Long thuongHieuId,
            Integer trangThai,
            Double giaMin,
            Double giaMax,
            Pageable pageable) {

        Specification<SanPham> spec = SanPhamSpecification.filter(
                tenSanPham, kieuSanPhamId, boSuuTapId, thuongHieuId, trangThai, giaMin, giaMax);

        Page<SanPham> page = sanPhamRepository.findAll(spec, pageable);

        // Convert sang DTO
        List<ResSanPhamDTO> dtoList = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Build meta phân trang
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1); // 1-indexed cho client
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(dtoList);

        return result;
    }
}
