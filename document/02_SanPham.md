# Sản Phẩm Controller

> **Base Path:** `/api/v1/san-pham`  
> **File:** `SanPhamController.java`  
> Quản lý sản phẩm: CRUD + tìm kiếm/lọc + phân trang.

---

## 1. Lấy danh sách sản phẩm (có lọc + phân trang)

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/san-pham` |
| **Xác thực** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham số         | Kiểu    | Bắt buộc | Mô tả                           |
| --------------- | ------- | -------- | ------------------------------- |
| `tenSanPham`    | String  | Không    | Lọc theo tên sản phẩm           |
| `kieuSanPhamId` | Long    | Không    | Lọc theo mã kiểu sản phẩm       |
| `boSuuTapId`    | Long    | Không    | Lọc theo mã bộ sưu tập          |
| `thuongHieuId`  | Long    | Không    | Lọc theo mã thương hiệu         |
| `trangThai`     | Integer | Không    | Lọc theo trạng thái             |
| `giaMin`        | Double  | Không    | Giá tối thiểu                   |
| `giaMax`        | Double  | Không    | Giá tối đa                      |
| `page`          | Integer | Không    | Số trang (mặc định: 0)          |
| `size`          | Integer | Không    | Kích thước trang (mặc định: 20) |
| `sort`          | String  | Không    | Sắp xếp (vd: `giaBan,asc`)      |

**Ví dụ request:**

```
GET /api/v1/san-pham?tenSanPham=ao&kieuSanPhamId=1&thuongHieuId=2&trangThai=1&giaMin=100000&giaMax=500000&page=0&size=10&sort=giaBan,asc
```

**Response:** `200 OK`

```json
{
  "meta": {
    "page": 1,
    "pageSize": 10,
    "pages": 5,
    "total": 50
  },
  "result": [
    {
      "id": "long",
      "tenSanPham": "string",
      "giaVon": "double",
      "giaBan": "double",
      "giaGiam": "integer",
      "moTa": "string",
      "soLuong": "integer",
      "trangThai": "integer",
      "hinhAnh": "string"
    }
  ]
}
```

---

## 2. Xem chi tiết sản phẩm

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/san-pham/{id}` |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã sản phẩm |

**Response:** `200 OK` — Trả về `ResSanPhamDTO`

**Lỗi:**

- `400` — Không tìm thấy sản phẩm

---

## 3. Tạo sản phẩm mới

| Thuộc tính       | Chi tiết                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `POST /api/v1/san-pham`                  |
| **Content-Type** | `multipart/form-data`                    |
| **Xác thực**     | Bearer Token (JWT) — yêu cầu quyền ADMIN |

**Form Data:**

| Tham số         | Kiểu    | Bắt buộc | Mô tả                           |
| --------------- | ------- | -------- | ------------------------------- |
| `tenSanPham`    | String  | Không    | Tên sản phẩm                    |
| `giaVon`        | Double  | Không    | Giá vốn                         |
| `giaBan`        | Double  | Không    | Giá bán                         |
| `giaGiam`       | Integer | Không    | Giảm giá (%)                    |
| `moTa`          | String  | Không    | Mô tả                           |
| `soLuong`       | Integer | Không    | Số lượng                        |
| `trangThai`     | Integer | Không    | Trạng thái                      |
| `kieuSanPhamId` | Long    | Không    | Mã kiểu sản phẩm                |
| `boSuuTapId`    | Long    | Không    | Mã bộ sưu tập                   |
| `thuongHieuId`  | Long    | Không    | Mã thương hiệu                  |
| `file`          | File    | Không    | Ảnh sản phẩm (upload lên MinIO) |

**Response:** `201 Created` — Trả về `ResSanPhamDTO`

---

## 4. Cập nhật sản phẩm

| Thuộc tính       | Chi tiết                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `PUT /api/v1/san-pham`                   |
| **Content-Type** | `multipart/form-data`                    |
| **Xác thực**     | Bearer Token (JWT) — yêu cầu quyền ADMIN |

**Form Data:** Giống tạo mới, thêm trường:

| Tham số | Kiểu | Bắt buộc | Mô tả                    |
| ------- | ---- | -------- | ------------------------ |
| `id`    | Long | **Có**   | Mã sản phẩm cần cập nhật |

**Response:** `200 OK` — Trả về `ResSanPhamDTO`

**Lỗi:**

- `400` — Mã sản phẩm không được để trống

---

## 5. Xóa sản phẩm

| Thuộc tính   | Chi tiết                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `DELETE /api/v1/san-pham/{id}`           |
| **Xác thực** | Bearer Token (JWT) — yêu cầu quyền ADMIN |

**Response:** `204 No Content`

**Lỗi:**

- `400` — Không tìm thấy sản phẩm
