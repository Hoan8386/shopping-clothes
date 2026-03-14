package com.vn.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.TraHang;
import com.vn.shopping.domain.request.ReqTraHangDTO;
import com.vn.shopping.domain.response.ResTraHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.TraHangService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/tra-hang")
public class TraHangController {

    private final TraHangService traHangService;

    public TraHangController(TraHangService traHangService) {
        this.traHangService = traHangService;
    }

    @PostMapping
    @ApiMessage("Tạo phiếu trả hàng")
    public ResponseEntity<ResTraHangDTO> taoPhieuTraHang(@RequestBody ReqTraHangDTO req) throws IdInvalidException {
        TraHang traHang = traHangService.taoPhieuTraHang(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(traHangService.convertToDTO(traHang));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách phiếu trả hàng")
    public ResponseEntity<ResultPaginationDTO> getAll(Pageable pageable) {
        return ResponseEntity.ok(traHangService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy phiếu trả hàng theo mã")
    public ResponseEntity<ResTraHangDTO> getById(@PathVariable Long id) throws IdInvalidException {
        TraHang traHang = traHangService.findById(id);
        if (traHang == null) {
            throw new IdInvalidException("Không tìm thấy phiếu trả hàng: " + id);
        }
        return ResponseEntity.ok(traHangService.convertToDTO(traHang));
    }

    @GetMapping("/don-hang/{donHangId}")
    @ApiMessage("Lấy danh sách phiếu trả hàng theo đơn hàng")
    public ResponseEntity<List<ResTraHangDTO>> getByDonHangId(@PathVariable Long donHangId) {
        List<TraHang> traHangs = traHangService.findByDonHangId(donHangId);
        List<ResTraHangDTO> dtos = traHangs.stream()
                .map(traHangService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/trang-thai")
    @ApiMessage("Cập nhật trạng thái phiếu trả hàng")
    public ResponseEntity<ResTraHangDTO> capNhatTrangThai(
            @PathVariable Long id,
            @RequestParam Integer trangThai) throws IdInvalidException {
        TraHang traHang = traHangService.capNhatTrangThai(id, trangThai);
        return ResponseEntity.ok(traHangService.convertToDTO(traHang));
    }
}
