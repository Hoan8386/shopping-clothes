# Đánh Giá Sản Phẩm Controller

> **Base Path:** `/api/v1/danh-gia-san-pham`  
> **File:** `DanhGiaSanPhamController.java`  
> Quản lý đánh giá sản phẩm — Khách hàng đánh giá từng sản phẩm trong đơn hàng sau khi đã nhận hàng, hỗ trợ upload ảnh lên MinIO.

---

## Tổng quan

### Cấu trúc dữ liệu `DanhGiaSanPham`

| Trường           | Kiểu           | Mô tả                                                      |
| ---------------- | -------------- | ---------------------------------------------------------- |
| `id`             | Long           | Mã đánh giá (auto-increment)                               |
| `khachHang`      | KhachHang      | Khách hàng đánh giá (FK)                                   |
| `chiTietDonHang` | ChiTietDonHang | Chi tiết đơn hàng được đánh giá (FK) — 1 đánh giá / 1 dòng |
| `soSao`          | Integer        | Số sao đánh giá (1–5)                                      |
| `ghiTru`         | String(255)    | Nội dung đánh giá                                          |
| `hinhAnh`        | String(255)    | URL ảnh đánh giá (upload lên MinIO)                        |
| `ngayTao`        | LocalDateTime  | Ngày tạo (tự động)                                         |
| `ngayCapNhat`    | LocalDateTime  | Ngày cập nhật (tự động)                                    |
| `json`           | String(TEXT)   | Dữ liệu mở rộng (tuỳ chọn)                                 |

### Điều kiện đánh giá

| Điều kiện                                                | Mô tả                                                       |
| -------------------------------------------------------- | ----------------------------------------------------------- |
| ✅ Đã đăng nhập bằng tài khoản **khách hàng**            | Lấy thông tin KH từ JWT token                               |
| ✅ `ChiTietDonHang` thuộc **đơn hàng của khách hàng đó** | KH chỉ đánh giá được đơn hàng của mình                      |
| ✅ Đơn hàng **trạng thái = 5** (Đã nhận hàng)            | Phải nhận hàng xong mới được đánh giá                       |
| ✅ Chưa đánh giá `ChiTietDonHang` này                    | Mỗi dòng sản phẩm trong đơn chỉ đánh giá **1 lần duy nhất** |

> Sau khi đã tạo đánh giá, khách hàng **chỉ có thể cập nhật hoặc xóa**, không thể tạo thêm đánh giá cho cùng 1 dòng sản phẩm.

### Trạng thái đơn hàng (tham khảo)

| Giá trị | Ý nghĩa         |
| ------- | --------------- |
| `0`     | Chờ xác nhận    |
| `1`     | Đã xác nhận     |
| `2`     | Đang đóng gói   |
| `3`     | Đang giao hàng  |
| `4`     | Đã hủy          |
| `5`     | Đã nhận hàng ✅ |

---

## 1. Xem tất cả đánh giá

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham` |
| **Method**   | `GET`                           |
| **Xác thực** | Bearer Token (JWT)              |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "khachHangId": 3,
    "tenKhachHang": "Hoa",
    "chiTietDonHangId": 7,
    "donHangId": 3,
    "sanPhamId": 3,
    "tenSanPham": "Váy Hoa",
    "soSao": 5,
    "ghiTru": "Váy rất đẹp, đúng size, vải mát",
    "hinhAnh": null,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

---

## 2. Xem đánh giá theo ID

| Thuộc tính   | Chi tiết                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/danh-gia-san-pham/{id}` |
| **Method**   | `GET`                                |
| **Xác thực** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã đánh giá |

**Response:** `200 OK`

```json
{
  "id": 1,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "chiTietDonHangId": 7,
  "donHangId": 3,
  "sanPhamId": 3,
  "tenSanPham": "Váy Hoa",
  "soSao": 5,
  "ghiTru": "Váy rất đẹp, đúng size, vải mát",
  "hinhAnh": null,
  "ngayTao": "2026-03-01T10:00:00",
  "ngayCapNhat": null
}
```

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy đánh giá |

---

