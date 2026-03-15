package com.vn.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.DonLuanChuyen;
import com.vn.shopping.domain.request.ReqDonLuanChuyenDTO;
import com.vn.shopping.domain.response.ResDonLuanChuyenDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.DonLuanChuyenService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/don-luan-chuyen")
public class DonLuanChuyenController {

    private final DonLuanChuyenService donLuanChuyenService;

    public DonLuanChuyenController(DonLuanChuyenService donLuanChuyenService) {
        this.donLuanChuyenService = donLuanChuyenService;
    }

    @PostMapping
    @ApiMessage("Tạo đơn luân chuyển")
    public ResponseEntity<ResDonLuanChuyenDTO> taoDonLuanChuyen(@RequestBody ReqDonLuanChuyenDTO req)
            throws IdInvalidException {
        DonLuanChuyen donLuanChuyen = donLuanChuyenService.taoDonLuanChuyen(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(donLuanChuyenService.convertToDTO(donLuanChuyen));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách đơn luân chuyển")
    public ResponseEntity<ResultPaginationDTO> getAll(Pageable pageable) {
        return ResponseEntity.ok(donLuanChuyenService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy đơn luân chuyển theo mã")
    public ResponseEntity<ResDonLuanChuyenDTO> getById(@PathVariable Long id) throws IdInvalidException {
        DonLuanChuyen donLuanChuyen = donLuanChuyenService.findById(id);
        if (donLuanChuyen == null) {
            throw new IdInvalidException("Không tìm thấy đơn luân chuyển: " + id);
        }
        return ResponseEntity.ok(donLuanChuyenService.convertToDTO(donLuanChuyen));
    }

    @GetMapping("/cua-hang-dat/{cuaHangId}")
    @ApiMessage("Lấy danh sách đơn luân chuyển theo cửa hàng đặt")
    public ResponseEntity<List<ResDonLuanChuyenDTO>> getByCuaHangDat(@PathVariable Long cuaHangId) {
        List<DonLuanChuyen> donLuanChuyens = donLuanChuyenService.findByCuaHangDatId(cuaHangId);
        List<ResDonLuanChuyenDTO> dtos = donLuanChuyens.stream()
                .map(donLuanChuyenService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cua-hang-gui/{cuaHangId}")
    @ApiMessage("Lấy danh sách đơn luân chuyển theo cửa hàng gửi")
    public ResponseEntity<List<ResDonLuanChuyenDTO>> getByCuaHangGui(@PathVariable Long cuaHangId) {
        List<DonLuanChuyen> donLuanChuyens = donLuanChuyenService.findByCuaHangGuiId(cuaHangId);
        List<ResDonLuanChuyenDTO> dtos = donLuanChuyens.stream()
                .map(donLuanChuyenService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/trang-thai")
    @ApiMessage("Cập nhật trạng thái đơn luân chuyển")
    public ResponseEntity<ResDonLuanChuyenDTO> capNhatTrangThai(
            @PathVariable Long id,
            @RequestParam Integer trangThai) throws IdInvalidException {
        DonLuanChuyen donLuanChuyen = donLuanChuyenService.capNhatTrangThai(id, trangThai);
        return ResponseEntity.ok(donLuanChuyenService.convertToDTO(donLuanChuyen));
    }
}
