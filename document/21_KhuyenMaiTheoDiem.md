# Khuyến Mãi Theo Điểm Controller

> **Base Path:** `/api/v1/khuyen-mai-theo-diem`  
> **File:** `KhuyenMaiTheoDiemController.java`  
> Quản lý chương trình khuyến mãi áp dụng bằng điểm tích lũy của khách hàng.

---

## Tổng quan

Khuyến mãi theo điểm cho phép khách hàng sử dụng điểm tích lũy để đổi mã giảm giá áp dụng cho đơn hàng. Mỗi chương trình có thời gian hiệu lực và số lượng giới hạn.

### Quy tắc tích điểm

- Khi đơn hàng chuyển sang trạng thái **"Thành công" (trangThai = 3)**, hệ thống tự động cộng điểm tích lũy cho khách hàng.
- **Công thức:** Mỗi **100.000 VND** tiền thanh toán (`tongTienTra`) = **10 điểm**.
- Ví dụ: Đơn 350.000đ → 30 điểm, đơn 1.200.000đ → 120 điểm.

### Cấu trúc dữ liệu `KhuyenMaiTheoDiem`

| Trường            | Kiểu          | Mô tả                                          |
| ----------------- | ------------- | ---------------------------------------------- |
| `id`              | Long          | Mã khuyến mãi (auto-increment)                 |
| `tenKhuyenMai`    | String(255)   | Tên chương trình khuyến mãi                    |
| `giamToiDa`       | Integer       | Số tiền giảm tối đa (VND)                      |
| `hoaDonToiDa`     | Integer       | Giá trị hóa đơn tối thiểu để áp dụng (VND)     |
| `phanTramGiam`    | Double        | Phần trăm giảm giá (%)                         |
| `hinhThuc`        | Integer       | Hình thức áp dụng (0: tất cả, 1: có điều kiện) |
| `thoiGianBatDau`  | LocalDateTime | Thời gian bắt đầu hiệu lực                     |
| `thoiGianKetThuc` | LocalDateTime | Thời gian kết thúc hiệu lực                    |
| `soLuong`         | Integer       | Số lượng mã khuyến mãi còn lại                 |
| `trangThai`       | Integer       | Trạng thái (0: ngừng, 1: hoạt động)            |
| `ngayTao`         | LocalDateTime | Ngày tạo (tự động)                             |
| `ngayCapNhat`     | LocalDateTime | Ngày cập nhật (tự động)                        |

---

## 1. Lấy danh sách khuyến mãi theo điểm

| Thuộc tính   | Chi tiết                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/khuyen-mai-theo-diem`       |
| **Method**   | `GET`                                    |
| **Xác thực** | Bearer Token (JWT) — Tất cả đã đăng nhập |

**Response:** `200 OK` — Trả về `List<KhuyenMaiTheoDiem>`

```json
[
  {
    "id": 1,
    "tenKhuyenMai": "Đổi 50 điểm giảm 15%",
    "giamToiDa": 200000,
    "hoaDonToiDa": 800000,
    "phanTramGiam": 15.0,
    "hinhThuc": 1,
    "thoiGianBatDau": "2026-01-01T00:00:00",
    "thoiGianKetThuc": "2026-12-31T23:59:59",
    "soLuong": 100,
    "trangThai": 1,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

---

## 2. Lấy khuyến mãi theo điểm theo ID

| Thuộc tính   | Chi tiết                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/khuyen-mai-theo-diem/{id}`  |
| **Method**   | `GET`                                    |
| **Xác thực** | Bearer Token (JWT) — Tất cả đã đăng nhập |

**Path Parameters:**

| Tham số | Kiểu | Mô tả              |
| ------- | ---- | ------------------ |
| `id`    | Long | Mã khuyến mãi điểm |

**Response:** `200 OK` — Trả về `KhuyenMaiTheoDiem`

```json
{
  "id": 1,
  "tenKhuyenMai": "Đổi 50 điểm giảm 15%",
  "giamToiDa": 200000,
  "hoaDonToiDa": 800000,
  "phanTramGiam": 15.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-01-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
  "soLuong": 100,
  "trangThai": 1,
  "ngayTao": "2026-03-01T10:00:00",
  "ngayCapNhat": null
}
```

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy khuyến mãi theo điểm |

---

## 3. Tạo khuyến mãi theo điểm

| Thuộc tính       | Chi tiết                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `POST /api/v1/khuyen-mai-theo-diem`      |
| **Method**       | `POST`                                   |
| **Content-Type** | `application/json`                       |
| **Xác thực**     | Bearer Token (JWT) — Yêu cầu quyền ADMIN |

**Request Body:**

```json
{
  "tenKhuyenMai": "Đổi 30 điểm giảm 10%",
  "giamToiDa": 100000,
  "hoaDonToiDa": 500000,
  "phanTramGiam": 10.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
  "soLuong": 100,
  "trangThai": 1
}
```

**Response:** `201 Created` — Trả về `KhuyenMaiTheoDiem`

> **Lưu ý:** Trường `ngayTao` được tự động gán khi tạo mới.

---

## 4. Cập nhật khuyến mãi theo điểm

| Thuộc tính       | Chi tiết                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `PUT /api/v1/khuyen-mai-theo-diem`       |
| **Method**       | `PUT`                                    |
| **Content-Type** | `application/json`                       |
| **Xác thực**     | Bearer Token (JWT) — Yêu cầu quyền ADMIN |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "tenKhuyenMai": "Đổi 50 điểm giảm 20% (cập nhật)",
  "giamToiDa": 300000,
  "hoaDonToiDa": 1000000,
  "phanTramGiam": 20.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
  "soLuong": 150,
  "trangThai": 1
}
```

**Response:** `200 OK` — Trả về `KhuyenMaiTheoDiem`

**Lỗi:**

| HTTP Status | Mô tả                                       |
| ----------- | ------------------------------------------- |
| `400`       | Mã khuyến mãi theo điểm không được để trống |
| `500`       | Không tìm thấy khuyến mãi để cập nhật       |

> **Lưu ý:** Trường `ngayCapNhat` được tự động cập nhật.

---

## 5. Xóa khuyến mãi theo điểm

| Thuộc tính   | Chi tiết                                   |
| ------------ | ------------------------------------------ |
| **URL**      | `DELETE /api/v1/khuyen-mai-theo-diem/{id}` |
| **Method**   | `DELETE`                                   |
| **Xác thực** | Bearer Token (JWT) — Yêu cầu quyền ADMIN   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                 |
| ------- | ---- | --------------------- |
| `id`    | Long | Mã khuyến mãi cần xóa |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy khuyến mãi theo điểm |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| QUAN_LY    | ✅        | ❌         | ❌        | ❌           |
| NHAN_VIEN  | ✅        | ❌         | ❌        | ❌           |
| KHACH_HANG | ✅        | ❌         | ❌        | ❌           |
