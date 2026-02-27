package com.vn.shopping.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     */
    @PostMapping
    @ApiMessage("Create a product")
    public ResponseEntity<ResSanPhamDTO> create(@RequestBody SanPham sanPham) {
        SanPham created = sanPhamService.create(sanPham);
        return ResponseEntity.status(HttpStatus.CREATED).body(sanPhamService.convertToDTO(created));
    }

    /**
     * Cập nhật sản phẩm — chỉ ADMIN có permission mới gọi được
     */
    @PutMapping
    @ApiMessage("Update product")
    public ResponseEntity<ResSanPhamDTO> update(@RequestBody SanPham sanPham) throws IdInvalidException {
        if (sanPham.getId() == 0) {
            throw new IdInvalidException("Mã sản phẩm không được để trống");
        }
        SanPham updated = sanPhamService.update(sanPham);
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
