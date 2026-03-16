package com.vn.shopping.controller;

import com.vn.shopping.domain.LoaiKiemKe;
import com.vn.shopping.domain.request.ReqLoaiKiemKeDTO;
import com.vn.shopping.service.LoaiKiemKeService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loai-kiem-ke")
public class LoaiKiemKeController {

    private final LoaiKiemKeService loaiKiemKeService;

    public LoaiKiemKeController(LoaiKiemKeService loaiKiemKeService) {
        this.loaiKiemKeService = loaiKiemKeService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách loại kiểm kê")
    public ResponseEntity<List<LoaiKiemKe>> getAll() {
        return ResponseEntity.ok(loaiKiemKeService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy loại kiểm kê theo id")
    public ResponseEntity<LoaiKiemKe> getById(@PathVariable("id") Long id) throws IdInvalidException {
        LoaiKiemKe item = loaiKiemKeService.findById(id);
        if (item == null) {
            throw new IdInvalidException("Không tìm thấy loại kiểm kê: " + id);
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping
    @ApiMessage("Tạo loại kiểm kê")
    public ResponseEntity<LoaiKiemKe> create(@RequestBody ReqLoaiKiemKeDTO dto) {
        LoaiKiemKe created = loaiKiemKeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    @ApiMessage("Cập nhật loại kiểm kê")
    public ResponseEntity<LoaiKiemKe> update(@RequestBody ReqLoaiKiemKeDTO dto) throws IdInvalidException {
        if (dto.getId() == null || dto.getId() == 0) {
            throw new IdInvalidException("Mã loại kiểm kê không được để trống");
        }
        return ResponseEntity.ok(loaiKiemKeService.update(dto));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa loại kiểm kê")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        if (loaiKiemKeService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy loại kiểm kê: " + id);
        }
        loaiKiemKeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
