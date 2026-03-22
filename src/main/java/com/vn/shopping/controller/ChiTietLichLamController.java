package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietLichLam;
import com.vn.shopping.service.ChiTietLichLamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/chi-tiet-lich-lam")
public class ChiTietLichLamController {

    private final ChiTietLichLamService chiTietLichLamService;

    public ChiTietLichLamController(ChiTietLichLamService chiTietLichLamService) {
        this.chiTietLichLamService = chiTietLichLamService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách chi tiết lịch làm")
    public ResponseEntity<List<ChiTietLichLam>> getAll() {
        return ResponseEntity.ok(chiTietLichLamService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy chi tiết lịch làm theo id")
    public ResponseEntity<ChiTietLichLam> getById(@PathVariable("id") long id) throws IdInvalidException {
        ChiTietLichLam ct = chiTietLichLamService.findById(id);
        if (ct == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết lịch làm: " + id);
        }
        return ResponseEntity.ok(ct);
    }

    @GetMapping("/lich-lam-viec/{lichLamViecId}")
    @ApiMessage("Lấy chi tiết lịch làm theo lịch làm việc")
    public ResponseEntity<List<ChiTietLichLam>> getByLichLamViec(
            @PathVariable("lichLamViecId") Long lichLamViecId) {
        return ResponseEntity.ok(chiTietLichLamService.findByLichLamViecId(lichLamViecId));
    }

    @PostMapping
    @ApiMessage("Tạo chi tiết lịch làm")
    public ResponseEntity<ChiTietLichLam> create(@RequestBody ChiTietLichLam chiTietLichLam) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chiTietLichLamService.create(chiTietLichLam));
    }

    @PutMapping
    @ApiMessage("Cập nhật chi tiết lịch làm")
    public ResponseEntity<ChiTietLichLam> update(@RequestBody ChiTietLichLam chiTietLichLam)
            throws IdInvalidException {
        if (chiTietLichLam.getId() == null || chiTietLichLam.getId() == 0) {
            throw new IdInvalidException("Mã chi tiết lịch làm không được để trống");
        }
        return ResponseEntity.ok(chiTietLichLamService.update(chiTietLichLam));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa chi tiết lịch làm")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (chiTietLichLamService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết lịch làm: " + id);
        }
        chiTietLichLamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
