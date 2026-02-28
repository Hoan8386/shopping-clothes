package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.service.ChiTietPhieuNhapService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/chi-tiet-phieu-nhap")
public class ChiTietPhieuNhapController {

    private final ChiTietPhieuNhapService chiTietPhieuNhapService;

    public ChiTietPhieuNhapController(ChiTietPhieuNhapService chiTietPhieuNhapService) {
        this.chiTietPhieuNhapService = chiTietPhieuNhapService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách chi tiết phiếu nhập")
    public ResponseEntity<List<ChiTietPhieuNhap>> getAll() {
        return ResponseEntity.ok(chiTietPhieuNhapService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy chi tiết phiếu nhập theo id")
    public ResponseEntity<ChiTietPhieuNhap> getById(@PathVariable("id") long id) throws IdInvalidException {
        ChiTietPhieuNhap ct = chiTietPhieuNhapService.findById(id);
        if (ct == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết phiếu nhập: " + id);
        }
        return ResponseEntity.ok(ct);
    }

    @GetMapping("/phieu-nhap/{phieuNhapId}")
    @ApiMessage("Lấy chi tiết phiếu nhập theo mã phiếu nhập")
    public ResponseEntity<List<ChiTietPhieuNhap>> getByPhieuNhapId(
            @PathVariable("phieuNhapId") long phieuNhapId) {
        return ResponseEntity.ok(chiTietPhieuNhapService.findByPhieuNhapId(phieuNhapId));
    }

    @PostMapping
    @ApiMessage("Tạo chi tiết phiếu nhập")
    public ResponseEntity<ChiTietPhieuNhap> create(
            @RequestParam(value = "phieuNhapId", required = false) Long phieuNhapId,
            @RequestParam(value = "chiTietSanPhamId", required = false) Long chiTietSanPhamId,
            @RequestParam(value = "soLuong", required = false) Integer soLuong,
            @RequestParam(value = "ghiTru", required = false) String ghiTru,
            @RequestParam(value = "ghiTruKiemHang", required = false) String ghiTruKiemHang,
            @RequestParam(value = "trangThai", required = false) Integer trangThai) {
        ChiTietPhieuNhap created = chiTietPhieuNhapService.create(
                phieuNhapId, chiTietSanPhamId, soLuong, ghiTru, ghiTruKiemHang, trangThai);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    @ApiMessage("Cập nhật chi tiết phiếu nhập")
    public ResponseEntity<ChiTietPhieuNhap> update(@RequestBody ChiTietPhieuNhap chiTietPhieuNhap)
            throws IdInvalidException {
        if (chiTietPhieuNhap.getId() == null || chiTietPhieuNhap.getId() == 0) {
            throw new IdInvalidException("Mã chi tiết phiếu nhập không được để trống");
        }
        return ResponseEntity.ok(chiTietPhieuNhapService.update(chiTietPhieuNhap));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa chi tiết phiếu nhập")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (chiTietPhieuNhapService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết phiếu nhập: " + id);
        }
        chiTietPhieuNhapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
