package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietGioHang;
import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.service.GioHangService;
import com.vn.shopping.service.KhachHangService;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/gio-hang")
public class GioHangController {

    private final GioHangService gioHangService;
    private final KhachHangService khachHangService;

    public GioHangController(GioHangService gioHangService, KhachHangService khachHangService) {
        this.gioHangService = gioHangService;
        this.khachHangService = khachHangService;
    }

    /**
     * Lấy khách hàng đang đăng nhập từ JWT
     */
    private KhachHang getCurrentKhachHang() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new IdInvalidException("Bạn chưa đăng nhập"));
        KhachHang khachHang = khachHangService.findByEmail(email);
        if (khachHang == null) {
            throw new IdInvalidException("Không tìm thấy tài khoản khách hàng");
        }
        return khachHang;
    }

    /**
     * Xem giỏ hàng
     */
    @GetMapping
    public ResponseEntity<List<ChiTietGioHang>> getCart() throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        var gioHang = gioHangService.getOrCreateGioHang(khachHang);
        List<ChiTietGioHang> items = gioHangService.getChiTietGioHang(gioHang);
        return ResponseEntity.ok(items);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     * Body: { "maChiTietSanPham": "CT1", "soLuong": 2 }
     */
    @PostMapping
    public ResponseEntity<ChiTietGioHang> addToCart(@RequestBody AddToCartRequest request)
            throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        ChiTietGioHang item = gioHangService.addToCart(
                khachHang, request.getMaChiTietSanPham(), request.getSoLuong());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     * Body: { "maChiTietSanPham": "CT1", "soLuong": 5 }
     */
    @PutMapping
    public ResponseEntity<ChiTietGioHang> updateCartItem(@RequestBody AddToCartRequest request)
            throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        ChiTietGioHang item = gioHangService.updateCartItem(
                khachHang, request.getMaChiTietSanPham(), request.getSoLuong());
        return ResponseEntity.ok(item);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @DeleteMapping("/{maChiTietSanPham}")
    public ResponseEntity<Void> removeFromCart(@PathVariable String maChiTietSanPham)
            throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        gioHangService.removeFromCart(khachHang, maChiTietSanPham);
        return ResponseEntity.noContent().build();
    }

    /**
     * DTO cho request thêm/cập nhật giỏ hàng
     */
    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    public static class AddToCartRequest {
        private String maChiTietSanPham;
        private int soLuong = 1;
    }
}
