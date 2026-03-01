# Đơn Hàng Controller

> **Base Path:** `/api/v1/don-hang`  
> **File:** `DonHangController.java`  
> Quản lý đơn hàng: tạo đơn online (khách hàng), tạo đơn tại quầy (nhân viên), lọc + phân trang, cập nhật trạng thái.

---

## Tổng quan

### Cấu trúc dữ liệu `DonHang`

| Trường               | Kiểu          | Mô tả                                     |
| -------------------- | ------------- | ----------------------------------------- |
| `id`                 | Long          | Mã đơn hàng (auto-increment)              |
| `cuaHang`            | CuaHang       | Cửa hàng xử lý đơn                        |
| `khachHang`          | KhachHang     | Khách hàng đặt đơn                        |
| `nhanVien`           | NhanVien      | Nhân viên xử lý đơn                       |
| `maKhuyenMaiHoaDon`  | Long          | Mã khuyến mãi theo hóa đơn (FK, nullable) |
| `maKhuyenMaiDiem`    | Long          | Mã khuyến mãi theo điểm (FK, nullable)    |
| `diaChi`             | String(255)   | Địa chỉ giao hàng                         |
| `tongTien`           | Integer       | Tổng tiền trước giảm (VND)                |
| `tienGiam`           | Integer       | Số tiền giảm (VND)                        |
| `tongTienGiam`       | Integer       | Tổng tiền giảm (VND)                      |
| `tongTienTra`        | Integer       | Tổng tiền phải trả sau giảm (VND)         |
| `trangThai`          | Integer       | Trạng thái đơn hàng (xem bảng bên dưới)   |
| `trangThaiThanhToan` | Integer       | Trạng thái thanh toán (xem bảng bên dưới) |
| `hinhThucDonHang`    | Integer       | Hình thức đơn hàng (xem bảng bên dưới)    |
| `chiTietDonHangs`    | List          | Danh sách chi tiết đơn hàng               |
| `ngayTao`            | LocalDateTime | Ngày tạo đơn (tự động)                    |
| `ngayCapNhat`        | LocalDateTime | Ngày cập nhật (tự động)                   |

### Mã trạng thái đơn hàng (`trangThai`)

| Giá trị | Ý nghĩa        | Mô tả                                        |
| ------- | -------------- | -------------------------------------------- |
| `0`     | Chờ xác nhận   | Đơn mới tạo, chưa được xử lý                 |
| `1`     | Đã xác nhận    | Đơn đã được nhân viên xác nhận               |
| `2`     | Đang giao hàng | Đơn đang trong quá trình giao                |
| `3`     | **Thành công** | Đơn giao thành công → **cộng điểm tích lũy** |
| `4`     | Đã hủy         | Đơn bị hủy                                   |

### Trạng thái thanh toán (`trangThaiThanhToan`)

| Giá trị | Ý nghĩa         |
| ------- | --------------- |
| `0`     | Chưa thanh toán |
| `1`     | Đã thanh toán   |

### Hình thức đơn hàng (`hinhThucDonHang`)

| Giá trị | Ý nghĩa  |
| ------- | -------- |
| `0`     | Tại quầy |
| `1`     | Online   |

### Quy tắc cộng điểm tích lũy

Khi cập nhật đơn hàng và `trangThai` chuyển sang **3 (Thành công)**:

- Hệ thống tự động cộng điểm tích lũy cho khách hàng.
- **Công thức:** `(tongTienTra / 100.000) × 10 điểm`
- Ví dụ: Đơn 350.000đ → 30 điểm, đơn 1.200.000đ → 120 điểm.
- Điểm chỉ được cộng **1 lần** khi chuyển trạng thái sang 3, không cộng lại nếu đã ở trạng thái 3.

---

