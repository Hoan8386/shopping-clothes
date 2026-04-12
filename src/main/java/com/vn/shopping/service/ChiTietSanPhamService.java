package com.vn.shopping.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.HinhAnh;
import com.vn.shopping.domain.KichThuoc;
import com.vn.shopping.domain.MauSac;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.repository.CuaHangRepository;
import com.vn.shopping.domain.response.ResChiTietSanPhamDTO;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.KichThuocRepository;
import com.vn.shopping.repository.MauSacRepository;
import com.vn.shopping.repository.SanPhamRepository;
import com.vn.shopping.util.error.IdInvalidException;

import jakarta.persistence.EntityManager;

@Service
public class ChiTietSanPhamService {

    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final EntityManager entityManager;
    private final StorageService storageService;
    private final HinhAnhService hinhAnhService;
    private final CuaHangService cuaHangService;
    private final CuaHangRepository cuaHangRepository;
    private final SanPhamRepository sanPhamRepository;
    private final MauSacRepository mauSacRepository;
    private final KichThuocRepository kichThuocRepository;

    public ChiTietSanPhamService(ChiTietSanPhamRepository chiTietSanPhamRepository,
            EntityManager entityManager,
            StorageService storageService,
            HinhAnhService hinhAnhService,
            CuaHangService cuaHangService,
            CuaHangRepository cuaHangRepository,
            SanPhamRepository sanPhamRepository,
            MauSacRepository mauSacRepository,
            KichThuocRepository kichThuocRepository) {
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.entityManager = entityManager;
        this.storageService = storageService;
        this.hinhAnhService = hinhAnhService;
        this.cuaHangService = cuaHangService;
        this.cuaHangRepository = cuaHangRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.mauSacRepository = mauSacRepository;
        this.kichThuocRepository = kichThuocRepository;
    }

