package com.vn.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.DoiHang;
import com.vn.shopping.domain.request.ReqDoiHangDTO;
import com.vn.shopping.domain.response.ResDoiHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.DoiHangService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/doi-hang")
public class DoiHangController {

    private final DoiHangService doiHangService;

    public DoiHangController(DoiHangService doiHangService) {
        this.doiHangService = doiHangService;
    }

    @PostMapping
    @ApiMessage("Tạo phiếu đổi hàng")
    public ResponseEntity<ResDoiHangDTO> taoPhieuDoiHang(@RequestBody ReqDoiHangDTO req) throws IdInvalidException {
        DoiHang doiHang = doiHangService.taoPhieuDoiHang(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(doiHangService.convertToDTO(doiHang));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách phiếu đổi hàng")
    public ResponseEntity<ResultPaginationDTO> getAll(Pageable pageable) {
        return ResponseEntity.ok(doiHangService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy phiếu đổi hàng theo mã")
    public ResponseEntity<ResDoiHangDTO> getById(@PathVariable Long id) throws IdInvalidException {
        DoiHang doiHang = doiHangService.findById(id);
        if (doiHang == null) {
            throw new IdInvalidException("Không tìm thấy phiếu đổi hàng: " + id);
        }
        return ResponseEntity.ok(doiHangService.convertToDTO(doiHang));
    }

    @GetMapping("/don-hang/{donHangId}")
    @ApiMessage("Lấy danh sách phiếu đổi hàng theo đơn hàng")
    public ResponseEntity<List<ResDoiHangDTO>> getByDonHangId(@PathVariable Long donHangId) {
        List<DoiHang> doiHangs = doiHangService.findByDonHangId(donHangId);
        List<ResDoiHangDTO> dtos = doiHangs.stream()
                .map(doiHangService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/trang-thai")
    @ApiMessage("Cập nhật trạng thái phiếu đổi hàng")
    public ResponseEntity<ResDoiHangDTO> capNhatTrangThai(
            @PathVariable Long id,
            @RequestParam Integer trangThai) throws IdInvalidException {
        DoiHang doiHang = doiHangService.capNhatTrangThai(id, trangThai);
        return ResponseEntity.ok(doiHangService.convertToDTO(doiHang));
    }
}
