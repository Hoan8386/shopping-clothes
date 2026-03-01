# Sản Phẩm Controller

> **Base Path:** `/api/v1/san-pham`  
> **File:** `SanPhamController.java`  
> Quản lý sản phẩm: CRUD + tìm kiếm/lọc + phân trang.

---

## Tổng quan

### Cấu trúc dữ liệu `SanPham`

| Trường         | Kiểu          | Mô tả                               |
| -------------- | ------------- | ----------------------------------- |
| `id`           | Long          | Mã sản phẩm (auto-increment)        |
| `kieuSanPham`  | KieuSanPham   | Kiểu/loại sản phẩm (FK)             |
| `boSuuTap`     | BoSuuTap      | Bộ sưu tập (FK)                     |
| `thuongHieu`   | ThuongHieu    | Thương hiệu (FK)                    |
| `tenSanPham`   | String(255)   | Tên sản phẩm                        |
| `giaVon`       | Double        | Giá vốn (VND)                       |
| `giaBan`       | Double        | Giá bán (VND)                       |
| `giaGiam`      | Integer       | Phần trăm giảm giá (%)              |
| `hinhAnhChinh` | String(255)   | Tên file ảnh chính (lưu trên MinIO) |
| `moTa`         | String(255)   | Mô tả sản phẩm                      |
| `soLuong`      | Integer       | Tổng số lượng tồn kho               |
| `trangThai`    | Integer       | Trạng thái (0: ẩn, 1: hiển thị)     |
| `ngayTao`      | LocalDateTime | Ngày tạo (tự động)                  |
| `ngayCapNhat`  | LocalDateTime | Ngày cập nhật (tự động)             |

---

## 1. Lấy danh sách sản phẩm (có lọc + phân trang)

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/san-pham` |
| **Method**   | `GET`                  |
| **Xác thực** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham số         | Kiểu    | Bắt buộc | Mô tả                           |
| --------------- | ------- | -------- | ------------------------------- |
| `tenSanPham`    | String  | Không    | Lọc theo tên sản phẩm (like)    |
| `kieuSanPhamId` | Long    | Không    | Lọc theo mã kiểu sản phẩm       |
| `boSuuTapId`    | Long    | Không    | Lọc theo mã bộ sưu tập          |
| `thuongHieuId`  | Long    | Không    | Lọc theo mã thương hiệu         |
| `trangThai`     | Integer | Không    | Lọc theo trạng thái             |
| `giaMin`        | Double  | Không    | Giá bán tối thiểu               |
| `giaMax`        | Double  | Không    | Giá bán tối đa                  |
| `page`          | Integer | Không    | Số trang (mặc định: 0)          |
| `size`          | Integer | Không    | Kích thước trang (mặc định: 20) |
| `sort`          | String  | Không    | Sắp xếp (vd: `giaBan,asc`)      |

**Ví dụ request:**

```
GET /api/v1/san-pham?tenSanPham=ao&kieuSanPhamId=1&thuongHieuId=2&trangThai=1&giaMin=100000&giaMax=500000&page=0&size=10&sort=giaBan,asc
```

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

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
      "id": 1,
      "tenSanPham": "Áo Polo Classic",
      "giaVon": 120000,
      "giaBan": 250000,
      "giaGiam": 10,
      "moTa": "Áo polo nam cao cấp",
      "soLuong": 50,
      "trangThai": 1,
      "hinhAnh": "polo-classic.jpg",
      "kieuSanPham": { "id": 1, "tenKieuSanPham": "Áo" },
      "thuongHieu": { "id": 2, "tenThuongHieu": "Nike" },
      "boSuuTap": { "id": 1, "tenSuuTap": "Xuân Hè 2025" }
    }
  ]
}
```

> **Lưu ý:** Nếu không truyền bất kỳ tham số lọc nào → trả về tất cả sản phẩm (có phân trang).

---

## 2. Xem chi tiết sản phẩm

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/san-pham/{id}` |
| **Method**   | `GET`                       |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã sản phẩm |

**Response:** `200 OK` — Trả về `ResSanPhamDTO`

```json
{
  "id": 1,
  "tenSanPham": "Áo Polo Classic",
  "giaVon": 120000,
  "giaBan": 250000,
  "giaGiam": 10,
  "moTa": "Áo polo nam cao cấp",
  "soLuong": 50,
  "trangThai": 1,
  "hinhAnh": "polo-classic.jpg",
  "kieuSanPham": { "id": 1, "tenKieuSanPham": "Áo" },
  "thuongHieu": { "id": 2, "tenThuongHieu": "Nike" },
  "boSuuTap": { "id": 1, "tenSuuTap": "Xuân Hè 2025" }
}
```

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy sản phẩm |

---

## 3. Tạo sản phẩm mới

| Thuộc tính       | Chi tiết                |
| ---------------- | ----------------------- |
| **URL**          | `POST /api/v1/san-pham` |
| **Method**       | `POST`                  |
| **Content-Type** | `multipart/form-data`   |
| **Xác thực**     | Bearer Token (JWT)      |

**Form Data:**

| Tham số         | Kiểu    | Bắt buộc | Mô tả                           |
| --------------- | ------- | -------- | ------------------------------- |
| `tenSanPham`    | String  | Không    | Tên sản phẩm                    |
| `giaVon`        | Double  | Không    | Giá vốn (VND)                   |
| `giaBan`        | Double  | Không    | Giá bán (VND)                   |
| `giaGiam`       | Integer | Không    | Phần trăm giảm giá (%)          |
| `moTa`          | String  | Không    | Mô tả                           |
| `soLuong`       | Integer | Không    | Số lượng                        |
| `trangThai`     | Integer | Không    | Trạng thái (0/1)                |
| `kieuSanPhamId` | Long    | Không    | Mã kiểu sản phẩm                |
| `boSuuTapId`    | Long    | Không    | Mã bộ sưu tập                   |
| `thuongHieuId`  | Long    | Không    | Mã thương hiệu                  |
| `file`          | File    | Không    | Ảnh sản phẩm (upload lên MinIO) |

**Response:** `201 Created` — Trả về `ResSanPhamDTO`

> **Lưu ý:** Ảnh sản phẩm được upload lên MinIO. URL ảnh có thể truy cập qua `GET /storage/{fileName}`.

---

## 4. Cập nhật sản phẩm

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/san-pham` |
| **Method**       | `PUT`                  |
| **Content-Type** | `multipart/form-data`  |
| **Xác thực**     | Bearer Token (JWT)     |

**Form Data:** Giống tạo mới, thêm trường:

| Tham số | Kiểu | Bắt buộc | Mô tả                    |
| ------- | ---- | -------- | ------------------------ |
| `id`    | Long | **Có**   | Mã sản phẩm cần cập nhật |

**Response:** `200 OK` — Trả về `ResSanPhamDTO`

**Lỗi:**

| HTTP Status | Mô tả                           |
| ----------- | ------------------------------- |
| `400`       | Mã sản phẩm không được để trống |

---

## 5. Xóa sản phẩm

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/san-pham/{id}` |
| **Method**   | `DELETE`                       |
| **Xác thực** | Bearer Token (JWT)             |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã sản phẩm |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy sản phẩm |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ❌         | ❌        | ❌           |
| KHACH_HANG | ✅        | ❌         | ❌        | ❌           |
