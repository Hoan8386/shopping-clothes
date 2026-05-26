package com.vn.shopping.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.TraHang;
import com.vn.shopping.domain.request.ReqTraHangDTO;
import com.vn.shopping.domain.response.ResTraHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.service.TraHangService;
import com.vn.shopping.service.VNPayService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/tra-hang")
public class TraHangController {

    private final TraHangService traHangService;
    private final VNPayService vnPayService;

    public TraHangController(TraHangService traHangService, VNPayService vnPayService) {
        this.traHangService = traHangService;
        this.vnPayService = vnPayService;
    }

    @PostMapping
    @ApiMessage("Tạo phiếu trả hàng")
    public ResponseEntity<ResTraHangDTO> taoPhieuTraHang(@RequestBody ReqTraHangDTO req) throws IdInvalidException {
        TraHang traHang = traHangService.taoPhieuTraHang(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(traHangService.convertToDTO(traHang));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo phiếu trả hàng kèm ảnh")
    public ResponseEntity<ResTraHangDTO> taoPhieuTraHangKemAnh(
            @RequestPart("data") ReqTraHangDTO req,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IdInvalidException {
        TraHang traHang = traHangService.taoPhieuTraHang(req, file);
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

    @PostMapping("/{id}/vnpay-url")
    @ApiMessage("Tạo URL thanh toán VNPAY cho phiếu trả hàng")
    public ResponseEntity<Map<String, String>> taoLinkThanhToanVNPay(
            @PathVariable Long id,
            jakarta.servlet.http.HttpServletRequest request) throws IdInvalidException {
        TraHang traHang = traHangService.findById(id);
        if (traHang == null) {
            throw new IdInvalidException("Không tìm thấy phiếu trả hàng: " + id);
        }

        long tongTienTra = 0;
        if (traHang.getChiTietTraHangs() != null) {
            for (com.vn.shopping.domain.ChiTietTraHang chiTiet : traHang.getChiTietTraHangs()) {
                if (chiTiet != null && chiTiet.getSanPhamTra() != null) {
                    com.vn.shopping.domain.ChiTietDonHang ctdh = chiTiet.getSanPhamTra();
                    int soLuongTra = chiTiet.getSoLuongTra() != null
                            ? chiTiet.getSoLuongTra()
                            : (ctdh.getSoLuong() != null ? ctdh.getSoLuong() : 0);

                    double donGiaSauGiam;
                    if (ctdh.getThanhTien() != null && ctdh.getSoLuong() != null && ctdh.getSoLuong() > 0) {
                        donGiaSauGiam = ctdh.getThanhTien() / ctdh.getSoLuong();
                    } else {
                        donGiaSauGiam = ctdh.getGiaGiam() != null ? ctdh.getGiaGiam()
                                : (ctdh.getGiaSanPham() != null ? ctdh.getGiaSanPham() : 0D);
                    }

                    if (soLuongTra > 0 && donGiaSauGiam > 0) {
                        tongTienTra += Math.round(donGiaSauGiam * soLuongTra);
                    }
                }
            }
        }

        if (tongTienTra <= 0) {
            throw new IdInvalidException("Phiếu trả hàng không hợp lệ để thanh toán VNPAY");
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");
        String ipAddr = (forwardedFor != null && !forwardedFor.isBlank())
                ? forwardedFor.split(",")[0].trim()
                : request.getRemoteAddr();

        String paymentUrl = vnPayService.createReturnPaymentUrl(id, tongTienTra, ipAddr);

        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    @GetMapping("/vnpay/return")
    @ApiMessage("Xử lý thanh toán VNPAY cho phiếu trả hàng")
    public ResponseEntity<Map<String, String>> handleVNPayReturnForReturn(
            @RequestParam Map<String, String> params) throws IdInvalidException {
        Map<String, String> data = vnPayService.processReturn(params);

        String txnRef = data.getOrDefault("vnp_TxnRef", "");
        boolean success = "true".equalsIgnoreCase(data.getOrDefault("success", "false"));

        if (success && vnPayService.isReturnTxnRef(txnRef)) {
            Long traHangId = vnPayService.extractReturnId(txnRef);
            TraHang traHang = traHangService.capNhatTrangThai(
                    traHangId,
                    1,
                    data.getOrDefault("vnp_TransactionNo", ""));

            ResTraHangDTO traHangDTO = traHangService.convertToDTO(traHang);
            data.put("traHangId", String.valueOf(traHangDTO.getId()));
            data.put("paymentRef", traHangDTO.getPaymentRef() != null ? traHangDTO.getPaymentRef() : "");
        }

        return ResponseEntity.ok(data);
    }
}
