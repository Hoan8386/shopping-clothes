package com.vn.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.response.ResDataAIProductDTO;
import com.vn.shopping.service.ChiTietSanPhamService;
import com.vn.shopping.service.SanPhamService;
import com.vn.shopping.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/data-ai")
public class DataAIController {

    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;

    public DataAIController(SanPhamService sanPhamService, ChiTietSanPhamService chiTietSanPhamService) {
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping("/san-pham")
    @ApiMessage("Lấy danh sách sản phẩm cho AI")
    public ResponseEntity<List<ResDataAIProductDTO>> getProducts() {
        List<ResDataAIProductDTO> result = new ArrayList<>();

        for (SanPham sanPham : sanPhamService.findAll()) {
            List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamService.findBySanPhamId(sanPham.getId());
            ChiTietSanPham firstDetail = chiTietSanPhams.isEmpty() ? null : chiTietSanPhams.get(0);

            result.add(new ResDataAIProductDTO(
                    sanPham.getId() != null ? "SP" + sanPham.getId() : null,
                    sanPham.getTenSanPham(),
                    sanPham.getKieuSanPham() != null ? sanPham.getKieuSanPham().getTenKieuSanPham() : null,
                    firstDetail != null && firstDetail.getMauSac() != null ? firstDetail.getMauSac().getTenMauSac()
                            : null,
                    sanPham.getGiaBan() != null ? sanPham.getGiaBan().intValue() : null,
                    "không xác định",
                    sanPham.getMoTa()));
        }

        return ResponseEntity.ok(result);
    }
}