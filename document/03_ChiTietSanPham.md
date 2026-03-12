# Chi Tiết Sản Phẩm Controller

> **Base Path:** `/api/v1/chi-tiet-san-pham`  
> **File:** `ChiTietSanPhamController.java`  
> Quản lý chi tiết sản phẩm (biến thể sản phẩm theo màu sắc, kích thước, cửa hàng).

---

## Tổng quan

Mỗi sản phẩm (`SanPham`) có thể có nhiều biến thể (`ChiTietSanPham`) khác nhau theo **màu sắc**, **kích thước**, và phân bổ theo **cửa hàng**.

### Cấu trúc dữ liệu `ChiTietSanPham`

| Trường        | Kiểu          | Mô tả                                 |
| ------------- | ------------- | ------------------------------------- |
| `id`          | Long          | Mã chi tiết sản phẩm (auto-increment) |
| `sanPham`     | SanPham       | Sản phẩm cha (FK)                     |
| `maPhieuNhap` | Long          | Mã phiếu nhập liên quan (nullable)    |
| `mauSac`      | MauSac        | Màu sắc (FK)                          |
| `kichThuoc`   | KichThuoc     | Kích thước (FK)                       |
| `maCuaHang`   | Long          | Mã cửa hàng phân bổ                   |
| `soLuong`     | Integer       | Số lượng tồn kho                      |
| `trangThai`   | Integer       | Trạng thái (0: ẩn, 1: hiển thị)       |
| `moTa`        | String(255)   | Mô tả chi tiết                        |
| `ghiTru`      | String(255)   | Ghi chú trừ                           |
| `ngayTao`     | LocalDateTime | Ngày tạo (tự động)                    |
| `ngayCapNhat` | LocalDateTime | Ngày cập nhật (tự động)               |

---

## 1. Lấy tất cả chi tiết sản phẩm

| Thuộc tính   | Chi tiết                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham` |
| **Method**   | `GET`                           |
| **Xác thực** | Bearer Token (JWT)              |

**Query Parameters (lọc tùy chọn):**

| Tham số       | Kiểu    | Bắt buộc | Mô tả                                |
| ------------- | ------- | -------- | ------------------------------------ |
| `sanPhamId`   | Long    | Không    | Lọc theo mã sản phẩm                 |
| `mauSacId`    | Long    | Không    | Lọc theo mã màu sắc                  |
| `kichThuocId` | Long    | Không    | Lọc theo mã kích thước               |
| `maCuaHang`   | Long    | Không    | Lọc theo mã cửa hàng                 |
| `trangThai`   | Integer | Không    | Lọc theo trạng thái (0: ẩn, 1: hiển) |

**Ví dụ request:**

```
GET /api/v1/chi-tiet-san-pham?sanPhamId=1&mauSacId=2&kichThuocId=3&maCuaHang=1&trangThai=1
```

**Response:** `200 OK` — Trả về `List<ResChiTietSanPhamDTO>`

```json
[
  {
    "id": 1,
    "maPhieuNhap": null,
    "tenCuaHang": "Chi nhánh Quận 1",
    "soLuong": 15,
    "trangThai": 1,
    "moTa": "Áo polo đen size M",
    "ghiTru": null,
    "tenSanPham": "Áo Polo Classic",
    "tenMauSac": "Đen",
    "tenKichThuoc": "M",
    "hinhAnhUrls": ["polo-den-m-1.jpg", "polo-den-m-2.jpg"]
  }
]
```

**Kiểu dữ liệu:**

```json
{
  "id": "Long",
  "maPhieuNhap": "Long | null",
  "tenCuaHang": "String",
  "soLuong": "Integer",
  "trangThai": "Integer",
  "moTa": "String",
  "ghiTru": "String",
  "tenSanPham": "String",
  "tenMauSac": "String",
  "tenKichThuoc": "String",
  "hinhAnhUrls": "List<String>"
}
```

> **Lưu ý:** Nếu không truyền bất kỳ tham số lọc nào → trả về tất cả chi tiết sản phẩm.

---

## 2. Lấy chi tiết sản phẩm theo ID

| Thuộc tính   | Chi tiết                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/{id}` |
| **Method**   | `GET`                                |
| **Xác thực** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                |
| ------- | ---- | -------------------- |
| `id`    | Long | Mã chi tiết sản phẩm |

