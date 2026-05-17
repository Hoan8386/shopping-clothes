package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.VanChuyen;
import com.vn.shopping.service.VanChuyenService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/van-chuyen")
public class VanChuyenController {

    private final VanChuyenService vanChuyenService;

    public VanChuyenController(VanChuyenService vanChuyenService) {
        this.vanChuyenService = vanChuyenService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách bên vận chuyển")
    public ResponseEntity<List<VanChuyen>> getAll() {
        return ResponseEntity.ok(vanChuyenService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy bên vận chuyển theo id")
    public ResponseEntity<VanChuyen> getById(@PathVariable("id") long id) throws IdInvalidException {
        VanChuyen vanChuyen = vanChuyenService.findById(id);
        if (vanChuyen == null) {
            throw new IdInvalidException("Không tìm thấy bên vận chuyển: " + id);
        }
        return ResponseEntity.ok(vanChuyen);
    }

    @PostMapping
    @ApiMessage("Tạo bên vận chuyển")
    public ResponseEntity<VanChuyen> create(@RequestBody VanChuyen vanChuyen) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(vanChuyenService.create(vanChuyen));
    }

    @PutMapping
    @ApiMessage("Cập nhật bên vận chuyển")
    public ResponseEntity<VanChuyen> update(@RequestBody VanChuyen vanChuyen) throws IdInvalidException {
        if (vanChuyen.getId() == null || vanChuyen.getId() == 0) {
            throw new IdInvalidException("Mã bên vận chuyển không được để trống");
        }
        return ResponseEntity.ok(vanChuyenService.update(vanChuyen));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa bên vận chuyển")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (vanChuyenService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy bên vận chuyển: " + id);
        }
        vanChuyenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}