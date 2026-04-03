package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.response.ResNhanVienDTO;
import com.vn.shopping.service.NhanVienService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/nhan-vien")
public class NhanVienController {

    private final NhanVienService nhanVienService;

    public NhanVienController(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách nhân viên")
    public ResponseEntity<List<ResNhanVienDTO>> getAllVisibleByRole() throws IdInvalidException {
        return ResponseEntity.ok(nhanVienService.findAllVisibleByCurrentUser());
    }

    @PostMapping
    @ApiMessage("Tạo nhân viên")
    public ResponseEntity<ResNhanVienDTO> create(@RequestBody NhanVien nhanVien) throws IdInvalidException {
        NhanVien createdNhanVien = nhanVienService.handleCreateNhanVien(nhanVien);
        return ResponseEntity.status(HttpStatus.CREATED).body(nhanVienService.toDTO(createdNhanVien));
    }

    @PutMapping
    @ApiMessage("Cập nhật nhân viên")
    public ResponseEntity<ResNhanVienDTO> update(@RequestBody NhanVien nhanVien) throws IdInvalidException {
        if (nhanVien.getId() == null || nhanVien.getId() == 0) {
            throw new IdInvalidException("Mã nhân viên không được để trống");
        }
        NhanVien updatedNhanVien = nhanVienService.update(nhanVien);
        return ResponseEntity.ok(nhanVienService.toDTO(updatedNhanVien));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa nhân viên")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (nhanVienService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy nhân viên: " + id);
        }
        nhanVienService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
