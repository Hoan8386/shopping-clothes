# Kích Thước Controller

> **Base Path:** `/api/v1/kich-thuoc`  
> **File:** `KichThuocController.java`  
> Quản lý danh sách kích thước sản phẩm (ví dụ: S, M, L, XL, XXL, ...).

---

## Cấu trúc dữ liệu `KichThuoc`

| Trường         | Kiểu          | Mô tả                          |
| -------------- | ------------- | ------------------------------ |
| `id`           | Long          | Mã kích thước (auto-increment) |
| `tenKichThuoc` | String(255)   | Tên kích thước                 |
| `ngayTao`      | LocalDateTime | Ngày tạo (tự động)             |
| `ngayCapNhat`  | LocalDateTime | Ngày cập nhật (tự động)        |

---

## 1. Lấy danh sách kích thước

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/kich-thuoc` |
| **Method**   | `GET`                    |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK`

```json
[
  { "id": 1, "tenKichThuoc": "S", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 2, "tenKichThuoc": "M", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 3, "tenKichThuoc": "L", "ngayTao": "2026-01-01T00:00:00" }
]
```

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                         | Method   | Mô tả                  |
| -------------------------------- | -------- | ---------------------- |
| `GET /api/v1/kich-thuoc/{id}`    | `GET`    | Lấy kích thước theo ID |
| `POST /api/v1/kich-thuoc`        | `POST`   | Tạo kích thước mới     |
| `PUT /api/v1/kich-thuoc`         | `PUT`    | Cập nhật kích thước    |
| `DELETE /api/v1/kich-thuoc/{id}` | `DELETE` | Xóa kích thước         |

**Request Body (POST/PUT):**

```json
{ "id": 1, "tenKichThuoc": "XS" }
```

**Lỗi:**

| HTTP Status | Mô tả                             |
| ----------- | --------------------------------- |
| `400`       | Không tìm thấy kích thước         |
| `400`       | Mã kích thước không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
