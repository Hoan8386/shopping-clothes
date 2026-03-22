# Lương Cơ Bản Controller

> **Base Path:** `/api/v1/luong-co-ban`  
> **File:** `LuongCobanController.java`  
> Quản lý lương cơ bản của từng nhân viên theo từng thời điểm áp dụng.

---

## Cấu trúc dữ liệu `LuongCoban`

| Trường        | Kiểu          | Mô tả                            |
| ------------- | ------------- | -------------------------------- |
| `id`          | Long          | Mã lương cơ bản (auto-increment) |
| `nhanVien`    | Object        | Nhân viên (FK MaNhanVien)        |
| `luongCoBan`  | Long          | Mức lương cơ bản (VNĐ)          |
| `ngayApDung`  | LocalDateTime | Ngày bắt đầu áp dụng            |
| `trangThai`   | Integer       | 1=Đang áp dụng, 0=Hết hiệu lực  |
| `json`        | String (TEXT) | Dữ liệu mở rộng                 |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)               |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)          |

---

## 1. Lấy danh sách lương cơ bản

**URL:** `GET /api/v1/luong-co-ban` — **Response:** `200 OK`

---

## 2. Lấy lương cơ bản theo ID

**URL:** `GET /api/v1/luong-co-ban/{id}`

---

## 3. Lấy lương cơ bản theo nhân viên

| Thuộc tính   | Chi tiết                                              |
| ------------ | ----------------------------------------------------- |
| **URL**      | `GET /api/v1/luong-co-ban/nhan-vien/{nhanVienId}`    |
| **Method**   | `GET`                                                 |
| **Xác thực** | Bearer Token (JWT)                                    |

---

## 4. Tạo lương cơ bản

| Thuộc tính       | Chi tiết                      |
| ---------------- | ----------------------------- |
| **URL**          | `POST /api/v1/luong-co-ban`   |
| **Method**       | `POST`                        |
| **Content-Type** | `application/json`            |
| **Xác thực**     | Bearer Token (JWT)            |

**Request Body:**

```json
{
  "nhanVien": { "id": 1 },
  "luongCoBan": 5000000,
  "ngayApDung": "2026-01-01T00:00:00",
  "trangThai": 1
}
```

**Response:** `201 Created`

---

## 5. Cập nhật lương cơ bản

**URL:** `PUT /api/v1/luong-co-ban` — phải có `id`. **Response:** `200 OK`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ❌     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
