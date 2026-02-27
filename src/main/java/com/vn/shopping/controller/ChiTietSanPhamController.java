package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.response.ResChiTietSanPhamDTO;
import com.vn.shopping.service.ChiTietSanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/chi-tiet-san-pham")
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;

    public ChiTietSanPhamController(ChiTietSanPhamService chiTietSanPhamService) {
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping
    @ApiMessage("Get all product details")
    public ResponseEntity<List<ResChiTietSanPhamDTO>> getAll() {
        return ResponseEntity.ok(chiTietSanPhamService.findAllDTO());
    }

    @GetMapping("/{id}")
    @ApiMessage("Get product detail by id")
    public ResponseEntity<ResChiTietSanPhamDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        ResChiTietSanPhamDTO dto = chiTietSanPhamService.findByIdDTO(id);
        if (dto == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + id);
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/san-pham/{sanPhamId}")
    @ApiMessage("Get product details by product id")
    public ResponseEntity<List<ResChiTietSanPhamDTO>> getBySanPhamId(@PathVariable("sanPhamId") long sanPhamId) {
        return ResponseEntity.ok(chiTietSanPhamService.findBySanPhamIdDTO(sanPhamId));
    }

    @PostMapping
    @ApiMessage("Create product detail")
    public ResponseEntity<ResChiTietSanPhamDTO> create(@RequestBody ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham created = chiTietSanPhamService.create(chiTietSanPham);
        return ResponseEntity.status(HttpStatus.CREATED).body(chiTietSanPhamService.convertToDTO(created));
    }

    @PutMapping
    @ApiMessage("Update product detail")
    public ResponseEntity<ResChiTietSanPhamDTO> update(@RequestBody ChiTietSanPham chiTietSanPham)
            throws IdInvalidException {
        if (chiTietSanPham.getId() == 0) {
            throw new IdInvalidException("Mã chi tiết sản phẩm không được để trống");
        }
        ChiTietSanPham updated = chiTietSanPhamService.update(chiTietSanPham);
        return ResponseEntity.ok(chiTietSanPhamService.convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete product detail")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (chiTietSanPhamService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + id);
        }
        chiTietSanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
