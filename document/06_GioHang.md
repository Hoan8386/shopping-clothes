# Giỏ Hàng Controller

> **Base Path:** `/api/v1/gio-hang`  
> **File:** `GioHangController.java`  
> Quản lý giỏ hàng của khách hàng: thêm sản phẩm, xem giỏ, xóa sản phẩm.

---

## Tổng quan

Mỗi khách hàng có **1 giỏ hàng duy nhất** (quan hệ 1-1 với `KhachHang`). Giỏ hàng chứa danh sách `ChiTietGioHang` (các biến thể sản phẩm đã thêm).

### Cấu trúc dữ liệu `GioHang`

| Trường            | Kiểu                 | Mô tả                              |
| ----------------- | -------------------- | ---------------------------------- |
| `maGioHang`       | Long                 | Mã giỏ hàng (auto-increment)       |
| `khachHang`       | KhachHang            | Khách hàng sở hữu giỏ (FK, unique) |
| `ngayTao`         | LocalDateTime        | Ngày tạo (tự động)                 |
| `chiTietGioHangs` | List<ChiTietGioHang> | Danh sách sản phẩm trong giỏ       |

---

## 1. Thêm sản phẩm vào giỏ hàng

| Thuộc tính       | Chi tiết                              |
| ---------------- | ------------------------------------- |
| **URL**          | `POST /api/v1/gio-hang/them-san-pham` |
| **Method**       | `POST`                                |
| **Content-Type** | `application/json`                    |
| **Xác thực**     | Bearer Token (JWT) — KHACH_HANG       |

**Request Body:** `ReqThemGioHangDTO`

```json
{
  "maChiTietSanPham": 1,
  "soLuong": 2
}
```

**Kiểu dữ liệu:**

```json
{
  "maChiTietSanPham": "Long",
  "soLuong": "Integer"
}
```

| Trường             | Kiểu    | Bắt buộc | Mô tả                |
| ------------------ | ------- | -------- | -------------------- |
| `maChiTietSanPham` | Long    | **Có**   | Mã biến thể sản phẩm |
| `soLuong`          | Integer | **Có**   | Số lượng cần thêm    |

**Response:** `201 Created` — Trả về `ChiTietGioHang`

> **Lưu ý:** Nếu sản phẩm đã có trong giỏ → tăng số lượng. Nếu chưa có → tạo dòng mới.

---

## 2. Lấy giỏ hàng của tôi

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/gio-hang/cua-toi`  |
| **Method**   | `GET`                           |
| **Xác thực** | Bearer Token (JWT) — KHACH_HANG |

**Response:** `200 OK` — Trả về `ResGioHangDTO`

```json
{
  "maGioHang": 1,
  "tongSoLuong": 3,
  "tongTien": 750000,
  "chiTietGioHangs": [
    {
      "maChiTietGioHang": 1,
      "maChiTietSanPham": 1,
      "tenSanPham": "Áo Polo Classic",
      "kichThuoc": "M",
      "mauSac": "Đen",
      "giaBan": 250000,
      "soLuong": 2,
      "thanhTien": 500000
    }
  ]
}
```

**Kiểu dữ liệu:**

```json
{
  "maGioHang": "Long",
  "tongSoLuong": "Integer",
  "tongTien": "Double",
  "chiTietGioHangs": [
    {
      "maChiTietGioHang": "Long",
      "maChiTietSanPham": "Long",
      "tenSanPham": "String",
      "kichThuoc": "String",
      "mauSac": "String",
      "giaBan": "Double",
      "soLuong": "Integer",
      "thanhTien": "Double"
    }
  ]
}
```

> **Lưu ý:** Hệ thống tự xác định khách hàng từ JWT token (SecurityContext).

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy giỏ hàng |

---

## 3. Xóa sản phẩm khỏi giỏ hàng

| Thuộc tính   | Chi tiết                                              |
| ------------ | ----------------------------------------------------- |
| **URL**      | `DELETE /api/v1/gio-hang/chi-tiet/{maChiTietGioHang}` |
| **Method**   | `DELETE`                                              |
| **Xác thực** | Bearer Token (JWT) — KHACH_HANG                       |

**Path Parameters:**

| Tham số            | Kiểu | Mô tả                        |
| ------------------ | ---- | ---------------------------- |
| `maChiTietGioHang` | Long | Mã chi tiết giỏ hàng cần xóa |

**Response:** `204 No Content`

---

## 4. Lấy danh sách khuyến mãi hợp lệ cho giỏ hàng

| Thuộc tính   | Chi tiết                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/gio-hang/khuyen-mai-hop-le` |
| **Method**   | `GET`                                    |
| **Xác thực** | Bearer Token (JWT) — KHACH_HANG          |

