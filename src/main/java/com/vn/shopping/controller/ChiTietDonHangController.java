package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietDonHang;
import com.vn.shopping.service.ChiTietDonHangService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/chi-tiet-don-hang")
public class ChiTietDonHangController {

    private final ChiTietDonHangService chiTietDonHangService;

    public ChiTietDonHangController(ChiTietDonHangService chiTietDonHangService) {
        this.chiTietDonHangService = chiTietDonHangService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách chi tiết đơn hàng")
    public ResponseEntity<List<ChiTietDonHang>> getAll() {
        return ResponseEntity.ok(chiTietDonHangService.findAll());
    }

    @GetMapping("/don-hang/{donHangId}")
    @ApiMessage("Lấy chi tiết đơn hàng theo mã đơn")
    public ResponseEntity<List<ChiTietDonHang>> getByDonHangId(@PathVariable("donHangId") long donHangId) {
        return ResponseEntity.ok(chiTietDonHangService.findByDonHangId(donHangId));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy chi tiết đơn hàng theo id")
    public ResponseEntity<ChiTietDonHang> getById(@PathVariable("id") long id) throws IdInvalidException {
        ChiTietDonHang chiTiet = chiTietDonHangService.findById(id);
        if (chiTiet == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết đơn hàng: " + id);
        }
        return ResponseEntity.ok(chiTiet);
    }

    @PostMapping
    @ApiMessage("Tạo chi tiết đơn hàng")
    public ResponseEntity<ChiTietDonHang> create(@RequestBody ChiTietDonHang chiTiet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chiTietDonHangService.create(chiTiet));
    }

    @PutMapping
    @ApiMessage("Cập nhật chi tiết đơn hàng")
    public ResponseEntity<ChiTietDonHang> update(@RequestBody ChiTietDonHang chiTiet) throws IdInvalidException {
        if (chiTiet.getId() == null || chiTiet.getId() == 0) {
            throw new IdInvalidException("Mã chi tiết đơn hàng không được để trống");
        }
        return ResponseEntity.ok(chiTietDonHangService.update(chiTiet));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa chi tiết đơn hàng")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (chiTietDonHangService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết đơn hàng: " + id);
        }
        chiTietDonHangService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