## 1. Lấy danh sách đơn hàng (có lọc + phân trang)

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/don-hang` |
| **Method**   | `GET`                  |
| **Xác thực** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham số              | Kiểu    | Bắt buộc | Mô tả                                       |
| -------------------- | ------- | -------- | ------------------------------------------- |
| `cuaHangId`          | Long    | Không    | Lọc theo mã cửa hàng                        |
| `nhanVienId`         | Long    | Không    | Lọc theo mã nhân viên                       |
| `trangThai`          | Integer | Không    | Lọc theo trạng thái đơn (0-4)               |
| `trangThaiThanhToan` | Integer | Không    | Lọc theo trạng thái thanh toán (0-1)        |
| `hinhThucDonHang`    | Integer | Không    | Lọc theo hình thức đơn (0: quầy, 1: online) |
| `page`               | Integer | Không    | Số trang (mặc định: 0)                      |
| `size`               | Integer | Không    | Kích thước trang (mặc định: 20)             |
| `sort`               | String  | Không    | Sắp xếp (vd: `ngayTao,desc`)                |

**Ví dụ request:**

```
GET /api/v1/don-hang?trangThai=1&hinhThucDonHang=1&page=0&size=10&sort=ngayTao,desc
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
      "cuaHang": { "id": 1, "tenCuaHang": "Chi nhánh Quận 1" },
      "khachHang": { "id": 1, "tenKhachHang": "Lan", "diemTichLuy": 10 },
      "nhanVien": { "id": 5, "tenNhanVien": "Hùng" },
      "maKhuyenMaiHoaDon": null,
      "maKhuyenMaiDiem": null,
      "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
      "tongTien": 600,
      "tienGiam": 0,
      "tongTienGiam": 0,
      "tongTienTra": 600,
      "trangThai": 1,
      "trangThaiThanhToan": 1,
      "hinhThucDonHang": 1,
      "chiTietDonHangs": [...],
      "ngayTao": "2026-03-01T10:00:00",
      "ngayCapNhat": null
    }
  ]
}
```

> **Lưu ý phân quyền:**
>
> - **Khách hàng:** Chỉ xem được đơn hàng của mình (hệ thống tự lọc theo `khachHangId`).
> - **Nhân viên / Admin:** Xem được tất cả đơn hàng, có thể lọc tùy ý.

---

## 2. Lấy đơn hàng theo ID

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/don-hang/{id}` |
| **Method**   | `GET`                       |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã đơn hàng |

**Response:** `200 OK` — Trả về `DonHang` (bao gồm `chiTietDonHangs`)

**Lỗi:**

| HTTP Status | Mô tả                               |
| ----------- | ----------------------------------- |
| `400`       | Không tìm thấy đơn hàng             |
| `400`       | Bạn không có quyền xem đơn hàng này |

---

## 3. Tạo đơn hàng online (Khách hàng)

| Thuộc tính       | Chi tiết                                      |
| ---------------- | --------------------------------------------- |
| **URL**          | `POST /api/v1/don-hang/online`                |
| **Method**       | `POST`                                        |
| **Content-Type** | `application/json`                            |
| **Xác thực**     | Bearer Token (JWT) — Yêu cầu quyền KHACH_HANG |

**Request Body:** `ReqTaoDonHangDTO`

```json
{
  "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
  "cuaHangId": 1,
  "maKhuyenMaiHoaDon": null,
  "maKhuyenMaiDiem": null
}
```

| Trường              | Kiểu   | Bắt buộc | Mô tả                            |
| ------------------- | ------ | -------- | -------------------------------- |
| `diaChi`            | String | **Có**   | Địa chỉ giao hàng                |
| `cuaHangId`         | Long   | Không    | Mã cửa hàng xử lý                |
| `maKhuyenMaiHoaDon` | Long   | Không    | Mã khuyến mãi theo hóa đơn       |
| `maKhuyenMaiDiem`   | Long   | Không    | Mã khuyến mãi theo điểm tích lũy |

**Response:** `201 Created` — Trả về `DonHang`

