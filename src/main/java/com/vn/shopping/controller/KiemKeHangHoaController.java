package com.vn.shopping.controller;

import com.vn.shopping.domain.KiemKeHangHoa;
import com.vn.shopping.domain.request.ReqDuyetKiemKeDTO;
import com.vn.shopping.domain.request.ReqKiemKeHangHoaDTO;
import com.vn.shopping.domain.response.ResKiemKeHangHoaDTO;
import com.vn.shopping.service.KiemKeHangHoaService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/kiem-ke-hang-hoa")
public class KiemKeHangHoaController {

    private final KiemKeHangHoaService kiemKeHangHoaService;

    public KiemKeHangHoaController(KiemKeHangHoaService kiemKeHangHoaService) {
        this.kiemKeHangHoaService = kiemKeHangHoaService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách phiếu kiểm kê")
    public ResponseEntity<List<ResKiemKeHangHoaDTO>> getAll() throws IdInvalidException {
        List<ResKiemKeHangHoaDTO> result = kiemKeHangHoaService.findByCurrentNhanVien().stream()
                .map(kiemKeHangHoaService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy phiếu kiểm kê theo id")
    public ResponseEntity<ResKiemKeHangHoaDTO> getById(@PathVariable("id") Long id) throws IdInvalidException {
        KiemKeHangHoa phieu = kiemKeHangHoaService.findById(id);
        if (phieu == null) {
            throw new IdInvalidException("Không tìm thấy phiếu kiểm kê: " + id);
        }
        return ResponseEntity.ok(kiemKeHangHoaService.convertToDTO(phieu));
    }

    @PostMapping
    @ApiMessage("Nhân viên tạo phiếu kiểm kê")
    public ResponseEntity<ResKiemKeHangHoaDTO> create(@RequestBody ReqKiemKeHangHoaDTO dto) throws IdInvalidException {
        KiemKeHangHoa created = kiemKeHangHoaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(kiemKeHangHoaService.convertToDTO(created));
    }

    @PutMapping
    @ApiMessage("Nhân viên cập nhật và điền phiếu kiểm kê")
    public ResponseEntity<ResKiemKeHangHoaDTO> update(@RequestBody ReqKiemKeHangHoaDTO dto) throws IdInvalidException {
        KiemKeHangHoa updated = kiemKeHangHoaService.update(dto);
        return ResponseEntity.ok(kiemKeHangHoaService.convertToDTO(updated));
    }

    @PutMapping("/{id}/gui-duyet")
    @ApiMessage("Nhân viên gửi phiếu kiểm kê chờ admin duyệt")
    public ResponseEntity<ResKiemKeHangHoaDTO> guiDuyet(@PathVariable("id") Long id) throws IdInvalidException {
        KiemKeHangHoa updated = kiemKeHangHoaService.guiDuyet(id);
        return ResponseEntity.ok(kiemKeHangHoaService.convertToDTO(updated));
    }

    @PutMapping("/{id}/duyet")
    @ApiMessage("Admin duyệt điều chỉnh tồn kho hoặc yêu cầu kiểm kê lại")
    public ResponseEntity<ResKiemKeHangHoaDTO> duyet(
            @PathVariable("id") Long id,
            @RequestBody ReqDuyetKiemKeDTO req) throws IdInvalidException {
        KiemKeHangHoa updated = kiemKeHangHoaService.duyet(id, req);
        return ResponseEntity.ok(kiemKeHangHoaService.convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa phiếu kiểm kê")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        kiemKeHangHoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
