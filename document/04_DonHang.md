# Đơn Hàng Controller

> **Base Path:** `/api/v1/don-hang`  
> **File:** `DonHangController.java`  
> Quản lý đơn hàng: tạo đơn online, tạo đơn tại quầy, lọc + phân trang.

---

## 1. Lấy danh sách đơn hàng (có lọc + phân trang)

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/don-hang` |
| **Xác thực** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham số              | Kiểu    | Bắt buộc | Mô tả                          |
| -------------------- | ------- | -------- | ------------------------------ |
| `cuaHangId`          | Long    | Không    | Lọc theo cửa hàng              |
| `nhanVienId`         | Long    | Không    | Lọc theo nhân viên             |
| `trangThai`          | Integer | Không    | Lọc theo trạng thái đơn        |
| `trangThaiThanhToan` | Integer | Không    | Lọc theo trạng thái thanh toán |
| `hinhThucDonHang`    | Integer | Không    | Lọc theo hình thức đơn hàng    |
| `page`               | Integer | Không    | Số trang                       |
| `size`               | Integer | Không    | Kích thước trang               |
| `sort`               | String  | Không    | Sắp xếp                        |

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

```json
{
  "meta": {
    "page": 1,
    "pageSize": 10,
    "pages": 5,
    "total": 50
  },
  "result": [ ... ]
}
```

---

## 2. Lấy đơn hàng theo ID

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/don-hang/{id}` |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã đơn hàng |

**Response:** `200 OK` — Trả về `DonHang`

> **Lưu ý:** Chỉ trả về đơn hàng thuộc về người dùng hiện tại.

---

## 3. Tạo đơn hàng online (Khách hàng)

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `POST /api/v1/don-hang/online`  |
| **Content-Type** | `application/json`              |
| **Xác thực**     | Bearer Token (JWT) — Khách hàng |

**Request Body:** `ReqTaoDonHangDTO`

```json
{
  // Thông tin đơn hàng từ giỏ hàng
}
```

**Response:** `201 Created` — Trả về `DonHang`

**Logic xử lý:**

1. Lấy thông tin khách hàng từ JWT token
2. Tạo đơn hàng từ giỏ hàng
3. Xóa giỏ hàng sau khi tạo đơn thành công

---

## 4. Tạo đơn hàng tại quầy (Nhân viên)

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/don-hang/tai-quay` |
| **Content-Type** | `application/json`               |
| **Xác thực**     | Bearer Token (JWT) — Nhân viên   |

**Request Body:** `DonHang`

**Response:** `201 Created` — Trả về `DonHang`

**Logic xử lý:**

1. Lấy thông tin nhân viên từ JWT token
2. Tự gán cửa hàng của nhân viên cho đơn hàng

---

## 5. Cập nhật đơn hàng

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/don-hang` |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:** `DonHang` (phải có `id`)

**Response:** `200 OK` — Trả về `DonHang`

**Lỗi:**

- `400` — Mã đơn hàng không được để trống

---

## 6. Xóa đơn hàng

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/don-hang/{id}` |
| **Xác thực** | Bearer Token (JWT)             |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy đơn hàng
