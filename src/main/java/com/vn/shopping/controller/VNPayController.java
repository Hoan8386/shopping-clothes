package com.vn.shopping.controller;

import com.vn.shopping.domain.request.ReqCreateVNPayPaymentDTO;
import com.vn.shopping.domain.response.ResDonHangDTO;
import com.vn.shopping.service.DonHangService;
import com.vn.shopping.service.GioHangNhanVienService;
import com.vn.shopping.service.VNPayService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/vnpay")
public class VNPayController {

    private final VNPayService vnpayService;
    private final GioHangNhanVienService gioHangNhanVienService;
    private final DonHangService donHangService;

    public VNPayController(
            VNPayService vnpayService,
            GioHangNhanVienService gioHangNhanVienService,
            DonHangService donHangService) {
        this.vnpayService = vnpayService;
        this.gioHangNhanVienService = gioHangNhanVienService;
        this.donHangService = donHangService;
    }

    @PostMapping("/create-payment-url")
    @ApiMessage("Tạo đường dẫn thanh toán VNPAY")
    public ResponseEntity<Map<String, String>> createPaymentUrl(
            @RequestBody ReqCreateVNPayPaymentDTO req,
            HttpServletRequest request) throws IdInvalidException {

        String forwardedFor = request.getHeader("X-Forwarded-For");
        String ipAddr = (forwardedFor != null && !forwardedFor.isBlank())
                ? forwardedFor.split(",")[0].trim()
                : request.getRemoteAddr();

        String paymentUrl = vnpayService.createPaymentUrl(req.getDonHangId(), ipAddr);

        Map<String, String> data = new HashMap<>();
        data.put("paymentUrl", paymentUrl);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/return")
    @ApiMessage("Xử lý dữ liệu trả về từ VNPAY")
    public ResponseEntity<Map<String, String>> handleVNPayReturn(
            @RequestParam Map<String, String> params) throws IdInvalidException {
        Map<String, String> data = vnpayService.processReturn(params);

        String txnRef = data.getOrDefault("vnp_TxnRef", "");
        boolean success = "true".equalsIgnoreCase(data.getOrDefault("success", "false"));

        if (success && vnpayService.isStaffCartTxnRef(txnRef)) {
            ResDonHangDTO donHang = gioHangNhanVienService.hoanTatThanhToanVNPayTuCallback(
                    txnRef,
                    data.getOrDefault("vnp_TransactionNo", ""));

            data.put("donHangId", String.valueOf(donHang.getId()));
            data.put("paymentRef", donHang.getPaymentRef() != null ? donHang.getPaymentRef() : "");
        }

        if (success && vnpayService.isOnlineCartTxnRef(txnRef)) {
            ResDonHangDTO donHang = donHangService.convertToDTO(
                    donHangService.hoanTatDonHangOnlineTuCallback(
                            txnRef,
                            data.getOrDefault("vnp_TransactionNo", "")));

            data.put("donHangId", String.valueOf(donHang.getId()));
            data.put("paymentRef", donHang.getPaymentRef() != null ? donHang.getPaymentRef() : "");
        }

        return ResponseEntity.ok(data);
    }
}
