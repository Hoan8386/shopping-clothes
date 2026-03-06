# Phiếu Nhập Controller

> **Base Path:** `/api/v1/phieu-nhap`  
> **File:** `PhieuNhapController.java`  
> Quản lý phiếu nhập hàng từ nhà cung cấp.

---

## Tổng quan

### Cấu trúc dữ liệu `PhieuNhap`

| Trường              | Kiểu          | Mô tả                          |
| ------------------- | ------------- | ------------------------------ |
| `id`                | Long          | Mã phiếu nhập (auto-increment) |
| `cuaHang`           | CuaHang       | Cửa hàng nhập hàng (FK)        |
| `nhaCungCap`        | NhaCungCap    | Nhà cung cấp (FK)              |
| `tenPhieuNhap`      | String(255)   | Tên phiếu nhập                 |
| `trangThai`         | Integer       | Trạng thái phiếu nhập          |
| `ngayDatHang`       | LocalDateTime | Ngày đặt hàng                  |
| `ngayNhanHang`      | LocalDateTime | Ngày thực nhận hàng            |
| `chiTietPhieuNhaps` | List          | Danh sách chi tiết phiếu nhập  |
| `ngayTao`           | LocalDateTime | Ngày tạo (tự động)             |
| `ngayCapNhat`       | LocalDateTime | Ngày cập nhật (tự động)        |

### Mã trạng thái phiếu nhập (`trangThai`)

| Giá trị | Ý nghĩa        | Mô tả                                          |
| ------- | -------------- | ---------------------------------------------- |
| `0`     | Đã đặt         | Phiếu nhập mới tạo, chờ nhà cung cấp giao hàng |
| `1`     | Đã nhận        | Hàng đã được nhận, chờ kiểm kê                 |
| `2`     | Chậm giao      | Nhà cung cấp giao chậm                         |
| `3`     | Hủy            | Phiếu nhập bị hủy                              |
| `4`     | Thiếu hàng     | Kiểm kê xong, có ít nhất 1 chi tiết thiếu hàng |
| `5`     | **Hoàn thành** | Kiểm kê xong, tất cả chi tiết đầy đủ           |

### Luồng trạng thái

```
Tạo phiếu → 0 (Đã đặt)
  ├── Cập nhật → 1 (Đã nhận) → Kiểm kê → 4 (Thiếu hàng) hoặc 5 (Hoàn thành)
  ├── Cập nhật → 2 (Chậm giao) → 1 (Đã nhận) → Kiểm kê
  └── Cập nhật → 3 (Hủy) ← Không thể cập nhật sau khi hủy
```

**Quy tắc:**

- Phiếu đã **hủy** (3) → không thể cập nhật
- Phiếu đã **nhận/thiếu hàng/hoàn thành** (1, 4, 5) → không thể thay đổi trạng thái
- Khi chuyển sang **Đã nhận** (1), `ngayNhanHang` tự động được gán

---

## Response DTO: `ResPhieuNhapDTO`

```json
{
  "id": 1,
  "tenPhieuNhap": "Nhập hàng đợt 1 - CN Q.1",
  "trangThai": 1,
  "trangThaiText": "Đã nhận",
  "ngayDatHang": "2026-03-01T10:00:00",
  "ngayNhanHang": "2026-03-03T10:00:00",
  "ngayTao": "2026-02-28T10:00:00",
  "ngayCapNhat": "2026-03-03T10:00:00",
  "cuaHang": {
    "id": 1,
    "tenCuaHang": "Chi nhánh Quận 1",
    "diaChi": "123 Nguyễn Huệ, Q.1, TP.HCM",
    "soDienThoai": "02812345678"
  },
  "nhaCungCap": {
    "id": 1,
    "tenNhaCungCap": "Công ty TNHH Vải Việt",
    "soDienThoai": "02838001001",
    "email": "vaiviet@ncc.com"
  },
  "chiTietPhieuNhaps": [
    {
      "id": 1,
      "chiTietSanPhamId": 1,
      "tenSanPham": "Áo Oxford",
      "tenMauSac": "Trắng",
      "tenKichThuoc": "M",
      "soLuong": 50,
      "soLuongThieu": null,
      "ghiTru": null,
      "ghiTruKiemHang": null,
      "trangThai": 0,
      "trangThaiText": "Đủ"
    }
  ]
}
```

---

## 1. Lấy danh sách phiếu nhập (có lọc + phân trang)

