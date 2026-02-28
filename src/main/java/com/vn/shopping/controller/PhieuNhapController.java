package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.service.PhieuNhapService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/phieu-nhap")
public class PhieuNhapController {

    private final PhieuNhapService phieuNhapService;

    public PhieuNhapController(PhieuNhapService phieuNhapService) {
        this.phieuNhapService = phieuNhapService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách phiếu nhập")
    public ResponseEntity<List<PhieuNhap>> getAll() {
        return ResponseEntity.ok(phieuNhapService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy phiếu nhập theo id")
    public ResponseEntity<PhieuNhap> getById(@PathVariable("id") long id) throws IdInvalidException {
        PhieuNhap phieuNhap = phieuNhapService.findById(id);
        if (phieuNhap == null) {
            throw new IdInvalidException("Không tìm thấy phiếu nhập: " + id);
        }
        return ResponseEntity.ok(phieuNhap);
    }

    @PostMapping
    @ApiMessage("Tạo phiếu nhập")
    public ResponseEntity<PhieuNhap> create(@RequestBody PhieuNhap phieuNhap) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuNhapService.create(phieuNhap));
    }

    @PutMapping
    @ApiMessage("Cập nhật phiếu nhập")
    public ResponseEntity<PhieuNhap> update(@RequestBody PhieuNhap phieuNhap) throws IdInvalidException {
        if (phieuNhap.getId() == null || phieuNhap.getId() == 0) {
            throw new IdInvalidException("Mã phiếu nhập không được để trống");
        }
        return ResponseEntity.ok(phieuNhapService.update(phieuNhap));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa phiếu nhập")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (phieuNhapService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy phiếu nhập: " + id);
        }
        phieuNhapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
