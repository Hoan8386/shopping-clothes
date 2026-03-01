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
  "chiTietSanPhamId": 1,
  "soLuong": 2
}
```

| Trường             | Kiểu    | Bắt buộc | Mô tả                |
| ------------------ | ------- | -------- | -------------------- |
| `chiTietSanPhamId` | Long    | **Có**   | Mã biến thể sản phẩm |
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
  "chiTietGioHangs": [
    {
      "maChiTietGioHang": 1,
      "chiTietSanPham": {
        "id": 1,
        "sanPham": { "tenSanPham": "Áo Polo Classic", "giaBan": 250000 },
        "mauSac": { "tenMauSac": "Đen" },
        "kichThuoc": { "tenKichThuoc": "M" }
      },
      "soLuong": 2
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

## Phân quyền

| Vai trò    | Thêm SP | Xem giỏ | Xóa SP |
| ---------- | ------- | ------- | ------ |
| ADMIN      | ❌      | ❌      | ❌     |
| NHAN_VIEN  | ❌      | ❌      | ❌     |
| KHACH_HANG | ✅      | ✅      | ✅     |
