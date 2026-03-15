package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.LoaiDonLuanChuyen;
import com.vn.shopping.service.LoaiDonLuanChuyenService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/loai-don-luan-chuyen")
public class LoaiDonLuanChuyenController {

    private final LoaiDonLuanChuyenService loaiDonLuanChuyenService;

    public LoaiDonLuanChuyenController(LoaiDonLuanChuyenService loaiDonLuanChuyenService) {
        this.loaiDonLuanChuyenService = loaiDonLuanChuyenService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách loại đơn luân chuyển")
    public ResponseEntity<List<LoaiDonLuanChuyen>> getAll() {
        return ResponseEntity.ok(loaiDonLuanChuyenService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy loại đơn luân chuyển theo id")
    public ResponseEntity<LoaiDonLuanChuyen> getById(@PathVariable("id") long id) throws IdInvalidException {
        LoaiDonLuanChuyen loai = loaiDonLuanChuyenService.findById(id);
        if (loai == null) {
            throw new IdInvalidException("Không tìm thấy loại đơn luân chuyển: " + id);
        }
        return ResponseEntity.ok(loai);
    }

    @PostMapping
    @ApiMessage("Tạo loại đơn luân chuyển")
    public ResponseEntity<LoaiDonLuanChuyen> create(@RequestBody LoaiDonLuanChuyen loaiDonLuanChuyen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loaiDonLuanChuyenService.create(loaiDonLuanChuyen));
    }

    @PutMapping
    @ApiMessage("Cập nhật loại đơn luân chuyển")
    public ResponseEntity<LoaiDonLuanChuyen> update(@RequestBody LoaiDonLuanChuyen loaiDonLuanChuyen)
            throws IdInvalidException {
        if (loaiDonLuanChuyen.getId() == null || loaiDonLuanChuyen.getId() == 0) {
            throw new IdInvalidException("Mã loại đơn luân chuyển không được để trống");
        }
        return ResponseEntity.ok(loaiDonLuanChuyenService.update(loaiDonLuanChuyen));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa loại đơn luân chuyển")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (loaiDonLuanChuyenService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy loại đơn luân chuyển: " + id);
        }
        loaiDonLuanChuyenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
