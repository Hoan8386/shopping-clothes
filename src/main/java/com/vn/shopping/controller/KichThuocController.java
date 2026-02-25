package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.KichThuoc;
import com.vn.shopping.service.KichThuocService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/kich-thuoc")
public class KichThuocController {

    private final KichThuocService kichThuocService;

    public KichThuocController(KichThuocService kichThuocService) {
        this.kichThuocService = kichThuocService;
    }

    @GetMapping
    @ApiMessage("Get all sizes")
    public ResponseEntity<List<KichThuoc>> getAll() {
        return ResponseEntity.ok(kichThuocService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Get size by id")
    public ResponseEntity<KichThuoc> getById(@PathVariable("id") long id) throws IdInvalidException {
        KichThuoc kichThuoc = kichThuocService.findById(id);
        if (kichThuoc == null) {
            throw new IdInvalidException("Không tìm thấy kích thước: " + id);
        }
        return ResponseEntity.ok(kichThuoc);
    }

    @PostMapping
    @ApiMessage("Create a size")
    public ResponseEntity<KichThuoc> create(@RequestBody KichThuoc kichThuoc) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kichThuocService.create(kichThuoc));
    }

    @PutMapping
    @ApiMessage("Update size")
    public ResponseEntity<KichThuoc> update(@RequestBody KichThuoc kichThuoc) throws IdInvalidException {
        if (kichThuoc.getId() == 0) {
            throw new IdInvalidException("Mã kích thước không được để trống");
        }
        return ResponseEntity.ok(kichThuocService.update(kichThuoc));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete size")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (kichThuocService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy kích thước: " + id);
        }
        kichThuocService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
