# Bộ Sưu Tập Controller

> **Base Path:** `/api/v1/bo-suu-tap`  
> **File:** `BoSuuTapController.java`  
> Quản lý bộ sưu tập sản phẩm.

---

## 1. Lấy danh sách bộ sưu tập

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/bo-suu-tap` |
| **Xác thực** | Bearer Token (JWT)       |

**Response:** `200 OK` — Trả về `List<BoSuuTap>`

---

## 2. Lấy bộ sưu tập theo ID

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/bo-suu-tap/{id}` |
| **Xác thực** | Bearer Token (JWT)            |

**Path Parameters:**

| Tham số | Kiểu | Mô tả         |
| ------- | ---- | ------------- |
| `id`    | Long | Mã bộ sưu tập |

**Response:** `200 OK` — Trả về `BoSuuTap`

**Lỗi:**

- `400` — Không tìm thấy bộ sưu tập

---

## 3. Tạo bộ sưu tập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/bo-suu-tap` |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** `BoSuuTap`

**Response:** `201 Created` — Trả về `BoSuuTap`

---

## 4. Cập nhật bộ sưu tập

| Thuộc tính       | Chi tiết                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/bo-suu-tap` |
| **Content-Type** | `application/json`       |
| **Xác thực**     | Bearer Token (JWT)       |

**Request Body:** `BoSuuTap` (phải có `id`)

**Response:** `200 OK` — Trả về `BoSuuTap`

**Lỗi:**

- `400` — Mã bộ sưu tập không được để trống

---

## 5. Xóa bộ sưu tập

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/bo-suu-tap/{id}` |
| **Xác thực** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy bộ sưu tập
