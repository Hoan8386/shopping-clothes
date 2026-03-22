# Lỗi Phát Sinh Controller

> **Base Path:** `/api/v1/loi-phat-sinh`  
> **File:** `LoiPhatSinhController.java`  
> Quản lý các lỗi phát sinh trong ca làm việc (đi trễ, vắng mặt, vi phạm quy định, ...) kèm mức phạt tiền.

---

## Cấu trúc dữ liệu `LoiPhatSinh`

| Trường           | Kiểu          | Mô tả                              |
| ---------------- | ------------- | ---------------------------------- |
| `id`             | Long          | Mã lỗi phát sinh (auto-increment)  |
| `lichLamViec`    | Object        | Lịch làm việc (FK MaLichLam)       |
| `chiTietLichLam` | Object        | Chi tiết ca xảy ra lỗi             |
| `tenLoiPhatSinh` | String(500)   | Tên/mô tả lỗi                      |
| `soTienTru`      | Long          | Số tiền bị trừ (VNĐ)               |
| `trangThai`      | Integer       | 1=Đã xử lý, 0=Chờ xử lý           |
| `json`           | String (TEXT) | Dữ liệu mở rộng                   |
| `ngayTao`        | LocalDateTime | Ngày tạo (tự động)                 |
| `ngayCapNhat`    | LocalDateTime | Ngày cập nhật (tự động)            |

---

## 1. Lấy danh sách lỗi phát sinh

**URL:** `GET /api/v1/loi-phat-sinh` — **Response:** `200 OK`

---

## 2. Lấy lỗi phát sinh theo ID

**URL:** `GET /api/v1/loi-phat-sinh/{id}`

---

## 3. Lấy lỗi phát sinh theo lịch làm việc

**URL:** `GET /api/v1/loi-phat-sinh/lich-lam-viec/{lichLamViecId}`

---

## 4. Tạo lỗi phát sinh

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `POST /api/v1/loi-phat-sinh`    |
| **Method**       | `POST`                          |
| **Content-Type** | `application/json`              |
| **Xác thực**     | Bearer Token (JWT)              |

**Request Body:**

```json
{
  "lichLamViec": { "id": 1 },
  "chiTietLichLam": { "id": 1 },
  "tenLoiPhatSinh": "Đi trễ 30 phút",
  "soTienTru": 50000,
  "trangThai": 0
}
```

**Response:** `201 Created`

---

## 5. Cập nhật lỗi phát sinh

**URL:** `PUT /api/v1/loi-phat-sinh` — phải có `id`. **Response:** `200 OK`

---

## 6. Xóa lỗi phát sinh

**URL:** `DELETE /api/v1/loi-phat-sinh/{id}` — **Response:** `204 No Content`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ✅   | ✅  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
