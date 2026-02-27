package com.vn.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.HinhAnh;
import com.vn.shopping.service.ChiTietSanPhamService;
import com.vn.shopping.service.HinhAnhService;
import com.vn.shopping.service.MinioStorageService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/hinh-anh")
public class HinhAnhController {

    private final HinhAnhService hinhAnhService;
    private final MinioStorageService minioStorageService;
    private final ChiTietSanPhamService chiTietSanPhamService;

    public HinhAnhController(HinhAnhService hinhAnhService, MinioStorageService minioStorageService,
            ChiTietSanPhamService chiTietSanPhamService) {
        this.hinhAnhService = hinhAnhService;
        this.minioStorageService = minioStorageService;
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách hình ảnh")
    public ResponseEntity<List<HinhAnh>> getAll() {
        return ResponseEntity.ok(hinhAnhService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy hình ảnh theo id")
    public ResponseEntity<HinhAnh> getById(@PathVariable("id") long id) throws IdInvalidException {
        HinhAnh hinhAnh = hinhAnhService.findById(id);
        if (hinhAnh == null) {
            throw new IdInvalidException("Không tìm thấy hình ảnh: " + id);
        }
        return ResponseEntity.ok(hinhAnh);
    }

    @GetMapping("/chi-tiet-san-pham/{chiTietSanPhamId}")
    @ApiMessage("Lấy hình ảnh theo chi tiết sản phẩm")
    public ResponseEntity<List<HinhAnh>> getByChiTietSanPham(@PathVariable("chiTietSanPhamId") long chiTietSanPhamId) {
        return ResponseEntity.ok(hinhAnhService.findByChiTietSanPhamId(chiTietSanPhamId));
    }

    /**
     * Upload nhiều ảnh cho 1 chi tiết sản phẩm
     * Gửi multipart/form-data: "chiTietSanPhamId" + "files" (nhiều ảnh)
     */
    @PostMapping(value = "/upload/{chiTietSanPhamId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Upload hình ảnh cho chi tiết sản phẩm")
    public ResponseEntity<List<HinhAnh>> uploadImages(
            @PathVariable("chiTietSanPhamId") long chiTietSanPhamId,
            @RequestPart("files") List<MultipartFile> files) throws IdInvalidException {

        ChiTietSanPham chiTietSanPham = chiTietSanPhamService.findById(chiTietSanPhamId);
        if (chiTietSanPham == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + chiTietSanPhamId);
        }

        List<String> imageUrls = minioStorageService.uploadMultipleFiles(files);
        List<HinhAnh> savedImages = new ArrayList<>();

        for (String url : imageUrls) {
            HinhAnh hinhAnh = new HinhAnh();
            hinhAnh.setChiTietSanPham(chiTietSanPham);
            hinhAnh.setTenHinhAnh(url);
            savedImages.add(hinhAnhService.create(hinhAnh));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
    }

    @PostMapping
    @ApiMessage("Tạo hình ảnh")
    public ResponseEntity<HinhAnh> create(@RequestBody HinhAnh hinhAnh) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hinhAnhService.create(hinhAnh));
    }

    @PutMapping
    @ApiMessage("Cập nhật hình ảnh")
    public ResponseEntity<HinhAnh> update(@RequestBody HinhAnh hinhAnh) throws IdInvalidException {
        if (hinhAnh.getId() == null || hinhAnh.getId() == 0) {
            throw new IdInvalidException("Mã hình ảnh không được để trống");
        }
        return ResponseEntity.ok(hinhAnhService.update(hinhAnh));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa hình ảnh")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (hinhAnhService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy hình ảnh: " + id);
        }
        hinhAnhService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
