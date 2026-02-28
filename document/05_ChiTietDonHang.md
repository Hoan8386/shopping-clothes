# Chi Tiết Đơn Hàng Controller

> **Base Path:** `/api/v1/chi-tiet-don-hang`  
> **File:** `ChiTietDonHangController.java`  
> Quản lý chi tiết (dòng sản phẩm) của đơn hàng.

---

## 1. Lấy tất cả chi tiết đơn hàng

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-don-hang` |
| **Xác thực** | Bearer Token (JWT)              |

**Response:** `200 OK` — Trả về `List<ChiTietDonHang>`

---

## 2. Lấy chi tiết đơn hàng theo mã đơn

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}` |
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
| **Xác thực** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                |
| ------- | ---- | -------------------- |
| `id`    | Long | Mã chi tiết đơn hàng |

**Response:** `200 OK` — Trả về `ChiTietDonHang`

**Lỗi:**

- `400` — Không tìm thấy chi tiết đơn hàng

---

## 4. Tạo chi tiết đơn hàng

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-don-hang` |
| **Content-Type** | `application/json`               |
| **Xác thực**     | Bearer Token (JWT)               |

**Request Body:** `ChiTietDonHang`

**Response:** `201 Created` — Trả về `ChiTietDonHang`

---

## 5. Cập nhật chi tiết đơn hàng

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-don-hang` |
| **Content-Type** | `application/json`              |
| **Xác thực**     | Bearer Token (JWT)              |

**Request Body:** `ChiTietDonHang` (phải có `id`)

**Response:** `200 OK` — Trả về `ChiTietDonHang`

**Lỗi:**

- `400` — Mã chi tiết đơn hàng không được để trống

---

## 6. Xóa chi tiết đơn hàng

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-don-hang/{id}` |
| **Xác thực** | Bearer Token (JWT)                      |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy chi tiết đơn hàng
