# Phiếu Nhập Controller

> **Base Path:** `/api/v1/phieu-nhap`  
> **File:** `PhieuNhapController.java`  
> Quản lý phiếu nhập hàng.

---

## 1. Lấy danh sách phiếu nhập

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/phieu-nhap` |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK` — Trả về `List<PhieuNhap>`

---

## 2. Lấy phiếu nhập theo ID

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/phieu-nhap/{id}` |
| **Xác thực** | Bearer Token (JWT)            |

**Path Parameters:**

| Tham số | Kiểu | Mô tả         |
| ------- | ---- | ------------- |
| `id`    | Long | Mã phiếu nhập |

**Response:** `200 OK` — Trả về `PhieuNhap`

**Lỗi:**

- `400` — Không tìm thấy phiếu nhập

---

## 3. Tạo phiếu nhập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/phieu-nhap` |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** `PhieuNhap`

**Response:** `201 Created` — Trả về `PhieuNhap`

---

## 4. Cập nhật phiếu nhập

| Thuộc tính       | Chi tiết                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/phieu-nhap` |
| **Content-Type** | `application/json`       |
| **Xác thực**     | Bearer Token (JWT)       |

**Request Body:** `PhieuNhap` (phải có `id`)

**Response:** `200 OK` — Trả về `PhieuNhap`

**Lỗi:**

- `400` — Mã phiếu nhập không được để trống

---

## 5. Xóa phiếu nhập

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/phieu-nhap/{id}` |
| **Xác thực** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy phiếu nhập
