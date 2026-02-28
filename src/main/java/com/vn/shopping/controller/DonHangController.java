package com.vn.shopping.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.DonHang;
import com.vn.shopping.domain.request.ReqTaoDonHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.DonHangService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/don-hang")
public class DonHangController {

    private final DonHangService donHangService;

    public DonHangController(DonHangService donHangService) {
        this.donHangService = donHangService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách đơn hàng")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(required = false) Long cuaHangId,
            @RequestParam(required = false) Long nhanVienId,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Integer trangThaiThanhToan,
            @RequestParam(required = false) Integer hinhThucDonHang,
            Pageable pageable) {
        return ResponseEntity.ok(donHangService.findAllWithFilter(
                cuaHangId, nhanVienId, trangThai, trangThaiThanhToan, hinhThucDonHang, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy đơn hàng theo id")
    public ResponseEntity<DonHang> getById(@PathVariable("id") long id) throws IdInvalidException {
        return ResponseEntity.ok(donHangService.findByIdForCurrentUser(id));
    }

    /**
     * Khách hàng đặt hàng online:
     * - Lấy thông tin KH từ token
     * - Tạo đơn hàng từ giỏ hàng
     * - Xóa giỏ hàng sau khi tạo đơn
     */
    @PostMapping("/online")
    @ApiMessage("Khách hàng tạo đơn hàng online từ giỏ hàng")
    public ResponseEntity<DonHang> taoDonHangOnline(@RequestBody ReqTaoDonHangDTO req) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(donHangService.taoDonHangOnline(req));
    }

    /**
     * Nhân viên tạo đơn hàng tại quầy:
     * - Lấy thông tin NV từ token
     * - Tự gán cửa hàng của nhân viên
     */
    @PostMapping("/tai-quay")
    @ApiMessage("Nhân viên tạo đơn hàng tại quầy")
    public ResponseEntity<DonHang> taoDonHangTaiQuay(@RequestBody DonHang donHang) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(donHangService.taoDonHangTaiQuay(donHang));
    }

    @PutMapping
    @ApiMessage("Cập nhật đơn hàng")
    public ResponseEntity<DonHang> update(@RequestBody DonHang donHang) throws IdInvalidException {
        if (donHang.getId() == null || donHang.getId() == 0) {
            throw new IdInvalidException("Mã đơn hàng không được để trống");
        }
        return ResponseEntity.ok(donHangService.update(donHang));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa đơn hàng")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (donHangService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy đơn hàng: " + id);
        }
        donHangService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
