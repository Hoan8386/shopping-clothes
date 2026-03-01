# Đánh Giá Sản Phẩm Controller

> **Base Path:** `/api/v1/danh-gia-san-pham`  
> **File:** `DanhGiaSanPhamController.java`  
> Quản lý đánh giá sản phẩm — Khách hàng đánh giá sau khi đơn hàng giao thành công, hỗ trợ upload ảnh lên MinIO.

---

## Tổng quan

### Cấu trúc dữ liệu `DanhGiaSanPham`

| Trường        | Kiểu          | Mô tả                               |
| ------------- | ------------- | ----------------------------------- |
| `id`          | Long          | Mã đánh giá (auto-increment)        |
| `khachHang`   | KhachHang     | Khách hàng đánh giá (FK)            |
| `sanPham`     | SanPham       | Sản phẩm được đánh giá (FK)         |
| `donHang`     | DonHang       | Đơn hàng liên quan (FK)             |
| `soSao`       | Integer       | Số sao đánh giá (1-5)               |
| `ghiChu`      | String(255)   | Nội dung đánh giá                   |
| `hinhAnh`     | String(255)   | URL ảnh đánh giá (upload lên MinIO) |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)                  |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)             |

### Điều kiện đánh giá

| Điều kiện                                     | Mô tả                                             |
| --------------------------------------------- | ------------------------------------------------- |
| ✅ Đã đăng nhập bằng tài khoản **khách hàng** | Lấy thông tin KH từ JWT token                     |
| ✅ Đơn hàng thuộc về khách hàng               | KH chỉ đánh giá được đơn hàng của mình            |
| ✅ Đơn hàng **trạng thái = 3** (Thành công)   | Chỉ đánh giá khi đơn hàng đã giao thành công      |
| ✅ Sản phẩm có trong đơn hàng                 | SP phải nằm trong chi tiết đơn hàng               |
| ✅ Chưa đánh giá sản phẩm này trong đơn này   | Mỗi SP chỉ được đánh giá 1 lần trong mỗi đơn hàng |

### Trạng thái đơn hàng (tham khảo)

| Giá trị | Ý nghĩa           |
| ------- | ----------------- |
| `0`     | Chờ xác nhận      |
| `1`     | Đang xử lý        |
| `2`     | Đang giao         |
| `3`     | **Thành công** ✅ |

> **Xem ảnh đánh giá:** Truy cập `GET /storage/{fileName}` để lấy file ảnh (không cần xác thực).

---

