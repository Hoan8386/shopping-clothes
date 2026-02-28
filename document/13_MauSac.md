# Màu Sắc Controller

> **Base Path:** `/api/v1/mau-sac`  
> **File:** `MauSacController.java`  
> Quản lý danh sách màu sắc sản phẩm.

---

## 1. Lấy danh sách màu sắc

| Thuộc tính   | Chi tiết              |
| ------------ | --------------------- |
| **URL**      | `GET /api/v1/mau-sac` |
| **Xác thực** | Bearer Token (JWT)    |

**Response:** `200 OK` — Trả về `List<MauSac>`

---

## 2. Lấy màu sắc theo ID

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/mau-sac/{id}` |
| **Xác thực** | Bearer Token (JWT)         |

**Path Parameters:**

| Tham số | Kiểu | Mô tả      |
| ------- | ---- | ---------- |
| `id`    | Long | Mã màu sắc |

**Response:** `200 OK` — Trả về `MauSac`

**Lỗi:**

- `400` — Không tìm thấy màu sắc

---

## 3. Tạo màu sắc

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `POST /api/v1/mau-sac` |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:** `MauSac`

**Response:** `201 Created` — Trả về `MauSac`

---

## 4. Cập nhật màu sắc

| Thuộc tính       | Chi tiết              |
| ---------------- | --------------------- |
| **URL**          | `PUT /api/v1/mau-sac` |
| **Content-Type** | `application/json`    |
| **Xác thực**     | Bearer Token (JWT)    |

**Request Body:** `MauSac` (phải có `id`)

**Response:** `200 OK` — Trả về `MauSac`

**Lỗi:**

- `400` — Mã màu sắc không được để trống

---

## 5. Xóa màu sắc

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `DELETE /api/v1/mau-sac/{id}` |
| **Xác thực** | Bearer Token (JWT)            |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy màu sắc
