package com.vn.shopping.service;

import com.vn.shopping.domain.DonHang;
import com.vn.shopping.repository.DonHangRepository;
import com.vn.shopping.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Service
public class VNPayService {

    @Value("${VNPAY_TMN_CODE:}")
    private String vnpTmnCode;

    @Value("${VNPAY_HASH_SECRET:}")
    private String vnpHashSecret;

    @Value("${VNPAY_URL:}")
    private String vnpUrl;

    @Value("${VNPAY_RETURN_URL:}")
    private String vnpReturnUrl;

    private final DonHangRepository donHangRepository;

    public VNPayService(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    public String createPaymentUrl(Long donHangId, String ipAddr) throws IdInvalidException {
        if (donHangId == null) {
            throw new IdInvalidException("Mã đơn hàng không được để trống");
        }

        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + donHangId));

        if (vnpTmnCode == null || vnpTmnCode.isBlank() || vnpHashSecret == null || vnpHashSecret.isBlank()
                || vnpUrl == null || vnpUrl.isBlank() || vnpReturnUrl == null || vnpReturnUrl.isBlank()) {
            throw new IdInvalidException("Cấu hình VNPAY chưa đầy đủ");
        }

        long tongTien = donHang.getTongTienTra() != null ? donHang.getTongTienTra() : 0;
        if (tongTien <= 0) {
            tongTien = donHang.getTongTien() != null ? donHang.getTongTien() : 0;
        }
        if (tongTien <= 0) {
            throw new IdInvalidException("Đơn hàng không hợp lệ để thanh toán");
        }

        String txnRef = donHang.getPaymentRef() != null && !donHang.getPaymentRef().isBlank()
                ? donHang.getPaymentRef()
                : String.valueOf(donHang.getId());

        // VNPay expects Vietnam local time (UTC+7).
        // Do not use "Etc/GMT+7" because its sign is inverted (actually UTC-7).
        TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        Calendar cal = Calendar.getInstance(tz);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(tz);
        String createDate = sdf.format(cal.getTime());
        cal.add(Calendar.MINUTE, 15);
        String expireDate = sdf.format(cal.getTime());

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(tongTien * 100));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang #" + donHang.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", (ipAddr == null || ipAddr.isBlank()) ? "127.0.0.1" : ipAddr);
        vnpParams.put("vnp_CreateDate", createDate);
        vnpParams.put("vnp_ExpireDate", expireDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue == null || fieldValue.isBlank()) {
                continue;
            }
            if (hashData.length() > 0) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(fieldName)
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

            query.append(fieldName)
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
        }

        String secureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnpUrl + "?" + query;
    }

    @Transactional
    public Map<String, String> processReturn(Map<String, String> rawParams) throws IdInvalidException {
        if (rawParams == null || rawParams.isEmpty()) {
            throw new IdInvalidException("Không nhận được dữ liệu trả về từ VNPAY");
        }

        if (vnpHashSecret == null || vnpHashSecret.isBlank()) {
            throw new IdInvalidException("Cấu hình VNPAY chưa đầy đủ");
        }

        Map<String, String> params = new HashMap<>(rawParams);
        String secureHash = params.getOrDefault("vnp_SecureHash", "");
        if (secureHash.isBlank()) {
            throw new IdInvalidException("Thiếu chữ ký bảo mật từ VNPAY");
        }

        String calculatedHash = buildSecureHash(params);
        if (!secureHash.equalsIgnoreCase(calculatedHash)) {
            throw new IdInvalidException("Chữ ký VNPAY không hợp lệ");
        }

        String txnRef = params.get("vnp_TxnRef");
        if (txnRef == null || txnRef.isBlank()) {
            throw new IdInvalidException("Thiếu mã tham chiếu đơn hàng (vnp_TxnRef)");
        }

        DonHang donHang = findOrderByTxnRef(txnRef);
        String transactionNo = params.getOrDefault("vnp_TransactionNo", "");
        String responseCode = params.getOrDefault("vnp_ResponseCode", "");
        String transactionStatus = params.getOrDefault("vnp_TransactionStatus", "");
        boolean success = "00".equals(responseCode) && "00".equals(transactionStatus);

        if (!transactionNo.isBlank()) {
            donHang.setPaymentRef(transactionNo);
        }

        if (success) {
            donHang.setTrangThaiThanhToan(1);
        }

        donHangRepository.save(donHang);

        Map<String, String> result = new HashMap<>();
        result.put("donHangId", String.valueOf(donHang.getId()));
        result.put("paymentRef", donHang.getPaymentRef() != null ? donHang.getPaymentRef() : "");
        result.put("vnp_TxnRef", txnRef);
        result.put("vnp_TransactionNo", transactionNo);
        result.put("vnp_ResponseCode", responseCode);
        result.put("vnp_TransactionStatus", transactionStatus);
        result.put("success", String.valueOf(success));
        return result;
    }

    private DonHang findOrderByTxnRef(String txnRef) throws IdInvalidException {
        Long orderId = null;
        try {
            orderId = Long.parseLong(txnRef);
        } catch (NumberFormatException ignored) {
            // Keep null and fall back to payment_ref lookup.
        }

        if (orderId != null) {
            Optional<DonHang> byId = donHangRepository.findById(orderId);
            if (byId.isPresent()) {
                return byId.get();
            }
        }

        return donHangRepository.findByPaymentRef(txnRef)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng theo vnp_TxnRef: " + txnRef));
    }

    private String buildSecureHash(Map<String, String> params) throws IdInvalidException {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            if ("vnp_SecureHash".equals(fieldName) || "vnp_SecureHashType".equals(fieldName)) {
                continue;
            }

            String fieldValue = params.get(fieldName);
            if (fieldValue == null || fieldValue.isBlank()) {
                continue;
            }

            if (hashData.length() > 0) {
                hashData.append('&');
            }

            hashData.append(fieldName)
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
        }

        return hmacSHA512(vnpHashSecret, hashData.toString());
    }

    private String hmacSHA512(String key, String data) throws IdInvalidException {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new IdInvalidException("Không thể tạo chữ ký VNPAY: " + e.getMessage());
        }
    }
}