    @Transactional
    @CacheEvict(cacheNames = "product_detail", allEntries = true)
    public ChiTietSanPham create(ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham saved = chiTietSanPhamRepository.save(chiTietSanPham);

        if (saved.getMaVach() == null || saved.getMaVach().isBlank()) {
            Long sanPhamId = saved.getSanPham() != null ? saved.getSanPham().getId() : null;
            saved.setMaVach(buildBarcode(sanPhamId, saved.getId(), saved.getMaCuaHang()));
            saved = chiTietSanPhamRepository.save(saved);
        }

        entityManager.flush();
        entityManager.clear();
        return chiTietSanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * Tạo chi tiết sản phẩm cho TẤT CẢ cửa hàng + upload nhiều ảnh lên Cloudinary
     * Mỗi cửa hàng sẽ có 1 bản ghi ChiTietSanPham riêng với cùng thông tin sản phẩm
     */
    @Transactional
    @CacheEvict(cacheNames = "product_detail", allEntries = true)
    public List<ResChiTietSanPhamDTO> createChiTietSanPham(
            Long sanPhamId, Long maPhieuNhap, Long mauSacId, Long kichThuocId,
            Integer soLuong, Integer trangThai, String moTa, String ghiTru,
            List<MultipartFile> files) throws Exception {

        // Resolve shared entities once
        SanPham sp = null;
        if (sanPhamId != null) {
            sp = sanPhamRepository.findById(sanPhamId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy sản phẩm: " + sanPhamId));
        }
        MauSac ms = null;
        if (mauSacId != null) {
            ms = mauSacRepository.findById(mauSacId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy màu sắc: " + mauSacId));
        }
        KichThuoc kt = null;
        if (kichThuocId != null) {
            kt = kichThuocRepository.findById(kichThuocId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy kích thước: " + kichThuocId));
        }

        // Upload ảnh một lần, dùng chung URL cho tất cả cửa hàng
        List<String> imageUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            imageUrls = storageService.uploadMultipleFiles(files);
        }

        List<CuaHang> cuaHangs = cuaHangRepository.findAll();
        List<ResChiTietSanPhamDTO> results = new ArrayList<>();

        for (CuaHang cuaHang : cuaHangs) {
            ChiTietSanPham ct = new ChiTietSanPham();
            ct.setMaPhieuNhap(maPhieuNhap);
            ct.setMaCuaHang(cuaHang.getId());
            ct.setSoLuong(soLuong);
            ct.setTrangThai(trangThai);
            ct.setMoTa(moTa);
            ct.setGhiTru(ghiTru);
            ct.setSanPham(sp);
            ct.setMauSac(ms);
            ct.setKichThuoc(kt);

            // Lưu + flush, KHÔNG clear để tránh detach entity trước khi convert DTO
            ChiTietSanPham created = chiTietSanPhamRepository.save(ct);
            created.setMaVach(buildBarcode(
                    created.getSanPham() != null ? created.getSanPham().getId() : null,
                    created.getId(),
                    created.getMaCuaHang()));
            created = chiTietSanPhamRepository.save(created);
            entityManager.flush();

            // Gắn ảnh cho từng chi tiết sản phẩm
            for (String url : imageUrls) {
                HinhAnh hinhAnh = new HinhAnh();
                hinhAnh.setChiTietSanPham(created);
                hinhAnh.setTenHinhAnh(url);
                hinhAnhService.create(hinhAnh);
            }

            // Convert sang DTO ngay lập tức khi entity vẫn còn attached trong session
            results.add(convertToDTO(created));
        }

        return results;
    }

    /**
     * Cập nhật chi tiết sản phẩm từ các trường riêng lẻ + upload thêm ảnh mới
     */
    @Transactional
    @CacheEvict(cacheNames = "product_detail", allEntries = true)
    public ChiTietSanPham updateChiTietSanPham(
            Long id, Long sanPhamId, Long maPhieuNhap, Long mauSacId, Long kichThuocId,
            Long maCuaHang, Integer soLuong, Integer trangThai, String moTa, String ghiTru,
            List<MultipartFile> files) throws Exception {

        ChiTietSanPham ct = new ChiTietSanPham();
        ct.setId(id);
        ct.setMaPhieuNhap(maPhieuNhap);
        ct.setMaCuaHang(maCuaHang);
        ct.setSoLuong(soLuong);
        ct.setTrangThai(trangThai);
        ct.setMoTa(moTa);
        ct.setGhiTru(ghiTru);

        if (sanPhamId != null) {
            SanPham sp = sanPhamRepository.findById(sanPhamId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy sản phẩm: " + sanPhamId));
            ct.setSanPham(sp);
        }
        if (mauSacId != null) {
            MauSac ms = mauSacRepository.findById(mauSacId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy màu sắc: " + mauSacId));
            ct.setMauSac(ms);
        }
        if (kichThuocId != null) {
            KichThuoc kt = kichThuocRepository.findById(kichThuocId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy kích thước: " + kichThuocId));
            ct.setKichThuoc(kt);
        }

        ChiTietSanPham updated = this.update(ct);

        // Upload ảnh mới lên Cloudinary và lưu vào bảng HinhAnh
        if (files != null && !files.isEmpty()) {
            List<String> imageUrls = storageService.uploadMultipleFiles(files);
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
    @CacheEvict(cacheNames = "product_detail", allEntries = true)
    public ChiTietSanPham update(ChiTietSanPham chiTietSanPham) throws IdInvalidException {
        ChiTietSanPham existing = chiTietSanPhamRepository.findById(chiTietSanPham.getId())
                .orElseThrow(
                        () -> new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + chiTietSanPham.getId()));
        existing.setSanPham(chiTietSanPham.getSanPham());
        existing.setKichThuoc(chiTietSanPham.getKichThuoc());
        existing.setMauSac(chiTietSanPham.getMauSac());
        existing.setSoLuong(chiTietSanPham.getSoLuong());
        existing.setTrangThai(chiTietSanPham.getTrangThai());
        existing.setMoTa(chiTietSanPham.getMoTa());
        existing.setGhiTru(chiTietSanPham.getGhiTru());
        ChiTietSanPham saved = chiTietSanPhamRepository.save(existing);
        entityManager.flush();
        entityManager.clear();
        return chiTietSanPhamRepository.findById(saved.getId()).orElse(saved);
    }

    @CacheEvict(cacheNames = "product_detail", allEntries = true)
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
        dto.setSoLuong(ct.getSoLuong());
        dto.setTrangThai(ct.getTrangThai());
        dto.setMoTa(ct.getMoTa());
        dto.setGhiTru(ct.getGhiTru());
        dto.setMaVach(ct.getMaVach());

        // Lấy tên cửa hàng từ mã cửa hàng
        if (ct.getMaCuaHang() != null) {
            dto.setMaCuaHang(ct.getMaCuaHang());
            CuaHang cuaHang = cuaHangService.findById(ct.getMaCuaHang());
            if (cuaHang != null) {
                dto.setTenCuaHang(cuaHang.getTenCuaHang());
            }
        }

        if (ct.getSanPham() != null) {
            dto.setSanPhamId(ct.getSanPham().getId());
            dto.setTenSanPham(ct.getSanPham().getTenSanPham());
            dto.setGiaBan(ct.getSanPham().getGiaBan());
        }
        if (ct.getMauSac() != null) {
            dto.setTenMauSac(ct.getMauSac().getTenMauSac());
        }
        if (ct.getKichThuoc() != null) {
            dto.setTenKichThuoc(ct.getKichThuoc().getTenKichThuoc());
        }

        // Lấy danh sách URL hình ảnh
        List<HinhAnh> hinhAnhs = hinhAnhService.findByChiTietSanPhamId(ct.getId());
        List<String> hinhAnhUrls = hinhAnhs.stream()
                .map(HinhAnh::getTenHinhAnh)
                .collect(Collectors.toList());
        dto.setHinhAnhUrls(hinhAnhUrls);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ResChiTietSanPhamDTO> convertToListDTO(List<ChiTietSanPham> list) {
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "product_detail", key = "'by_id_' + #id")
    public ResChiTietSanPhamDTO findByIdDTO(long id) {
        ChiTietSanPham ct = chiTietSanPhamRepository.findById(id).orElse(null);
        return convertToDTO(ct);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "product_detail", key = "'all'")
    public List<ResChiTietSanPhamDTO> findAllDTO() {
        return convertToListDTO(chiTietSanPhamRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ResChiTietSanPhamDTO> findAllDTOWithFilter(
            Long sanPhamId, Long mauSacId, Long kichThuocId,
            Long maCuaHang, Integer trangThai) {

        Specification<ChiTietSanPham> spec = Specification.where(null);

        if (sanPhamId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("sanPham").get("id"), sanPhamId));
        }
        if (mauSacId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("mauSac").get("id"), mauSacId));
        }
        if (kichThuocId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("kichThuoc").get("id"), kichThuocId));
        }
        if (maCuaHang != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("maCuaHang"), maCuaHang));
        }
        if (trangThai != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("trangThai"), trangThai));
        }

