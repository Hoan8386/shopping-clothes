package com.vn.shopping.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.response.ResSanPhamDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.SanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/san-pham")
public class SanPhamController {

    private final SanPhamService sanPhamService;

    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    /**
     * Xem danh sách sản phẩm + lọc theo nhiều tiêu chí + phân trang
     * Ví dụ: GET /api/v1/san-pham?tenSanPham=ao&kieuSanPhamId=1&thuongHieuId=2
     * &trangThai=1&giaMin=100000&giaMax=500000&page=1&size=10&sort=giaBan,asc
     * Nếu không truyền tham số nào => trả về tất cả sản phẩm (có phân trang)
     */
    @GetMapping
    @ApiMessage("Get all product")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(required = false) String tenSanPham,
            @RequestParam(required = false) Long kieuSanPhamId,
            @RequestParam(required = false) Long boSuuTapId,
            @RequestParam(required = false) Long thuongHieuId,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Double giaMin,
            @RequestParam(required = false) Double giaMax,
            Pageable pageable) {

        ResultPaginationDTO result = sanPhamService.filterSanPham(
                tenSanPham, kieuSanPhamId, boSuuTapId, thuongHieuId, trangThai, giaMin, giaMax, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Xem chi tiết 1 sản phẩm
     */
    @GetMapping("/{id}")
    @ApiMessage("Get detail product")
    public ResponseEntity<ResSanPhamDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        ResSanPhamDTO dto = sanPhamService.findByIdDTO(id);
        if (dto == null) {
            throw new IdInvalidException("Không tìm thấy sản phẩm: " + id);
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Tạo sản phẩm mới — chỉ ADMIN có permission mới gọi được
     * Gửi multipart/form-data: từng trường + "file" (ảnh, không bắt buộc)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Create a product")
    public ResponseEntity<ResSanPhamDTO> create(
            @RequestParam(value = "tenSanPham", required = false) String tenSanPham,
            @RequestParam(value = "giaVon", required = false) Double giaVon,
            @RequestParam(value = "giaBan", required = false) Double giaBan,
            @RequestParam(value = "giaGiam", required = false) Integer giaGiam,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "kieuSanPhamId", required = false) Long kieuSanPhamId,
            @RequestParam(value = "boSuuTapId", required = false) Long boSuuTapId,
            @RequestParam(value = "thuongHieuId", required = false) Long thuongHieuId,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        SanPham created = sanPhamService.createSanPham(tenSanPham, giaVon, giaBan, giaGiam,
                moTa, trangThai, kieuSanPhamId, boSuuTapId, thuongHieuId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(sanPhamService.convertToDTO(created));
    }

    /**
     * Cập nhật sản phẩm — chỉ ADMIN có permission mới gọi được
     * Gửi multipart/form-data: từng trường + "file" (ảnh mới, không bắt buộc)
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Update product")
    public ResponseEntity<ResSanPhamDTO> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "tenSanPham", required = false) String tenSanPham,
            @RequestParam(value = "giaVon", required = false) Double giaVon,
            @RequestParam(value = "giaBan", required = false) Double giaBan,
            @RequestParam(value = "giaGiam", required = false) Integer giaGiam,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "kieuSanPhamId", required = false) Long kieuSanPhamId,
            @RequestParam(value = "boSuuTapId", required = false) Long boSuuTapId,
            @RequestParam(value = "thuongHieuId", required = false) Long thuongHieuId,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        if (id == null || id == 0) {
            throw new IdInvalidException("Mã sản phẩm không được để trống");
        }

        SanPham updated = sanPhamService.updateSanPham(id, tenSanPham, giaVon, giaBan, giaGiam,
                moTa, trangThai, kieuSanPhamId, boSuuTapId, thuongHieuId, file);
        return ResponseEntity.ok(sanPhamService.convertToDTO(updated));
    }

    /**
     * Xóa sản phẩm — chỉ ADMIN có permission mới gọi được
     */
    @DeleteMapping("/{id}")
    @ApiMessage("Delete product ")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        SanPham sp = sanPhamService.findById(id);
        if (sp == null) {
            throw new IdInvalidException("Không tìm thấy sản phẩm: " + id);
        }
        sanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
