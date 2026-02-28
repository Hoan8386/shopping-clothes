# Chi Tiết Phiếu Nhập Controller

> **Base Path:** `/api/v1/chi-tiet-phieu-nhap`  
> **File:** `ChiTietPhieuNhapController.java`  
> Quản lý chi tiết phiếu nhập hàng.

---

## 1. Lấy danh sách chi tiết phiếu nhập

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap` |
| **Xác thực** | Bearer Token (JWT)                |

**Response:** `200 OK` — Trả về `List<ChiTietPhieuNhap>`

---

## 2. Lấy chi tiết phiếu nhập theo ID

| Thuộc tính   | Chi tiết                               |
| ------------ | -------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Xác thực** | Bearer Token (JWT)                     |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                  |
| ------- | ---- | ---------------------- |
| `id`    | Long | Mã chi tiết phiếu nhập |

**Response:** `200 OK` — Trả về `ChiTietPhieuNhap`

**Lỗi:**

- `400` — Không tìm thấy chi tiết phiếu nhập

---

## 3. Lấy chi tiết phiếu nhập theo mã phiếu nhập

| Thuộc tính   | Chi tiết                                                   |
| ------------ | ---------------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}` |
| **Xác thực** | Bearer Token (JWT)                                         |

**Path Parameters:**

| Tham số       | Kiểu | Mô tả         |
| ------------- | ---- | ------------- |
| `phieuNhapId` | Long | Mã phiếu nhập |

**Response:** `200 OK` — Trả về `List<ChiTietPhieuNhap>`

---

## 4. Tạo chi tiết phiếu nhập

| Thuộc tính       | Chi tiết                            |
| ---------------- | ----------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-phieu-nhap`  |
| **Content-Type** | `application/x-www-form-urlencoded` |
| **Xác thực**     | Bearer Token (JWT)                  |

**Request Parameters:**

| Tham số            | Kiểu    | Bắt buộc | Mô tả                |
| ------------------ | ------- | -------- | -------------------- |
| `phieuNhapId`      | Long    | Không    | Mã phiếu nhập        |
| `chiTietSanPhamId` | Long    | Không    | Mã chi tiết sản phẩm |
| `soLuong`          | Integer | Không    | Số lượng             |
| `ghiTru`           | String  | Không    | Ghi trừ              |
| `ghiTruKiemHang`   | String  | Không    | Ghi trừ kiểm hàng    |
| `trangThai`        | Integer | Không    | Trạng thái           |

**Response:** `201 Created` — Trả về `ChiTietPhieuNhap`

---

## 5. Cập nhật chi tiết phiếu nhập

| Thuộc tính       | Chi tiết                          |
| ---------------- | --------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-phieu-nhap` |
| **Content-Type** | `application/json`                |
| **Xác thực**     | Bearer Token (JWT)                |

**Request Body:** `ChiTietPhieuNhap` (phải có `id`)

**Response:** `200 OK` — Trả về `ChiTietPhieuNhap`

**Lỗi:**

- `400` — Mã chi tiết phiếu nhập không được để trống

---

## 6. Xóa chi tiết phiếu nhập

| Thuộc tính   | Chi tiết                                  |
| ------------ | ----------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Xác thực** | Bearer Token (JWT)                        |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy chi tiết phiếu nhập
