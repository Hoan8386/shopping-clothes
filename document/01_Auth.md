# Auth Controller

> **Base Path:** `/api/v1/auth`  
> **File:** `AuthController.java`  
> Quản lý xác thực người dùng: đăng nhập, đăng ký, refresh token, đăng xuất, xem thông tin tài khoản.

---

## Tổng quan

Hệ thống sử dụng **JWT (JSON Web Token)** để xác thực. Hỗ trợ 2 loại người dùng:

- **NhanVien (Nhân viên):** Được kiểm tra trước khi đăng nhập.
- **KhachHang (Khách hàng):** Được kiểm tra nếu không phải nhân viên.

### Luồng xác thực

1. Gọi `POST /api/v1/auth/login` → Nhận `access_token` + cookie `refresh_token`
2. Đính kèm token vào header: `Authorization: Bearer <access_token>`
3. Khi `access_token` hết hạn → Gọi `GET /api/v1/auth/refresh` (dùng cookie `refresh_token`)
4. Đăng xuất → Gọi `POST /api/v1/auth/logout` → Xóa `refresh_token`

---

## 1. Đăng nhập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/auth/login` |
| **Method**       | `POST`                    |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Không yêu cầu (🔓 Public) |

**Request Body:**

```json
{
  "username": "lan@g.com",
  "password": "123456"
}
```

**Kiểu dữ liệu:**

```json
{
  "username": "String",
  "password": "String"
}
```

| Trường     | Kiểu   | Bắt buộc | Mô tả                 |
| ---------- | ------ | -------- | --------------------- |
| `username` | String | **Có**   | Email đăng nhập       |
| `password` | String | **Có**   | Mật khẩu (plain text) |

**Response:** `200 OK`

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "user": {
    "id": 1,
    "email": "lan@g.com",
    "name": "Lan",
    "role": {
      "id": 4,
      "name": "KHACH_HANG",
      "description": "Khách hàng mua sắm",
      "active": 1,
      "permissions": [...]
    },
    "diemTichLuy": 10
  }
}
```

**Kiểu dữ liệu:**

```json
{
  "access_token": "String",
  "user": {
    "id": "Long",
    "email": "String",
    "name": "String",
    "role": {
      "id": "Long",
      "name": "String",
      "description": "String",
      "active": "Integer",
      "permissions": "List"
    },
    "diemTichLuy": "Integer"
  }
}
```

| Trường             | Kiểu    | Mô tả                                                 |
| ------------------ | ------- | ----------------------------------------------------- |
| `access_token`     | String  | JWT token để xác thực các request tiếp theo           |
| `user.id`          | Long    | Mã người dùng                                         |
| `user.email`       | String  | Email đăng nhập                                       |
| `user.name`        | String  | Tên hiển thị                                          |
| `user.role`        | Object  | Thông tin vai trò + quyền hạn                         |
| `user.diemTichLuy` | Integer | Điểm tích lũy (chỉ có với KhachHang, NhanVien = null) |

> **Lưu ý:**
>
> - Response trả kèm cookie `refresh_token` (httpOnly, secure, path=/).
> - Hệ thống kiểm tra bảng NhanVien trước → nếu không tìm thấy → kiểm tra KhachHang.
> - Trường `diemTichLuy` chỉ trả về giá trị khi đăng nhập bằng tài khoản khách hàng. Nhân viên sẽ nhận `null`.

---

## 2. Lấy thông tin tài khoản hiện tại

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/auth/account` |
| **Method**   | `GET`                      |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK`

```json
{
  "user": {
    "id": 1,
    "email": "lan@g.com",
    "name": "Lan",
    "role": {
      "id": 4,
      "name": "KHACH_HANG",
      "description": "Khách hàng mua sắm",
      "active": 1,
      "permissions": [...]
    },
    "diemTichLuy": 10
  }
}
```

**Kiểu dữ liệu:**

```json
{
  "user": {
    "id": "Long",
    "email": "String",
    "name": "String",
    "role": {
      "id": "Long",
      "name": "String",
      "description": "String",
      "active": "Integer",
      "permissions": "List"
    },
    "diemTichLuy": "Integer"
  }
}
```

> **Lưu ý:** Trường `diemTichLuy` chỉ có giá trị với tài khoản KhachHang, NhanVien trả `null`.

---

## 3. Refresh Token

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/auth/refresh` |
| **Method**   | `GET`                      |
| **Xác thực** | Cookie `refresh_token`     |

