# Cửa Hàng Controller

> **Base Path:** `/api/v1/cua-hang`  
> **File:** `CuaHangController.java`  
> Quản lý danh sách cửa hàng / chi nhánh.

---

## Cấu trúc dữ liệu `CuaHang`

| Trường        | Kiểu        | Mô tả                        |
| ------------- | ----------- | ---------------------------- |
| `id`          | Long        | Mã cửa hàng (auto-increment) |
| `tenCuaHang`  | String(255) | Tên cửa hàng / chi nhánh     |
| `diaChi`      | String(255) | Địa chỉ                      |
| `viTri`       | String(255) | Vị trí (tọa độ / khu vực)    |
| `soDienThoai` | String(255) | Số điện thoại liên hệ        |
| `email`       | String(255) | Email liên hệ                |
| `trangThai`   | Integer     | Trạng thái (0: đóng, 1: mở)  |

---

## 1. Lấy danh sách cửa hàng

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/cua-hang` |
| **Method**   | `GET`                  |
| **Xác thực** | Bearer Token (JWT)     |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenCuaHang": "Chi nhánh Quận 1",
    "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
    "soDienThoai": "028-1234-5678",
    "email": "q1@shop.vn",
    "trangThai": 1
  }
]
```

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                       | Method   | Mô tả                |
| ------------------------------ | -------- | -------------------- |
| `GET /api/v1/cua-hang/{id}`    | `GET`    | Lấy cửa hàng theo ID |
| `POST /api/v1/cua-hang`        | `POST`   | Tạo cửa hàng mới     |
| `PUT /api/v1/cua-hang`         | `PUT`    | Cập nhật cửa hàng    |
| `DELETE /api/v1/cua-hang/{id}` | `DELETE` | Xóa cửa hàng         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenCuaHang": "Chi nhánh Quận 1",
  "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
  "soDienThoai": "028-1234-5678",
  "email": "q1@shop.vn",
  "trangThai": 1
}
```

**Lỗi:**

| HTTP Status | Mô tả                           |
| ----------- | ------------------------------- |
| `400`       | Không tìm thấy cửa hàng         |
| `400`       | Mã cửa hàng không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
