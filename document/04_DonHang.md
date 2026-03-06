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
| `sdt`                | String(255)   | Số điện thoại nhận hàng                   |
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

| Giá trị | Ý nghĩa          | Mô tả                                                  |
| ------- | ---------------- | ------------------------------------------------------ |
| `0`     | Chờ xác nhận     | Đơn mới tạo, chưa được xử lý                           |
| `1`     | Đã xác nhận      | Đơn đã được nhân viên xác nhận                         |
| `2`     | Đang đóng gói    | Đơn đang được đóng gói                                 |
| `3`     | Đang giao hàng   | Đơn đang trong quá trình giao                          |
| `4`     | Đã hủy           | Đơn bị hủy                                             |
| `5`     | **Đã nhận hàng** | Khách hàng xác nhận nhận hàng → **cộng điểm tích lũy** |

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

### Luồng chuyển trạng thái

- **Nhân viên:** `0 → 1 → 2 → 3` (xác nhận → đóng gói → gửi hàng)
- **Khách hàng:** `3 → 5` (xác nhận đã nhận hàng)
- **Hủy đơn:** `0 → 4` hoặc `1 → 4` (chỉ khi chưa đóng gói)
- **Cập nhật địa chỉ/SĐT:** Chỉ cho phép khi `trangThai < 2` (chưa đóng gói)

### Quy tắc cộng điểm tích lũy

Khi cập nhật đơn hàng và `trangThai` chuyển sang **5 (Đã nhận hàng)**:

