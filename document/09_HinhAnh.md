# Hình Ảnh Controller

> **Base Path:** `/api/v1/hinh-anh`  
> **File:** `HinhAnhController.java`  
> Quản lý hình ảnh sản phẩm, hỗ trợ upload lên MinIO.

---

## 1. Lấy danh sách hình ảnh

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/hinh-anh` |
| **Xác thực** | Bearer Token (JWT)     |

**Response:** `200 OK` — Trả về `List<HinhAnh>`

---

## 2. Lấy hình ảnh theo ID

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/hinh-anh/{id}` |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã hình ảnh |

**Response:** `200 OK` — Trả về `HinhAnh`

**Lỗi:**

- `400` — Không tìm thấy hình ảnh

---

## 3. Lấy hình ảnh theo chi tiết sản phẩm

| Thuộc tính   | Chi tiết                                                    |
| ------------ | ----------------------------------------------------------- |
| **URL**      | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}` |
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

> **Lưu ý:** Ảnh được upload lên MinIO storage và URL được lưu vào database.

**Lỗi:**

- `400` — Không tìm thấy chi tiết sản phẩm

---

## 5. Tạo hình ảnh (JSON)

| Thuộc tính       | Chi tiết                |
| ---------------- | ----------------------- |
| **URL**          | `POST /api/v1/hinh-anh` |
| **Content-Type** | `application/json`      |
| **Xác thực**     | Bearer Token (JWT)      |

**Request Body:** `HinhAnh`

**Response:** `201 Created` — Trả về `HinhAnh`

---

## 6. Cập nhật hình ảnh

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/hinh-anh` |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:** `HinhAnh` (phải có `id`)

**Response:** `200 OK` — Trả về `HinhAnh`

**Lỗi:**

- `400` — Mã hình ảnh không được để trống

---

## 7. Xóa hình ảnh

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/hinh-anh/{id}` |
| **Xác thực** | Bearer Token (JWT)             |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy hình ảnh
