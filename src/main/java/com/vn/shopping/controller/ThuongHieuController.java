package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ThuongHieu;
import com.vn.shopping.service.ThuongHieuService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/thuong-hieu")
public class ThuongHieuController {

    private final ThuongHieuService thuongHieuService;

    public ThuongHieuController(ThuongHieuService thuongHieuService) {
        this.thuongHieuService = thuongHieuService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách thương hiệu")
    public ResponseEntity<List<ThuongHieu>> getAll() {
        return ResponseEntity.ok(thuongHieuService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy thương hiệu theo id")
    public ResponseEntity<ThuongHieu> getById(@PathVariable("id") long id) throws IdInvalidException {
        ThuongHieu thuongHieu = thuongHieuService.findById(id);
        if (thuongHieu == null) {
            throw new IdInvalidException("Không tìm thấy thương hiệu: " + id);
        }
        return ResponseEntity.ok(thuongHieu);
    }

    @PostMapping
    @ApiMessage("Tạo thương hiệu")
    public ResponseEntity<ThuongHieu> create(@RequestBody ThuongHieu thuongHieu) {
        return ResponseEntity.status(HttpStatus.CREATED).body(thuongHieuService.create(thuongHieu));
    }

    @PutMapping
    @ApiMessage("Cập nhật thương hiệu")
    public ResponseEntity<ThuongHieu> update(@RequestBody ThuongHieu thuongHieu) throws IdInvalidException {
        if (thuongHieu.getId() == null || thuongHieu.getId() == 0) {
            throw new IdInvalidException("Mã thương hiệu không được để trống");
        }
        return ResponseEntity.ok(thuongHieuService.update(thuongHieu));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa thương hiệu")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (thuongHieuService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy thương hiệu: " + id);
        }
        thuongHieuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
