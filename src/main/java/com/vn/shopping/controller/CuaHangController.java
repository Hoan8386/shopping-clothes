package com.vn.shopping.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.response.ResCuaHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.CuaHangService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/cua-hang")
public class CuaHangController {

    private final CuaHangService cuaHangService;

    public CuaHangController(CuaHangService cuaHangService) {
        this.cuaHangService = cuaHangService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách cửa hàng")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer status,
            Pageable pageable) {
        return ResponseEntity.ok(cuaHangService.filter(name, address, status, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy cửa hàng theo id")
    public ResponseEntity<ResCuaHangDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        CuaHang cuaHang = cuaHangService.findById(id);
        if (cuaHang == null) {
            throw new IdInvalidException("Không tìm thấy cửa hàng: " + id);
        }
        return ResponseEntity.ok(cuaHangService.toDTO(cuaHang));
    }

    @PostMapping
    @ApiMessage("Tạo cửa hàng")
    public ResponseEntity<ResCuaHangDTO> create(@RequestBody CuaHang cuaHang) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuaHangService.toDTO(cuaHangService.create(cuaHang)));
    }

    @PutMapping
    @ApiMessage("Cập nhật cửa hàng")
    public ResponseEntity<ResCuaHangDTO> update(@RequestBody CuaHang cuaHang) throws IdInvalidException {
        if (cuaHang.getId() == null || cuaHang.getId() == 0) {
            throw new IdInvalidException("Mã cửa hàng không được để trống");
        }
        return ResponseEntity.ok(cuaHangService.toDTO(cuaHangService.update(cuaHang)));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa cửa hàng")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (cuaHangService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy cửa hàng: " + id);
        }
        cuaHangService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
