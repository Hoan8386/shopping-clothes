package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.DoiCa;
import com.vn.shopping.service.DoiCaService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/doi-ca")
public class DoiCaController {

    private final DoiCaService doiCaService;

    public DoiCaController(DoiCaService doiCaService) {
        this.doiCaService = doiCaService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách đổi ca")
    public ResponseEntity<List<DoiCa>> getAll() {
        return ResponseEntity.ok(doiCaService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy đổi ca theo id")
    public ResponseEntity<DoiCa> getById(@PathVariable("id") long id) throws IdInvalidException {
        DoiCa doiCa = doiCaService.findById(id);
        if (doiCa == null) {
            throw new IdInvalidException("Không tìm thấy đổi ca: " + id);
        }
        return ResponseEntity.ok(doiCa);
    }

    @GetMapping("/lich-lam-viec/{lichLamViecId}")
    @ApiMessage("Lấy đổi ca theo lịch làm việc")
    public ResponseEntity<List<DoiCa>> getByLichLamViec(@PathVariable("lichLamViecId") Long lichLamViecId) {
        return ResponseEntity.ok(doiCaService.findByLichLamViecId(lichLamViecId));
    }

    @PostMapping
    @ApiMessage("Tạo đổi ca")
    public ResponseEntity<DoiCa> create(@RequestBody DoiCa doiCa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doiCaService.create(doiCa));
    }

    @PutMapping
    @ApiMessage("Cập nhật đổi ca")
    public ResponseEntity<DoiCa> update(@RequestBody DoiCa doiCa) throws IdInvalidException {
        if (doiCa.getId() == null || doiCa.getId() == 0) {
            throw new IdInvalidException("Mã đổi ca không được để trống");
        }
        return ResponseEntity.ok(doiCaService.update(doiCa));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa đổi ca")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (doiCaService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy đổi ca: " + id);
        }
        doiCaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
