package com.vn.shopping.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.domain.request.ReqPhieuNhapDTO;
import com.vn.shopping.domain.response.ResPhieuNhapDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.PhieuNhapService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/phieu-nhap")
public class PhieuNhapController {

    private final PhieuNhapService phieuNhapService;

    public PhieuNhapController(PhieuNhapService phieuNhapService) {
        this.phieuNhapService = phieuNhapService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách phiếu nhập")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(required = false) String tenPhieuNhap,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) String tenCuaHang,
            @RequestParam(required = false) String tenNhaCungCap,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayTaoTu,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayTaoDen,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayDatHangTu,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayDatHangDen,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayNhanHangTu,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayNhanHangDen,
            Pageable pageable) {
        ResultPaginationDTO result = phieuNhapService.filterPhieuNhap(
                tenPhieuNhap, trangThai, tenCuaHang, tenNhaCungCap,
                ngayTaoTu, ngayTaoDen,
                ngayDatHangTu, ngayDatHangDen, ngayNhanHangTu, ngayNhanHangDen, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy phiếu nhập theo id")
    public ResponseEntity<ResPhieuNhapDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        PhieuNhap phieuNhap = phieuNhapService.findById(id);
        if (phieuNhap == null) {
            throw new IdInvalidException("Không tìm thấy phiếu nhập: " + id);
        }
        return ResponseEntity.ok(phieuNhapService.convertToDTO(phieuNhap));
    }

    @PostMapping
    @ApiMessage("Tạo phiếu nhập")
    public ResponseEntity<ResPhieuNhapDTO> create(@RequestBody ReqPhieuNhapDTO dto) throws IdInvalidException {
        PhieuNhap created = phieuNhapService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuNhapService.convertToDTO(created));
    }

    @PutMapping
    @ApiMessage("Cập nhật phiếu nhập")
    public ResponseEntity<ResPhieuNhapDTO> update(@RequestBody ReqPhieuNhapDTO dto) throws IdInvalidException {
        if (dto.getId() == null || dto.getId() == 0) {
            throw new IdInvalidException("Mã phiếu nhập không được để trống");
        }
        PhieuNhap updated = phieuNhapService.update(dto);
        return ResponseEntity.ok(phieuNhapService.convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa phiếu nhập")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (phieuNhapService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy phiếu nhập: " + id);
        }
        phieuNhapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