| Thuộc tính   | Chi tiết                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/phieu-nhap` |
| **Method**   | `GET`                    |
| **Xác thực** | Bearer Token (JWT)       |

**Query Parameters:**

| Tham số           | Kiểu          | Bắt buộc | Mô tả                        |
| ----------------- | ------------- | -------- | ---------------------------- |
| `tenPhieuNhap`    | String        | Không    | Lọc theo tên phiếu nhập      |
| `trangThai`       | Integer       | Không    | Lọc theo trạng thái (0-5)    |
| `tenCuaHang`      | String        | Không    | Lọc theo tên cửa hàng        |
| `tenNhaCungCap`   | String        | Không    | Lọc theo tên nhà cung cấp    |
| `ngayTaoTu`       | LocalDateTime | Không    | Ngày tạo từ                  |
| `ngayTaoDen`      | LocalDateTime | Không    | Ngày tạo đến                 |
| `ngayDatHangTu`   | LocalDateTime | Không    | Ngày đặt hàng từ             |
| `ngayDatHangDen`  | LocalDateTime | Không    | Ngày đặt hàng đến            |
| `ngayNhanHangTu`  | LocalDateTime | Không    | Ngày nhận hàng từ            |
| `ngayNhanHangDen` | LocalDateTime | Không    | Ngày nhận hàng đến           |
| `page`            | Integer       | Không    | Số trang (mặc định: 0)       |
| `size`            | Integer       | Không    | Kích thước trang             |
| `sort`            | String        | Không    | Sắp xếp (vd: `ngayTao,desc`) |

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

```json
{
  "meta": {
    "page": 1,
    "pageSize": 20,
    "pages": 1,
    "total": 3
  },
  "result": [...]
}
```

---

## 2. Lấy phiếu nhập theo ID

| Thuộc tính   | Chi tiết                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/phieu-nhap/{id}` |
| **Method**   | `GET`                         |
| **Xác thực** | Bearer Token (JWT)            |

**Response:** `200 OK` — Trả về `ResPhieuNhapDTO`

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy phiếu nhập |

---

## 3. Tạo phiếu nhập

| Thuộc tính       | Chi tiết                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/phieu-nhap` |
| **Method**       | `POST`                    |
| **Content-Type** | `application/json`        |
| **Xác thực**     | Bearer Token (JWT)        |

**Request Body:** `ReqPhieuNhapDTO`

```json
{
  "tenPhieuNhap": "Nhập hàng tháng 3",
  "cuaHangId": 1,
  "nhaCungCapId": 1
}
```

**Kiểu dữ liệu:**

```json
{
  "tenPhieuNhap": "String",
  "cuaHangId": "Long",
  "nhaCungCapId": "Long"
}
```

**Logic:**

- `trangThai` tự động gán = `0` (Đã đặt)
- `ngayDatHang` tự động gán = thời điểm hiện tại

**Response:** `201 Created` — Trả về `ResPhieuNhapDTO`

**Lỗi:**

| HTTP Status | Mô tả                       |
| ----------- | --------------------------- |
| `400`       | Không tìm thấy cửa hàng     |
| `400`       | Không tìm thấy nhà cung cấp |

---

## 4. Cập nhật phiếu nhập

| Thuộc tính       | Chi tiết                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/phieu-nhap` |
| **Method**       | `PUT`                    |
| **Content-Type** | `application/json`       |
| **Xác thực**     | Bearer Token (JWT)       |

**Request Body:** `ReqPhieuNhapDTO` (phải có `id`)

```json
{
  "id": 1,
  "tenPhieuNhap": "Nhập hàng đợt 1 (cập nhật)",
  "cuaHangId": 1,
  "nhaCungCapId": 1,
  "trangThai": 1
}
```

**Kiểu dữ liệu:**

```json
{
  "id": "Long (bắt buộc)",
  "tenPhieuNhap": "String",
  "cuaHangId": "Long",
  "nhaCungCapId": "Long",
  "trangThai": "Integer (0-5)"
}
```

**Logic:**

- Khi chuyển sang **Đã nhận** (1): `ngayNhanHang` tự động gán
- Không cho cập nhật phiếu đã **Hủy** (3)
- Không cho thay đổi trạng thái khi đã **Đã nhận/Thiếu hàng/Hoàn thành** (1, 4, 5)

**Response:** `200 OK` — Trả về `ResPhieuNhapDTO`

**Lỗi:**

| HTTP Status | Mô tả                                                  |
| ----------- | ------------------------------------------------------ |
| `400`       | Mã phiếu nhập không được để trống                      |
| `400`       | Không tìm thấy phiếu nhập                              |
| `400`       | Phiếu nhập đã hủy, không thể cập nhật                  |
| `400`       | Phiếu nhập đã nhận hàng, không thể thay đổi trạng thái |
| `400`       | Trạng thái không hợp lệ                                |

---

## 5. Kiểm kê phiếu nhập

| Thuộc tính   | Chi tiết                              |
| ------------ | ------------------------------------- |
| **URL**      | `PUT /api/v1/phieu-nhap/kiem-ke/{id}` |
| **Method**   | `PUT`                                 |
| **Xác thực** | Bearer Token (JWT)                    |

**Path Parameters:**

| Tham số | Kiểu | Mô tả         |
| ------- | ---- | ------------- |
| `id`    | Long | Mã phiếu nhập |

**Điều kiện:** Phiếu nhập phải ở trạng thái **Đã nhận** (1).

**Logic kiểm kê:**

