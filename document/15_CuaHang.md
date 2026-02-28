# Cửa Hàng Controller

> **Base Path:** `/api/v1/cua-hang`  
> **File:** `CuaHangController.java`  
> Quản lý danh sách cửa hàng.

---

## 1. Lấy danh sách cửa hàng

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/cua-hang` |
| **Xác thực** | Bearer Token (JWT)     |

**Response:** `200 OK` — Trả về `List<CuaHang>`

---

## 2. Lấy cửa hàng theo ID

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/cua-hang/{id}` |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã cửa hàng |

**Response:** `200 OK` — Trả về `CuaHang`

**Lỗi:**

- `400` — Không tìm thấy cửa hàng

---

## 3. Tạo cửa hàng

| Thuộc tính       | Chi tiết                |
| ---------------- | ----------------------- |
| **URL**          | `POST /api/v1/cua-hang` |
| **Content-Type** | `application/json`      |
| **Xác thực**     | Bearer Token (JWT)      |

**Request Body:** `CuaHang`

**Response:** `201 Created` — Trả về `CuaHang`

---

## 4. Cập nhật cửa hàng

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/cua-hang` |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:** `CuaHang` (phải có `id`)

**Response:** `200 OK` — Trả về `CuaHang`

**Lỗi:**

- `400` — Mã cửa hàng không được để trống

---

## 5. Xóa cửa hàng

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/cua-hang/{id}` |
| **Xác thực** | Bearer Token (JWT)             |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy cửa hàng
