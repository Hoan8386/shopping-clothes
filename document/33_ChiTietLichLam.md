# Chi Tiết Lịch Làm Controller

> **Base Path:** `/api/v1/chi-tiet-lich-lam`  
> **File:** `ChiTietLichLamController.java`  
> Quản lý chi tiết từng ca trong lịch làm việc (lịch làm → ca làm việc).

---

## Cấu trúc dữ liệu `ChiTietLichLam`

| Trường        | Kiểu          | Mô tả                                |
| ------------- | ------------- | ------------------------------------ |
| `id`          | Long          | Mã chi tiết lịch (auto-increment)    |
| `lichLamViec` | Object        | Lịch làm việc (FK MaLichLam)         |
| `caLamViec`   | Object        | Ca làm việc (FK MaCaLam)             |
| `trangThai`   | Integer       | 1=Xác nhận, 0=Chờ duyệt             |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)                   |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)              |

---

## 1. Lấy danh sách chi tiết lịch làm

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-lich-lam`   |
| **Method**   | `GET`                             |
| **Xác thực** | Bearer Token (JWT)                |

---

## 2. Lấy chi tiết theo ID

| Thuộc tính   | Chi tiết                               |
| ------------ | -------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-lich-lam/{id}`   |
| **Method**   | `GET`                                  |
| **Xác thực** | Bearer Token (JWT)                     |

---

## 3. Lấy chi tiết theo lịch làm việc

| Thuộc tính   | Chi tiết                                                             |
| ------------ | -------------------------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-lich-lam/lich-lam-viec/{lichLamViecId}`      |
| **Method**   | `GET`                                                                |
| **Xác thực** | Bearer Token (JWT)                                                   |

---

## 4. Tạo chi tiết lịch làm

| Thuộc tính       | Chi tiết                          |
| ---------------- | --------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-lich-lam`  |
| **Method**       | `POST`                            |
| **Content-Type** | `application/json`                |
| **Xác thực**     | Bearer Token (JWT)                |

**Request Body:**

```json
{
  "lichLamViec": { "id": 1 },
  "caLamViec": { "id": 1 },
  "trangThai": 1
}
```

**Response:** `201 Created`

---

## 5. Cập nhật chi tiết lịch làm

**URL:** `PUT /api/v1/chi-tiet-lich-lam` — phải có `id`. **Response:** `200 OK`

---

## 6. Xóa chi tiết lịch làm

**URL:** `DELETE /api/v1/chi-tiet-lich-lam/{id}` — **Response:** `204 No Content`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ✅   | ✅  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
