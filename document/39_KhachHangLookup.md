# Tra Cứu Khách Hàng Theo SĐT

> Base Path: `/api/v1/khach-hang`  
> File: `KhachHangLookupController.java`

---

## 1. Endpoint

| Method | Endpoint                                      | Mô tả                             |
| ------ | --------------------------------------------- | --------------------------------- |
| GET    | `/api/v1/khach-hang/lookup?sdt={soDienThoai}` | Tìm khách hàng theo số điện thoại |

---

## 2. Request/Response

### Request

- URL mẫu: `GET /api/v1/khach-hang/lookup?sdt=0911000001`
- Query bắt buộc: `sdt`

### Response thành công (tìm thấy)

```json
{
  "id": 1,
  "tenKhachHang": "Lan",
  "sdt": "0911000001",
  "email": "lan@g.com",
  "diemTichLuy": 10
}
```

### Response thành công nhưng không có dữ liệu

Controller hiện trả `200 OK` với body `null` khi không tìm thấy khách hàng.

---

## 3. Mục đích sử dụng

- Dùng trong luồng bán tại quầy để nhân viên tra cứu nhanh thông tin khách.
- Có thể kết hợp với module giỏ hàng nhân viên để gắn người mua trước khi thanh toán.
