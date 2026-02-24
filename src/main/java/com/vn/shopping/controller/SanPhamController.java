package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.SanPham;
import com.vn.shopping.service.SanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/san-pham")
public class SanPhamController {

    private final SanPhamService sanPhamService;

    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    /**
     * Xem danh sách sản phẩm (tất cả đều xem được)
     */
    @GetMapping
    @ApiMessage("Get all product")
    public ResponseEntity<List<SanPham>> getAll() {
        return ResponseEntity.ok(sanPhamService.findAll());
    }

    /**
     * Xem chi tiết 1 sản phẩm
     */
    @GetMapping("/{id}")
    @ApiMessage("Get detail product")
    public ResponseEntity<SanPham> getById(@PathVariable("id") String maSanPham) throws IdInvalidException {
        SanPham sp = sanPhamService.findById(maSanPham);
        if (sp == null) {
            throw new IdInvalidException("Không tìm thấy sản phẩm: " + maSanPham);
        }
        return ResponseEntity.ok(sp);
    }

    /**
     * Tạo sản phẩm mới — chỉ ADMIN có permission mới gọi được
     * (kiểm tra qua PermissionInterceptor: POST /api/v1/san-pham)
     */
    @PostMapping
    @ApiMessage("Create a product")
    public ResponseEntity<SanPham> create(@RequestBody SanPham sanPham) {
        SanPham created = sanPhamService.create(sanPham);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Cập nhật sản phẩm — chỉ ADMIN có permission mới gọi được
     */
    @PutMapping
    @ApiMessage("Update product")
    public ResponseEntity<SanPham> update(@RequestBody SanPham sanPham) throws IdInvalidException {
        if (sanPham.getMaSanPham() == null) {
            throw new IdInvalidException("Mã sản phẩm không được để trống");
        }
        SanPham updated = sanPhamService.update(sanPham);
        return ResponseEntity.ok(updated);
    }

    /**
     * Xóa sản phẩm — chỉ ADMIN có permission mới gọi được
     */
    @DeleteMapping("/{id}")
    @ApiMessage("Delete product ")
    public ResponseEntity<Void> delete(@PathVariable("id") String maSanPham) throws IdInvalidException {
        SanPham sp = sanPhamService.findById(maSanPham);
        if (sp == null) {
            throw new IdInvalidException("Không tìm thấy sản phẩm: " + maSanPham);
        }
        sanPhamService.delete(maSanPham);
        return ResponseEntity.noContent().build();
    }
}
