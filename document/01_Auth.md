# Auth Controller

> **Base Path:** `/api/v1/auth`  
> **File:** `AuthController.java`  
> Quản lý xác thực người dùng: đăng nhập, đăng ký, refresh token, đăng xuất.

---

## 1. Đăng nhập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/auth/login` |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Không yêu cầu             |

**Request Body:**

```json
{
  "username": "string (email)",
  "password": "string"
}
```

**Response:** `200 OK`

```json
{
  "accessToken": "string (JWT)",
  "user": {
    "id": "long",
    "email": "string",
    "name": "string",
    "role": {
      "id": "long",
      "name": "string"
    }
  }
}
```

> **Lưu ý:** Response trả về cookie `refresh_token` (httpOnly, secure).  
> Hệ thống kiểm tra NhanVien trước, nếu không có thì kiểm tra KhachHang.

---

## 2. Lấy thông tin tài khoản hiện tại

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/auth/account` |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK`

```json
{
  "user": {
    "id": "long",
    "email": "string",
    "name": "string",
    "role": {
      "id": "long",
      "name": "string"
    }
  }
}
```

---

## 3. Refresh Token

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/auth/refresh` |
| **Xác thực** | Cookie `refresh_token`     |

**Response:** `200 OK`

```json
{
  "accessToken": "string (JWT mới)",
  "user": {
    "id": "long",
    "email": "string",
    "name": "string",
    "role": { ... }
  }
}
```

> **Lưu ý:** Trả về cookie `refresh_token` mới thay thế cũ.

**Lỗi:**

- `400` — Refresh Token không hợp lệ

---

## 4. Đăng xuất

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `POST /api/v1/auth/logout` |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK` — Body rỗng, xóa cookie `refresh_token`.

**Lỗi:**

- `400` — Access Token không hợp lệ

---

## 5. Đăng ký tài khoản khách hàng

| Thuộc tính       | Chi tiết                     |
| ---------------- | ---------------------------- |
| **URL**          | `POST /api/v1/auth/register` |
| **Content-Type** | `application/json`           |
| **Xác thực**     | Không yêu cầu                |

**Request Body:**

```json
{
  "email": "string",
  "password": "string",
  "tenKhachHang": "string",
  "sdt": "string"
}
```

**Response:** `201 Created`

```json
{
  "id": "long",
  "email": "string",
  "tenKhachHang": "string"
}
```

**Lỗi:**

- `400` — Email đã tồn tại
- `400` — Số điện thoại đã tồn tại
