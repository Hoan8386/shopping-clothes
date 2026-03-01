# Thương Hiệu Controller

> **Base Path:** `/api/v1/thuong-hieu`  
> **File:** `ThuongHieuController.java`  
> Quản lý thương hiệu sản phẩm (ví dụ: Nike, Adidas, Uniqlo, ...).

---

## Cấu trúc dữ liệu `ThuongHieu`

| Trường              | Kiểu          | Mô tả                               |
| ------------------- | ------------- | ----------------------------------- |
| `id`                | Long          | Mã thương hiệu (auto-increment)     |
| `tenThuongHieu`     | String(255)   | Tên thương hiệu                     |
| `trangThaiHoatDong` | Integer       | TT hoạt động (0: ngừng, 1: đang HĐ) |
| `trangThaiHienThi`  | Integer       | TT hiển thị (0: ẩn, 1: hiển thị)    |
| `ngayTao`           | LocalDateTime | Ngày tạo (tự động)                  |
| `ngayCapNhat`       | LocalDateTime | Ngày cập nhật (tự động)             |

---

## 1. Lấy danh sách thương hiệu

| Thuộc tính   | Chi tiết                  |
| ------------ | ------------------------- |
| **URL**      | `GET /api/v1/thuong-hieu` |
| **Method**   | `GET`                     |
| **Xác thực** | Bearer Token (JWT)        |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenThuongHieu": "Nike",
    "trangThaiHoatDong": 1,
    "trangThaiHienThi": 1
  },
  {
    "id": 2,
    "tenThuongHieu": "Adidas",
    "trangThaiHoatDong": 1,
    "trangThaiHienThi": 1
  }
]
```

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                          | Method   | Mô tả                   |
| --------------------------------- | -------- | ----------------------- |
| `GET /api/v1/thuong-hieu/{id}`    | `GET`    | Lấy thương hiệu theo ID |
| `POST /api/v1/thuong-hieu`        | `POST`   | Tạo thương hiệu mới     |
| `PUT /api/v1/thuong-hieu`         | `PUT`    | Cập nhật thương hiệu    |
| `DELETE /api/v1/thuong-hieu/{id}` | `DELETE` | Xóa thương hiệu         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenThuongHieu": "Nike Vietnam",
  "trangThaiHoatDong": 1,
  "trangThaiHienThi": 1
}
```

**Lỗi:**

| HTTP Status | Mô tả                              |
| ----------- | ---------------------------------- |
| `400`       | Không tìm thấy thương hiệu         |
| `400`       | Mã thương hiệu không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
