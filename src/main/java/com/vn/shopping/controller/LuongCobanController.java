package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.LuongCoban;
import com.vn.shopping.service.LuongCobanService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/luong-co-ban")
public class LuongCobanController {

    private final LuongCobanService luongCobanService;

    public LuongCobanController(LuongCobanService luongCobanService) {
        this.luongCobanService = luongCobanService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách lương cơ bản")
    public ResponseEntity<List<LuongCoban>> getAll() {
        return ResponseEntity.ok(luongCobanService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy lương cơ bản theo id")
    public ResponseEntity<LuongCoban> getById(@PathVariable("id") long id) throws IdInvalidException {
        LuongCoban luongCoban = luongCobanService.findById(id);
        if (luongCoban == null) {
            throw new IdInvalidException("Không tìm thấy lương cơ bản: " + id);
        }
        return ResponseEntity.ok(luongCoban);
    }

    @GetMapping("/nhan-vien/{nhanVienId}")
    @ApiMessage("Lấy lương cơ bản theo nhân viên")
    public ResponseEntity<List<LuongCoban>> getByNhanVien(@PathVariable("nhanVienId") Long nhanVienId) {
        return ResponseEntity.ok(luongCobanService.findByNhanVienId(nhanVienId));
    }

    @PostMapping
    @ApiMessage("Tạo lương cơ bản")
    public ResponseEntity<LuongCoban> create(@RequestBody LuongCoban luongCoban) {
        return ResponseEntity.status(HttpStatus.CREATED).body(luongCobanService.create(luongCoban));
    }

    @PutMapping
    @ApiMessage("Cập nhật lương cơ bản")
    public ResponseEntity<LuongCoban> update(@RequestBody LuongCoban luongCoban) throws IdInvalidException {
        if (luongCoban.getId() == null || luongCoban.getId() == 0) {
            throw new IdInvalidException("Mã lương cơ bản không được để trống");
        }
        return ResponseEntity.ok(luongCobanService.update(luongCoban));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa lương cơ bản")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (luongCobanService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy lương cơ bản: " + id);
        }
        luongCobanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
