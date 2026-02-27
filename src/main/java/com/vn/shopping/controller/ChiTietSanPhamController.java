package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.response.ResChiTietSanPhamDTO;
import com.vn.shopping.service.ChiTietSanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/chi-tiet-san-pham")
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;

    public ChiTietSanPhamController(ChiTietSanPhamService chiTietSanPhamService) {
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping
    @ApiMessage("Get all product details")
    public ResponseEntity<List<ResChiTietSanPhamDTO>> getAll() {
        return ResponseEntity.ok(chiTietSanPhamService.findAllDTO());
    }

    @GetMapping("/{id}")
    @ApiMessage("Get product detail by id")
    public ResponseEntity<ResChiTietSanPhamDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        ResChiTietSanPhamDTO dto = chiTietSanPhamService.findByIdDTO(id);
        if (dto == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + id);
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/san-pham/{sanPhamId}")
    @ApiMessage("Get product details by product id")
    public ResponseEntity<List<ResChiTietSanPhamDTO>> getBySanPhamId(@PathVariable("sanPhamId") long sanPhamId) {
        return ResponseEntity.ok(chiTietSanPhamService.findBySanPhamIdDTO(sanPhamId));
    }

    /**
     * Tạo chi tiết sản phẩm + upload nhiều hình ảnh lên MinIO
     * Gửi multipart/form-data: từng trường + "files" (nhiều ảnh, không bắt buộc)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Create product detail")
    public ResponseEntity<ResChiTietSanPhamDTO> create(
            @RequestParam(value = "sanPhamId", required = false) Long sanPhamId,
            @RequestParam(value = "maPhieuNhap", required = false) Long maPhieuNhap,
            @RequestParam(value = "mauSacId", required = false) Long mauSacId,
            @RequestParam(value = "kichThuocId", required = false) Long kichThuocId,
            @RequestParam(value = "maCuaHang", required = false) Long maCuaHang,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "ghiTru", required = false) String ghiTru,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception {

        ChiTietSanPham created = chiTietSanPhamService.createChiTietSanPham(
                sanPhamId, maPhieuNhap, mauSacId, kichThuocId,
                maCuaHang, trangThai, moTa, ghiTru, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(chiTietSanPhamService.convertToDTO(created));
    }

    /**
     * Cập nhật chi tiết sản phẩm + upload thêm hình ảnh mới
     * Gửi multipart/form-data: từng trường + "files" (ảnh mới, không bắt buộc)
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Update product detail")
    public ResponseEntity<ResChiTietSanPhamDTO> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "sanPhamId", required = false) Long sanPhamId,
            @RequestParam(value = "maPhieuNhap", required = false) Long maPhieuNhap,
            @RequestParam(value = "mauSacId", required = false) Long mauSacId,
            @RequestParam(value = "kichThuocId", required = false) Long kichThuocId,
            @RequestParam(value = "maCuaHang", required = false) Long maCuaHang,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "ghiTru", required = false) String ghiTru,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception {

        if (id == null || id == 0) {
            throw new IdInvalidException("Mã chi tiết sản phẩm không được để trống");
        }

        ChiTietSanPham updated = chiTietSanPhamService.updateChiTietSanPham(
                id, sanPhamId, maPhieuNhap, mauSacId, kichThuocId,
                maCuaHang, trangThai, moTa, ghiTru, files);
        return ResponseEntity.ok(chiTietSanPhamService.convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete product detail")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (chiTietSanPhamService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + id);
        }
        chiTietSanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
