# Đổi Ca Controller

> **Base Path:** `/api/v1/doi-ca`  
> **File:** `DoiCaController.java`  
> Quản lý yêu cầu đổi ca giữa các nhân viên.

---

## Cấu trúc dữ liệu `DoiCa`

| Trường           | Kiểu          | Mô tả                             |
| ---------------- | ------------- | --------------------------------- |
| `id`             | Long          | Mã đổi ca (auto-increment)        |
| `lichLamViec`    | Object        | Lịch làm việc gốc (FK MaLichLam) |
| `chiTietLichLam` | Object        | Chi tiết ca cần đổi               |
| `nhanVienNhanCa` | Object        | Nhân viên nhận ca (FK)            |
| `trangThai`      | Integer       | 0=Chờ, 1=Đồng ý, 2=Từ chối       |
| `json`           | String (TEXT) | Dữ liệu mở rộng                  |
| `ngayTao`        | LocalDateTime | Ngày tạo (tự động)                |
| `ngayCapNhat`    | LocalDateTime | Ngày cập nhật (tự động)           |

---

## 1. Lấy danh sách đổi ca

**URL:** `GET /api/v1/doi-ca` — **Response:** `200 OK`

---

## 2. Lấy đổi ca theo ID

**URL:** `GET /api/v1/doi-ca/{id}`

---

## 3. Lấy đổi ca theo lịch làm việc

**URL:** `GET /api/v1/doi-ca/lich-lam-viec/{lichLamViecId}`

---

## 4. Tạo yêu cầu đổi ca

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `POST /api/v1/doi-ca`  |
| **Method**       | `POST`                 |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:**

```json
{
  "lichLamViec": { "id": 1 },
  "chiTietLichLam": { "id": 1 },
  "nhanVienNhanCa": { "id": 2 },
  "trangThai": 0
}
```

**Response:** `201 Created`

---

## 5. Cập nhật đổi ca

**URL:** `PUT /api/v1/doi-ca` — phải có `id`. **Response:** `200 OK`

---

## 6. Xóa đổi ca

**URL:** `DELETE /api/v1/doi-ca/{id}` — **Response:** `204 No Content`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ✅   | ✅  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
