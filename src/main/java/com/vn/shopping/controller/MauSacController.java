package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.MauSac;
import com.vn.shopping.service.MauSacService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/mau-sac")
public class MauSacController {

    private final MauSacService mauSacService;

    public MauSacController(MauSacService mauSacService) {
        this.mauSacService = mauSacService;
    }

    @GetMapping
    @ApiMessage("Get all colors")
    public ResponseEntity<List<MauSac>> getAll() {
        return ResponseEntity.ok(mauSacService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Get color by id")
    public ResponseEntity<MauSac> getById(@PathVariable("id") long id) throws IdInvalidException {
        MauSac mauSac = mauSacService.findById(id);
        if (mauSac == null) {
            throw new IdInvalidException("Không tìm thấy màu sắc: " + id);
        }
        return ResponseEntity.ok(mauSac);
    }

    @PostMapping
    @ApiMessage("Create a color")
    public ResponseEntity<MauSac> create(@RequestBody MauSac mauSac) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mauSacService.create(mauSac));
    }

    @PutMapping
    @ApiMessage("Update color")
    public ResponseEntity<MauSac> update(@RequestBody MauSac mauSac) throws IdInvalidException {
        if (mauSac.getId() == 0) {
            throw new IdInvalidException("Mã màu sắc không được để trống");
        }
        return ResponseEntity.ok(mauSacService.update(mauSac));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete color")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (mauSacService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy màu sắc: " + id);
        }
        mauSacService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