## 1. Xem tất cả đánh giá

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham` |
| **Method**   | `GET`                           |
| **Xác thực** | Bearer Token (JWT)              |

**Response:** `200 OK` — Trả về `List<ResDanhGiaSanPhamDTO>`

```json
[
  {
    "id": 1,
    "khachHangId": 3,
    "tenKhachHang": "Hoa",
    "sanPhamId": 3,
    "tenSanPham": "Váy Hoa",
    "donHangId": 3,
    "soSao": 5,
    "ghiChu": "Váy rất đẹp, đúng size, vải mát",
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

**Response:** `200 OK` — Trả về `ResDanhGiaSanPhamDTO`

```json
{
  "id": 1,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "sanPhamId": 3,
  "tenSanPham": "Váy Hoa",
  "donHangId": 3,
  "soSao": 5,
  "ghiChu": "Váy rất đẹp, đúng size, vải mát",
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

## 3. Xem đánh giá theo sản phẩm (có phân trang)

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}` |
| **Method**   | `GET`                                                |
| **Xác thực** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham số     | Kiểu | Mô tả       |
| ----------- | ---- | ----------- |
| `sanPhamId` | Long | Mã sản phẩm |

**Query Parameters:**

| Tham số | Kiểu    | Bắt buộc | Mô tả                           |
| ------- | ------- | -------- | ------------------------------- |
| `page`  | Integer | Không    | Số trang (mặc định: 1)          |
| `size`  | Integer | Không    | Kích thước trang (mặc định: 20) |

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

```json
{
  "meta": {
    "page": 1,
    "pageSize": 10,
    "pages": 1,
    "total": 1
  },
  "result": [
    {
      "id": 1,
      "khachHangId": 3,
      "tenKhachHang": "Hoa",
      "sanPhamId": 3,
      "tenSanPham": "Váy Hoa",
      "donHangId": 3,
      "soSao": 5,
      "ghiChu": "Váy rất đẹp, đúng size, vải mát",
      "hinhAnh": null,
      "ngayTao": "2026-03-01T10:00:00"
    }
  ]
}
```

---

## 4. Xem đánh giá của tôi

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/cua-toi` |
| **Method**   | `GET`                                   |
| **Xác thực** | Bearer Token (JWT) — Khách hàng         |

**Response:** `200 OK` — Trả về `List<ResDanhGiaSanPhamDTO>` (chỉ đánh giá của KH đang đăng nhập)

```json
[
  {
    "id": 2,
    "khachHangId": 5,
    "tenKhachHang": "Yến",
    "sanPhamId": 1,
    "tenSanPham": "Áo Oxford",
    "donHangId": 5,
    "soSao": 4,
    "ghiChu": "Áo Oxford chất lượng tốt",
    "hinhAnh": null,
    "ngayTao": "2026-03-01T10:00:00"
  }
]
```

---

## 5. Tạo đánh giá sản phẩm

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/danh-gia-san-pham` |
| **Method**       | `POST`                           |
| **Content-Type** | `multipart/form-data`            |
| **Xác thực**     | Bearer Token (JWT) — Khách hàng  |

**Form Data:**

| Tham số     | Kiểu    | Bắt buộc | Mô tả                           |
| ----------- | ------- | -------- | ------------------------------- |
| `sanPhamId` | Long    | **Có**   | Mã sản phẩm cần đánh giá        |
| `donHangId` | Long    | **Có**   | Mã đơn hàng (phải thành công)   |
| `soSao`     | Integer | **Có**   | Số sao (1-5)                    |
| `ghiChu`    | String  | Không    | Nội dung đánh giá               |
| `file`      | File    | Không    | Ảnh đánh giá (upload lên MinIO) |

**Response:** `201 Created` — Trả về `ResDanhGiaSanPhamDTO`

```json
{
  "id": 4,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "sanPhamId": 3,
  "tenSanPham": "Váy Hoa",
  "donHangId": 3,
  "soSao": 5,
  "ghiChu": "Sản phẩm rất đẹp, chất lượng tốt!",
  "hinhAnh": "/storage/abc123-uuid.jpg",
  "ngayTao": "2026-03-01T15:30:00"
}
```

**Lỗi:**

| HTTP Status | Mô tả                                             |
| ----------- | ------------------------------------------------- |
| `400`       | Không tìm thấy sản phẩm                           |
| `400`       | Không tìm thấy đơn hàng                           |
| `400`       | Đơn hàng không thuộc về bạn                       |
| `400`       | Chỉ được đánh giá khi đơn hàng đã giao thành công |
| `400`       | Sản phẩm này không có trong đơn hàng              |
| `400`       | Bạn đã đánh giá sản phẩm này trong đơn hàng này   |
| `400`       | Số sao phải từ 1 đến 5                            |

> **Lưu ý:** Ảnh đánh giá được upload lên MinIO. URL ảnh có thể truy cập qua `GET /storage/{fileName}`.

---

## 6. Cập nhật đánh giá

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/danh-gia-san-pham` |
| **Method**       | `PUT`                           |
| **Content-Type** | `multipart/form-data`           |
| **Xác thực**     | Bearer Token (JWT) — Khách hàng |

**Form Data:**

| Tham số  | Kiểu    | Bắt buộc | Mô tả                      |
| -------- | ------- | -------- | -------------------------- |
| `id`     | Long    | **Có**   | Mã đánh giá cần cập nhật   |
| `soSao`  | Integer | Không    | Số sao mới (1-5)           |
| `ghiChu` | String  | Không    | Nội dung đánh giá mới      |
| `file`   | File    | Không    | Ảnh mới (upload lên MinIO) |

**Response:** `200 OK` — Trả về `ResDanhGiaSanPhamDTO`

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy đánh giá             |
| `400`       | Bạn không có quyền sửa đánh giá này |
| `400`       | Số sao phải từ 1 đến 5              |

> **Lưu ý:** Chỉ chủ đánh giá (khách hàng đã tạo) mới được cập nhật.

---

## 7. Xóa đánh giá

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

| Vai trò    | GET (Xem tất cả) | GET (Xem theo SP) | GET (Của tôi) | POST (Tạo) | PUT (Sửa)     | DELETE (Xóa)  |
| ---------- | ---------------- | ----------------- | ------------- | ---------- | ------------- | ------------- |
| ADMIN      | ✅               | ✅                | ❌            | ❌         | ❌            | ✅ (tất cả)   |
| NHAN_VIEN  | ✅               | ✅                | ❌            | ❌         | ❌            | ❌            |
| KHACH_HANG | ✅               | ✅                | ✅            | ✅         | ✅ (của mình) | ✅ (của mình) |

---

## Flow đánh giá mẫu

```
1. KH Hoa đăng nhập (h@g.com / 123456)
2. Xem đơn hàng #3 → Trạng thái = 3 (Thành công) ✅
3. Đơn #3 có sản phẩm: Váy Hoa (id=3)
4. POST /api/v1/danh-gia-san-pham
   - sanPhamId: 3
   - donHangId: 3
   - soSao: 5
   - ghiChu: "Váy rất đẹp!"
   - file: (ảnh tùy chọn)
5. → Đánh giá thành công!
```
