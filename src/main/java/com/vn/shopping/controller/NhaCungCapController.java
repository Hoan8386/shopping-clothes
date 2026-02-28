package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.service.NhaCungCapService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/nha-cung-cap")
public class NhaCungCapController {

    private final NhaCungCapService nhaCungCapService;

    public NhaCungCapController(NhaCungCapService nhaCungCapService) {
        this.nhaCungCapService = nhaCungCapService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách nhà cung cấp")
    public ResponseEntity<List<NhaCungCap>> getAll() {
        return ResponseEntity.ok(nhaCungCapService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy nhà cung cấp theo id")
    public ResponseEntity<NhaCungCap> getById(@PathVariable("id") long id) throws IdInvalidException {
        NhaCungCap nhaCungCap = nhaCungCapService.findById(id);
        if (nhaCungCap == null) {
            throw new IdInvalidException("Không tìm thấy nhà cung cấp: " + id);
        }
        return ResponseEntity.ok(nhaCungCap);
    }

    @PostMapping
    @ApiMessage("Tạo nhà cung cấp")
    public ResponseEntity<NhaCungCap> create(@RequestBody NhaCungCap nhaCungCap) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nhaCungCapService.create(nhaCungCap));
    }

    @PutMapping
    @ApiMessage("Cập nhật nhà cung cấp")
    public ResponseEntity<NhaCungCap> update(@RequestBody NhaCungCap nhaCungCap) throws IdInvalidException {
        if (nhaCungCap.getId() == null || nhaCungCap.getId() == 0) {
            throw new IdInvalidException("Mã nhà cung cấp không được để trống");
        }
        return ResponseEntity.ok(nhaCungCapService.update(nhaCungCap));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa nhà cung cấp")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (nhaCungCapService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy nhà cung cấp: " + id);
        }
        nhaCungCapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
