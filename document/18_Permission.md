# Permission Controller

> **Base Path:** `/api/v1/permissions`  
> **File:** `PermissionController.java`  
> Quản lý quyền hạn (Permission) trong hệ thống.

---

## Tổng quan

Mỗi **Permission** đại diện cho 1 quyền truy cập vào 1 API endpoint cụ thể. Quyền được gán cho **Role** qua quan hệ nhiều-nhiều.

### Cấu trúc dữ liệu `Permission`

| Trường      | Kiểu              | Mô tả                                  |
| ----------- | ----------------- | -------------------------------------- |
| `id`        | Long              | Mã quyền (auto-increment)              |
| `name`      | String (NotBlank) | Tên quyền (vd: "Xem sản phẩm")         |
| `apiPath`   | String (NotBlank) | Đường dẫn API (vd: `/api/v1/san-pham`) |
| `method`    | String (NotBlank) | HTTP method (GET/POST/PUT/DELETE)      |
| `module`    | String (NotBlank) | Module (vd: SAN_PHAM, DON_HANG, ...)   |
| `createdAt` | Instant           | Ngày tạo (tự động — audit)             |
| `updatedAt` | Instant           | Ngày cập nhật (tự động — audit)        |
| `createdBy` | String            | Người tạo (tự động — audit)            |
| `updatedBy` | String            | Người cập nhật (tự động — audit)       |

### Danh sách module

| Module                | Mô tả                   |
| --------------------- | ----------------------- |
| `SAN_PHAM`            | Sản phẩm                |
| `CHI_TIET_SAN_PHAM`   | Chi tiết sản phẩm       |
| `DON_HANG`            | Đơn hàng                |
| `CHI_TIET_DON_HANG`   | Chi tiết đơn hàng       |
| `GIO_HANG`            | Giỏ hàng                |
| `PHIEU_NHAP`          | Phiếu nhập              |
| `CHI_TIET_PHIEU_NHAP` | Chi tiết phiếu nhập     |
| `HINH_ANH`            | Hình ảnh                |
| `BO_SUU_TAP`          | Bộ sưu tập              |
| `KIEU_SAN_PHAM`       | Kiểu sản phẩm           |
| `THUONG_HIEU`         | Thương hiệu             |
| `MAU_SAC`             | Màu sắc                 |
| `KICH_THUOC`          | Kích thước              |
| `CUA_HANG`            | Cửa hàng                |
| `NHA_CUNG_CAP`        | Nhà cung cấp            |
| `ROLE`                | Vai trò                 |
| `PERMISSION`          | Quyền hạn               |
| `KHUYEN_MAI_HOA_DON`  | Khuyến mãi theo hóa đơn |
| `KHUYEN_MAI_DIEM`     | Khuyến mãi theo điểm    |

---

## 1. Lấy danh sách quyền (phân trang)

| Thuộc tính   | Chi tiết                  |
| ------------ | ------------------------- |
| **URL**      | `GET /api/v1/permissions` |
| **Method**   | `GET`                     |
| **Xác thực** | Bearer Token (JWT)        |

**Query Parameters:**

| Tham số | Kiểu    | Mô tả      |
| ------- | ------- | ---------- |
| `page`  | Integer | Số trang   |
| `size`  | Integer | Kích thước |

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

```json
{
  "meta": { "page": 1, "pageSize": 10, "pages": 10, "total": 98 },
  "result": [
    {
      "id": 1,
      "name": "Xem sản phẩm",
      "apiPath": "/api/v1/san-pham",
      "method": "GET",
      "module": "SAN_PHAM",
      "createdAt": "2026-01-01T00:00:00Z"
    }
  ]
}
```

---

## 2. Lấy quyền theo ID

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `GET /api/v1/permissions/{id}` |
| **Method**   | `GET`                          |
| **Xác thực** | Bearer Token (JWT)             |

**Response:** `200 OK` — Trả về `Permission`

**Lỗi:**

| HTTP Status | Mô tả                |
| ----------- | -------------------- |
| `400`       | Không tìm thấy quyền |

---

## 3. Tạo quyền

| Thuộc tính       | Chi tiết                   |
| ---------------- | -------------------------- |
| **URL**          | `POST /api/v1/permissions` |
| **Method**       | `POST`                     |
| **Content-Type** | `application/json`         |
| **Xác thực**     | Bearer Token (JWT)         |

**Request Body:**

```json
{
  "name": "Xem báo cáo",
  "apiPath": "/api/v1/bao-cao",
  "method": "GET",
  "module": "BAO_CAO"
}
```

| Trường    | Bắt buộc | Mô tả         |
| --------- | -------- | ------------- |
| `name`    | **Có**   | Tên quyền     |
| `apiPath` | **Có**   | Đường dẫn API |
| `method`  | **Có**   | HTTP method   |
| `module`  | **Có**   | Tên module    |

**Response:** `201 Created` — Trả về `Permission`

**Lỗi:**

| HTTP Status | Mô tả                                   |
| ----------- | --------------------------------------- |
| `400`       | Quyền đã tồn tại (trùng apiPath+method) |

---

## 4. Cập nhật quyền

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `PUT /api/v1/permissions` |
| **Method**       | `PUT`                     |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** (phải có `id`)

```json
{
  "id": 99,
  "name": "Xem báo cáo — updated",
  "apiPath": "/api/v1/bao-cao",
  "method": "GET",
  "module": "BAO_CAO"
}
```

**Response:** `200 OK` — Trả về `Permission`

**Lỗi:**

| HTTP Status | Mô tả                |
| ----------- | -------------------- |
| `400`       | Không tìm thấy quyền |

---

## 5. Xóa quyền

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `DELETE /api/v1/permissions/{id}` |
| **Method**   | `DELETE`                          |
| **Xác thực** | Bearer Token (JWT)                |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                |
| ----------- | -------------------- |
| `400`       | Không tìm thấy quyền |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ❌  | ❌   | ❌  | ❌     |
| KHACH_HANG | ❌  | ❌   | ❌  | ❌     |
