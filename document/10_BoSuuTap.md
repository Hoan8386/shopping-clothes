# Bộ Sưu Tập Controller

> **Base Path:** `/api/v1/bo-suu-tap`  
> **File:** `BoSuuTapController.java`  
> Quản lý bộ sưu tập sản phẩm (ví dụ: Xuân Hè 2025, Thu Đông 2025, ...).

---

## Cấu trúc dữ liệu `BoSuuTap`

| Trường        | Kiểu          | Mô tả                          |
| ------------- | ------------- | ------------------------------ |
| `id`          | Long          | Mã bộ sưu tập (auto-increment) |
| `tenSuuTap`   | String(255)   | Tên bộ sưu tập                 |
| `moTa`        | String(255)   | Mô tả                          |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)             |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)        |

---

## 1. Lấy danh sách bộ sưu tập

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/bo-suu-tap` |
| **Method**   | `GET`                    |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenSuuTap": "Xuân Hè 2025",
    "moTa": "BST mùa xuân hè",
    "ngayTao": "2026-01-01T00:00:00"
  },
  {
    "id": 2,
    "tenSuuTap": "Thu Đông 2025",
    "moTa": "BST mùa thu đông",
    "ngayTao": "2026-01-01T00:00:00"
  }
]
```

---

## 2. Lấy bộ sưu tập theo ID

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/bo-suu-tap/{id}` |
| **Method**   | `GET`                         |
| **Xác thực** | Bearer Token (JWT)            |

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy bộ sưu tập |

---

## 3. Tạo bộ sưu tập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/bo-suu-tap` |
| **Method**       | `POST`                    |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:**

```json
{ "tenSuuTap": "Xuân Hè 2026", "moTa": "Bộ sưu tập mới" }
```

**Response:** `201 Created`

---

## 4. Cập nhật bộ sưu tập

| Thuộc tính       | Chi tiết                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/bo-suu-tap` |
| **Method**       | `PUT`                    |
| **Content-Type** | `application/json`       |
| **Xác thực**     | Bearer Token (JWT)       |

**Request Body:** (phải có `id`)

```json
{ "id": 1, "tenSuuTap": "Xuân Hè 2025 - Updated", "moTa": "Mô tả mới" }
```

**Lỗi:**

| HTTP Status | Mô tả                             |
| ----------- | --------------------------------- |
| `400`       | Mã bộ sưu tập không được để trống |

---

## 5. Xóa bộ sưu tập

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/bo-suu-tap/{id}` |
| **Method**   | `DELETE`                         |
| **Xác thực** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy bộ sưu tập |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
