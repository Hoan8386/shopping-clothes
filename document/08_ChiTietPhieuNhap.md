# Chi Tiết Phiếu Nhập Controller

> **Base Path:** `/api/v1/chi-tiet-phieu-nhap`  
> **File:** `ChiTietPhieuNhapController.java`  
> Quản lý chi tiết phiếu nhập hàng (từng dòng sản phẩm trong phiếu nhập).

---

## Tổng quan

### Cấu trúc dữ liệu `ChiTietPhieuNhap`

| Trường           | Kiểu           | Mô tả                                            |
| ---------------- | -------------- | ------------------------------------------------ |
| `id`             | Long           | Mã chi tiết phiếu nhập (auto-increment)          |
| `phieuNhap`      | PhieuNhap      | Phiếu nhập cha (FK, ẩn trong JSON)               |
| `chiTietSanPham` | ChiTietSanPham | Biến thể sản phẩm được nhập (FK)                 |
| `soLuong`        | Integer        | Số lượng nhập theo phiếu                         |
| `soLuongThieu`   | Integer        | Số lượng thiếu (dùng khi kiểm kê)                |
| `soLuongDaNhap`  | Integer        | Số lượng đã thực nhập vào kho (tính sau kiểm kê) |
| `ghiTru`         | String(255)    | Ghi chú trừ                                      |
| `ghiTruKiemHang` | String(255)    | Ghi chú kiểm hàng                                |
| `trangThai`      | Integer        | Trạng thái (0: Đủ, 1: Thiếu)                     |
| `ngayTao`        | LocalDateTime  | Ngày tạo (tự động)                               |
| `ngayCapNhat`    | LocalDateTime  | Ngày cập nhật (tự động)                          |

### Mã trạng thái chi tiết phiếu nhập (`trangThai`)

| Giá trị | Ý nghĩa | `trangThaiText` |
| ------- | ------- | --------------- |
| `0`     | Đủ      | "Đủ"            |
| `1`     | Thiếu   | "Thiếu"         |

---

## 1. Lấy danh sách chi tiết phiếu nhập

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap` |
| **Method**   | `GET`                             |
| **Xác thực** | Bearer Token (JWT)                |

**Response:** `200 OK` — Trả về `List<ResChiTietPhieuNhapDTO>`

```json
[
  {
    "id": 1,
    "phieuNhapId": 1,
    "tenPhieuNhap": "Nhập hàng đợt 1 - CN Q.1",
    "chiTietSanPham": {
      "id": 1,
      "soLuong": 7,
      "tenSanPham": "Áo Oxford",
      "tenMauSac": "Trắng",
      "tenKichThuoc": "M"
    },
    "soLuong": 10,
    "soLuongThieu": 3,
    "soLuongDaNhap": 7,
    "ghiTru": null,
    "ghiTruKiemHang": "Thiếu 3 cái do hư hỏng",
    "trangThai": 1,
    "trangThaiText": "Thiếu",
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": "2026-03-03T10:00:00"
  }
]
```

---

## 2. Lấy chi tiết phiếu nhập theo ID

| Thuộc tính   | Chi tiết                               |
| ------------ | -------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Method**   | `GET`                                  |
| **Xác thực** | Bearer Token (JWT)                     |

**Response:** `200 OK` — Trả về `ResChiTietPhieuNhapDTO`

**Lỗi:**

| HTTP Status | Mô tả                              |
| ----------- | ---------------------------------- |
| `400`       | Không tìm thấy chi tiết phiếu nhập |

---

## 3. Lấy chi tiết theo mã phiếu nhập

| Thuộc tính   | Chi tiết                                                   |
| ------------ | ---------------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}` |
| **Method**   | `GET`                                                      |
| **Xác thực** | Bearer Token (JWT)                                         |

**Path Parameters:**

| Tham số       | Kiểu | Mô tả         |
| ------------- | ---- | ------------- |
| `phieuNhapId` | Long | Mã phiếu nhập |

**Response:** `200 OK` — Trả về `List<ResChiTietPhieuNhapDTO>`

---

## 4. Tạo chi tiết phiếu nhập

