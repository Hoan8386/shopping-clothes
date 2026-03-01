package com.vn.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.DanhGiaSanPham;
import com.vn.shopping.domain.request.ReqDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
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

    /**
     * Xem tất cả đánh giá
     */
    @GetMapping
    @ApiMessage("Lấy tất cả đánh giá sản phẩm")
    public ResponseEntity<List<ResDanhGiaSanPhamDTO>> getAll() {
        List<ResDanhGiaSanPhamDTO> dtos = danhGiaSanPhamService.findAll().stream()
                .map(danhGiaSanPhamService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Xem đánh giá theo id
     */
    @GetMapping("/{id}")
    @ApiMessage("Lấy đánh giá theo id")
    public ResponseEntity<ResDanhGiaSanPhamDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        DanhGiaSanPham dg = danhGiaSanPhamService.findById(id);
        if (dg == null) {
            throw new IdInvalidException("Không tìm thấy đánh giá: " + id);
        }
        return ResponseEntity.ok(danhGiaSanPhamService.convertToDTO(dg));
    }

    /**
     * Xem đánh giá theo sản phẩm + phân trang
     */
    @GetMapping("/san-pham/{sanPhamId}")
    @ApiMessage("Lấy đánh giá theo sản phẩm")
    public ResponseEntity<ResultPaginationDTO> getBySanPham(
            @PathVariable("sanPhamId") Long sanPhamId,
            Pageable pageable) {
        return ResponseEntity.ok(danhGiaSanPhamService.findBySanPhamIdPaginated(sanPhamId, pageable));
    }

    /**
     * Xem đánh giá của tôi (khách hàng đang đăng nhập)
     */
    @GetMapping("/cua-toi")
    @ApiMessage("Lấy đánh giá của khách hàng đang đăng nhập")
    public ResponseEntity<List<ResDanhGiaSanPhamDTO>> getMyReviews() throws IdInvalidException {
        return ResponseEntity.ok(danhGiaSanPhamService.findByCurrentKhachHang());
    }

    /**
     * Tạo đánh giá sản phẩm — Khách hàng
     * Điều kiện: đã mua hàng + đơn hàng thành công (trạng thái = 3)
     * Gửi multipart/form-data: thông tin đánh giá + file ảnh (không bắt buộc)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo đánh giá sản phẩm")
    public ResponseEntity<ResDanhGiaSanPhamDTO> create(
            @RequestParam("sanPhamId") Long sanPhamId,
            @RequestParam("donHangId") Long donHangId,
            @RequestParam("soSao") Integer soSao,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IdInvalidException {

        ReqDanhGiaSanPhamDTO req = new ReqDanhGiaSanPhamDTO();
        req.setSanPhamId(sanPhamId);
        req.setDonHangId(donHangId);
        req.setSoSao(soSao);
        req.setGhiChu(ghiChu);

        DanhGiaSanPham created = danhGiaSanPhamService.create(req, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(danhGiaSanPhamService.convertToDTO(created));
    }

    /**
     * Cập nhật đánh giá — chỉ chủ đánh giá mới được sửa
     * Gửi multipart/form-data: thông tin cập nhật + file ảnh mới (không bắt buộc)
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Cập nhật đánh giá sản phẩm")
    public ResponseEntity<ResDanhGiaSanPhamDTO> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "soSao", required = false) Integer soSao,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IdInvalidException {

        if (id == null || id == 0) {
            throw new IdInvalidException("Mã đánh giá không được để trống");
        }

        ReqDanhGiaSanPhamDTO req = new ReqDanhGiaSanPhamDTO();
        req.setSoSao(soSao);
        req.setGhiChu(ghiChu);

        DanhGiaSanPham updated = danhGiaSanPhamService.update(id, req, file);
        return ResponseEntity.ok(danhGiaSanPhamService.convertToDTO(updated));
    }

    /**
     * Xóa đánh giá — chủ đánh giá hoặc Admin
     */
    @DeleteMapping("/{id}")
    @ApiMessage("Xóa đánh giá sản phẩm")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        danhGiaSanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
