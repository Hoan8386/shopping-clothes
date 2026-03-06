package com.vn.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.DanhGiaSanPham;
import com.vn.shopping.domain.request.ReqDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResDanhGiaSanPhamDTO;
import com.vn.shopping.service.DanhGiaSanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/danh-gia-san-pham")
public class DanhGiaSanPhamController {

    private final DanhGiaSanPhamService danhGiaSanPhamService;

    public DanhGiaSanPhamController(DanhGiaSanPhamService danhGiaSanPhamService) {
        this.danhGiaSanPhamService = danhGiaSanPhamService;
    }

    @GetMapping
    @ApiMessage("Lấy tất cả đánh giá sản phẩm")
    public ResponseEntity<List<ResDanhGiaSanPhamDTO>> getAll() {
        List<ResDanhGiaSanPhamDTO> dtos = danhGiaSanPhamService.findAll().stream()
                .map(danhGiaSanPhamService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy đánh giá theo id")
    public ResponseEntity<ResDanhGiaSanPhamDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        DanhGiaSanPham dg = danhGiaSanPhamService.findById(id);
        if (dg == null) {
            throw new IdInvalidException("Không tìm thấy đánh giá: " + id);
        }
        return ResponseEntity.ok(danhGiaSanPhamService.convertToDTO(dg));
    }

    @GetMapping("/san-pham/{sanPhamId}")
    @ApiMessage("Lấy đánh giá theo sản phẩm")
    public ResponseEntity<List<ResDanhGiaSanPhamDTO>> getBySanPham(
            @PathVariable("sanPhamId") Long sanPhamId) {
        List<ResDanhGiaSanPhamDTO> dtos = danhGiaSanPhamService.findBySanPhamId(sanPhamId).stream()
                .map(danhGiaSanPhamService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/chi-tiet-don-hang/{chiTietDonHangId}")
    @ApiMessage("Lấy đánh giá theo chi tiết đơn hàng")
    public ResponseEntity<List<ResDanhGiaSanPhamDTO>> getByChiTietDonHang(
            @PathVariable("chiTietDonHangId") Long chiTietDonHangId) {
        List<ResDanhGiaSanPhamDTO> dtos = danhGiaSanPhamService.findByChiTietDonHangId(chiTietDonHangId).stream()
                .map(danhGiaSanPhamService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cua-toi")
    @ApiMessage("Lấy đánh giá của khách hàng đang đăng nhập")
    public ResponseEntity<List<ResDanhGiaSanPhamDTO>> getMyReviews() throws IdInvalidException {
        return ResponseEntity.ok(danhGiaSanPhamService.findByCurrentKhachHang());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo đánh giá sản phẩm")
    public ResponseEntity<ResDanhGiaSanPhamDTO> create(
            @RequestParam("chiTietDonHangId") Long chiTietDonHangId,
            @RequestParam("soSao") Integer soSao,
            @RequestParam(value = "ghiTru", required = false) String ghiTru,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IdInvalidException {

        ReqDanhGiaSanPhamDTO req = new ReqDanhGiaSanPhamDTO();
        req.setChiTietDonHangId(chiTietDonHangId);
        req.setSoSao(soSao);
        req.setGhiTru(ghiTru);

        DanhGiaSanPham created = danhGiaSanPhamService.create(req, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(danhGiaSanPhamService.convertToDTO(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Cập nhật đánh giá sản phẩm")
    public ResponseEntity<ResDanhGiaSanPhamDTO> update(
            @PathVariable("id") Long id,
            @RequestParam(value = "soSao", required = false) Integer soSao,
            @RequestParam(value = "ghiTru", required = false) String ghiTru,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IdInvalidException {

        ReqDanhGiaSanPhamDTO req = new ReqDanhGiaSanPhamDTO();
        req.setSoSao(soSao);
        req.setGhiTru(ghiTru);

        DanhGiaSanPham updated = danhGiaSanPhamService.update(id, req, file);
        return ResponseEntity.ok(danhGiaSanPhamService.convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa đánh giá sản phẩm")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        danhGiaSanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
