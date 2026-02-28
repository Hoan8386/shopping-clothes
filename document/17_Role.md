# Role Controller

> **Base Path:** `/api/v1/roles`  
> **File:** `RoleController.java`  
> Quản lý vai trò (role) trong hệ thống.

---

## 1. Lấy danh sách vai trò

| Thuộc tính   | Chi tiết            |
| ------------ | ------------------- |
| **URL**      | `GET /api/v1/roles` |
| **Xác thực** | Bearer Token (JWT)  |

**Response:** `200 OK` — Trả về `List<Role>`

---

## 2. Lấy vai trò theo ID

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/roles/{id}` |
| **Xác thực** | Bearer Token (JWT)       |

**Path Parameters:**

| Tham số | Kiểu | Mô tả      |
| ------- | ---- | ---------- |
| `id`    | Long | Mã vai trò |

**Response:** `200 OK` — Trả về `Role`

**Lỗi:**

- `400` — Không tìm thấy vai trò

---

## 3. Tạo vai trò

| Thuộc tính       | Chi tiết             |
| ---------------- | -------------------- |
| **URL**          | `POST /api/v1/roles` |
| **Content-Type** | `application/json`   |
| **Xác thực**     | Bearer Token (JWT)   |

**Request Body:** `Role`

**Response:** `201 Created` — Trả về `Role`

**Lỗi:**

- `400` — Vai trò đã tồn tại (trùng tên)

---

## 4. Cập nhật vai trò

| Thuộc tính       | Chi tiết            |
| ---------------- | ------------------- |
| **URL**          | `PUT /api/v1/roles` |
| **Content-Type** | `application/json`  |
| **Xác thực**     | Bearer Token (JWT)  |

**Request Body:** `Role` (phải có `id`)

**Response:** `200 OK` — Trả về `Role`

**Lỗi:**

- `400` — Mã vai trò không được để trống

---

## 5. Xóa vai trò

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `DELETE /api/v1/roles/{id}` |
| **Xác thực** | Bearer Token (JWT)          |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy vai trò