**Response:** `200 OK` — Trả về `ResChiTietSanPhamDTO`

**Lỗi:**

| HTTP Status | Mô tả                            |
| ----------- | -------------------------------- |
| `400`       | Không tìm thấy chi tiết sản phẩm |

---

## 3. Lấy chi tiết sản phẩm theo mã sản phẩm

| Thuộc tính   | Chi tiết                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}` |
| **Method**   | `GET`                                                |
| **Xác thực** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham số     | Kiểu | Mô tả           |
| ----------- | ---- | --------------- |
| `sanPhamId` | Long | Mã sản phẩm cha |

**Response:** `200 OK` — Trả về `List<ResChiTietSanPhamDTO>`

> **Lưu ý:** Trả về tất cả biến thể (màu sắc, kích thước) của sản phẩm được chỉ định.

---

## 4. Tạo chi tiết sản phẩm

| Thuộc tính       | Chi tiết                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-san-pham` |
| **Method**       | `POST`                           |
| **Content-Type** | `multipart/form-data`            |
| **Xác thực**     | Bearer Token (JWT)               |

**Form Data:**

| Tham số       | Kiểu         | Bắt buộc | Mô tả                                 |
| ------------- | ------------ | -------- | ------------------------------------- |
| `sanPhamId`   | Long         | Không    | Mã sản phẩm                           |
| `maPhieuNhap` | Long         | Không    | Mã phiếu nhập                         |
| `mauSacId`    | Long         | Không    | Mã màu sắc                            |
| `kichThuocId` | Long         | Không    | Mã kích thước                         |
| `soLuong`     | Integer      | Không    | Số lượng                              |
| `trangThai`   | Integer      | Không    | Trạng thái                            |
| `moTa`        | String       | Không    | Mô tả                                 |
| `ghiTru`      | String       | Không    | Ghi chú trừ                           |
| `files`       | List\<File\> | Không    | Danh sách hình ảnh (upload lên MinIO) |

**Response:** `201 Created` — Trả về `List<ResChiTietSanPhamDTO>`

> **Lưu ý:** Khi tạo chi tiết sản phẩm, hệ thống tự động tạo cho **TẤT CẢ cửa hàng** hiện có và cập nhật tổng `soLuong` của sản phẩm cha.

---

## 5. Cập nhật chi tiết sản phẩm

| Thuộc tính       | Chi tiết                        |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-san-pham` |
| **Method**       | `PUT`                           |
| **Content-Type** | `multipart/form-data`           |
| **Xác thực**     | Bearer Token (JWT)              |

**Form Data:** Giống tạo mới, thêm trường:

| Tham số | Kiểu | Bắt buộc | Mô tả                             |
| ------- | ---- | -------- | --------------------------------- |
| `id`    | Long | **Có**   | Mã chi tiết sản phẩm cần cập nhật |

**Response:** `200 OK` — Trả về `ResChiTietSanPhamDTO`

**Lỗi:**

| HTTP Status | Mô tả                                    |
| ----------- | ---------------------------------------- |
| `400`       | Mã chi tiết sản phẩm không được để trống |

---

## 6. Xóa chi tiết sản phẩm

| Thuộc tính   | Chi tiết                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-san-pham/{id}` |
| **Method**   | `DELETE`                                |
| **Xác thực** | Bearer Token (JWT)                      |

**Path Parameters:**

| Tham số | Kiểu | Mô tả                |
| ------- | ---- | -------------------- |
| `id`    | Long | Mã chi tiết sản phẩm |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                            |
| ----------- | -------------------------------- |
| `400`       | Không tìm thấy chi tiết sản phẩm |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ❌         | ❌        | ❌           |
| KHACH_HANG | ✅        | ❌         | ❌        | ❌           |