## 3. Xem đánh giá theo sản phẩm

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}` |
| **Method**   | `GET`                                                |
| **Xác thực** | Bearer Token (JWT)                                   |

> Truy vấn tất cả đánh giá của một sản phẩm thông qua đường dẫn: `DanhGia → ChiTietDonHang → ChiTietSanPham → SanPham`.  
> Dùng khi hiển thị danh sách đánh giá trên trang chi tiết sản phẩm.

**Path Parameters:**

| Tham số     | Kiểu | Mô tả       |
| ----------- | ---- | ----------- |
| `sanPhamId` | Long | Mã sản phẩm |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "khachHangId": 3,
    "tenKhachHang": "Hoa",
    "chiTietDonHangId": 7,
    "donHangId": 3,
    "sanPhamId": 3,
    "tenSanPham": "Váy Hoa",
    "soSao": 5,
    "ghiTru": "Váy rất đẹp, đúng size, vải mát",
    "hinhAnh": null,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

---

## 4. Xem đánh giá theo chi tiết đơn hàng

| Thuộc tính   | Chi tiết                                                             |
| ------------ | -------------------------------------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` |
| **Method**   | `GET`                                                                |
| **Xác thực** | Bearer Token (JWT)                                                   |

**Path Parameters:**

| Tham số            | Kiểu | Mô tả                |
| ------------------ | ---- | -------------------- |
| `chiTietDonHangId` | Long | Mã chi tiết đơn hàng |

**Response:** `200 OK` — Tương tự mục 3.

---

## 5. Xem đánh giá của tôi

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/cua-toi` |
| **Method**   | `GET`                                   |
| **Xác thực** | Bearer Token (JWT) — Khách hàng         |

Trả về tất cả đánh giá của khách hàng đang đăng nhập.

**Response:** `200 OK` — Tương tự mục 3.

---

## 6. Tạo đánh giá sản phẩm

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/danh-gia-san-pham` |
| **Method**       | `POST`                           |
| **Content-Type** | `multipart/form-data`            |
| **Xác thực**     | Bearer Token (JWT) — Khách hàng  |

**Form Data:**

| Tham số            | Kiểu    | Bắt buộc | Mô tả                                |
| ------------------ | ------- | -------- | ------------------------------------ |
| `chiTietDonHangId` | Long    | **Có**   | Mã chi tiết đơn hàng (dòng sản phẩm) |
| `soSao`            | Integer | **Có**   | Số sao (1–5)                         |
| `ghiTru`           | String  | Không    | Nội dung đánh giá                    |
| `file`             | File    | Không    | Ảnh đánh giá (upload lên MinIO)      |

**Response:** `201 Created`

```json
{
  "id": 4,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "chiTietDonHangId": 7,
  "donHangId": 3,
  "sanPhamId": 3,
  "tenSanPham": "Váy Hoa",
  "soSao": 5,
  "ghiTru": "Sản phẩm rất đẹp, chất lượng tốt!",
  "hinhAnh": "/storage/abc123-uuid.jpg",
  "ngayTao": "2026-03-01T15:30:00",
  "ngayCapNhat": null
}
```

**Lỗi:**

| HTTP Status | Mô tả                                                       |
| ----------- | ----------------------------------------------------------- |
| `400`       | Không tìm thấy chi tiết đơn hàng                            |
| `400`       | Đơn hàng không thuộc về bạn                                 |
| `400`       | Đơn hàng phải ở trạng thái đã nhận hàng mới có thể đánh giá |
| `400`       | Bạn đã đánh giá sản phẩm này rồi                            |
| `400`       | Số sao phải từ 1 đến 5                                      |

---

## 7. Cập nhật đánh giá

| Thuộc tính       | Chi tiết                             |
| ---------------- | ------------------------------------ |
| **URL**          | `PUT /api/v1/danh-gia-san-pham/{id}` |
| **Method**       | `PUT`                                |
| **Content-Type** | `multipart/form-data`                |
| **Xác thực**     | Bearer Token (JWT) — Khách hàng      |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã đánh giá |

**Form Data:**

| Tham số  | Kiểu    | Bắt buộc | Mô tả                      |
| -------- | ------- | -------- | -------------------------- |
| `soSao`  | Integer | Không    | Số sao mới (1–5)           |
| `ghiTru` | String  | Không    | Nội dung đánh giá mới      |
| `file`   | File    | Không    | Ảnh mới (upload lên MinIO) |

**Response:** `200 OK` — Trả về `ResDanhGiaSanPhamDTO` đã cập nhật.

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy đánh giá             |
| `400`       | Bạn không có quyền sửa đánh giá này |
| `400`       | Số sao phải từ 1 đến 5              |

