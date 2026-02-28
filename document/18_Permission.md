# Permission Controller

> **Base Path:** `/api/v1/permissions`  
> **File:** `PermissionController.java`  
> Quản lý quyền hạn (permission) trong hệ thống.

---

## 1. Lấy danh sách quyền

| Thuộc tính   | Chi tiết                  |
| ------------ | ------------------------- |
| **URL**      | `GET /api/v1/permissions` |
| **Xác thực** | Bearer Token (JWT)        |

**Response:** `200 OK` — Trả về `List<Permission>`

---

## 2. Lấy quyền theo ID

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `GET /api/v1/permissions/{id}` |
| **Xác thực** | Bearer Token (JWT)             |

**Path Parameters:**

| Tham số | Kiểu | Mô tả    |
| ------- | ---- | -------- |
| `id`    | Long | Mã quyền |

**Response:** `200 OK` — Trả về `Permission`

**Lỗi:**

- `400` — Không tìm thấy quyền

---

## 3. Tạo quyền

| Thuộc tính       | Chi tiết                   |
| ---------------- | -------------------------- |
| **URL**          | `POST /api/v1/permissions` |
| **Content-Type** | `application/json`         |
| **Xác thực**     | Bearer Token (JWT)         |

**Request Body:** `Permission`

**Response:** `201 Created` — Trả về `Permission`

**Lỗi:**

- `400` — Quyền với module/apiPath/method này đã tồn tại

---

## 4. Cập nhật quyền

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `PUT /api/v1/permissions` |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** `Permission` (phải có `id`)

**Response:** `200 OK` — Trả về `Permission`

**Lỗi:**

- `400` — Mã quyền không được để trống

---

## 5. Xóa quyền

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `DELETE /api/v1/permissions/{id}` |
| **Xác thực** | Bearer Token (JWT)                |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy quyền
