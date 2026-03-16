# VNPay Controller

> Base Path: `/api/v1/auth/vnpay`
> File: `VNPayController.java`

Module tạo URL thanh toán VNPay cho đơn hàng.

## 1) Tạo đường dẫn thanh toán

- Method: `POST`
- Endpoint: `/api/v1/auth/vnpay/create-payment-url`
- Body: `ReqCreateVNPayPaymentDTO`
- Response: `200 OK` + `{"paymentUrl": "..."}`

Body mẫu:

```json
{
  "donHangId": 1001
}
```

Response mẫu:

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?..."
}
```

## Nguồn IP thanh toán

Controller lấy IP theo thứ tự:

1. Header `X-Forwarded-For` (nếu có).
2. `request.getRemoteAddr()` nếu không có header.

## Ràng buộc nghiệp vụ chính (theo VNPayService)

- `donHangId` bắt buộc.
- Đơn hàng phải tồn tại.
- Cấu hình VNPay phải đủ:
  - `VNPAY_TMN_CODE`
  - `VNPAY_HASH_SECRET`
  - `VNPAY_URL`
  - `VNPAY_RETURN_URL`
- Tổng tiền thanh toán phải lớn hơn 0.
- Hạn URL thanh toán: 15 phút từ thời điểm tạo.

## Mã lỗi thường gặp

- `400`: Mã đơn hàng không được để trống.
- `400`: Không tìm thấy đơn hàng.
- `400`: Cấu hình VNPay chưa đầy đủ.
- `400`: Đơn hàng không hợp lệ để thanh toán.

## Ghi chú tích hợp frontend

- FE gọi API này để lấy `paymentUrl`, sau đó redirect trình duyệt sang URL đó.
- Sau khi thanh toán, VNPay sẽ điều hướng về `VNPAY_RETURN_URL` đã cấu hình ở backend.
- Nên hiển thị trạng thái chờ/đang chuyển trang để tránh người dùng bấm nhiều lần.