| Thuộc tính       | Chi tiết                           |
| ---------------- | ---------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-phieu-nhap` |
| **Method**       | `POST`                             |
| **Content-Type** | `application/json`                 |
| **Xác thực**     | Bearer Token (JWT)                 |

**Request Body:** `ReqChiTietPhieuNhapDTO`

```json
{
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "soLuongThieu": null,
  "ghiTru": null,
  "ghiTruKiemHang": null,
  "trangThai": 0
}
```

**Kiểu dữ liệu:**

```json
{
  "phieuNhapId": "Long",
  "chiTietSanPhamId": "Long",
  "soLuong": "Integer",
  "soLuongThieu": "Integer",
  "ghiTru": "String",
  "ghiTruKiemHang": "String",
  "trangThai": "Integer"
}
```

**Response:** `201 Created` — Trả về `ResChiTietPhieuNhapDTO`

**Quy tắc:**

- Chỉ được thêm chi tiết khi phiếu nhập đang ở trạng thái **Đã đặt** (0) hoặc **Chậm giao** (2)
- Không được thêm chi tiết khi phiếu đã ở trạng thái **Đã nhận** (1), **Thiếu hàng** (4), **Hoàn thành** (5) hoặc **Hủy** (3)

**Lỗi:**

| HTTP Status | Mô tả                                                        |
| ----------- | ------------------------------------------------------------ |
| `400`       | Không tìm thấy phiếu nhập                                    |
| `400`       | Không thể thêm chi tiết cho phiếu nhập ở trạng thái hiện tại |
| `400`       | Không tìm thấy chi tiết sản phẩm                             |

---

## 5. Cập nhật chi tiết phiếu nhập

| Thuộc tính       | Chi tiết                          |
| ---------------- | --------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-phieu-nhap` |
| **Method**       | `PUT`                             |
| **Content-Type** | `application/json`                |
| **Xác thực**     | Bearer Token (JWT)                |

**Request Body:** `ReqChiTietPhieuNhapDTO` (phải có `id`)

```json
{
  "id": 1,
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "soLuongThieu": 0,
  "ghiTru": null,
  "ghiTruKiemHang": "Đã kiểm tra đủ",
  "trangThai": 0
}
```

**Kiểu dữ liệu:**

```json
{
  "id": "Long (bắt buộc)",
  "phieuNhapId": "Long",
  "chiTietSanPhamId": "Long",
  "soLuong": "Integer",
  "soLuongThieu": "Integer",
  "ghiTru": "String",
  "ghiTruKiemHang": "String",
  "trangThai": "Integer"
}
```

**Response:** `200 OK` — Trả về `ResChiTietPhieuNhapDTO`

**Quy tắc:**

- Chỉ được cập nhật khi phiếu nhập cha đang ở trạng thái **Đã đặt** (0), **Đã nhận** (1), **Chậm giao** (2) hoặc **Thiếu hàng** (4)
- Không được cập nhật khi phiếu đã **Hoàn thành** (5) hoặc **Hủy** (3)
- Sau khi cập nhật chi tiết ở phiếu **Thiếu hàng** (4), gọi kiểm kê lại để cập nhật tồn kho

**Lỗi:**

| HTTP Status | Mô tả                                                                   |
| ----------- | ----------------------------------------------------------------------- |
| `400`       | Mã chi tiết phiếu nhập không được để trống                              |
| `400`       | Không tìm thấy chi tiết phiếu nhập                                      |
| `400`       | Không thể cập nhật chi tiết phiếu nhập khi phiếu đã Hoàn thành hoặc Hủy |
| `400`       | Không tìm thấy phiếu nhập                                               |
| `400`       | Không tìm thấy chi tiết sản phẩm                                        |

---

## 6. Xóa chi tiết phiếu nhập

| Thuộc tính   | Chi tiết                                  |
| ------------ | ----------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Method**   | `DELETE`                                  |
| **Xác thực** | Bearer Token (JWT)                        |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                              |
| ----------- | ---------------------------------- |
| `400`       | Không tìm thấy chi tiết phiếu nhập |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ✅         | ✅        | ❌           |
| KHACH_HANG | ❌        | ❌         | ❌        | ❌           |


