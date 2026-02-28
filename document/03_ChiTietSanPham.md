# Chi Tiết Sản Phẩm Controller

> **Base Path:** `/api/v1/chi-tiet-san-pham`  
> **File:** `ChiTietSanPhamController.java`  
> Quản lý chi tiết sản phẩm (biến thể sản phẩm theo màu sắc, kích thước, cửa hàng, ...).

---

## 1. Lấy tất cả chi tiết sản phẩm

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham` |
| **Xác thực** | Bearer Token (JWT)              |

**Response:** `200 OK` — Trả về `List<ResChiTietSanPhamDTO>`

---

## 2. Lấy chi tiết sản phẩm theo ID

| Thuộc tính   | Chi tiết                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/{id}` |
| **Xác thực** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                |
| ------- | ---- | -------------------- |
| `id`    | Long | Mã chi tiết sản phẩm |

**Response:** `200 OK` — Trả về `ResChiTietSanPhamDTO`

**Lỗi:**

- `400` — Không tìm thấy chi tiết sản phẩm

---

## 3. Lấy chi tiết sản phẩm theo mã sản phẩm

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}` |
| **Xác thực** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham số     | Kiểu | Mô tả           |
| ----------- | ---- | --------------- |
| `sanPhamId` | Long | Mã sản phẩm cha |

**Response:** `200 OK` — Trả về `List<ResChiTietSanPhamDTO>`

---

## 4. Tạo chi tiết sản phẩm

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-san-pham` |
| **Content-Type** | `multipart/form-data`            |
| **Xác thực**     | Bearer Token (JWT)               |

**Form Data:**

| Tham số       | Kiểu         | Bắt buộc | Mô tả                                 |
| ------------- | ------------ | -------- | ------------------------------------- |
| `sanPhamId`   | Long         | Không    | Mã sản phẩm                           |
| `maPhieuNhap` | Long         | Không    | Mã phiếu nhập                         |
| `mauSacId`    | Long         | Không    | Mã màu sắc                            |
| `kichThuocId` | Long         | Không    | Mã kích thước                         |
| `maCuaHang`   | Long         | Không    | Mã cửa hàng                           |
| `soLuong`     | Integer      | Không    | Số lượng                              |
| `trangThai`   | Integer      | Không    | Trạng thái                            |
| `moTa`        | String       | Không    | Mô tả                                 |
| `ghiTru`      | String       | Không    | Ghi trừ                               |
| `files`       | List\<File\> | Không    | Danh sách hình ảnh (upload lên MinIO) |

**Response:** `201 Created` — Trả về `ResChiTietSanPhamDTO`

---

## 5. Cập nhật chi tiết sản phẩm

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-san-pham` |
| **Content-Type** | `multipart/form-data`           |
| **Xác thực**     | Bearer Token (JWT)              |

**Form Data:** Giống tạo mới, thêm trường:

| Tham số | Kiểu | Bắt buộc | Mô tả                             |
| ------- | ---- | -------- | --------------------------------- |
| `id`    | Long | **Có**   | Mã chi tiết sản phẩm cần cập nhật |

**Response:** `200 OK` — Trả về `ResChiTietSanPhamDTO`

**Lỗi:**

- `400` — Mã chi tiết sản phẩm không được để trống

---

## 6. Xóa chi tiết sản phẩm

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-san-pham/{id}` |
| **Xác thực** | Bearer Token (JWT)                      |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy chi tiết sản phẩm
