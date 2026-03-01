package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.KhuyenMaiTheoDiem;
import com.vn.shopping.service.KhuyenMaiTheoDiemService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/khuyen-mai-theo-diem")
public class KhuyenMaiTheoDiemController {

    private final KhuyenMaiTheoDiemService khuyenMaiTheoDiemService;

    public KhuyenMaiTheoDiemController(KhuyenMaiTheoDiemService khuyenMaiTheoDiemService) {
        this.khuyenMaiTheoDiemService = khuyenMaiTheoDiemService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách khuyến mãi theo điểm")
    public ResponseEntity<List<KhuyenMaiTheoDiem>> getAll() {
        return ResponseEntity.ok(khuyenMaiTheoDiemService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy khuyến mãi theo điểm theo id")
    public ResponseEntity<KhuyenMaiTheoDiem> getById(@PathVariable("id") long id) throws IdInvalidException {
        KhuyenMaiTheoDiem khuyenMai = khuyenMaiTheoDiemService.findById(id);
        if (khuyenMai == null) {
            throw new IdInvalidException("Không tìm thấy khuyến mãi theo điểm: " + id);
        }
        return ResponseEntity.ok(khuyenMai);
    }

    @PostMapping
    @ApiMessage("Tạo khuyến mãi theo điểm")
    public ResponseEntity<KhuyenMaiTheoDiem> create(@RequestBody KhuyenMaiTheoDiem khuyenMai) {
        return ResponseEntity.status(HttpStatus.CREATED).body(khuyenMaiTheoDiemService.create(khuyenMai));
    }

    @PutMapping
    @ApiMessage("Cập nhật khuyến mãi theo điểm")
    public ResponseEntity<KhuyenMaiTheoDiem> update(@RequestBody KhuyenMaiTheoDiem khuyenMai)
            throws IdInvalidException {
        if (khuyenMai.getId() == null || khuyenMai.getId() == 0) {
            throw new IdInvalidException("Mã khuyến mãi theo điểm không được để trống");
        }
        return ResponseEntity.ok(khuyenMaiTheoDiemService.update(khuyenMai));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa khuyến mãi theo điểm")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (khuyenMaiTheoDiemService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy khuyến mãi theo điểm: " + id);
        }
        khuyenMaiTheoDiemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
