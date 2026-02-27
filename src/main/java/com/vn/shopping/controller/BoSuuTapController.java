package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.BoSuuTap;
import com.vn.shopping.service.BoSuuTapService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/bo-suu-tap")
public class BoSuuTapController {

    private final BoSuuTapService boSuuTapService;

    public BoSuuTapController(BoSuuTapService boSuuTapService) {
        this.boSuuTapService = boSuuTapService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách bộ sưu tập")
    public ResponseEntity<List<BoSuuTap>> getAll() {
        return ResponseEntity.ok(boSuuTapService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy bộ sưu tập theo id")
    public ResponseEntity<BoSuuTap> getById(@PathVariable("id") long id) throws IdInvalidException {
        BoSuuTap boSuuTap = boSuuTapService.findById(id);
        if (boSuuTap == null) {
            throw new IdInvalidException("Không tìm thấy bộ sưu tập: " + id);
        }
        return ResponseEntity.ok(boSuuTap);
    }

    @PostMapping
    @ApiMessage("Tạo bộ sưu tập")
    public ResponseEntity<BoSuuTap> create(@RequestBody BoSuuTap boSuuTap) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boSuuTapService.create(boSuuTap));
    }

    @PutMapping
    @ApiMessage("Cập nhật bộ sưu tập")
    public ResponseEntity<BoSuuTap> update(@RequestBody BoSuuTap boSuuTap) throws IdInvalidException {
        if (boSuuTap.getId() == null || boSuuTap.getId() == 0) {
            throw new IdInvalidException("Mã bộ sưu tập không được để trống");
        }
        return ResponseEntity.ok(boSuuTapService.update(boSuuTap));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa bộ sưu tập")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (boSuuTapService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy bộ sưu tập: " + id);
        }
        boSuuTapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