1. Duyệt từng chi tiết phiếu nhập:
   - `trangThai = 0` (đủ hàng): số lượng thực nhập = `soLuong`
   - `trangThai = 1` (thiếu hàng): số lượng thực nhập = `soLuong - soLuongThieu`
2. Cập nhật `ChiTietSanPham.soLuong` += số lượng thực nhập
3. Gán `ChiTietSanPham.maPhieuNhap` và `ChiTietSanPham.maCuaHang`
4. Tính lại tổng `SanPham.soLuong` = tổng soLuong của tất cả ChiTietSanPham
5. Kết quả:
   - Có ít nhất 1 chi tiết thiếu → phiếu nhập = **4 (Thiếu hàng)**
   - Tất cả đủ → phiếu nhập = **5 (Hoàn thành)**

**Response:** `200 OK` — Trả về `ResPhieuNhapDTO`

**Lỗi:**

| HTTP Status | Mô tả                                                |
| ----------- | ---------------------------------------------------- |
| `400`       | Không tìm thấy phiếu nhập                            |
| `400`       | Chỉ có thể kiểm kê phiếu nhập ở trạng thái 'Đã nhận' |

---

## 6. Xóa phiếu nhập

| Thuộc tính   | Chi tiết                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/phieu-nhap/{id}` |
| **Method**   | `DELETE`                         |
| **Xác thực** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `400`       | Không tìm thấy phiếu nhập |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | PUT (Kiểm kê) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅            | ✅           |
| NHAN_VIEN  | ✅        | ✅         | ✅        | ✅            | ❌           |
| KHACH_HANG | ❌        | ❌         | ❌        | ❌            | ❌           |

---

## Hướng dẫn test luồng nhập hàng

> **Điều kiện tiên quyết:** Đảm bảo đã có dữ liệu **Cửa hàng**, **Nhà cung cấp**, và **Chi tiết sản phẩm** trong DB (có thể chạy file `insert_data.sql`).

### Bước 1: Đăng nhập lấy Token

```
POST /api/v1/auth/login
```

```json
{
  "username": "lan@g.com",
  "password": "123456"
}
```

→ Copy `access_token` từ response, dùng cho tất cả request sau:  
`Authorization: Bearer <access_token>`

---

### Bước 2: Tạo phiếu nhập

```
POST /api/v1/phieu-nhap
```

```json
{
  "tenPhieuNhap": "Nhập hàng tháng 3",
  "cuaHangId": 1,
  "nhaCungCapId": 1
}
```

→ Hệ thống tự gán `trangThai = 0` (Đã đặt) và `ngayDatHang` = thời điểm hiện tại.  
→ Ghi nhớ `id` phiếu nhập trả về (ví dụ: `id = 1`).

---

### Bước 3: Thêm chi tiết phiếu nhập (từng dòng sản phẩm)

```
POST /api/v1/chi-tiet-phieu-nhap
```

```json
{
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "trangThai": 0
}
```

- `chiTietSanPhamId`: ID biến thể sản phẩm (ví dụ: Áo Oxford - Trắng - M)
- `trangThai = 0`: đủ hàng | `trangThai = 1`: thiếu hàng
- Gọi nhiều lần nếu phiếu nhập có nhiều sản phẩm

---

### Bước 4: Cập nhật trạng thái → "Đã nhận" (1)

```
PUT /api/v1/phieu-nhap
```

```json
{
  "id": 1,
  "tenPhieuNhap": "Nhập hàng tháng 3",
  "cuaHangId": 1,
  "nhaCungCapId": 1,
  "trangThai": 1
}
```

→ Hệ thống tự gán `ngayNhanHang`. Phiếu chuyển sang trạng thái **Đã nhận**.

---

### Bước 5 (tuỳ chọn): Cập nhật chi tiết nếu có thiếu hàng

```
PUT /api/v1/chi-tiet-phieu-nhap
```

```json
{
  "id": 1,
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "soLuongThieu": 5,
  "ghiTruKiemHang": "Thiếu 5 cái do hư hỏng",
  "trangThai": 1
}
```

---

### Bước 6: Kiểm kê (nhập kho thực tế)

```
PUT /api/v1/phieu-nhap/kiem-ke/1
```

→ Hệ thống sẽ:

1. Cộng số lượng thực nhập (`soLuong - soLuongThieu`) vào tồn kho `ChiTietSanPham`
2. Gán `maPhieuNhap` và `maCuaHang` cho `ChiTietSanPham`
3. Tính lại tổng `SanPham.soLuong`
4. Cập nhật trạng thái phiếu:
   - **5 (Hoàn thành)** nếu tất cả chi tiết đủ hàng
   - **4 (Thiếu hàng)** nếu có ít nhất 1 chi tiết thiếu

---

### Tóm tắt luồng

```
Login → Tạo phiếu nhập (0-Đã đặt)
      → Thêm chi tiết sản phẩm
      → Cập nhật trạng thái (1-Đã nhận)
      → (Tuỳ chọn) Cập nhật chi tiết nếu thiếu hàng
      → Kiểm kê → (4-Thiếu hàng) hoặc (5-Hoàn thành)
```
