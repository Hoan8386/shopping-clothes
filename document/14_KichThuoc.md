# Kích Thước Controller

> **Base Path:** `/api/v1/kich-thuoc`  
> **File:** `KichThuocController.java`  
> Quản lý danh sách kích thước sản phẩm.

---

## 1. Lấy danh sách kích thước

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/kich-thuoc` |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK` — Trả về `List<KichThuoc>`

---

## 2. Lấy kích thước theo ID

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/kich-thuoc/{id}` |
| **Xác thực** | Bearer Token (JWT)            |

**Path Parameters:**

| Tham số | Kiểu | Mô tả         |
| ------- | ---- | ------------- |
| `id`    | Long | Mã kích thước |

**Response:** `200 OK` — Trả về `KichThuoc`

**Lỗi:**

- `400` — Không tìm thấy kích thước

---

## 3. Tạo kích thước

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/kich-thuoc` |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** `KichThuoc`

**Response:** `201 Created` — Trả về `KichThuoc`

---

## 4. Cập nhật kích thước

| Thuộc tính       | Chi tiết                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/kich-thuoc` |
| **Content-Type** | `application/json`       |
| **Xác thực**     | Bearer Token (JWT)       |

**Request Body:** `KichThuoc` (phải có `id`)

**Response:** `200 OK` — Trả về `KichThuoc`

**Lỗi:**

- `400` — Mã kích thước không được để trống

---

## 5. Xóa kích thước

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/kich-thuoc/{id}` |
| **Xác thực** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy kích thước
