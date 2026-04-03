package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.response.ResKhachHangLookupDTO;
import com.vn.shopping.service.KhachHangService;
import com.vn.shopping.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/khach-hang")
public class KhachHangLookupController {

    private final KhachHangService khachHangService;

    public KhachHangLookupController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách khách hàng")
    public ResponseEntity<List<ResKhachHangLookupDTO>> getAll() {
        return ResponseEntity.ok(khachHangService.findAllLookupDTO());
    }

    @GetMapping("/lookup")
    @ApiMessage("Tìm khách hàng theo số điện thoại")
    public ResponseEntity<ResKhachHangLookupDTO> lookupByPhone(@RequestParam("sdt") String sdt) {
        KhachHang kh = khachHangService.findBySdt(sdt);
        if (kh == null) {
            return ResponseEntity.<ResKhachHangLookupDTO>ok(null);
        }

        return ResponseEntity.ok(new ResKhachHangLookupDTO(
                kh.getId(),
                kh.getTenKhachHang(),
                kh.getSdt(),
                kh.getEmail(),
                kh.getDiemTichLuy()));
    }
}
