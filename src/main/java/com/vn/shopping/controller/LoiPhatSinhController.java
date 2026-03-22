package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.LoiPhatSinh;
import com.vn.shopping.service.LoiPhatSinhService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/loi-phat-sinh")
public class LoiPhatSinhController {

    private final LoiPhatSinhService loiPhatSinhService;

    public LoiPhatSinhController(LoiPhatSinhService loiPhatSinhService) {
        this.loiPhatSinhService = loiPhatSinhService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách lỗi phát sinh")
    public ResponseEntity<List<LoiPhatSinh>> getAll() {
        return ResponseEntity.ok(loiPhatSinhService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy lỗi phát sinh theo id")
    public ResponseEntity<LoiPhatSinh> getById(@PathVariable("id") long id) throws IdInvalidException {
        LoiPhatSinh loiPhatSinh = loiPhatSinhService.findById(id);
        if (loiPhatSinh == null) {
            throw new IdInvalidException("Không tìm thấy lỗi phát sinh: " + id);
        }
        return ResponseEntity.ok(loiPhatSinh);
    }

    @GetMapping("/lich-lam-viec/{lichLamViecId}")
    @ApiMessage("Lấy lỗi phát sinh theo lịch làm việc")
    public ResponseEntity<List<LoiPhatSinh>> getByLichLamViec(
            @PathVariable("lichLamViecId") Long lichLamViecId) {
        return ResponseEntity.ok(loiPhatSinhService.findByLichLamViecId(lichLamViecId));
    }

    @PostMapping
    @ApiMessage("Tạo lỗi phát sinh")
    public ResponseEntity<LoiPhatSinh> create(@RequestBody LoiPhatSinh loiPhatSinh) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loiPhatSinhService.create(loiPhatSinh));
    }

    @PutMapping
    @ApiMessage("Cập nhật lỗi phát sinh")
    public ResponseEntity<LoiPhatSinh> update(@RequestBody LoiPhatSinh loiPhatSinh) throws IdInvalidException {
        if (loiPhatSinh.getId() == null || loiPhatSinh.getId() == 0) {
            throw new IdInvalidException("Mã lỗi phát sinh không được để trống");
        }
        return ResponseEntity.ok(loiPhatSinhService.update(loiPhatSinh));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa lỗi phát sinh")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (loiPhatSinhService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy lỗi phát sinh: " + id);
        }
        loiPhatSinhService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
