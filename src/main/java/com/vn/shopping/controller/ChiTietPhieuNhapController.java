package com.vn.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.request.ReqChiTietPhieuNhapDTO;
import com.vn.shopping.domain.response.ResChiTietPhieuNhapDTO;
import com.vn.shopping.service.ChiTietPhieuNhapService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/chi-tiet-phieu-nhap")
public class ChiTietPhieuNhapController {

    private final ChiTietPhieuNhapService chiTietPhieuNhapService;

    public ChiTietPhieuNhapController(ChiTietPhieuNhapService chiTietPhieuNhapService) {
        this.chiTietPhieuNhapService = chiTietPhieuNhapService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách chi tiết phiếu nhập")
    public ResponseEntity<List<ResChiTietPhieuNhapDTO>> getAll() {
        List<ResChiTietPhieuNhapDTO> result = chiTietPhieuNhapService.findAll().stream()
                .map(chiTietPhieuNhapService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy chi tiết phiếu nhập theo id")
    public ResponseEntity<ResChiTietPhieuNhapDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        ChiTietPhieuNhap ct = chiTietPhieuNhapService.findById(id);
        if (ct == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết phiếu nhập: " + id);
        }
        return ResponseEntity.ok(chiTietPhieuNhapService.convertToDTO(ct));
    }

    @GetMapping("/phieu-nhap/{phieuNhapId}")
    @ApiMessage("Lấy chi tiết phiếu nhập theo mã phiếu nhập")
    public ResponseEntity<List<ResChiTietPhieuNhapDTO>> getByPhieuNhapId(
            @PathVariable("phieuNhapId") long phieuNhapId) {
        List<ResChiTietPhieuNhapDTO> result = chiTietPhieuNhapService.findByPhieuNhapId(phieuNhapId).stream()
                .map(chiTietPhieuNhapService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ApiMessage("Tạo chi tiết phiếu nhập")
    public ResponseEntity<ResChiTietPhieuNhapDTO> create(@RequestBody ReqChiTietPhieuNhapDTO dto)
            throws IdInvalidException {
        ChiTietPhieuNhap created = chiTietPhieuNhapService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(chiTietPhieuNhapService.convertToDTO(created));
    }

    @PutMapping
    @ApiMessage("Cập nhật chi tiết phiếu nhập")
    public ResponseEntity<ResChiTietPhieuNhapDTO> update(@RequestBody ReqChiTietPhieuNhapDTO dto)
            throws IdInvalidException {
        if (dto.getId() == null || dto.getId() == 0) {
            throw new IdInvalidException("Mã chi tiết phiếu nhập không được để trống");
        }
        ChiTietPhieuNhap updated = chiTietPhieuNhapService.update(dto);
        return ResponseEntity.ok(chiTietPhieuNhapService.convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa chi tiết phiếu nhập")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (chiTietPhieuNhapService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy chi tiết phiếu nhập: " + id);
        }
        chiTietPhieuNhapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
