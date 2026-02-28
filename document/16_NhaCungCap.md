# Nhà Cung Cấp Controller

> **Base Path:** `/api/v1/nha-cung-cap`  
> **File:** `NhaCungCapController.java`  
> Quản lý danh sách nhà cung cấp.

---

## 1. Lấy danh sách nhà cung cấp

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/nha-cung-cap` |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK` — Trả về `List<NhaCungCap>`

---

## 2. Lấy nhà cung cấp theo ID

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/nha-cung-cap/{id}` |
| **Xác thực** | Bearer Token (JWT)              |

**Path Parameters:**

| Tham số | Kiểu | Mô tả           |
| ------- | ---- | --------------- |
| `id`    | Long | Mã nhà cung cấp |

**Response:** `200 OK` — Trả về `NhaCungCap`

**Lỗi:**

- `400` — Không tìm thấy nhà cung cấp

---

## 3. Tạo nhà cung cấp

| Thuộc tính       | Chi tiết                    |
| ---------------- | --------------------------- |
| **URL**          | `POST /api/v1/nha-cung-cap` |
| **Content-Type** | `application/json`          |
| **Xác thực**     | Bearer Token (JWT)          |

**Request Body:** `NhaCungCap`

**Response:** `201 Created` — Trả về `NhaCungCap`

---

## 4. Cập nhật nhà cung cấp

| Thuộc tính       | Chi tiết                   |
| ---------------- | -------------------------- |
| **URL**          | `PUT /api/v1/nha-cung-cap` |
| **Content-Type** | `application/json`         |
| **Xác thực**     | Bearer Token (JWT)         |

**Request Body:** `NhaCungCap` (phải có `id`)

**Response:** `200 OK` — Trả về `NhaCungCap`

**Lỗi:**

- `400` — Mã nhà cung cấp không được để trống

---

## 5. Xóa nhà cung cấp

| Thuộc tính   | Chi tiết                           |
| ------------ | ---------------------------------- |
| **URL**      | `DELETE /api/v1/nha-cung-cap/{id}` |
| **Xác thực** | Bearer Token (JWT)                 |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy nhà cung cấp
