package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vn.shopping.domain.request.ReqCapNhatKhachGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqCapNhatKhuyenMaiGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqCapNhatSoLuongGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqThanhToanGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqThemSanPhamGioHangNhanVienDTO;
import com.vn.shopping.domain.response.ResDonHangDTO;
import com.vn.shopping.domain.response.ResGioHangNhanVienDTO;
import com.vn.shopping.service.GioHangNhanVienService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/gio-hang-nhan-vien")
public class GioHangNhanVienController {

    private final GioHangNhanVienService gioHangNhanVienService;

    public GioHangNhanVienController(GioHangNhanVienService gioHangNhanVienService) {
        this.gioHangNhanVienService = gioHangNhanVienService;
    }

    @GetMapping("/danh-sach")
    @ApiMessage("Lấy danh sách các giỏ hàng đang tạo (chưa thanh toán) của nhân viên")
    public ResponseEntity<List<ResGioHangNhanVienDTO>> getAllDraftCarts() throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.getAllDraftCarts());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy chi tiết một giỏ hàng theo ID")
    public ResponseEntity<ResGioHangNhanVienDTO> getDraftCartById(@PathVariable("id") Long id)
            throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.getDraftCartById(id));
    }

    @PostMapping("/moi")
    @ApiMessage("Tạo một giỏ hàng mới để bắt đầu tạo đơn tại quầy")
    public ResponseEntity<ResGioHangNhanVienDTO> createNewDraftCart() throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.createNewDraftCart());
    }

    @GetMapping("/hien-tai")
    @ApiMessage("Lấy giỏ hàng hiện tại của nhân viên (hoặc tạo mới nếu chưa có)")
    public ResponseEntity<ResGioHangNhanVienDTO> getCurrentCart() throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.getCurrentCart());
    }

    @PutMapping("/thong-tin-khach")
    @ApiMessage("Cập nhật thông tin khách hàng cho giỏ hàng nhân viên")
    public ResponseEntity<ResGioHangNhanVienDTO> capNhatThongTinKhach(
            @RequestBody ReqCapNhatKhachGioHangNhanVienDTO req,
            @RequestParam(required = false) Long cartId) throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.capNhatThongTinKhach(req, cartId));
    }

    @PostMapping("/them-san-pham")
    @ApiMessage("Thêm sản phẩm vào giỏ hàng nhân viên bằng chọn tay hoặc quét mã vạch")
    public ResponseEntity<ResGioHangNhanVienDTO> themSanPham(
            @RequestBody ReqThemSanPhamGioHangNhanVienDTO req,
            @RequestParam(required = false) Long cartId) throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.themSanPham(req, cartId));
    }

    @PutMapping("/chi-tiet/{id}")
    @ApiMessage("Cập nhật số lượng sản phẩm trong giỏ hàng nhân viên")
    public ResponseEntity<ResGioHangNhanVienDTO> capNhatSoLuong(
            @PathVariable("id") Long id,
            @RequestBody ReqCapNhatSoLuongGioHangNhanVienDTO req,
            @RequestParam(required = false) Long cartId) throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.capNhatSoLuong(id, req, cartId));
    }

    @DeleteMapping("/chi-tiet/{id}")
    @ApiMessage("Xóa sản phẩm khỏi giỏ hàng nhân viên")
    public ResponseEntity<ResGioHangNhanVienDTO> xoaSanPham(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Long cartId) throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.xoaSanPham(id, cartId));
    }

    @PutMapping("/khuyen-mai")
    @ApiMessage("Cập nhật mã khuyến mãi áp dụng cho giỏ hàng nhân viên")
    public ResponseEntity<ResGioHangNhanVienDTO> capNhatKhuyenMai(
            @RequestBody ReqCapNhatKhuyenMaiGioHangNhanVienDTO req,
            @RequestParam(required = false) Long cartId) throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.capNhatKhuyenMai(req, cartId));
    }

    @PostMapping("/thanh-toan")
    @ApiMessage("Thanh toán giỏ hàng nhân viên và tạo đơn hàng")
    public ResponseEntity<ResDonHangDTO> thanhToan(
            @RequestBody ReqThanhToanGioHangNhanVienDTO req,
            @RequestParam(required = false) Long cartId)
            throws IdInvalidException {
        return ResponseEntity.ok(gioHangNhanVienService.thanhToan(req, cartId));
    }
}
