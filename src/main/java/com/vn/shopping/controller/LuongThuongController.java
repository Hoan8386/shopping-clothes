package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.LuongThuong;
import com.vn.shopping.service.LuongThuongService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/luong-thuong")
public class LuongThuongController {

    private final LuongThuongService luongThuongService;

    public LuongThuongController(LuongThuongService luongThuongService) {
        this.luongThuongService = luongThuongService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách lương thưởng")
    public ResponseEntity<List<LuongThuong>> getAll() {
        return ResponseEntity.ok(luongThuongService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy lương thưởng theo id")
    public ResponseEntity<LuongThuong> getById(@PathVariable("id") long id) throws IdInvalidException {
        LuongThuong luongThuong = luongThuongService.findById(id);
        if (luongThuong == null) {
            throw new IdInvalidException("Không tìm thấy lương thưởng: " + id);
        }
        return ResponseEntity.ok(luongThuong);
    }

    @GetMapping("/nhan-vien/{nhanVienId}")
    @ApiMessage("Lấy lương thưởng theo nhân viên")
    public ResponseEntity<List<LuongThuong>> getByNhanVien(@PathVariable("nhanVienId") Long nhanVienId) {
        return ResponseEntity.ok(luongThuongService.findByNhanVienId(nhanVienId));
    }

    @PostMapping
    @ApiMessage("Tạo lương thưởng")
    public ResponseEntity<LuongThuong> create(@RequestBody LuongThuong luongThuong) {
        return ResponseEntity.status(HttpStatus.CREATED).body(luongThuongService.create(luongThuong));
    }

    @PutMapping
    @ApiMessage("Cập nhật lương thưởng")
    public ResponseEntity<LuongThuong> update(@RequestBody LuongThuong luongThuong) throws IdInvalidException {
        if (luongThuong.getId() == null || luongThuong.getId() == 0) {
            throw new IdInvalidException("Mã lương thưởng không được để trống");
        }
        return ResponseEntity.ok(luongThuongService.update(luongThuong));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa lương thưởng")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (luongThuongService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy lương thưởng: " + id);
        }
        luongThuongService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
