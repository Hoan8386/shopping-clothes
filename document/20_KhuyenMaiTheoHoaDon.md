# Khuyến Mãi Theo Hóa Đơn Controller

> **Base Path:** `/api/v1/khuyen-mai-theo-hoa-don`  
> **File:** `KhuyenMaiTheoHoaDonController.java`  
> Quản lý chương trình khuyến mãi áp dụng cho hóa đơn (giảm giá theo tổng giá trị đơn hàng).

---

## Tổng quan

Khuyến mãi theo hóa đơn cho phép giảm giá trực tiếp trên tổng giá trị đơn hàng. Mỗi chương trình khuyến mãi có thời gian hiệu lực, số lượng sử dụng giới hạn, và các điều kiện áp dụng (hóa đơn tối đa, giảm tối đa).

### Cấu trúc dữ liệu `KhuyenMaiTheoHoaDon`

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

## 1. Lấy danh sách khuyến mãi theo hóa đơn

| Thuộc tính   | Chi tiết                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/khuyen-mai-theo-hoa-don`    |
| **Method**   | `GET`                                    |
| **Xác thực** | Bearer Token (JWT) — Tất cả đã đăng nhập |

**Response:** `200 OK` — Trả về `List<KhuyenMaiTheoHoaDon>`

```json
[
  {
    "id": 1,
    "tenKhuyenMai": "Giảm 10% đơn từ 500K",
    "giamToiDa": 100000,
    "hoaDonToiDa": 500000,
    "phanTramGiam": 10.0,
    "hinhThuc": 1,
    "thoiGianBatDau": "2026-01-01T00:00:00",
    "thoiGianKetThuc": "2026-06-30T23:59:59",
    "soLuong": 100,
    "trangThai": 1,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

---

## 2. Lấy khuyến mãi theo hóa đơn theo ID

| Thuộc tính   | Chi tiết                                   |
| ------------ | ------------------------------------------ |
| **URL**      | `GET /api/v1/khuyen-mai-theo-hoa-don/{id}` |
| **Method**   | `GET`                                      |
| **Xác thực** | Bearer Token (JWT) — Tất cả đã đăng nhập   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                 |
| ------- | ---- | --------------------- |
| `id`    | Long | Mã khuyến mãi hóa đơn |

**Response:** `200 OK` — Trả về `KhuyenMaiTheoHoaDon`

```json
{
  "id": 1,
  "tenKhuyenMai": "Giảm 10% đơn từ 500K",
  "giamToiDa": 100000,
  "hoaDonToiDa": 500000,
  "phanTramGiam": 10.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-01-01T00:00:00",
  "thoiGianKetThuc": "2026-06-30T23:59:59",
  "soLuong": 100,
  "trangThai": 1,
  "ngayTao": "2026-03-01T10:00:00",
  "ngayCapNhat": null
}
```

**Lỗi:**

| HTTP Status | Mô tả                                  |
| ----------- | -------------------------------------- |
| `400`       | Không tìm thấy khuyến mãi theo hóa đơn |

---

## 3. Tạo khuyến mãi theo hóa đơn

| Thuộc tính       | Chi tiết                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `POST /api/v1/khuyen-mai-theo-hoa-don`   |
| **Method**       | `POST`                                   |
| **Content-Type** | `application/json`                       |
| **Xác thực**     | Bearer Token (JWT) — Yêu cầu quyền ADMIN |

**Request Body:**

```json
{
  "tenKhuyenMai": "Giảm 10% đơn từ 300K",
  "giamToiDa": 50000,
  "hoaDonToiDa": 300000,
  "phanTramGiam": 10.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-06-30T23:59:59",
  "soLuong": 100,
  "trangThai": 1
}
```

**Response:** `201 Created` — Trả về `KhuyenMaiTheoHoaDon`

> **Lưu ý:** Trường `ngayTao` được tự động gán khi tạo mới.

---

## 4. Cập nhật khuyến mãi theo hóa đơn

| Thuộc tính       | Chi tiết                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `PUT /api/v1/khuyen-mai-theo-hoa-don`    |
| **Method**       | `PUT`                                    |
| **Content-Type** | `application/json`                       |
| **Xác thực**     | Bearer Token (JWT) — Yêu cầu quyền ADMIN |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "tenKhuyenMai": "Giảm 15% đơn từ 500K (cập nhật)",
  "giamToiDa": 150000,
  "hoaDonToiDa": 500000,
  "phanTramGiam": 15.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
  "soLuong": 200,
  "trangThai": 1
}
```

**Response:** `200 OK` — Trả về `KhuyenMaiTheoHoaDon`

**Lỗi:**

| HTTP Status | Mô tả                                          |
| ----------- | ---------------------------------------------- |
| `400`       | Mã khuyến mãi theo hóa đơn không được để trống |
| `500`       | Không tìm thấy khuyến mãi để cập nhật          |

> **Lưu ý:** Trường `ngayCapNhat` được tự động cập nhật.

---

## 5. Xóa khuyến mãi theo hóa đơn

| Thuộc tính   | Chi tiết                                      |
| ------------ | --------------------------------------------- |
| **URL**      | `DELETE /api/v1/khuyen-mai-theo-hoa-don/{id}` |
| **Method**   | `DELETE`                                      |
| **Xác thực** | Bearer Token (JWT) — Yêu cầu quyền ADMIN      |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                 |
| ------- | ---- | --------------------- |
| `id`    | Long | Mã khuyến mãi cần xóa |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                                  |
| ----------- | -------------------------------------- |
| `400`       | Không tìm thấy khuyến mãi theo hóa đơn |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| QUAN_LY    | ✅        | ❌         | ❌        | ❌           |
| NHAN_VIEN  | ✅        | ❌         | ❌        | ❌           |
| KHACH_HANG | ✅        | ❌         | ❌        | ❌           |