**Response:** `200 OK`

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9... (token mới)",
  "user": {
    "id": 1,
    "email": "lan@g.com",
    "name": "Lan",
    "role": {
      "id": 4,
      "name": "KHACH_HANG"
    },
    "diemTichLuy": 10
  }
}
```

**Kiểu dữ liệu:**

```json
{
  "access_token": "String",
  "user": {
    "id": "Long",
    "email": "String",
    "name": "String",
    "role": {
      "id": "Long",
      "name": "String"
    },
    "diemTichLuy": "Integer"
  }
}
```

> **Lưu ý:**
>
> - Sử dụng cookie `refresh_token` được set tự động khi đăng nhập.
> - Trả về `access_token` mới + cookie `refresh_token` mới thay thế cũ.
> - Cả NhanVien và KhachHang đều có thể refresh.

**Lỗi:**

| HTTP Status | Mô tả                      |
| ----------- | -------------------------- |
| `400`       | Refresh Token không hợp lệ |

---

## 4. Đăng xuất

| Thuộc tính   | Chi tiết                   |
| ------------ | -------------------------- |
| **URL**      | `POST /api/v1/auth/logout` |
| **Method**   | `POST`                     |
| **Xác thực** | Bearer Token (JWT)         |

**Response:** `200 OK` — Body rỗng

> **Lưu ý:**
>
> - Xóa `refreshToken` trong database (cho cả NhanVien và KhachHang).
> - Xóa cookie `refresh_token` (set maxAge=0).

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Access Token không hợp lệ |

---

## 5. Đăng ký tài khoản khách hàng

| Thuộc tính       | Chi tiết                     |
| ---------------- | ---------------------------- |
| **URL**          | `POST /api/v1/auth/register` |
| **Method**       | `POST`                       |
| **Content-Type** | `application/json`           |
| **Xác thực**     | Không yêu cầu (🔓 Public)    |

**Request Body:**

```json
{
  "tenKhachHang": "Nguyễn Test",
  "sdt": "0999999999",
  "email": "test@gmail.com",
  "password": "123456"
}
```

**Kiểu dữ liệu:**

```json
{
  "tenKhachHang": "String",
  "sdt": "String",
  "email": "String",
  "password": "String"
}
```

| Trường         | Kiểu   | Bắt buộc | Mô tả                     |
| -------------- | ------ | -------- | ------------------------- |
| `tenKhachHang` | String | **Có**   | Tên khách hàng            |
| `sdt`          | String | **Có**   | Số điện thoại (unique)    |
| `email`        | String | **Có**   | Email đăng nhập (unique)  |
| `password`     | String | **Có**   | Mật khẩu (sẽ được mã hóa) |

**Response:** `201 Created`

```json
{
  "id": 6,
  "tenKhachHang": "Nguyễn Test",
  "email": "test@gmail.com",
  "sdt": "0999999999"
}
```

**Kiểu dữ liệu:**

```json
{
  "id": "Long",
  "tenKhachHang": "String",
  "email": "String",
  "sdt": "String"
}
```

> **Lưu ý:**
>
> - Tài khoản khách hàng mới tự động được gán `role_id = 4` (KHACH_HANG).
> - Password được mã hóa bằng BCrypt trước khi lưu.
> - `diemTichLuy` mặc định = 0.

**Lỗi:**

| HTTP Status | Mô tả                    |
| ----------- | ------------------------ |
| `400`       | Email đã tồn tại         |
| `400`       | Số điện thoại đã tồn tại |

---

## Tài khoản mẫu

| Loại       | Email       | Password | Vai trò    | Mô tả                 |
| ---------- | ----------- | -------- | ---------- | --------------------- |
| Nhân viên  | `h@s.com`   | `123456` | ADMIN      | NV Hùng - Admin       |
| Nhân viên  | `an@s.com`  | `123456` | QUAN_LY    | NV An - Quản lý       |
| Nhân viên  | `b@s.com`   | `123456` | NHAN_VIEN  | NV Bình - NV bán hàng |
| Khách hàng | `lan@g.com` | `123456` | KHACH_HANG | KH Lan (10 điểm)      |
| Khách hàng | `m@g.com`   | `123456` | KHACH_HANG | KH Minh (0 điểm)      |
| Khách hàng | `h@g.com`   | `123456` | KHACH_HANG | KH Hoa (100 điểm)     |
