# Chi Tiết Đơn Hàng Controller

> **Base Path:** `/api/v1/chi-tiet-don-hang`  
> **File:** `ChiTietDonHangController.java`  
> Quản lý chi tiết (dòng sản phẩm) của đơn hàng.

---

## Tổng quan

### Cấu trúc dữ liệu `ChiTietDonHang`

| Trường           | Kiểu           | Mô tả                                     |
| ---------------- | -------------- | ----------------------------------------- |
| `id`             | Long           | Mã chi tiết đơn hàng (auto-increment)     |
| `donHang`        | DonHang        | Đơn hàng cha (FK, ẩn trong JSON response) |
| `chiTietSanPham` | ChiTietSanPham | Biến thể sản phẩm (FK)                    |
| `giaSanPham`     | Double         | Giá sản phẩm tại thời điểm mua (VND)      |
| `giamGia`        | Double         | Phần trăm giảm giá (%)                    |
| `giaGiam`        | Double         | Số tiền giảm (VND)                        |
| `soLuong`        | Integer        | Số lượng mua                              |
| `thanhTien`      | Double         | Thành tiền (VND)                          |
| `ngayTao`        | LocalDateTime  | Ngày tạo (tự động)                        |
| `ngayCapNhat`    | LocalDateTime  | Ngày cập nhật (tự động)                   |

---

## 1. Lấy tất cả chi tiết đơn hàng

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-don-hang` |
| **Method**   | `GET`                           |
| **Xác thực** | Bearer Token (JWT)              |

**Response:** `200 OK` — Trả về `List<ChiTietDonHang>`

```json
[
  {
    "id": 1,
    "chiTietSanPham": {
      "id": 1,
      "soLuong": 15,
      "mauSac": { "tenMauSac": "Đen" },
      "kichThuoc": { "tenKichThuoc": "M" }
    },
    "giaSanPham": 250000,
    "giamGia": 10,
    "giaGiam": 25000,
    "soLuong": 2,
    "thanhTien": 450000,
    "ngayTao": "2026-03-01T10:00:00"
  }
]
```

---

## 2. Lấy chi tiết đơn hàng theo mã đơn

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}` |
| **Method**   | `GET`                                                |
| **Xác thực** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham số     | Kiểu | Mô tả       |
| ----------- | ---- | ----------- |
| `donHangId` | Long | Mã đơn hàng |

**Response:** `200 OK` — Trả về `List<ChiTietDonHang>`

---

## 3. Lấy chi tiết đơn hàng theo ID

| Thuộc tính   | Chi tiết                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/chi-tiet-don-hang/{id}` |
| **Method**   | `GET`                                |
| **Xác thực** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                |
| ------- | ---- | -------------------- |
| `id`    | Long | Mã chi tiết đơn hàng |

**Response:** `200 OK` — Trả về `ChiTietDonHang`

**Lỗi:**

| HTTP Status | Mô tả                            |
| ----------- | -------------------------------- |
| `400`       | Không tìm thấy chi tiết đơn hàng |

---

## 4. Tạo chi tiết đơn hàng

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-don-hang` |
| **Method**       | `POST`                           |
| **Content-Type** | `application/json`               |
| **Xác thực**     | Bearer Token (JWT)               |

**Request Body:**

```json
{
  "donHang": { "id": 1 },
  "chiTietSanPham": { "id": 1 },
  "giaSanPham": 250000,
  "giamGia": 10,
  "giaGiam": 25000,
  "soLuong": 2,
  "thanhTien": 450000
}
```

**Response:** `201 Created` — Trả về `ChiTietDonHang`

---

## 5. Cập nhật chi tiết đơn hàng

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-don-hang` |
| **Method**       | `PUT`                           |
| **Content-Type** | `application/json`              |
| **Xác thực**     | Bearer Token (JWT)              |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "soLuong": 3,
  "thanhTien": 675000
}
```

**Response:** `200 OK` — Trả về `ChiTietDonHang`

**Lỗi:**

| HTTP Status | Mô tả                                    |
| ----------- | ---------------------------------------- |
| `400`       | Mã chi tiết đơn hàng không được để trống |

---

## 6. Xóa chi tiết đơn hàng

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-don-hang/{id}` |
| **Method**   | `DELETE`                                |
| **Xác thực** | Bearer Token (JWT)                      |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                            |
| ----------- | -------------------------------- |
| `400`       | Không tìm thấy chi tiết đơn hàng |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ✅         | ❌        | ❌           |
| KHACH_HANG | ✅        | ❌         | ❌        | ❌           |
