# Lịch Làm Việc Controller

> **Base Path:** `/api/v1/lich-lam-viec`  
> **File:** `LichLamViecController.java`  
> Quản lý lịch làm việc của nhân viên. Hỗ trợ **import hàng loạt từ file Excel**.

---

## Cấu trúc dữ liệu `LichLamViec`

| Trường         | Kiểu          | Mô tả                              |
| -------------- | ------------- | ---------------------------------- |
| `id`           | Long          | Mã lịch làm (auto-increment)       |
| `nhanVien`     | Object        | Nhân viên (FK MaNhanVien)          |
| `ngayLamViec`  | LocalDate     | Ngày làm việc (yyyy-MM-dd)         |
| `trangThai`    | Integer       | 1=Đang làm, 0=Nghỉ                 |
| `json`         | String (TEXT) | Dữ liệu mở rộng                   |
| `ngayTao`      | LocalDateTime | Ngày tạo (tự động)                 |
| `ngayCapNhat`  | LocalDateTime | Ngày cập nhật (tự động)            |

---

## 1. Lấy danh sách lịch làm việc

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/lich-lam-viec`   |
| **Method**   | `GET`                         |
| **Xác thực** | Bearer Token (JWT)            |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "nhanVien": { "id": 1, "tenNhanVien": "An" },
    "ngayLamViec": "2026-03-18",
    "trangThai": 1,
    "ngayTao": "2026-03-17T20:00:00"
  }
]
```

---

## 2. Lấy lịch làm việc theo ID

| Thuộc tính   | Chi tiết                           |
| ------------ | ---------------------------------- |
| **URL**      | `GET /api/v1/lich-lam-viec/{id}`   |
| **Method**   | `GET`                              |
| **Xác thực** | Bearer Token (JWT)                 |

---

## 3. Lấy lịch làm việc theo nhân viên

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/lich-lam-viec/nhan-vien/{nhanVienId}`  |
| **Method**   | `GET`                                                |
| **Xác thực** | Bearer Token (JWT)                                   |

---

## 4. Tạo lịch làm việc

| Thuộc tính       | Chi tiết                      |
| ---------------- | ----------------------------- |
| **URL**          | `POST /api/v1/lich-lam-viec`  |
| **Method**       | `POST`                        |
| **Content-Type** | `application/json`            |
| **Xác thực**     | Bearer Token (JWT)            |

**Request Body:**

```json
{
  "nhanVien": { "id": 1 },
  "ngayLamViec": "2026-03-18",
  "trangThai": 1
}
```

**Response:** `201 Created`

---

## 5. Import lịch làm việc từ Excel ⭐

| Thuộc tính       | Chi tiết                              |
| ---------------- | ------------------------------------- |
| **URL**          | `POST /api/v1/lich-lam-viec/import`   |
| **Method**       | `POST`                                |
| **Content-Type** | `multipart/form-data`                 |
| **Xác thực**     | Bearer Token (JWT)                    |

**Form Field:**

| Tên   | Kiểu | Mô tả             |
| ----- | ---- | ----------------- |
| `file`| File | File `.xlsx` cần import |

**Cấu trúc file Excel (3 cột):**

| Cột A         | Cột B                        | Cột C                     |
| ------------- | ---------------------------- | ------------------------- |
| MaNhanVien(*) | NgayLamViec(*) yyyy-MM-dd    | TrangThai (1=làm, 0=nghỉ) |
| 1             | 2026-03-18                   | 1                         |
| 2             | 2026-03-18                   | 1                         |

> [!NOTE]
> Dòng đầu tiên là header sẽ bị bỏ qua. Các dòng trống cũng bị bỏ qua.  
> Nếu `MaNhanVien` không tồn tại trong DB thì dòng đó bị bỏ qua.

**Response:** `201 Created` — danh sách các bản ghi đã import thành công.

---

## 6. Tải file Excel mẫu ⭐

| Thuộc tính   | Chi tiết                                           |
| ------------ | -------------------------------------------------- |
| **URL**      | `GET /api/v1/lich-lam-viec/download-template`      |
| **Method**   | `GET`                                              |
| **Xác thực** | Bearer Token (JWT)                                 |

**Response:** File `lich-lam-viec-mau.xlsx` được tải về ngay, chứa header và 3 dòng dữ liệu mẫu.

---

## 7. Cập nhật lịch làm việc

| Thuộc tính       | Chi tiết                     |
| ---------------- | ---------------------------- |
| **URL**          | `PUT /api/v1/lich-lam-viec`  |
| **Method**       | `PUT`                        |
| **Content-Type** | `application/json`           |
| **Xác thực**     | Bearer Token (JWT)           |

**Request Body:** (phải có `id`)

```json
{ "id": 1, "nhanVien": { "id": 1 }, "ngayLamViec": "2026-03-19", "trangThai": 0 }
```

---

## 8. Xóa lịch làm việc

| Thuộc tính   | Chi tiết                              |
| ------------ | ------------------------------------- |
| **URL**      | `DELETE /api/v1/lich-lam-viec/{id}`   |
| **Method**   | `DELETE`                              |
| **Xác thực** | Bearer Token (JWT)                    |

**Response:** `204 No Content`

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE | Import | Template |
| ---------- | --- | ---- | --- | ------ | ------ | -------- |
| ADMIN      | ✅  | ✅   | ✅  | ✅     | ✅     | ✅       |
| NHAN_VIEN  | ✅  | ✅   | ✅  | ❌     | ✅     | ✅       |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     | ❌     | ❌       |
