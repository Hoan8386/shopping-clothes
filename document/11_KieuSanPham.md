# Kiểu Sản Phẩm Controller

> **Base Path:** `/api/v1/kieu-san-pham`  
> **File:** `KieuSanPhamController.java`  
> Quản lý kiểu/loại sản phẩm.

---

## 1. Lấy danh sách kiểu sản phẩm

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/kieu-san-pham` |
| **Xác thực** | Bearer Token (JWT)          |

**Response:** `200 OK` — Trả về `List<KieuSanPham>`

---

## 2. Lấy kiểu sản phẩm theo ID

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `GET /api/v1/kieu-san-pham/{id}` |
| **Xác thực** | Bearer Token (JWT)               |

**Path Parameters:**

| Tham số | Kiểu | Mô tả            |
| ------- | ---- | ---------------- |
| `id`    | Long | Mã kiểu sản phẩm |

**Response:** `200 OK` — Trả về `KieuSanPham`

**Lỗi:**

- `400` — Không tìm thấy kiểu sản phẩm

---

## 3. Tạo kiểu sản phẩm

| Thuộc tính       | Chi tiết                     |
| ---------------- | ---------------------------- |
| **URL**          | `POST /api/v1/kieu-san-pham` |
| **Content-Type** | `application/json`           |
| **Xác thực**     | Bearer Token (JWT)           |

**Request Body:** `KieuSanPham`

**Response:** `201 Created` — Trả về `KieuSanPham`

---

## 4. Cập nhật kiểu sản phẩm

| Thuộc tính       | Chi tiết                    |
| ---------------- | --------------------------- |
| **URL**          | `PUT /api/v1/kieu-san-pham` |
| **Content-Type** | `application/json`          |
| **Xác thực**     | Bearer Token (JWT)          |

**Request Body:** `KieuSanPham` (phải có `id`)

**Response:** `200 OK` — Trả về `KieuSanPham`

**Lỗi:**

- `400` — Mã kiểu sản phẩm không được để trống

---

## 5. Xóa kiểu sản phẩm

| Thuộc tính   | Chi tiết                            |
| ------------ | ----------------------------------- |
| **URL**      | `DELETE /api/v1/kieu-san-pham/{id}` |
| **Xác thực** | Bearer Token (JWT)                  |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy kiểu sản phẩm
