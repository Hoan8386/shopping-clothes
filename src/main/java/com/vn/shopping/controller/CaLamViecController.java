package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.CaLamViec;
import com.vn.shopping.service.CaLamViecService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/ca-lam-viec")
public class CaLamViecController {

    private final CaLamViecService caLamViecService;

    public CaLamViecController(CaLamViecService caLamViecService) {
        this.caLamViecService = caLamViecService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách ca làm việc")
    public ResponseEntity<List<CaLamViec>> getAll() {
        return ResponseEntity.ok(caLamViecService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy ca làm việc theo id")
    public ResponseEntity<CaLamViec> getById(@PathVariable("id") long id) throws IdInvalidException {
        CaLamViec caLamViec = caLamViecService.findById(id);
        if (caLamViec == null) {
            throw new IdInvalidException("Không tìm thấy ca làm việc: " + id);
        }
        return ResponseEntity.ok(caLamViec);
    }

    @PostMapping
    @ApiMessage("Tạo ca làm việc")
    public ResponseEntity<CaLamViec> create(@RequestBody CaLamViec caLamViec) {
        return ResponseEntity.status(HttpStatus.CREATED).body(caLamViecService.create(caLamViec));
    }

    @PutMapping
    @ApiMessage("Cập nhật ca làm việc")
    public ResponseEntity<CaLamViec> update(@RequestBody CaLamViec caLamViec) throws IdInvalidException {
        if (caLamViec.getId() == null || caLamViec.getId() == 0) {
            throw new IdInvalidException("Mã ca làm việc không được để trống");
        }
        return ResponseEntity.ok(caLamViecService.update(caLamViec));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa ca làm việc")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (caLamViecService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy ca làm việc: " + id);
        }
        caLamViecService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
