package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;
import com.vn.shopping.service.KhuyenMaiTheoHoaDonService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/khuyen-mai-theo-hoa-don")
public class KhuyenMaiTheoHoaDonController {

    private final KhuyenMaiTheoHoaDonService khuyenMaiTheoHoaDonService;

    public KhuyenMaiTheoHoaDonController(KhuyenMaiTheoHoaDonService khuyenMaiTheoHoaDonService) {
        this.khuyenMaiTheoHoaDonService = khuyenMaiTheoHoaDonService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách khuyến mãi theo hóa đơn")
    public ResponseEntity<List<KhuyenMaiTheoHoaDon>> getAll() {
        return ResponseEntity.ok(khuyenMaiTheoHoaDonService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy khuyến mãi theo hóa đơn theo id")
    public ResponseEntity<KhuyenMaiTheoHoaDon> getById(@PathVariable("id") long id) throws IdInvalidException {
        KhuyenMaiTheoHoaDon khuyenMai = khuyenMaiTheoHoaDonService.findById(id);
        if (khuyenMai == null) {
            throw new IdInvalidException("Không tìm thấy khuyến mãi theo hóa đơn: " + id);
        }
        return ResponseEntity.ok(khuyenMai);
    }

    @PostMapping
    @ApiMessage("Tạo khuyến mãi theo hóa đơn")
    public ResponseEntity<KhuyenMaiTheoHoaDon> create(@RequestBody KhuyenMaiTheoHoaDon khuyenMai) {
        return ResponseEntity.status(HttpStatus.CREATED).body(khuyenMaiTheoHoaDonService.create(khuyenMai));
    }

    @PutMapping
    @ApiMessage("Cập nhật khuyến mãi theo hóa đơn")
    public ResponseEntity<KhuyenMaiTheoHoaDon> update(@RequestBody KhuyenMaiTheoHoaDon khuyenMai)
            throws IdInvalidException {
        if (khuyenMai.getId() == null || khuyenMai.getId() == 0) {
            throw new IdInvalidException("Mã khuyến mãi theo hóa đơn không được để trống");
        }
        return ResponseEntity.ok(khuyenMaiTheoHoaDonService.update(khuyenMai));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa khuyến mãi theo hóa đơn")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (khuyenMaiTheoHoaDonService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy khuyến mãi theo hóa đơn: " + id);
        }
        khuyenMaiTheoHoaDonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
