# Role Controller

> **Base Path:** `/api/v1/roles`  
> **File:** `RoleController.java`  
> Quản lý vai trò (Role) trong hệ thống phân quyền.

---

## Tổng quan

Hệ thống sử dụng **RBAC** (Role-Based Access Control). Mỗi vai trò gắn với các quyền (Permission) qua quan hệ **nhiều-nhiều**.

### Vai trò mặc định

| ID  | Name       | Mô tả              |
| --- | ---------- | ------------------ |
| 1   | ADMIN      | Quản trị hệ thống  |
| 2   | QUAN_LY    | Quản lý cửa hàng   |
| 3   | NHAN_VIEN  | Nhân viên bán hàng |
| 4   | KHACH_HANG | Khách hàng         |

### Cấu trúc dữ liệu `Role`

| Trường        | Kiểu               | Mô tả                            |
| ------------- | ------------------ | -------------------------------- |
| `id`          | Long               | Mã vai trò (auto-increment)      |
| `name`        | String (NotBlank)  | Tên vai trò (unique)             |
| `description` | String             | Mô tả                            |
| `active`      | boolean            | Trạng thái kích hoạt             |
| `permissions` | List\<Permission\> | Danh sách quyền (ManyToMany)     |
| `createdAt`   | Instant            | Ngày tạo (tự động — audit)       |
| `updatedAt`   | Instant            | Ngày cập nhật (tự động — audit)  |
| `createdBy`   | String             | Người tạo (tự động — audit)      |
| `updatedBy`   | String             | Người cập nhật (tự động — audit) |

> **Lưu ý:** Trường `createdBy`/`updatedBy` tự động lấy từ JWT token (SecurityContext) qua `SecurityUtil.getCurrentUserLogin()`.

---

## 1. Lấy danh sách vai trò (phân trang)

| Thuộc tính   | Chi tiết            |
| ------------ | ------------------- |
| **URL**      | `GET /api/v1/roles` |
| **Method**   | `GET`               |
| **Xác thực** | Bearer Token (JWT)  |

**Query Parameters:**

| Tham số | Kiểu    | Mô tả                           |
| ------- | ------- | ------------------------------- |
| `page`  | Integer | Số trang (mặc định: 0)          |
| `size`  | Integer | Kích thước trang (mặc định: 20) |

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

```json
{
  "meta": { "page": 1, "pageSize": 10, "pages": 1, "total": 4 },
  "result": [
    {
      "id": 1,
      "name": "ADMIN",
      "description": "Quản trị hệ thống",
      "active": true,
      "permissions": [
        {
          "id": 1,
          "name": "Xem sản phẩm",
          "apiPath": "/api/v1/san-pham",
          "method": "GET",
          "module": "SAN_PHAM"
        }
      ],
      "createdAt": "2026-01-01T00:00:00Z",
      "createdBy": "admin@gmail.com"
    }
  ]
}
```

---

## 2. Lấy vai trò theo ID

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/roles/{id}` |
| **Method**   | `GET`                    |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK` — Trả về `Role`

**Lỗi:**

| HTTP Status | Mô tả                  |
| ----------- | ---------------------- |
| `400`       | Không tìm thấy vai trò |

---

## 3. Tạo vai trò

| Thuộc tính       | Chi tiết             |
| ---------------- | -------------------- |
| **URL**          | `POST /api/v1/roles` |
| **Method**       | `POST`               |
| **Content-Type** | `application/json`   |
| **Xác thực**     | Bearer Token (JWT)   |

**Request Body:**

```json
{
  "name": "SUPERVISOR",
  "description": "Giám sát viên",
  "active": true,
  "permissions": [{ "id": 1 }, { "id": 2 }, { "id": 3 }]
}
```

| Trường        | Bắt buộc | Mô tả                   |
| ------------- | -------- | ----------------------- |
| `name`        | **Có**   | Tên vai trò (unique)    |
| `description` | Không    | Mô tả                   |
| `active`      | Không    | Trạng thái              |
| `permissions` | Không    | Ds quyền (chỉ cần `id`) |

**Response:** `201 Created` — Trả về `Role`

**Lỗi:**

| HTTP Status | Mô tả                  |
| ----------- | ---------------------- |
| `400`       | Tên vai trò đã tồn tại |

---

## 4. Cập nhật vai trò

| Thuộc tính       | Chi tiết            |
| ---------------- | ------------------- |
| **URL**          | `PUT /api/v1/roles` |
| **Method**       | `PUT`               |
| **Content-Type** | `application/json`  |
| **Xác thực**     | Bearer Token (JWT)  |

**Request Body:** (phải có `id`)

```json
{
  "id": 5,
  "name": "SUPERVISOR",
  "description": "Giám sát viên — cập nhật",
  "active": true,
  "permissions": [{ "id": 1 }, { "id": 2 }]
}
```

**Response:** `200 OK` — Trả về `Role`

**Lỗi:**

| HTTP Status | Mô tả                  |
| ----------- | ---------------------- |
| `400`       | Không tìm thấy vai trò |

---

## 5. Xóa vai trò

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `DELETE /api/v1/roles/{id}` |
| **Method**   | `DELETE`                    |
| **Xác thực** | Bearer Token (JWT)          |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                  |
| ----------- | ---------------------- |
| `400`       | Không tìm thấy vai trò |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ❌  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
