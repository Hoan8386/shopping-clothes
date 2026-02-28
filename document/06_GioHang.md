# Giỏ Hàng Controller

> **Base Path:** `/api/v1/gio-hang`  
> **File:** `GioHangController.java`  
> Quản lý giỏ hàng của khách hàng.

---

## 1. Thêm sản phẩm vào giỏ hàng

| Thuộc tính       | Chi tiết                              |
| ---------------- | ------------------------------------- |
| **URL**          | `POST /api/v1/gio-hang/them-san-pham` |
| **Content-Type** | `application/json`                    |
| **Xác thực**     | Bearer Token (JWT)                    |

**Request Body:** `ReqThemGioHangDTO`

```json
{
  // Thông tin sản phẩm cần thêm vào giỏ
}
```

**Response:** `201 Created` — Trả về `ChiTietGioHang`

---

## 2. Lấy giỏ hàng của tôi

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `GET /api/v1/gio-hang/cua-toi` |
| **Xác thực** | Bearer Token (JWT)             |

**Response:** `200 OK` — Trả về `ResGioHangDTO`

```json
{
  // Thông tin giỏ hàng và danh sách sản phẩm trong giỏ
}
```

**Lỗi:**

- `400` — Không tìm thấy giỏ hàng

---

## 3. Xóa sản phẩm khỏi giỏ hàng

| Thuộc tính   | Chi tiết                                              |
| ------------ | ----------------------------------------------------- |
| **URL**      | `DELETE /api/v1/gio-hang/chi-tiet/{maChiTietGioHang}` |
| **Xác thực** | Bearer Token (JWT)                                    |

**Path Parameters:**

| Tham số            | Kiểu | Mô tả                        |
| ------------------ | ---- | ---------------------------- |
| `maChiTietGioHang` | Long | Mã chi tiết giỏ hàng cần xóa |

**Response:** `204 No Content`