- Hệ thống tự động cộng điểm tích lũy cho khách hàng.
- **Công thức:** `(tongTienTra / 100.000) × 10 điểm`
- Ví dụ: Đơn 350.000đ → 30 điểm, đơn 1.200.000đ → 120 điểm.
- Điểm chỉ được cộng **1 lần** khi chuyển trạng thái sang 5, không cộng lại nếu đã ở trạng thái 5.

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
| `trangThai`          | Integer | Không    | Lọc theo trạng thái đơn (0-5)               |
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
      "khachHang": {
        "id": 1,
        "tenKhachHang": "Lan",
        "sdt": "0911000001",
        "email": "lan@g.com",
        "diemTichLuy": 10
      },
      "nhanVien": {
        "id": 5,
        "tenNhanVien": "Hùng",
        "email": "h@s.com",
        "soDienThoai": "0901000005"
      },
      "khuyenMaiHoaDon": null,
      "khuyenMaiDiem": null,
      "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
      "sdt": "0911000001",
      "tongTien": 600,
      "tienGiam": 0,
      "tongTienGiam": 0,
      "tongTienTra": 600,
      "trangThai": "Đã xác nhận",
      "trangThaiThanhToan": "Đã thanh toán",
      "hinhThucDonHang": "Online",
      "chiTietDonHangs": [
        {
          "id": 1,
          "chiTietSanPhamId": 1,
          "tenSanPham": "Áo Oxford",
          "hinhAnhChinh": "ao-oxford.jpg",
          "tenMauSac": "Trắng",
          "tenKichThuoc": "M",
          "giaSanPham": 200,
          "giamGia": 0,
          "giaGiam": 0,
          "soLuong": 2,
          "thanhTien": 400
        }
      ],
      "ngayTao": "2026-03-01T10:00:00",
      "ngayCapNhat": null
    }
  ]
}
```

**Kiểu dữ liệu:**

```json
{
  "meta": {
    "page": "Integer",
    "pageSize": "Integer",
    "pages": "Integer",
    "total": "Long"
  },
  "result": [
    {
      "id": "Long",
      "cuaHang": {
        "id": "Long",
        "tenCuaHang": "String"
      },
      "khachHang": {
        "id": "Long",
        "tenKhachHang": "String",
        "sdt": "String",
        "email": "String",
        "diemTichLuy": "Integer"
      },
      "nhanVien": {
        "id": "Long",
        "tenNhanVien": "String",
        "email": "String",
        "soDienThoai": "String"
      },
      "khuyenMaiHoaDon": {
        "id": "Long",
        "tenKhuyenMai": "String",
        "phanTramGiam": "Double",
        "giamToiDa": "Integer",
        "hoaDonToiDa": "Integer",
        "tienDaGiam": "Integer"
      },
      "khuyenMaiDiem": {
        "id": "Long",
        "tenKhuyenMai": "String",
        "phanTramGiam": "Double",
        "giamToiDa": "Integer",
        "hoaDonToiDa": "Integer",
        "diemToiThieu": "Integer",
        "tienDaGiam": "Integer"
      },
      "diaChi": "String",
      "sdt": "String",
      "tongTien": "Integer",
      "tienGiam": "Integer",
      "tongTienGiam": "Integer",
      "tongTienTra": "Integer",
      "trangThai": "String (text mô tả)",
      "trangThaiThanhToan": "String (text mô tả)",
      "hinhThucDonHang": "String (text mô tả)",
      "chiTietDonHangs": [
        {
          "id": "Long",
          "chiTietSanPhamId": "Long",
          "tenSanPham": "String",
          "hinhAnhChinh": "String",
          "tenMauSac": "String",
          "tenKichThuoc": "String",
          "giaSanPham": "Double",
          "giamGia": "Double (% giảm giá)",
          "giaGiam": "Double (số tiền giảm)",
          "soLuong": "Integer",
          "thanhTien": "Double"
        }
      ],
      "ngayTao": "LocalDateTime",
      "ngayCapNhat": "LocalDateTime | null"
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
  "sdt": "0911000001",
  "cuaHangId": 1,
  "maKhuyenMaiHoaDon": null,
  "maKhuyenMaiDiem": null
}
```

**Kiểu dữ liệu:**

```json
{
  "diaChi": "String",
  "sdt": "String",
  "cuaHangId": "Long",
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null"
}
```

| Trường              | Kiểu   | Bắt buộc | Mô tả                            |
| ------------------- | ------ | -------- | -------------------------------- |
| `diaChi`            | String | **Có**   | Địa chỉ giao hàng                |
| `sdt`               | String | Không    | Số điện thoại nhận hàng          |
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

**Kiểu dữ liệu:**

```json
{
  "khachHang": {
    "id": "Long"
  },
  "diaChi": "String",
  "tongTien": "Integer",
  "tienGiam": "Integer",
  "tongTienGiam": "Integer",
  "tongTienTra": "Integer",
  "trangThai": "Integer",
  "trangThaiThanhToan": "Integer",
  "chiTietDonHangs": [
    {
      "chiTietSanPham": {
        "id": "Long"
      },
      "giaSanPham": "Double",
      "giamGia": "Double",
      "giaGiam": "Double",
      "soLuong": "Integer",
      "thanhTien": "Double"
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

**Request Body:** `ReqCapNhatDonHangDTO`

```json
{
  "id": 4,
  "trangThai": 1,
  "diaChi": "789 Trần Hưng Đạo, Q.5, TP.HCM",
  "sdt": "0944000004"
}
```

**Kiểu dữ liệu:**

```json
{
  "id": "Long",
  "trangThai": "Integer (0-5)",
  "diaChi": "String | null",
  "sdt": "String | null"
}
```

| Trường      | Kiểu    | Bắt buộc | Mô tả                                              |
| ----------- | ------- | -------- | -------------------------------------------------- |
| `id`        | Long    | **Có**   | Mã đơn hàng cần cập nhật                           |
| `trangThai` | Integer | Không    | Trạng thái mới (0-5, theo luồng chuyển trạng thái) |
| `diaChi`    | String  | Không    | Địa chỉ mới (chỉ cập nhật khi trangThai < 2)       |
| `sdt`       | String  | Không    | SĐT mới (chỉ cập nhật khi trangThai < 2)           |

**Response:** `200 OK` — Trả về `ResDonHangDTO`

> **Quan trọng - Cộng điểm tích lũy:**
> Khi `trangThai` được chuyển sang **5 (Đã nhận hàng)** từ trạng thái 3:
>
> - Hệ thống tự động tính điểm: `(tongTienTra / 100.000) × 10`
> - Cộng điểm vào `KhachHang.diemTichLuy`

> **Gán nhân viên tự động:**
> Khi nhân viên cập nhật trạng thái đơn hàng online (1→2→3), hệ thống tự động gán nhân viên đang thao tác vào đơn hàng.

**Lỗi:**

| HTTP Status | Mô tả                                                           |
| ----------- | --------------------------------------------------------------- |
| `400`       | Mã đơn hàng không được để trống                                 |
| `400`       | Không tìm thấy đơn hàng                                         |
| `400`       | Không thể chuyển trạng thái từ X sang Y                         |
| `400`       | Không thể cập nhật địa chỉ khi đơn hàng đã đóng gói hoặc đã gửi |

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
