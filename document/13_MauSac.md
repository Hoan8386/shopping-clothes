# Màu Sắc Controller

> **Base Path:** `/api/v1/mau-sac`  
> **File:** `MauSacController.java`  
> Quản lý danh sách màu sắc sản phẩm (ví dụ: Đen, Trắng, Đỏ, Xanh, ...).

---

## Cấu trúc dữ liệu `MauSac`

| Trường        | Kiểu          | Mô tả                       |
| ------------- | ------------- | --------------------------- |
| `id`          | Long          | Mã màu sắc (auto-increment) |
| `tenMauSac`   | String(255)   | Tên màu sắc                 |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)          |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)     |

---

## 1. Lấy danh sách màu sắc

| Thuộc tính   | Chi tiết              |
| ------------ | --------------------- |
| **URL**      | `GET /api/v1/mau-sac` |
| **Method**   | `GET`                 |
| **Xác thực** | Bearer Token (JWT)    |

**Response:** `200 OK`

```json
[
  { "id": 1, "tenMauSac": "Đen", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 2, "tenMauSac": "Trắng", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 3, "tenMauSac": "Đỏ", "ngayTao": "2026-01-01T00:00:00" }
]
```

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                      | Method   | Mô tả               |
| ----------------------------- | -------- | ------------------- |
| `GET /api/v1/mau-sac/{id}`    | `GET`    | Lấy màu sắc theo ID |
| `POST /api/v1/mau-sac`        | `POST`   | Tạo màu sắc mới     |
| `PUT /api/v1/mau-sac`         | `PUT`    | Cập nhật màu sắc    |
| `DELETE /api/v1/mau-sac/{id}` | `DELETE` | Xóa màu sắc         |

**Request Body (POST/PUT):**

```json
{ "id": 1, "tenMauSac": "Đen nhám" }
```

**Lỗi:**

| HTTP Status | Mô tả                          |
| ----------- | ------------------------------ |
| `400`       | Không tìm thấy màu sắc         |
| `400`       | Mã màu sắc không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
