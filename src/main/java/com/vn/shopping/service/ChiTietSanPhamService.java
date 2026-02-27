package com.vn.shopping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.HinhAnh;
import com.vn.shopping.domain.KichThuoc;
import com.vn.shopping.domain.MauSac;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.response.ResChiTietSanPhamDTO;
import com.vn.shopping.repository.ChiTietSanPhamRepository;

import jakarta.persistence.EntityManager;

@Service
public class ChiTietSanPhamService {

    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final EntityManager entityManager;
    private final MinioStorageService minioStorageService;
    private final HinhAnhService hinhAnhService;

    public ChiTietSanPhamService(ChiTietSanPhamRepository chiTietSanPhamRepository,
            EntityManager entityManager,
            MinioStorageService minioStorageService,
            HinhAnhService hinhAnhService) {
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.entityManager = entityManager;
        this.minioStorageService = minioStorageService;
        this.hinhAnhService = hinhAnhService;
    }

    @Transactional
    public ChiTietSanPham create(ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham saved = chiTietSanPhamRepository.save(chiTietSanPham);
        entityManager.flush();
        entityManager.clear();
        return chiTietSanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * Tạo chi tiết sản phẩm từ các trường riêng lẻ + upload nhiều ảnh lên MinIO
     */
    @Transactional
    public ChiTietSanPham createChiTietSanPham(
            Long sanPhamId, Long maPhieuNhap, Long mauSacId, Long kichThuocId,
            Long maCuaHang, Integer trangThai, String moTa, String ghiTru,
            List<MultipartFile> files) throws Exception {

        ChiTietSanPham ct = new ChiTietSanPham();
        ct.setMaPhieuNhap(maPhieuNhap);
        ct.setMaCuaHang(maCuaHang);
        ct.setTrangThai(trangThai);
        ct.setMoTa(moTa);
        ct.setGhiTru(ghiTru);

        if (sanPhamId != null) {
            SanPham sp = new SanPham();
            sp.setId(sanPhamId);
            ct.setSanPham(sp);
        }
        if (mauSacId != null) {
            MauSac ms = new MauSac();
            ms.setId(mauSacId);
            ct.setMauSac(ms);
        }
        if (kichThuocId != null) {
            KichThuoc kt = new KichThuoc();
            kt.setId(kichThuocId);
            ct.setKichThuoc(kt);
        }

        ChiTietSanPham created = this.create(ct);

        // Upload nhiều ảnh lên MinIO và lưu vào bảng HinhAnh
        if (files != null && !files.isEmpty()) {
            List<String> imageUrls = minioStorageService.uploadMultipleFiles(files);
            for (String url : imageUrls) {
                HinhAnh hinhAnh = new HinhAnh();
                hinhAnh.setChiTietSanPham(created);
                hinhAnh.setTenHinhAnh(url);
                hinhAnhService.create(hinhAnh);
            }
        }

        return created;
    }

    /**
     * Cập nhật chi tiết sản phẩm từ các trường riêng lẻ + upload thêm ảnh mới
     */
    @Transactional
    public ChiTietSanPham updateChiTietSanPham(
            Long id, Long sanPhamId, Long maPhieuNhap, Long mauSacId, Long kichThuocId,
            Long maCuaHang, Integer trangThai, String moTa, String ghiTru,
            List<MultipartFile> files) throws Exception {

        ChiTietSanPham ct = new ChiTietSanPham();
        ct.setId(id);
        ct.setMaPhieuNhap(maPhieuNhap);
        ct.setMaCuaHang(maCuaHang);
        ct.setTrangThai(trangThai);
        ct.setMoTa(moTa);
        ct.setGhiTru(ghiTru);

        if (sanPhamId != null) {
            SanPham sp = new SanPham();
            sp.setId(sanPhamId);
            ct.setSanPham(sp);
        }
        if (mauSacId != null) {
            MauSac ms = new MauSac();
            ms.setId(mauSacId);
            ct.setMauSac(ms);
        }
        if (kichThuocId != null) {
            KichThuoc kt = new KichThuoc();
            kt.setId(kichThuocId);
            ct.setKichThuoc(kt);
        }

        ChiTietSanPham updated = this.update(ct);

        // Upload ảnh mới lên MinIO và lưu vào bảng HinhAnh
        if (files != null && !files.isEmpty()) {
            List<String> imageUrls = minioStorageService.uploadMultipleFiles(files);
            for (String url : imageUrls) {
                HinhAnh hinhAnh = new HinhAnh();
                hinhAnh.setChiTietSanPham(updated);
                hinhAnh.setTenHinhAnh(url);
                hinhAnhService.create(hinhAnh);
            }
        }

        return updated;
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