---

## 8. Xóa đánh giá

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/danh-gia-san-pham/{id}` |
| **Method**   | `DELETE`                                |
| **Xác thực** | Bearer Token (JWT)                      |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã đánh giá |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy đánh giá             |
| `400`       | Bạn không có quyền xóa đánh giá này |

> **Lưu ý:** Khách hàng chỉ xóa được đánh giá của mình. Admin/Nhân viên có thể xóa bất kỳ đánh giá nào.

---

## Phân quyền

| Vai trò    | GET (Tất cả) | GET (Theo SP) | GET (Theo CTDH) | GET (Của tôi) | POST (Tạo) | PUT (Sửa)     | DELETE (Xóa)  |
| ---------- | ------------ | ------------- | --------------- | ------------- | ---------- | ------------- | ------------- |
| ADMIN      | ✅           | ✅            | ✅              | ❌            | ❌         | ❌            | ✅ (tất cả)   |
| NHAN_VIEN  | ✅           | ✅            | ✅              | ❌            | ❌         | ❌            | ❌            |
| KHACH_HANG | ✅           | ✅            | ✅              | ✅            | ✅         | ✅ (của mình) | ✅ (của mình) |

---

## Ví dụ test thực tế (Postman / curl)

### Bước 1 — Đăng nhập lấy token

```
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "h@g.com",
  "password": "123456"
}
```

> Lưu lại `access_token` từ response để dùng ở các bước sau.

---

### Bước 2 — Xem đơn hàng đã nhận (trangThai = 5)

```
GET /api/v1/don-hang/cua-toi
Authorization: Bearer <access_token>
```

> Tìm đơn hàng có `trangThai = 5`. Ghi lại `id` của đơn hàng đó.

---

### Bước 3 — Xem chi tiết đơn hàng để lấy `chiTietDonHangId`

```
GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}
Authorization: Bearer <access_token>
```

> Mỗi dòng trong `result` là 1 sản phẩm trong đơn hàng.  
> Ghi lại `id` của dòng sản phẩm muốn đánh giá — đây là `chiTietDonHangId`.

---

### Bước 4 — Tạo đánh giá

```
POST /api/v1/danh-gia-san-pham
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

chiTietDonHangId = 7
soSao            = 5
ghiTru           = Sản phẩm rất đẹp, đúng size!
file             = (để trống hoặc chọn ảnh)
```

**Response `201 Created`:**

```json
{
  "id": 4,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "chiTietDonHangId": 7,
  "donHangId": 3,
  "sanPhamId": 3,
  "tenSanPham": "Váy Hoa",
  "soSao": 5,
  "ghiTru": "Sản phẩm rất đẹp, đúng size!",
  "hinhAnh": null,
  "ngayTao": "2026-03-06T10:00:00",
  "ngayCapNhat": null
}
```

---

### Bước 5 — Xem đánh giá theo sản phẩm (không cần token nếu public)

```
GET /api/v1/danh-gia-san-pham/san-pham/3
Authorization: Bearer <access_token>
```

> Trả về tất cả đánh giá của sản phẩm `id = 3` từ tất cả khách hàng.

---

### Bước 6 — Cập nhật đánh giá

```
PUT /api/v1/danh-gia-san-pham/4
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

soSao  = 4
ghiTru = Đẹp nhưng giao hơi chậm
```

---

### Bước 7 — Xóa đánh giá

```
DELETE /api/v1/danh-gia-san-pham/4
Authorization: Bearer <access_token>
```

**Response:** `204 No Content`

---

### Lưu ý khi test

| Trường hợp                                       | Kết quả mong đợi                                  |
| ------------------------------------------------ | ------------------------------------------------- |
| Tạo đánh giá lần 2 cho cùng `chiTietDonHangId`   | `400` — "Bạn đã đánh giá sản phẩm này rồi"        |
| Đơn hàng `trangThai != 5`                        | `400` — "Đơn hàng phải ở trạng thái đã nhận hàng" |
| `chiTietDonHangId` không thuộc đơn hàng của mình | `400` — "Đơn hàng không thuộc về bạn"             |
| `soSao = 0` hoặc `soSao = 6`                     | `400` — "Số sao phải từ 1 đến 5"                  |
| Sửa/xóa đánh giá của người khác                  | `400` — "Bạn không có quyền..."                   |
