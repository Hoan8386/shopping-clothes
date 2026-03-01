# Nhà Cung Cấp Controller

> **Base Path:** `/api/v1/nha-cung-cap`  
> **File:** `NhaCungCapController.java`  
> Quản lý danh sách nhà cung cấp hàng hóa.

---

## Cấu trúc dữ liệu `NhaCungCap`

| Trường          | Kiểu          | Mô tả                            |
| --------------- | ------------- | -------------------------------- |
| `id`            | Long          | Mã nhà cung cấp (auto-increment) |
| `tenNhaCungCap` | String(255)   | Tên nhà cung cấp                 |
| `soDienThoai`   | String(255)   | Số điện thoại                    |
| `email`         | String(255)   | Email liên hệ                    |
| `diaChi`        | String(255)   | Địa chỉ                          |
| `ghiTru`        | String(255)   | Ghi chú                          |
| `trangThai`     | Integer       | Trạng thái (0: ngừng, 1: HĐ)     |
| `ngayTao`       | LocalDateTime | Ngày tạo (tự động)               |
| `ngayCapNhat`   | LocalDateTime | Ngày cập nhật (tự động)          |

---

## 1. Lấy danh sách nhà cung cấp

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/nha-cung-cap` |
| **Method**   | `GET`                      |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenNhaCungCap": "Công ty TNHH ABC",
    "soDienThoai": "028-9876-5432",
    "email": "abc@supplier.vn",
    "diaChi": "456 Lê Lợi, Q.1, TP.HCM",
    "trangThai": 1
  }
]
```

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                           | Method   | Mô tả           |
| ---------------------------------- | -------- | --------------- |
| `GET /api/v1/nha-cung-cap/{id}`    | `GET`    | Lấy NCC theo ID |
| `POST /api/v1/nha-cung-cap`        | `POST`   | Tạo NCC mới     |
| `PUT /api/v1/nha-cung-cap`         | `PUT`    | Cập nhật NCC    |
| `DELETE /api/v1/nha-cung-cap/{id}` | `DELETE` | Xóa NCC         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenNhaCungCap": "Công ty TNHH ABC",
  "soDienThoai": "028-9876-5432",
  "email": "abc@supplier.vn",
  "diaChi": "456 Lê Lợi, Q.1, TP.HCM",
  "ghiTru": "NCC uy tín",
  "trangThai": 1
}
```

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy nhà cung cấp         |
| `400`       | Mã nhà cung cấp không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
