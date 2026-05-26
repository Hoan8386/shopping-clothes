package com.vn.shopping.service;

import com.vn.shopping.domain.DonHang;
import com.vn.shopping.domain.request.ReqTaoDonHangDTO;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VNPayService {

    private static final String STAFF_CART_TXN_PREFIX = "GHNV_";
    private static final String ONLINE_CART_TXN_PREFIX = "GHKH_";

    public static class PendingOnlineCheckout {
        private final String customerEmail;
        private final ReqTaoDonHangDTO request;

        public PendingOnlineCheckout(String customerEmail, ReqTaoDonHangDTO request) {
            this.customerEmail = customerEmail;
            this.request = request;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public ReqTaoDonHangDTO getRequest() {
            return request;
        }
    }

    @Value("${VNPAY_TMN_CODE:}")
    private String vnpTmnCode;

    @Value("${VNPAY_HASH_SECRET:}")
    private String vnpHashSecret;

    @Value("${VNPAY_URL:}")
    private String vnpUrl;

    @Value("${VNPAY_RETURN_URL:}")
    private String vnpReturnUrl;

    @Value("${VNPAY_RETURN_URL_STAFF:http://localhost:3000/staff/orders}")
    private String vnpStaffOrderReturnUrl;

    @Value("${VNPAY_RETURN_URL_RETURN:http://localhost:3000/staff/returns}")
    private String vnpStaffReturnUrl;

    private final DonHangRepository donHangRepository;
    private final Map<String, PendingOnlineCheckout> pendingOnlineCheckoutMap = new ConcurrentHashMap<>();

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

        return buildPaymentUrl(txnRef, tongTien, "Thanh toan don hang #" + donHang.getId(), ipAddr, null);
    }

    public String createPaymentUrlForStaffCart(Long cartId, int tongTien, String ipAddr) throws IdInvalidException {
        if (cartId == null) {
            throw new IdInvalidException("Mã giỏ hàng không được để trống");
        }
        if (tongTien <= 0) {
            throw new IdInvalidException("Giỏ hàng không hợp lệ để thanh toán VNPAY");
        }

        String txnRef = STAFF_CART_TXN_PREFIX + cartId + "_" + System.currentTimeMillis();
        return buildPaymentUrl(
                txnRef,
                tongTien,
                "Thanh toan gio hang tai quay #" + cartId,
                ipAddr,
                vnpStaffOrderReturnUrl);
    }

    public String createReturnPaymentUrl(Long traHangId, long tongTien, String ipAddr) throws IdInvalidException {
        if (traHangId == null) {
            throw new IdInvalidException("Mã phiếu trả hàng không được để trống");
        }
        if (tongTien <= 0) {
            throw new IdInvalidException("Phiếu trả hàng không hợp lệ để thanh toán VNPAY");
        }

        String txnRef = "TRH_" + traHangId + "_" + System.currentTimeMillis();
        return buildPaymentUrl(
                txnRef,
                tongTien,
                "Thanh toan hoan tien phieu tra #" + traHangId,
                ipAddr,
                vnpStaffReturnUrl);
    }

    public String createPaymentUrlForOnlineCart(Long cartId, int tongTien, String ipAddr) throws IdInvalidException {
        if (cartId == null) {
            throw new IdInvalidException("Mã giỏ hàng không được để trống");
        }
        if (tongTien <= 0) {
            throw new IdInvalidException("Giỏ hàng không hợp lệ để thanh toán VNPAY");
        }

        String txnRef = ONLINE_CART_TXN_PREFIX + cartId + "_" + System.currentTimeMillis();
        return buildPaymentUrl(txnRef, tongTien, "Thanh toan gio hang online #" + cartId, ipAddr, null);
    }

    public String createPaymentUrlForOnlineCartWithPending(Long cartId, int tongTien, String ipAddr,
            String customerEmail, ReqTaoDonHangDTO req) throws IdInvalidException {
        if (cartId == null) {
            throw new IdInvalidException("Mã giỏ hàng không được để trống");
        }
        if (tongTien <= 0) {
            throw new IdInvalidException("Giỏ hàng không hợp lệ để thanh toán VNPAY");
        }

        String txnRef = ONLINE_CART_TXN_PREFIX + cartId + "_" + System.currentTimeMillis();
        pendingOnlineCheckoutMap.put(txnRef, new PendingOnlineCheckout(customerEmail, req));
        return buildPaymentUrl(txnRef, tongTien, "Thanh toan gio hang online #" + cartId, ipAddr, null);
    }

    public void putPendingOnlineCheckout(String txnRef, String customerEmail, ReqTaoDonHangDTO req) {
        pendingOnlineCheckoutMap.put(txnRef, new PendingOnlineCheckout(customerEmail, req));
    }

    public PendingOnlineCheckout consumePendingOnlineCheckout(String txnRef) {
        return pendingOnlineCheckoutMap.remove(txnRef);
    }

    public boolean isStaffCartTxnRef(String txnRef) {
        return txnRef != null && txnRef.startsWith(STAFF_CART_TXN_PREFIX);
    }

    public boolean isOnlineCartTxnRef(String txnRef) {
        return txnRef != null && txnRef.startsWith(ONLINE_CART_TXN_PREFIX);
    }

    public boolean isReturnTxnRef(String txnRef) {
        return txnRef != null && txnRef.startsWith("TRH_");
    }

    public Long extractStaffCartId(String txnRef) throws IdInvalidException {
        if (!isStaffCartTxnRef(txnRef)) {
            throw new IdInvalidException("Mã tham chiếu không phải của giỏ hàng nhân viên");
        }

        String raw = txnRef.substring(STAFF_CART_TXN_PREFIX.length());
        String[] parts = raw.split("_");
        if (parts.length < 1 || parts[0].isBlank()) {
            throw new IdInvalidException("Mã tham chiếu giỏ hàng không hợp lệ");
        }

        try {
            return Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            throw new IdInvalidException("Không đọc được mã giỏ hàng từ tham chiếu VNPAY");
        }
    }

    public Long extractReturnId(String txnRef) throws IdInvalidException {
        if (!isReturnTxnRef(txnRef)) {
            throw new IdInvalidException("Mã tham chiếu không phải của phiếu trả hàng");
        }

        String raw = txnRef.substring("TRH_".length());
        String[] parts = raw.split("_");
        if (parts.length < 1 || parts[0].isBlank()) {
            throw new IdInvalidException("Mã tham chiếu phiếu trả hàng không hợp lệ");
        }

        try {
            return Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            throw new IdInvalidException("Không đọc được mã phiếu trả hàng từ tham chiếu VNPAY");
        }
    }

    private String buildPaymentUrl(String txnRef, long tongTien, String orderInfo, String ipAddr, String returnUrl)
            throws IdInvalidException {
        String effectiveReturnUrl = (returnUrl != null && !returnUrl.isBlank()) ? returnUrl : vnpReturnUrl;

        if (vnpTmnCode == null || vnpTmnCode.isBlank() || vnpHashSecret == null || vnpHashSecret.isBlank()
                || vnpUrl == null || vnpUrl.isBlank() || effectiveReturnUrl == null || effectiveReturnUrl.isBlank()) {
            throw new IdInvalidException("Cấu hình VNPAY chưa đầy đủ");
        }

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
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", effectiveReturnUrl);
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

        String transactionNo = params.getOrDefault("vnp_TransactionNo", "");
        String responseCode = params.getOrDefault("vnp_ResponseCode", "");
        String transactionStatus = params.getOrDefault("vnp_TransactionStatus", "");
        boolean success = "00".equals(responseCode) && "00".equals(transactionStatus);

        Map<String, String> result = new HashMap<>();
        result.put("vnp_TxnRef", txnRef);
        result.put("vnp_TransactionNo", transactionNo);
        result.put("vnp_ResponseCode", responseCode);
        result.put("vnp_TransactionStatus", transactionStatus);
        result.put("success", String.valueOf(success));

        // Cart-based VNPay flow: order is created later only when callback succeeds.
        if (isStaffCartTxnRef(txnRef) || isOnlineCartTxnRef(txnRef)) {
            result.put("donHangId", "");
            result.put("paymentRef", "");
            return result;
        }

        DonHang donHang = findOrderByTxnRef(txnRef);

        if (!transactionNo.isBlank()) {
            donHang.setPaymentRef(transactionNo);
        }

        if (success) {
            donHang.setTrangThaiThanhToan(1);
        }

        donHangRepository.save(donHang);
        result.put("donHangId", String.valueOf(donHang.getId()));
        result.put("paymentRef", donHang.getPaymentRef() != null ? donHang.getPaymentRef() : "");
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
