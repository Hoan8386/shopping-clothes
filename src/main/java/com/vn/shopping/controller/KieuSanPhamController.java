package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.KieuSanPham;
import com.vn.shopping.service.KieuSanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/kieu-san-pham")
public class KieuSanPhamController {

    private final KieuSanPhamService kieuSanPhamService;

    public KieuSanPhamController(KieuSanPhamService kieuSanPhamService) {
        this.kieuSanPhamService = kieuSanPhamService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách kiểu sản phẩm")
    public ResponseEntity<List<KieuSanPham>> getAll() {
        return ResponseEntity.ok(kieuSanPhamService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy kiểu sản phẩm theo id")
    public ResponseEntity<KieuSanPham> getById(@PathVariable("id") long id) throws IdInvalidException {
        KieuSanPham kieuSanPham = kieuSanPhamService.findById(id);
        if (kieuSanPham == null) {
            throw new IdInvalidException("Không tìm thấy kiểu sản phẩm: " + id);
        }
        return ResponseEntity.ok(kieuSanPham);
    }

    @PostMapping
    @ApiMessage("Tạo kiểu sản phẩm")
    public ResponseEntity<KieuSanPham> create(@RequestBody KieuSanPham kieuSanPham) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kieuSanPhamService.create(kieuSanPham));
    }

    @PutMapping
    @ApiMessage("Cập nhật kiểu sản phẩm")
    public ResponseEntity<KieuSanPham> update(@RequestBody KieuSanPham kieuSanPham) throws IdInvalidException {
        if (kieuSanPham.getId() == null || kieuSanPham.getId() == 0) {
            throw new IdInvalidException("Mã kiểu sản phẩm không được để trống");
        }
        return ResponseEntity.ok(kieuSanPhamService.update(kieuSanPham));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa kiểu sản phẩm")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (kieuSanPhamService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy kiểu sản phẩm: " + id);
        }
        kieuSanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