        return convertToListDTO(chiTietSanPhamRepository.findAll(spec));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "product_detail", key = "'by_product_' + #sanPhamId")
    public List<ResChiTietSanPhamDTO> findBySanPhamIdDTO(long sanPhamId) {
        return convertToListDTO(chiTietSanPhamRepository.findBySanPhamId(sanPhamId));
    }

    @Transactional(readOnly = true)
    public ResChiTietSanPhamDTO findByBarcodeInStore(String maVach, Long maCuaHang) {
        if (maVach == null || maVach.isBlank() || maCuaHang == null) {
            return null;
        }

        return chiTietSanPhamRepository.findFirstByMaVachAndMaCuaHang(maVach.trim(), maCuaHang)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ResChiTietSanPhamDTO findByBarcodeImageInStore(MultipartFile file, Long maCuaHang)
            throws IdInvalidException {
        if (file == null || file.isEmpty()) {
            throw new IdInvalidException("Vui lòng chọn ảnh mã vạch");
        }

        String maVach = decodeCode128(file);
        return findByBarcodeInStore(maVach, maCuaHang);
    }

    private String decodeCode128(MultipartFile file) throws IdInvalidException {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            if (image == null) {
                throw new IdInvalidException("File tải lên không phải ảnh hợp lệ");
            }

            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, List.of(BarcodeFormat.CODE_128));
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Result result = new MultiFormatReader().decode(bitmap, hints);
            String value = result.getText();
            if (value == null || value.isBlank()) {
                throw new IdInvalidException("Không đọc được mã vạch từ ảnh");
            }
            return value.trim();
        } catch (NotFoundException e) {
            throw new IdInvalidException("Không nhận diện được mã vạch Code 128 trong ảnh");
        } catch (IOException e) {
            throw new IdInvalidException("Không thể xử lý ảnh mã vạch");
        }
    }

    private String buildBarcode(Long sanPhamId, Long chiTietSanPhamId, Long cuaHangId) {
        String spPart = sanPhamId != null ? Long.toString(sanPhamId, 36).toUpperCase() : "0";
        String ctPart = chiTietSanPhamId != null ? Long.toString(chiTietSanPhamId, 36).toUpperCase() : "0";
        String chPart = cuaHangId != null ? Long.toString(cuaHangId, 36).toUpperCase() : "0";

        // SP + mã sản phẩm + mã chi tiết sản phẩm + mã cửa hàng
        String barcode = "SP" + spPart + "CT" + ctPart + "CH" + chPart;

        if (barcode.length() > 32) {
            barcode = barcode.substring(0, 32);
        }

        return barcode;
    }
}
