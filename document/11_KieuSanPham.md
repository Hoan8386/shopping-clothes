# Kiểu Sản Phẩm Controller

> **Base Path:** `/api/v1/kieu-san-pham`  
> **File:** `KieuSanPhamController.java`  
> Quản lý kiểu/loại sản phẩm (ví dụ: Áo, Quần, Giày, Phụ kiện, ...).

---

## Cấu trúc dữ liệu `KieuSanPham`

| Trường           | Kiểu          | Mô tả                             |
| ---------------- | ------------- | --------------------------------- |
| `id`             | Long          | Mã kiểu sản phẩm (auto-increment) |
| `tenKieuSanPham` | String(255)   | Tên kiểu sản phẩm                 |
| `ngayTao`        | LocalDateTime | Ngày tạo (tự động)                |
| `ngayCapNhat`    | LocalDateTime | Ngày cập nhật (tự động)           |

---

## 1. Lấy danh sách kiểu sản phẩm

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/kieu-san-pham` |
| **Method**   | `GET`                       |
| **Xác thực** | Bearer Token (JWT)          |

**Response:** `200 OK`

```json
[
  { "id": 1, "tenKieuSanPham": "Áo", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 2, "tenKieuSanPham": "Quần", "ngayTao": "2026-01-01T00:00:00" }
]
```

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                            | Method   | Mô tả               |
| ----------------------------------- | -------- | ------------------- |
| `GET /api/v1/kieu-san-pham/{id}`    | `GET`    | Lấy kiểu SP theo ID |
| `POST /api/v1/kieu-san-pham`        | `POST`   | Tạo kiểu SP mới     |
| `PUT /api/v1/kieu-san-pham`         | `PUT`    | Cập nhật kiểu SP    |
| `DELETE /api/v1/kieu-san-pham/{id}` | `DELETE` | Xóa kiểu SP         |

**Request Body (POST/PUT):**

```json
{ "id": 1, "tenKieuSanPham": "Áo khoác" }
```

**Lỗi:**

| HTTP Status | Mô tả                                |
| ----------- | ------------------------------------ |
| `400`       | Không tìm thấy kiểu sản phẩm         |
| `400`       | Mã kiểu sản phẩm không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
