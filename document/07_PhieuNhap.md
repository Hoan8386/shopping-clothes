# Phiếu Nhập Controller

> **Base Path:** `/api/v1/phieu-nhap`  
> **File:** `PhieuNhapController.java`  
> Quản lý phiếu nhập hàng từ nhà cung cấp.

---

## Tổng quan

### Cấu trúc dữ liệu `PhieuNhap`

| Trường              | Kiểu          | Mô tả                          |
| ------------------- | ------------- | ------------------------------ |
| `id`                | Long          | Mã phiếu nhập (auto-increment) |
| `cuaHang`           | CuaHang       | Cửa hàng nhập hàng (FK)        |
| `nhaCungCap`        | NhaCungCap    | Nhà cung cấp (FK)              |
| `tenPhieuNhap`      | String(255)   | Tên phiếu nhập                 |
| `trangThai`         | Integer       | Trạng thái phiếu nhập          |
| `ngayGiaoHang`      | LocalDateTime | Ngày giao hàng dự kiến         |
| `ngayNhanHang`      | LocalDateTime | Ngày thực nhận hàng            |
| `chiTietPhieuNhaps` | List          | Danh sách chi tiết phiếu nhập  |
| `ngayTao`           | LocalDateTime | Ngày tạo (tự động)             |
| `ngayCapNhat`       | LocalDateTime | Ngày cập nhật (tự động)        |

---

## 1. Lấy danh sách phiếu nhập

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/phieu-nhap` |
| **Method**   | `GET`                    |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK` — Trả về `List<PhieuNhap>`

```json
[
  {
    "id": 1,
    "cuaHang": { "id": 1, "tenCuaHang": "Chi nhánh Q.1" },
    "nhaCungCap": { "id": 1, "tenNhaCungCap": "Công ty ABC" },
    "tenPhieuNhap": "Nhập hàng tháng 3",
    "trangThai": 1,
    "ngayGiaoHang": "2026-03-01T00:00:00",
    "ngayNhanHang": "2026-03-03T00:00:00",
    "chiTietPhieuNhaps": [...],
    "ngayTao": "2026-02-28T10:00:00"
  }
]
```

---

## 2. Lấy phiếu nhập theo ID

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/phieu-nhap/{id}` |
| **Method**   | `GET`                         |
| **Xác thực** | Bearer Token (JWT)            |

**Response:** `200 OK` — Trả về `PhieuNhap` (bao gồm `chiTietPhieuNhaps`)

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy phiếu nhập |

---

## 3. Tạo phiếu nhập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/phieu-nhap` |
| **Method**       | `POST`                    |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:**

```json
{
  "cuaHang": { "id": 1 },
  "nhaCungCap": { "id": 1 },
  "tenPhieuNhap": "Nhập hàng tháng 3",
  "trangThai": 0,
  "ngayGiaoHang": "2026-03-01T00:00:00"
}
```

**Response:** `201 Created` — Trả về `PhieuNhap`

---

## 4. Cập nhật phiếu nhập

| Thuộc tính       | Chi tiết                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/phieu-nhap` |
| **Method**       | `PUT`                    |
| **Content-Type** | `application/json`       |
| **Xác thực**     | Bearer Token (JWT)       |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "trangThai": 1,
  "ngayNhanHang": "2026-03-03T10:00:00"
}
```

**Response:** `200 OK` — Trả về `PhieuNhap`

**Lỗi:**

| HTTP Status | Mô tả                             |
| ----------- | --------------------------------- |
| `400`       | Mã phiếu nhập không được để trống |

---

## 5. Xóa phiếu nhập

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/phieu-nhap/{id}` |
| **Method**   | `DELETE`                         |
| **Xác thực** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy phiếu nhập |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ✅         | ✅        | ❌           |
| KHACH_HANG | ❌        | ❌         | ❌        | ❌           |