**Response:** `200 OK` — Trả về `ResKhuyenMaiHopLeDTO`

```json
{
  "khuyenMaiHoaDon": [
    {
      "id": 1,
      "tenKhuyenMai": "Giảm 10% cho đơn từ 500k",
      "hoaDonToiThieu": 500000,
      "phanTramGiam": 10,
      "giamToiDa": 100000,
      "trangThai": 1
    }
  ],
  "khuyenMaiDiem": [
    {
      "id": 1,
      "tenKhuyenMai": "Đổi 50 điểm giảm 15%",
      "diemToiThieu": 50,
      "hoaDonToiThieu": 300000,
      "phanTramGiam": 15,
      "giamToiDa": 200000,
      "trangThai": 1
    }
  ]
}
```

**Kiểu dữ liệu:**

```json
{
  "khuyenMaiHoaDon": "List<KhuyenMaiTheoHoaDon>",
  "khuyenMaiDiem": "List<KhuyenMaiTheoDiem>"
}
```

> **Lưu ý:** Hệ thống tự lọc chỉ trả về các khuyến mãi:
>
> - Còn hạn (thời gian hiện tại nằm trong `thoiGianBatDau` – `thoiGianKetThuc`)
> - Đủ điều kiện (tổng tiền giỏ hàng ≥ `hoaDonToiThieu`, điểm tích lũy ≥ `diemToiThieu`)

---

## 5. Xem trước giảm giá khi áp dụng khuyến mãi

| Thuộc tính       | Chi tiết                                   |
| ---------------- | ------------------------------------------ |
| **URL**          | `POST /api/v1/gio-hang/ap-dung-khuyen-mai` |
| **Method**       | `POST`                                     |
| **Content-Type** | `application/json`                         |
| **Xác thực**     | Bearer Token (JWT) — KHACH_HANG            |

**Request Body:** `ReqApDungKhuyenMaiDTO`

```json
{
  "maKhuyenMaiHoaDon": 1,
  "maKhuyenMaiDiem": 2
}
```

**Kiểu dữ liệu:**

```json
{
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null"
}
```

| Trường              | Kiểu | Bắt buộc | Mô tả                      |
| ------------------- | ---- | -------- | -------------------------- |
| `maKhuyenMaiHoaDon` | Long | Không    | Mã khuyến mãi theo hóa đơn |
| `maKhuyenMaiDiem`   | Long | Không    | Mã khuyến mãi theo điểm    |

**Response:** `200 OK` — Trả về `ResApDungKhuyenMaiDTO`

```json
{
  "tongTienGoc": 750000,
  "maKhuyenMaiHoaDon": 1,
  "tenKhuyenMaiHoaDon": "Giảm 10% cho đơn từ 500k",
  "tienGiamHoaDon": 75000,
  "maKhuyenMaiDiem": 2,
  "tenKhuyenMaiDiem": "Đổi 50 điểm giảm 15%",
  "tienGiamDiem": 101250,
  "tongTienGiam": 176250,
  "tongTienTra": 573750
}
```

**Kiểu dữ liệu:**

```json
{
  "tongTienGoc": "Integer",
  "maKhuyenMaiHoaDon": "Long | null",
  "tenKhuyenMaiHoaDon": "String | null",
  "tienGiamHoaDon": "Integer | null",
  "maKhuyenMaiDiem": "Long | null",
  "tenKhuyenMaiDiem": "String | null",
  "tienGiamDiem": "Integer | null",
  "tongTienGiam": "Integer",
  "tongTienTra": "Integer"
}
```

> **Lưu ý:** Endpoint này chỉ **xem trước** kết quả giảm giá, không thực sự tạo đơn hàng. Khách hàng dùng thông tin này để xác nhận trước khi đặt hàng.

---

## Phân quyền

| Vai trò    | Thêm SP | Xem giỏ | Xóa SP | KM hợp lệ | Áp dụng KM |
| ---------- | ------- | ------- | ------ | --------- | ---------- |
| ADMIN      | ✅      | ✅      | ✅     | ✅        | ✅         |
| NHAN_VIEN  | ❌      | ❌      | ❌     | ❌        | ❌         |
| KHACH_HANG | ✅      | ✅      | ✅     | ✅        | ✅         |