**Logic xử lý:**

1. Lấy thông tin khách hàng từ JWT token
2. Lấy giỏ hàng → tạo chi tiết đơn hàng
3. Tính giá bán, giảm giá, thành tiền
4. Trừ số lượng tồn kho (`ChiTietSanPham.soLuong`)
5. Xóa giỏ hàng sau khi tạo đơn thành công
6. Gán `hinhThucDonHang = 1`, `trangThai = 0`, `trangThaiThanhToan = 0`

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy khách hàng |
| `400`       | Giỏ hàng trống            |

---

## 4. Tạo đơn hàng tại quầy (Nhân viên)

| Thuộc tính       | Chi tiết                                     |
| ---------------- | -------------------------------------------- |
| **URL**          | `POST /api/v1/don-hang/tai-quay`             |
| **Method**       | `POST`                                       |
| **Content-Type** | `application/json`                           |
| **Xác thực**     | Bearer Token (JWT) — Yêu cầu quyền NHAN_VIEN |

**Request Body:**

```json
{
  "khachHang": { "id": 1 },
  "diaChi": "Mua tại cửa hàng",
  "tongTien": 500,
  "tienGiam": 0,
  "tongTienGiam": 0,
  "tongTienTra": 500,
  "trangThai": 3,
  "trangThaiThanhToan": 1,
  "chiTietDonHangs": [
    {
      "chiTietSanPham": { "id": 1 },
      "giaSanPham": 200,
      "giamGia": 0,
      "giaGiam": 0,
      "soLuong": 2,
      "thanhTien": 400
    }
  ]
}
```

**Response:** `201 Created`

**Lỗi:**

| HTTP Status | Mô tả                    |
| ----------- | ------------------------ |
| `400`       | Không tìm thấy nhân viên |

---

## 5. Cập nhật đơn hàng

| Thuộc tính       | Chi tiết               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/don-hang` |
| **Method**       | `PUT`                  |
| **Content-Type** | `application/json`     |
| **Xác thực**     | Bearer Token (JWT)     |

**Request Body:**

```json
{
  "id": 4,
  "cuaHang": { "id": 1 },
  "khachHang": { "id": 4 },
  "trangThai": 3,
  "trangThaiThanhToan": 1,
  "tongTien": 200,
  "tongTienTra": 200
}
```

**Response:** `200 OK`

> **Quan trọng - Cộng điểm tích lũy:**
> Khi `trangThai` được chuyển sang **3 (Thành công)** từ một trạng thái khác:
>
> - Hệ thống tự động tính điểm: `(tongTienTra / 100.000) × 10`
> - Cộng điểm vào `KhachHang.diemTichLuy`

**Lỗi:**

| HTTP Status | Mô tả                           |
| ----------- | ------------------------------- |
| `400`       | Mã đơn hàng không được để trống |
| `500`       | Không tìm thấy đơn hàng         |

---

## 6. Xóa đơn hàng

| Thuộc tính   | Chi tiết                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/don-hang/{id}` |
| **Method**   | `DELETE`                       |
| **Xác thực** | Bearer Token (JWT)             |

**Path Parameters:**

| Tham số | Kiểu | Mô tả       |
| ------- | ---- | ----------- |
| `id`    | Long | Mã đơn hàng |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                   |
| ----------- | ----------------------- |
| `400`       | Không tìm thấy đơn hàng |

---

## Phân quyền

| Vai trò    | GET (Xem)       | POST Online | POST Tại quầy | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------------- | ----------- | ------------- | --------- | ------------ |
| ADMIN      | ✅ Tất cả       | ❌          | ✅            | ✅        | ✅           |
| NHAN_VIEN  | ✅ Tất cả       | ❌          | ✅            | ❌        | ❌           |
| KHACH_HANG | ✅ Chỉ đơn mình | ✅          | ❌            | ❌        | ❌           |
