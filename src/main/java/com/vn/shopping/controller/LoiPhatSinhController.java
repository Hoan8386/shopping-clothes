package com.vn.shopping.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.LoiPhatSinh;
import com.vn.shopping.service.LoiPhatSinhService;
import com.vn.shopping.service.StorageService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/loi-phat-sinh")
public class LoiPhatSinhController {

    private final LoiPhatSinhService loiPhatSinhService;
    private final StorageService storageService;

    public LoiPhatSinhController(LoiPhatSinhService loiPhatSinhService, StorageService storageService) {
        this.loiPhatSinhService = loiPhatSinhService;
        this.storageService = storageService;
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

    @GetMapping("/cua-hang/{cuaHangId}")
    @ApiMessage("Lấy lỗi phát sinh theo cửa hàng")
    public ResponseEntity<List<LoiPhatSinh>> getByCuaHang(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        return ResponseEntity.ok(loiPhatSinhService.findByCuaHangId(cuaHangId, year, month));
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Upload ảnh lỗi phát sinh")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file)
            throws IdInvalidException {
        if (file.isEmpty()) {
            throw new IdInvalidException("File không được để trống");
        }
        String imageUrl = storageService.uploadSingleFile(file, "error");
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);
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
