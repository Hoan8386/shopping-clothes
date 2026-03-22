# Ca Làm Việc Controller

> **Base Path:** `/api/v1/ca-lam-viec`  
> **File:** `CaLamViecController.java`  
> Quản lý ca làm việc (ca sáng, ca chiều, ca tối, ...).

---

## Cấu trúc dữ liệu `CaLamViec`

| Trường        | Kiểu          | Mô tả                         |
| ------------- | ------------- | ----------------------------- |
| `id`          | Long          | Mã ca làm (auto-increment)    |
| `tenCaLam`    | String(255)   | Tên ca làm việc               |
| `gioBatDau`   | LocalTime     | Giờ bắt đầu ca (HH:mm:ss)    |
| `gioKetThuc`  | LocalTime     | Giờ kết thúc ca (HH:mm:ss)   |
| `trangThai`   | Integer       | 1=Hoạt động, 0=Không HĐ      |
| `json`        | String (TEXT) | Dữ liệu mở rộng              |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)            |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)       |

---

## 1. Lấy danh sách ca làm việc

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/ca-lam-viec`  |
| **Method**   | `GET`                      |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenCaLam": "Ca Sáng",
    "gioBatDau": "07:00:00",
    "gioKetThuc": "15:00:00",
    "trangThai": 1,
    "ngayTao": "2026-03-17T20:00:00"
  }
]
```

---

## 2. Lấy ca làm việc theo ID

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/ca-lam-viec/{id}`  |
| **Method**   | `GET`                           |
| **Xác thực** | Bearer Token (JWT)              |

**Lỗi:**

| HTTP Status | Mô tả                    |
| ----------- | ------------------------ |
| `400`       | Không tìm thấy ca làm việc |

---

## 3. Tạo ca làm việc

| Thuộc tính       | Chi tiết                   |
| ---------------- | -------------------------- |
| **URL**          | `POST /api/v1/ca-lam-viec` |
| **Method**       | `POST`                     |
| **Content-Type** | `application/json`         |
| **Xác thực**     | Bearer Token (JWT)         |

**Request Body:**

```json
{
  "tenCaLam": "Ca Sáng",
  "gioBatDau": "07:00:00",
  "gioKetThuc": "15:00:00",
  "trangThai": 1
}
```

**Response:** `201 Created`

---

## 4. Cập nhật ca làm việc

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `PUT /api/v1/ca-lam-viec` |
| **Method**       | `PUT`                     |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "tenCaLam": "Ca Sáng - Updated",
  "gioBatDau": "06:30:00",
  "gioKetThuc": "14:30:00",
  "trangThai": 1
}
```

---

## 5. Xóa ca làm việc

| Thuộc tính   | Chi tiết                           |
| ------------ | ---------------------------------- |
| **URL**      | `DELETE /api/v1/ca-lam-viec/{id}`  |
| **Method**   | `DELETE`                           |
| **Xác thực** | Bearer Token (JWT)                 |

**Response:** `204 No Content`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
