# Thương Hiệu Controller

> **Base Path:** `/api/v1/thuong-hieu`  
> **File:** `ThuongHieuController.java`  
> Quản lý thương hiệu sản phẩm.

---

## 1. Lấy danh sách thương hiệu

| Thuộc tính   | Chi tiết                  |
| ------------ | ------------------------- |
| **URL**      | `GET /api/v1/thuong-hieu` |
| **Xác thực** | Bearer Token (JWT)        |

**Response:** `200 OK` — Trả về `List<ThuongHieu>`

---

## 2. Lấy thương hiệu theo ID

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `GET /api/v1/thuong-hieu/{id}` |
| **Xác thực** | Bearer Token (JWT)             |

**Path Parameters:**

| Tham số | Kiểu | Mô tả          |
| ------- | ---- | -------------- |
| `id`    | Long | Mã thương hiệu |

**Response:** `200 OK` — Trả về `ThuongHieu`

**Lỗi:**

- `400` — Không tìm thấy thương hiệu

---

## 3. Tạo thương hiệu

| Thuộc tính       | Chi tiết                   |
| ---------------- | -------------------------- |
| **URL**          | `POST /api/v1/thuong-hieu` |
| **Content-Type** | `application/json`         |
| **Xác thực**     | Bearer Token (JWT)         |

**Request Body:** `ThuongHieu`

**Response:** `201 Created` — Trả về `ThuongHieu`

---

## 4. Cập nhật thương hiệu

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `PUT /api/v1/thuong-hieu` |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** `ThuongHieu` (phải có `id`)

**Response:** `200 OK` — Trả về `ThuongHieu`

**Lỗi:**

- `400` — Mã thương hiệu không được để trống

---

## 5. Xóa thương hiệu

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `DELETE /api/v1/thuong-hieu/{id}` |
| **Xác thực** | Bearer Token (JWT)                |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy thương hiệu
