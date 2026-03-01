# Hình Ảnh Controller

> **Base Path:** `/api/v1/hinh-anh`  
> **File:** `HinhAnhController.java`  
> Quản lý hình ảnh sản phẩm, hỗ trợ upload lên MinIO.

---

## Tổng quan

### Cấu trúc dữ liệu `HinhAnh`

| Trường           | Kiểu           | Mô tả                           |
| ---------------- | -------------- | ------------------------------- |
| `id`             | Long           | Mã hình ảnh (auto-increment)    |
| `chiTietSanPham` | ChiTietSanPham | Chi tiết sản phẩm liên kết (FK) |
| `tenHinhAnh`     | String(255)    | Tên file ảnh (trên MinIO)       |
| `ngayTao`        | LocalDateTime  | Ngày tạo (tự động)              |
| `ngayCapNhat`    | LocalDateTime  | Ngày cập nhật (tự động)         |

> **Xem ảnh:** Truy cập `GET /storage/{tenHinhAnh}` để lấy file ảnh (không cần xác thực).

---

## 1. Lấy danh sách hình ảnh

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/hinh-anh` |
| **Method**   | `GET`                  |
| **Xác thực** | Bearer Token (JWT)     |

**Response:** `200 OK` — Trả về `List<HinhAnh>`

```json
[
  {
    "id": 1,
    "chiTietSanPham": { "id": 1 },
    "tenHinhAnh": "polo-den-m-1.jpg",
    "ngayTao": "2026-03-01T10:00:00"
  }
]
```

---

## 2. Lấy hình ảnh theo ID

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/hinh-anh/{id}` |
| **Method**   | `GET`                       |
| **Xác thực** | Bearer Token (JWT)          |

**Response:** `200 OK` — Trả về `HinhAnh`

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy hình ảnh |

---

## 3. Lấy hình ảnh theo chi tiết sản phẩm

| Thuộc tính   | Chi tiết                                                    |
| ------------ | ----------------------------------------------------------- |
| **URL**      | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}` |
| **Method**   | `GET`                                                       |
| **Xác thực** | Bearer Token (JWT)                                          |

**Path Parameters:**

| Tham số            | Kiểu | Mô tả                |
| ------------------ | ---- | -------------------- |
| `chiTietSanPhamId` | Long | Mã chi tiết sản phẩm |

**Response:** `200 OK` — Trả về `List<HinhAnh>`

---

## 4. Upload hình ảnh cho chi tiết sản phẩm

| Thuộc tính       | Chi tiết                                          |
| ---------------- | ------------------------------------------------- |
| **URL**          | `POST /api/v1/hinh-anh/upload/{chiTietSanPhamId}` |
| **Method**       | `POST`                                            |
| **Content-Type** | `multipart/form-data`                             |
| **Xác thực**     | Bearer Token (JWT)                                |

**Path Parameters:**

| Tham số            | Kiểu | Mô tả                |
| ------------------ | ---- | -------------------- |
| `chiTietSanPhamId` | Long | Mã chi tiết sản phẩm |

**Form Data:**

| Tham số | Kiểu         | Bắt buộc | Mô tả                         |
| ------- | ------------ | -------- | ----------------------------- |
| `files` | List\<File\> | **Có**   | Danh sách file ảnh cần upload |

**Response:** `201 Created` — Trả về `List<HinhAnh>`

> **Lưu ý:** Ảnh được upload lên MinIO storage. URL truy cập: `GET /storage/{tenHinhAnh}`.

**Lỗi:**

| HTTP Status | Mô tả                            |
| ----------- | -------------------------------- |
| `400`       | Không tìm thấy chi tiết sản phẩm |

---

## 5. Tạo hình ảnh (JSON)

| Thuộc tính       | Chi tiết                |
| ---------------- | ----------------------- |
| **URL**          | `POST /api/v1/hinh-anh` |
| **Method**       | `POST`                  |
| **Content-Type** | `application/json`      |
| **Xác thực**     | Bearer Token (JWT)      |

**Request Body:**

```json
{
  "chiTietSanPham": { "id": 1 },
  "tenHinhAnh": "custom-image.jpg"
}
```

**Response:** `201 Created` — Trả về `HinhAnh`

---

## 6. Cập nhật hình ảnh

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/hinh-anh` |
| **Method**       | `PUT`                  |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "tenHinhAnh": "updated-image.jpg"
}
```

**Response:** `200 OK` — Trả về `HinhAnh`

**Lỗi:**

| HTTP Status | Mô tả                           |
| ----------- | ------------------------------- |
| `400`       | Mã hình ảnh không được để trống |

---

## 7. Xóa hình ảnh

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/hinh-anh/{id}` |
| **Method**   | `DELETE`                       |
| **Xác thực** | Bearer Token (JWT)             |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy hình ảnh |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST/Upload | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ----------- | --------- | ------------ |
| ADMIN      | ✅        | ✅          | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ✅          | ✅        | ❌           |
| KHACH_HANG | ✅        | ❌          | ❌        | ❌           |
