# Nhân Viên Controller

> **Base Path:** `/api/v1/nhan-vien`  
> **File:** `NhanVienController.java`  
> Quản lý thông tin nhân viên (xem danh sách, thêm, cập nhật, xóa).

---

## Cấu trúc dữ liệu trả về `ResNhanVienDTO`

| Trường           | Kiểu          | Mô tả                            |
| ---------------- | ------------- | -------------------------------- |
| `id`             | Long          | Mã nhân viên                     |
| `tenNhanVien`    | String        | Tên nhân viên                    |
| `email`          | String        | Email                            |
| `soDienThoai`    | String        | Số điện thoại                    |
| `ngayBatDauLam`  | LocalDateTime | Ngày bắt đầu làm                 |
| `ngayKetThucLam` | LocalDateTime | Ngày kết thúc làm                |
| `trangThai`      | Integer       | Trạng thái nhân viên             |
| `cuaHang`        | Object        | Thông tin cửa hàng của nhân viên |
| `role`           | Object        | Thông tin vai trò                |

**`cuaHang` gồm:** `id`, `tenCuaHang`, `diaChi`, `soDienThoai`, `email`, `trangThai`  
**`role` gồm:** `id`, `name`, `description`, `active`

---

## 1. Xem danh sách nhân viên

| Thuộc tính   | Chi tiết                |
| ------------ | ----------------------- |
| **URL**      | `GET /api/v1/nhan-vien` |
| **Method**   | `GET`                   |
| **Xác thực** | Bearer Token (JWT)      |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenNhanVien": "An",
    "email": "an@s.com",
    "soDienThoai": "0901000001",
    "ngayBatDauLam": null,
    "ngayKetThucLam": null,
    "trangThai": 1,
    "cuaHang": {
      "id": 1,
      "tenCuaHang": "Chi nhánh Quận 1",
      "diaChi": "123 Nguyễn Huệ, Q.1, TP.HCM",
      "soDienThoai": "02812345678",
      "email": "q1@shop.com",
      "trangThai": 1
    },
    "role": {
      "id": 2,
      "name": "NHAN_VIEN",
      "description": "Nhân viên bán hàng",
      "active": true
    }
  }
]
```

**Quy tắc dữ liệu trả về theo vai trò:**

- `ADMIN`: xem toàn bộ nhân viên.
- `NHAN_VIEN`: chỉ xem nhân viên cùng cửa hàng với tài khoản đang đăng nhập.

---

## 2. Tạo nhân viên

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `POST /api/v1/nhan-vien` |
| **Method**   | `POST`                   |
| **Xác thực** | Bearer Token (JWT)       |

**Request Body:**

```json
{
  "cuaHang": { "id": 1 },
  "tenNhanVien": "Nguyen Van A",
  "email": "nva@s.com",
  "soDienThoai": "0901234567",
  "matKhau": "123456",
  "ngayBatDauLam": "2026-03-12T08:00:00",
  "trangThai": 1,
  "role": { "id": 2 }
}
```

> Nếu không truyền `role`, hệ thống sẽ tự gán role mặc định nhân viên.

**Response:** `201 Created`

```json
{
  "id": 10,
  "tenNhanVien": "Nguyen Van A",
  "email": "nva@s.com",
  "soDienThoai": "0901234567",
  "ngayBatDauLam": "2026-03-12T08:00:00",
  "ngayKetThucLam": null,
  "trangThai": 1,
  "cuaHang": {
    "id": 1,
    "tenCuaHang": "Chi nhánh Quận 1",
    "diaChi": "123 Nguyễn Huệ, Q.1, TP.HCM",
    "soDienThoai": "02812345678",
    "email": "q1@shop.com",
    "trangThai": 1
  },
  "role": {
    "id": 2,
    "name": "NHAN_VIEN",
    "description": "Nhân viên bán hàng",
    "active": true
  }
}
```

---

## 3. Cập nhật nhân viên

| Thuộc tính   | Chi tiết                |
| ------------ | ----------------------- |
| **URL**      | `PUT /api/v1/nhan-vien` |
| **Method**   | `PUT`                   |
| **Xác thực** | Bearer Token (JWT)      |

**Request Body:**

```json
{
  "id": 10,
  "cuaHang": { "id": 2 },
  "tenNhanVien": "Nguyen Van A Updated",
  "email": "nva@s.com",
  "soDienThoai": "0901234568",
  "matKhau": "123456",
  "ngayBatDauLam": "2026-03-12T08:00:00",
  "ngayKetThucLam": null,
  "trangThai": 1,
  "role": { "id": 2 }
}
```

**Response:** `200 OK` (dữ liệu `ResNhanVienDTO`)

---

## 4. Xóa nhân viên

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `DELETE /api/v1/nhan-vien/{id}` |
| **Method**   | `DELETE`                        |
| **Xác thực** | Bearer Token (JWT)              |

**Response:** `204 No Content`

---

## Lỗi thường gặp

| HTTP Status | Mô tả                            |
| ----------- | -------------------------------- |
| `400`       | Mã nhân viên không được để trống |
| `400`       | Không tìm thấy nhân viên theo ID |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |


