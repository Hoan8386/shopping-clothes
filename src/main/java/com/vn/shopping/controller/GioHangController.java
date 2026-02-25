package com.vn.shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietGioHang;
import com.vn.shopping.domain.request.ReqThemGioHangDTO;
import com.vn.shopping.domain.response.ResGioHangDTO;
import com.vn.shopping.service.GioHangService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/gio-hang")
public class GioHangController {

    private final GioHangService gioHangService;

    public GioHangController(GioHangService gioHangService) {
        this.gioHangService = gioHangService;
    }

    @PostMapping("/them-san-pham")
    @ApiMessage("Thêm sản phẩm vào giỏ hàng")
    public ResponseEntity<ChiTietGioHang> themSanPham(@RequestBody ReqThemGioHangDTO req) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(gioHangService.themSanPhamVaoGioHang(req));
    }

    @GetMapping("/khach-hang/{khachHangId}")
    @ApiMessage("Lấy giỏ hàng theo mã khách hàng")
    public ResponseEntity<ResGioHangDTO> getByKhachHangId(@PathVariable("khachHangId") Long khachHangId)
            throws IdInvalidException {
        ResGioHangDTO gioHang = gioHangService.findByKhachHangId(khachHangId);
        if (gioHang == null) {
            throw new IdInvalidException("Không tìm thấy giỏ hàng của khách hàng: " + khachHangId);
        }
        return ResponseEntity.ok(gioHang);
    }

    @DeleteMapping("/chi-tiet/{maChiTietGioHang}")
    @ApiMessage("Xóa sản phẩm khỏi giỏ hàng")
    public ResponseEntity<Void> xoaSanPham(
            @PathVariable("maChiTietGioHang") Long maChiTietGioHang) throws IdInvalidException {
        gioHangService.xoaChiTietGioHang(maChiTietGioHang);
        return ResponseEntity.noContent().build();
    }
}
