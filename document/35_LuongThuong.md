# Lương Thưởng Controller

> **Base Path:** `/api/v1/luong-thuong`  
> **File:** `LuongThuongController.java`  
> Quản lý lương thưởng (bonus) của nhân viên theo từng kỳ.

---

## Cấu trúc dữ liệu `LuongThuong`

| Trường        | Kiểu          | Mô tả                            |
| ------------- | ------------- | -------------------------------- |
| `id`          | Long          | Mã lương thưởng (auto-increment) |
| `nhanVien`    | Object        | Nhân viên (FK MaNhanVien)        |
| `tienThuong`  | Long          | Số tiền thưởng (VNĐ)            |
| `ngayBatDau`  | LocalDateTime | Ngày bắt đầu kỳ thưởng          |
| `ngayKetThuc` | LocalDateTime | Ngày kết thúc kỳ thưởng         |
| `trangThai`   | Integer       | 1=Đã chi, 0=Chờ chi             |
| `json`        | String (TEXT) | Dữ liệu mở rộng                 |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)               |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)          |

---

## 1. Lấy danh sách lương thưởng

**URL:** `GET /api/v1/luong-thuong` — **Response:** `200 OK`

---

## 2. Lấy lương thưởng theo ID

**URL:** `GET /api/v1/luong-thuong/{id}`

---

## 3. Lấy lương thưởng theo nhân viên

**URL:** `GET /api/v1/luong-thuong/nhan-vien/{nhanVienId}`

---

## 4. Tạo lương thưởng

| Thuộc tính       | Chi tiết                    |
| ---------------- | --------------------------- |
| **URL**          | `POST /api/v1/luong-thuong` |
| **Method**       | `POST`                      |
| **Content-Type** | `application/json`          |
| **Xác thực**     | Bearer Token (JWT)          |

**Request Body:**

```json
{
  "nhanVien": { "id": 1 },
  "tienThuong": 1000000,
  "ngayBatDau": "2026-03-01T00:00:00",
  "ngayKetThuc": "2026-03-31T23:59:59",
  "trangThai": 0
}
```

**Response:** `201 Created`

---

## 5. Cập nhật lương thưởng

**URL:** `PUT /api/v1/luong-thuong` — phải có `id`. **Response:** `200 OK`

---

## 6. Xóa lương thưởng

**URL:** `DELETE /api/v1/luong-thuong/{id}` — **Response:** `204 No Content`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
