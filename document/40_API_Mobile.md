# API Mobile (loc theo yeu cau)

Tai lieu nay tong hop cac API can cho mobile theo controller hien tai, trinh bay theo format tuong tu 02_SanPham.

---

# Auth Controller

> Base Path: `/api/v1/auth`
> File: `AuthController.java`

---

## 1. Dang nhap (nhan vien / khach hang)

| Thuoc tinh   | Chi tiet                  |
| ------------ | ------------------------- |
| **URL**      | `POST /api/v1/auth/login` |
| **Method**   | `POST`                    |
| **Xac thuc** | Khong can (Public)        |

**Request Body:**

```json
{
  "username": "h@s.com",
  "password": "123456"
}
```

**Kieu du lieu:**

```json
{
  "username": "String",
  "password": "String"
}
```

**Response:** `200 OK` — Tra ve `ResLoginDTO` va set cookie `refresh_token`

```json
{
  "access_token": "eyJhbGciOi...",
  "user": {
    "id": 5,
    "email": "h@s.com",
    "name": "Hung",
    "sdt": "0901000005",
    "avatar": "https://...",
    "role": {
      "id": 1,
      "name": "ADMIN",
      "description": "Nhan vien",
      "active": true
    },
    "diemTichLuy": null,
    "idCuaHang": 3
  }
}
```

**Kieu du lieu:**

```json
{
  "access_token": "String",
  "user": {
    "id": "Long",
    "email": "String",
    "name": "String",
    "sdt": "String",
    "avatar": "String",
    "role": {
      "id": "Long",
      "name": "String",
      "description": "String",
      "active": "Boolean"
    },
    "diemTichLuy": "Integer | null",
    "idCuaHang": "Long | null"
  }
}
```

**Ghi chu:**

- Nhan vien: `idCuaHang` co gia tri, `diemTichLuy` = null.
- Khach hang: `idCuaHang` = null, `diemTichLuy` co gia tri (hoac null neu chua co).
- Response set cookie `refresh_token` (HttpOnly, Secure, Path=/).

````

---

## 2. Lay thong tin tai khoan

| Thuoc tinh   | Chi tiet                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/auth/account` |
| **Method**   | `GET`                      |
| **Xac thuc** | Bearer Token (JWT)         |

**Response:** `200 OK`

```json
{
  "user": {
    "id": 1,
    "email": "lan@g.com",
    "name": "Lan",
    "sdt": "0911000001",
    "avatar": "https://...",
    "role": {
      "id": 4,
      "name": "KHACH_HANG",
      "description": "Khach hang",
      "active": true
    },
    "diemTichLuy": 10,
    "idCuaHang": null
  }
}
````

**Kieu du lieu:**

```json
{
  "user": {
    "id": "Long",
    "email": "String",
    "name": "String",
    "sdt": "String",
    "avatar": "String",
    "role": {
      "id": "Long",
      "name": "String",
      "description": "String",
      "active": "Boolean"
    },
    "diemTichLuy": "Integer | null",
    "idCuaHang": "Long | null"
  }
}
```

---

## 3. Refresh token

| Thuoc tinh   | Chi tiet                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/auth/refresh` |
| **Method**   | `GET`                      |
| **Xac thuc** | Cookie `refresh_token`     |

**Response:** `200 OK` — Tra ve `ResLoginDTO` (giong dang nhap)

```json
{
  "access_token": "eyJhbGciOi...",
  "user": {
    "id": 5,
    "email": "h@s.com",
    "name": "Hung",
    "sdt": "0901000005",
    "avatar": "https://...",
    "role": {
      "id": 1,
      "name": "ADMIN",
      "description": "Nhan vien",
      "active": true
    },
    "diemTichLuy": null,
    "idCuaHang": 3
  }
}
```

**Kieu du lieu:**

```json
{
  "access_token": "String",
  "user": {
    "id": "Long",
    "email": "String",
    "name": "String",
    "sdt": "String",
    "avatar": "String",
    "role": {
      "id": "Long",
      "name": "String",
      "description": "String",
      "active": "Boolean"
    },
    "diemTichLuy": "Integer | null",
    "idCuaHang": "Long | null"
  }
}
```

---

## 4. Dang xuat

| Thuoc tinh   | Chi tiet                   |
| ------------ | -------------------------- |
| **URL**      | `POST /api/v1/auth/logout` |
| **Method**   | `POST`                     |
| **Xac thuc** | Bearer Token (JWT)         |

**Response:** `200 OK` — Body rong

---

# Don Hang Controller

> Base Path: `/api/v1/don-hang`
> File: `DonHangController.java`

---

## 1. Lay danh sach don hang (loc + phan trang)

| Thuoc tinh   | Chi tiet               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/don-hang` |
| **Method**   | `GET`                  |
| **Xac thuc** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham so              | Kieu    | Bat buoc | Mo ta                    |
| -------------------- | ------- | -------- | ------------------------ |
| `cuaHangId`          | Long    | Khong    | Loc theo cua hang        |
| `nhanVienId`         | Long    | Khong    | Loc theo nhan vien       |
| `trangThai`          | Integer | Khong    | Trang thai don hang      |
| `trangThaiThanhToan` | Integer | Khong    | Trang thai thanh toan    |
| `hinhThucDonHang`    | Integer | Khong    | 0 = tai quay, 1 = online |
| `page`               | Integer | Khong    | Trang                    |
| `size`               | Integer | Khong    | Kich thuoc trang         |
| `sort`               | String  | Khong    | VD: `ngayTao,desc`       |

**Response:** `200 OK` — Tra ve `ResultPaginationDTO`

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
      "diaChi": "123 Tran Hung Dao",
      "sdt": "0911000001",
      "tongTien": 600000,
      "tongTienTra": 600000,
      "trangThai": "Da xac nhan",
      "phuongThucThanhToan": "VNPAY",
      "trangThaiThanhToan": "Da thanh toan",
      "hinhThucDonHang": "Online",
      "ngayTao": "2026-03-01T10:00:00"
    }
  ]
}
```

**Kieu du lieu:**

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
      "diaChi": "String",
      "sdt": "String",
      "tenNguoiMua": "String",
      "tongTien": "Integer",
      "tienGiam": "Integer",
      "tongTienGiam": "Integer",
      "tongTienTra": "Integer",
      "paymentRef": "String",
      "phuongThucThanhToan": "String",
      "trangThai": "String",
      "trangThaiThanhToan": "String",
      "hinhThucDonHang": "String",
      "vanChuyen": {
        "id": "Long",
        "tenVanChuyen": "String",
        "soDienThoai": "String",
        "website": "String",
        "ghiTru": "String"
      },
      "khuyenMaiHoaDon": {
        "id": "Long",
        "tenKhuyenMai": "String",
        "phanTramGiam": "Double",
        "giamToiDa": "Integer",
        "hoaDonToiThieu": "Integer",
        "tienDaGiam": "Integer"
      },
      "khuyenMaiDiem": {
        "id": "Long",
        "tenKhuyenMai": "String",
        "phanTramGiam": "Double",
        "giamToiDa": "Integer",
        "hoaDonToiThieu": "Integer",
        "diemToiThieu": "Integer",
        "tienDaGiam": "Integer"
      },
      "ngayTao": "LocalDateTime",
      "ngayCapNhat": "LocalDateTime",
      "cuaHang": {
        "id": "Long",
        "tenCuaHang": "String",
        "diaChi": "String",
        "soDienThoai": "String"
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
      "chiTietDonHangs": [
        {
          "id": "Long",
          "chiTietSanPhamId": "Long",
          "sanPhamId": "Long",
          "tenSanPham": "String",
          "hinhAnhChinh": "String",
          "tenMauSac": "String",
          "tenKichThuoc": "String",
          "giaSanPham": "Double",
          "giamGia": "Double",
          "giaGiam": "Double",
          "soLuong": "Integer",
          "thanhTien": "Double"
        }
      ]
    }
  ]
}
```

---

## 2. Xem chi tiet don hang

| Thuoc tinh   | Chi tiet                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/don-hang/{id}` |
| **Method**   | `GET`                       |
| **Xac thuc** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham so | Kieu | Mo ta       |
| ------- | ---- | ----------- |
| `id`    | Long | Ma don hang |

**Response:** `200 OK` — Tra ve `ResDonHangDTO`

---

## 3. Tao don hang online (khach hang)

| Thuoc tinh   | Chi tiet                       |
| ------------ | ------------------------------ |
| **URL**      | `POST /api/v1/don-hang/online` |
| **Method**   | `POST`                         |
| **Xac thuc** | Bearer Token (JWT)             |

**Request Body:**

```json
{
  "diaChi": "123 Tran Hung Dao",
  "sdt": "0911000001",
  "cuaHangId": 1,
  "vanChuyenId": 2,
  "maKhuyenMaiHoaDon": null,
  "maKhuyenMaiDiem": null,
  "hinhThucDonHang": 1
}
```

**Kieu du lieu:**

```json
{
  "diaChi": "String",
  "sdt": "String",
  "cuaHangId": "Long",
  "vanChuyenId": "Long",
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null",
  "hinhThucDonHang": "Integer"
}
```

**Response:** `201 Created` — Tra ve `ResDonHangDTO`

---

## 4. Tao don hang tai quay (nhan vien)

| Thuoc tinh   | Chi tiet                         |
| ------------ | -------------------------------- |
| **URL**      | `POST /api/v1/don-hang/tai-quay` |
| **Method**   | `POST`                           |
| **Xac thuc** | Bearer Token (JWT)               |

**Request Body:**

```json
{
  "tenNguoiMua": "Tran Van A",
  "sdt": "0911000001",
  "khachHangId": 1,
  "maKhuyenMaiHoaDon": null,
  "maKhuyenMaiDiem": null,
  "hinhThucDonHang": 0,
  "chiTietDonHangs": [
    {
      "chiTietSanPhamId": 10,
      "soLuong": 2
    }
  ]
}
```

**Kieu du lieu:**

```json
{
  "tenNguoiMua": "String",
  "sdt": "String",
  "khachHangId": "Long",
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null",
  "hinhThucDonHang": "Integer",
  "chiTietDonHangs": [
    {
      "chiTietSanPhamId": "Long",
      "soLuong": "Integer"
    }
  ]
}
```

**Response:** `201 Created` — Tra ve `ResDonHangDTO`

---

## 5. Cap nhat don hang

| Thuoc tinh   | Chi tiet               |
| ------------ | ---------------------- |
| **URL**      | `PUT /api/v1/don-hang` |
| **Method**   | `PUT`                  |
| **Xac thuc** | Bearer Token (JWT)     |

**Request Body:**

```json
{
  "id": 1,
  "trangThai": 1,
  "diaChi": "456 Le Loi",
  "sdt": "0911000001",
  "trangThaiThanhToan": 1,
  "hinhThucDonHang": 1,
  "paymentRef": "VNPAY-20260301-001"
}
```

**Kieu du lieu:**

```json
{
  "id": "Long",
  "trangThai": "Integer",
  "diaChi": "String",
  "sdt": "String",
  "trangThaiThanhToan": "Integer",
  "hinhThucDonHang": "Integer",
  "paymentRef": "String"
}
```

**Response:** `200 OK` — Tra ve `ResDonHangDTO`

### Trang thai don hang (trangThai)

#### 0 - Da dat hang

Don moi tao, chua xu ly.

#### 1 - Da nhan don

Nhan vien da tiep nhan don.

#### 2 - Dang dong goi

Don dang duoc dong goi.

#### 3 - Da gui

Don da ban giao cho van chuyen.

#### 4 - Huy

Don bi huy.

#### 5 - Da nhan hang

Khach hang xac nhan da nhan hang.

### Trang thai thanh toan (trangThaiThanhToan)

#### 0 - Chua thanh toan

#### 1 - Da thanh toan

#### 2 - That bai

### Hinh thuc don hang (hinhThucDonHang)

#### 0 - COD/Tien mat

#### 1 - VNPAY

---

## 6. Tao link thanh toan VNPAY cho online (chua tao don)

| Thuoc tinh   | Chi tiet                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `POST /api/v1/don-hang/online/vnpay-url` |
| **Method**   | `POST`                                   |
| **Xac thuc** | Bearer Token (JWT)                       |

**Request Body:** `ReqTaoDonHangDTO` (giong muc 3)

**Response:** `200 OK`

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?..."
}
```

---

# Gio Hang Nhan Vien Controller (tai quay)

> Base Path: `/api/v1/gio-hang-nhan-vien`
> File: `GioHangNhanVienController.java`

---

## 1. Lay danh sach gio hang nhap (chua thanh toan)

| Thuoc tinh   | Chi tiet                                   |
| ------------ | ------------------------------------------ |
| **URL**      | `GET /api/v1/gio-hang-nhan-vien/danh-sach` |
| **Method**   | `GET`                                      |
| **Xac thuc** | Bearer Token (JWT)                         |

**Response:** `200 OK` — Tra ve `List<ResGioHangNhanVienDTO>`

---

## 2. Lay chi tiet gio hang theo ID

| Thuoc tinh   | Chi tiet                              |
| ------------ | ------------------------------------- |
| **URL**      | `GET /api/v1/gio-hang-nhan-vien/{id}` |
| **Method**   | `GET`                                 |
| **Xac thuc** | Bearer Token (JWT)                    |

**Path Parameters:**

| Tham so | Kieu | Mo ta       |
| ------- | ---- | ----------- |
| `id`    | Long | Ma gio hang |

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 3. Xoa gio hang nhap

| Thuoc tinh   | Chi tiet                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `DELETE /api/v1/gio-hang-nhan-vien/{id}` |
| **Method**   | `DELETE`                                 |
| **Xac thuc** | Bearer Token (JWT)                       |

**Response:** `204 No Content`

---

## 4. Tao gio hang moi (bat dau ban tai quay)

| Thuoc tinh   | Chi tiet                              |
| ------------ | ------------------------------------- |
| **URL**      | `POST /api/v1/gio-hang-nhan-vien/moi` |
| **Method**   | `POST`                                |
| **Xac thuc** | Bearer Token (JWT)                    |

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 6. Cap nhat thong tin khach hang cho gio hang

| Thuoc tinh   | Chi tiet                                         |
| ------------ | ------------------------------------------------ |
| **URL**      | `PUT /api/v1/gio-hang-nhan-vien/thong-tin-khach` |
| **Method**   | `PUT`                                            |
| **Xac thuc** | Bearer Token (JWT)                               |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Request Body:**

```json
{
  "tenNguoiMua": "Tran Van A",
  "sdt": "0911000001"
}
```

**Kieu du lieu:**

```json
{
  "tenNguoiMua": "String",
  "sdt": "String"
}
```

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 7. Them san pham vao gio hang

| Thuoc tinh   | Chi tiet                                        |
| ------------ | ----------------------------------------------- |
| **URL**      | `POST /api/v1/gio-hang-nhan-vien/them-san-pham` |
| **Method**   | `POST`                                          |
| **Xac thuc** | Bearer Token (JWT)                              |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Request Body:**

```json
{
  "chiTietSanPhamId": 10,
  "maVach": null,
  "soLuong": 2
}
```

**Kieu du lieu:**

```json
{
  "chiTietSanPhamId": "Long",
  "maVach": "String | null",
  "soLuong": "Integer"
}
```

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 8. Quet ma vach them san pham

| Thuoc tinh   | Chi tiet                                        |
| ------------ | ----------------------------------------------- |
| **URL**      | `POST /api/v1/gio-hang-nhan-vien/them-san-pham` |
| **Method**   | `POST`                                          |
| **Xac thuc** | Bearer Token (JWT)                              |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Request Body:**

```json
{
  "chiTietSanPhamId": null,
  "maVach": "SP3HCTSGCH2",
  "soLuong": 1
}
```

**Kieu du lieu:**

```json
{
  "chiTietSanPhamId": "Long | null",
  "maVach": "String",
  "soLuong": "Integer"
}
```

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

**Ghi chu:** Neu can quet ma vach bang anh, dung API `POST /api/v1/chi-tiet-san-pham/scan-image` (xem muc "Tim san pham theo anh ma vach").

**Request:** `multipart/form-data`

| Tham so | Kieu | Bat buoc | Mo ta       |
| ------- | ---- | -------- | ----------- |
| `file`  | File | Co       | Anh ma vach |

**Response:** `200 OK` — Tra ve `ResChiTietSanPhamDTO`

```json
{
  "id": 1,
  "sanPhamId": 1,
  "maPhieuNhap": null,
  "maCuaHang": 1,
  "tenCuaHang": "Chi nhanh Q1",
  "soLuong": 15,
  "giaBan": 250000,
  "trangThai": 1,
  "moTa": "Ao polo den size M",
  "ghiTru": null,
  "maVach": "SP3HCTSGCH2",
  "tenSanPham": "Ao Polo Classic",
  "tenMauSac": "Den",
  "tenKichThuoc": "M",
  "hinhAnhUrls": ["polo-den-1.jpg", "polo-den-2.jpg"]
}
```

---

## 9. Cap nhat so luong san pham trong gio hang

| Thuoc tinh   | Chi tiet                                       |
| ------------ | ---------------------------------------------- |
| **URL**      | `PUT /api/v1/gio-hang-nhan-vien/chi-tiet/{id}` |
| **Method**   | `PUT`                                          |
| **Xac thuc** | Bearer Token (JWT)                             |

**Path Parameters:**

| Tham so | Kieu | Mo ta                |
| ------- | ---- | -------------------- |
| `id`    | Long | Ma chi tiet gio hang |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Request Body:**

```json
{
  "soLuong": 2
}
```

**Kieu du lieu:**

```json
{
  "soLuong": "Integer"
}
```

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 10. Xoa san pham khoi gio hang

| Thuoc tinh   | Chi tiet                                          |
| ------------ | ------------------------------------------------- |
| **URL**      | `DELETE /api/v1/gio-hang-nhan-vien/chi-tiet/{id}` |
| **Method**   | `DELETE`                                          |
| **Xac thuc** | Bearer Token (JWT)                                |

**Path Parameters:**

| Tham so | Kieu | Mo ta                |
| ------- | ---- | -------------------- |
| `id`    | Long | Ma chi tiet gio hang |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 11. Cap nhat ma khuyen mai cho gio hang

| Thuoc tinh   | Chi tiet                                    |
| ------------ | ------------------------------------------- |
| **URL**      | `PUT /api/v1/gio-hang-nhan-vien/khuyen-mai` |
| **Method**   | `PUT`                                       |
| **Xac thuc** | Bearer Token (JWT)                          |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Request Body:**

```json
{
  "maKhuyenMaiHoaDon": null,
  "maKhuyenMaiDiem": null
}
```

**Kieu du lieu:**

```json
{
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null"
}
```

**Response:** `200 OK` — Tra ve `ResGioHangNhanVienDTO`

---

## 12. Thanh toan tien mat va tao don hang

| Thuoc tinh   | Chi tiet                                     |
| ------------ | -------------------------------------------- |
| **URL**      | `POST /api/v1/gio-hang-nhan-vien/thanh-toan` |
| **Method**   | `POST`                                       |
| **Xac thuc** | Bearer Token (JWT)                           |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Request Body:**

```json
{
  "hinhThucDonHang": 0
}
```

**Kieu du lieu:**

```json
{
  "hinhThucDonHang": "Integer"
}
```

**Response:** `200 OK` — Tra ve `ResDonHangDTO`

---

## 13. Tao link thanh toan VNPAY (chua tao don)

| Thuoc tinh   | Chi tiet                                               |
| ------------ | ------------------------------------------------------ |
| **URL**      | `POST /api/v1/gio-hang-nhan-vien/thanh-toan/vnpay-url` |
| **Method**   | `POST`                                                 |
| **Xac thuc** | Bearer Token (JWT)                                     |

**Query Parameters:**

| Tham so  | Kieu | Bat buoc | Mo ta                |
| -------- | ---- | -------- | -------------------- |
| `cartId` | Long | Khong    | Ma gio hang (neu co) |

**Response:** `200 OK`

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?..."
}
```

**Kieu du lieu:**

```json
{
  "paymentUrl": "String"
}
```

---

# VNPay Controller

> Base Path: `/api/v1/auth/vnpay`
> File: `VNPayController.java`

---

## 1. Tao link thanh toan theo donHangId

| Thuoc tinh   | Chi tiet                                     |
| ------------ | -------------------------------------------- |
| **URL**      | `POST /api/v1/auth/vnpay/create-payment-url` |
| **Method**   | `POST`                                       |
| **Xac thuc** | Bearer Token (JWT)                           |

**Request Body:**

```json
{
  "donHangId": 1001
}
```

**Kieu du lieu:**

```json
{
  "donHangId": "Long"
}
```

**Response:** `200 OK`

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?..."
}
```

**Kieu du lieu:**

```json
{
  "paymentUrl": "String"
}
```

---

## 2. Xu ly return tu VNPAY

| Thuoc tinh   | Chi tiet                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/auth/vnpay/return` |
| **Method**   | `GET`                           |
| **Xac thuc** | Khong can (callback)            |

**Query Parameters:** VNPay return params

**Response:** `200 OK`

```json
{
  "success": "true",
  "vnp_TxnRef": "ONLINE-20260301-001",
  "vnp_TransactionNo": "12345678",
  "donHangId": "1001",
  "paymentRef": "VNPAY-20260301-001"
}
```

**Kieu du lieu:**

```json
{
  "success": "String",
  "vnp_TxnRef": "String",
  "vnp_TransactionNo": "String",
  "donHangId": "String",
  "paymentRef": "String"
}
```

---

# Tra Hang Controller

> Base Path: `/api/v1/tra-hang`
> File: `TraHangController.java`

**Quyen nhan vien:** chi duoc xem va cap nhat trang thai.

---

## 1. Lay danh sach phieu tra hang (phan trang)

| Thuoc tinh   | Chi tiet               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/tra-hang` |
| **Method**   | `GET`                  |
| **Xac thuc** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham so | Kieu    | Bat buoc | Mo ta              |
| ------- | ------- | -------- | ------------------ |
| `page`  | Integer | Khong    | Trang              |
| `size`  | Integer | Khong    | Kich thuoc trang   |
| `sort`  | String  | Khong    | VD: `ngayTao,desc` |

**Response:** `200 OK` — Tra ve `ResultPaginationDTO<ResTraHangDTO>`

---

## 2. Lay phieu tra hang theo id

| Thuoc tinh   | Chi tiet                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/tra-hang/{id}` |
| **Method**   | `GET`                       |
| **Xac thuc** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham so | Kieu | Mo ta             |
| ------- | ---- | ----------------- |
| `id`    | Long | Ma phieu tra hang |

**Response:** `200 OK` — Tra ve `ResTraHangDTO`

---

## 3. Lay phieu tra hang theo don hang

| Thuoc tinh   | Chi tiet                                    |
| ------------ | ------------------------------------------- |
| **URL**      | `GET /api/v1/tra-hang/don-hang/{donHangId}` |
| **Method**   | `GET`                                       |
| **Xac thuc** | Bearer Token (JWT)                          |

**Response:** `200 OK` — Tra ve `List<ResTraHangDTO>`

---

## 4. Cap nhat trang thai phieu tra hang (nhan vien)

| Thuoc tinh   | Chi tiet                               |
| ------------ | -------------------------------------- |
| **URL**      | `PUT /api/v1/tra-hang/{id}/trang-thai` |
| **Method**   | `PUT`                                  |
| **Xac thuc** | Bearer Token (JWT)                     |

**Query Parameters:**

| Tham so     | Kieu    | Bat buoc | Mo ta                     |
| ----------- | ------- | -------- | ------------------------- |
| `trangThai` | Integer | Co       | 1 = Da duyet, 2 = Tu choi |

**Response:** `200 OK` — Tra ve `ResTraHangDTO`

### Trang thai phieu tra hang

#### 0 - Cho xu ly

Phieu moi tao.

#### 1 - Da duyet

Chap nhan tra hang, hoan kho (neu co).

#### 2 - Tu choi

Khong chap nhan tra hang.

### Luong cap nhat trang thai

#### Chi co the cap nhat khi phieu dang o trang thai 0

0 -> 1 hoac 0 -> 2

### Phuong thuc hoan tien (phuongThucHoanTien)

#### 0 - Tien mat

#### 1 - Chuyen khoan

Can co `thongTinChuyenKhoan`.

---

## 5. Tao URL thanh toan VNPAY cho phieu tra hang

| Thuoc tinh   | Chi tiet                               |
| ------------ | -------------------------------------- |
| **URL**      | `POST /api/v1/tra-hang/{id}/vnpay-url` |
| **Method**   | `POST`                                 |
| **Xac thuc** | Bearer Token (JWT)                     |

**Path Parameters:**

| Tham so | Kieu | Mo ta             |
| ------- | ---- | ----------------- |
| `id`    | Long | Ma phieu tra hang |

**Response:** `200 OK`

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?..."
}
```

**Kieu du lieu:**

```json
{
  "paymentUrl": "String"
}
```

---

## 6. Xu ly return tu VNPAY cho phieu tra hang

| Thuoc tinh   | Chi tiet                            |
| ------------ | ----------------------------------- |
| **URL**      | `GET /api/v1/tra-hang/vnpay/return` |
| **Method**   | `GET`                               |
| **Xac thuc** | Khong can (callback)                |

**Query Parameters:** VNPay return params

**Response:** `200 OK`

```json
{
  "success": "true",
  "vnp_TxnRef": "TRH_1001_1716510000000",
  "vnp_TransactionNo": "12345678",
  "traHangId": "1001",
  "paymentRef": "VNPAY-20260301-001"
}
```

**Kieu du lieu:**

```json
{
  "success": "String",
  "vnp_TxnRef": "String",
  "vnp_TransactionNo": "String",
  "traHangId": "String",
  "paymentRef": "String"
}
```

---

# Khach Hang Lookup Controller

> Base Path: `/api/v1/khach-hang`
> File: `KhachHangLookupController.java`

---

## 1. Lay danh sach khach hang

| Thuoc tinh   | Chi tiet                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/khach-hang` |
| **Method**   | `GET`                    |
| **Xac thuc** | Bearer Token (JWT)       |

**Response:** `200 OK` — Tra ve `List<ResKhachHangLookupDTO>`

```json
[
  {
    "id": 1,
    "tenKhachHang": "Lan",
    "sdt": "0911000001",
    "email": "lan@g.com",
    "diemTichLuy": 10
  }
]
```

**Kieu du lieu:**

```json
[
  {
    "id": "Long",
    "tenKhachHang": "String",
    "sdt": "String",
    "email": "String",
    "diemTichLuy": "Integer"
  }
]
```

---

## 2. Tra cuu khach hang theo Sdt

| Thuoc tinh   | Chi tiet                                |
| ------------ | --------------------------------------- |
| **URL**      | `GET /api/v1/khach-hang/lookup?sdt=...` |
| **Method**   | `GET`                                   |
| **Xac thuc** | Bearer Token (JWT)                      |

**Query Parameters:**

| Tham so | Kieu   | Bat buoc | Mo ta         |
| ------- | ------ | -------- | ------------- |
| `sdt`   | String | Co       | So dien thoai |

**Response:** `200 OK` — Tra ve `ResKhachHangLookupDTO` (co the null neu khong tim thay)

---

# San Pham Controller

> Base Path: `/api/v1/san-pham`
> File: `SanPhamController.java`

---

## 1. Lay danh sach san pham (loc + phan trang)

| Thuoc tinh   | Chi tiet               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/san-pham` |
| **Method**   | `GET`                  |
| **Xac thuc** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham so         | Kieu    | Bat buoc | Mo ta                |
| --------------- | ------- | -------- | -------------------- |
| `tenSanPham`    | String  | Khong    | Loc theo ten sp      |
| `kieuSanPhamId` | Long    | Khong    | Loc theo kieu sp     |
| `boSuuTapId`    | Long    | Khong    | Loc theo bo suu tap  |
| `thuongHieuId`  | Long    | Khong    | Loc theo thuong hieu |
| `kichThuocId`   | Long    | Khong    | Loc theo kich thuoc  |
| `mauSacId`      | Long    | Khong    | Loc theo mau sac     |
| `trangThai`     | Integer | Khong    | Loc theo trang thai  |
| `giaMin`        | Double  | Khong    | Gia ban toi thieu    |
| `giaMax`        | Double  | Khong    | Gia ban toi da       |
| `page`          | Integer | Khong    | Trang                |
| `size`          | Integer | Khong    | Kich thuoc trang     |
| `sort`          | String  | Khong    | VD: `giaBan,asc`     |

**Response:** `200 OK` — Tra ve `ResultPaginationDTO`

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
      "tenSanPham": "Ao Polo Classic",
      "giaVon": 120000,
      "giaBan": 250000,
      "giaGiam": 10,
      "hinhAnhChinh": "polo-classic.jpg",
      "moTa": "Ao polo nam",
      "soLuong": 50,
      "trangThai": 1,
      "tenKieuSanPham": "Ao",
      "tenBoSuuTap": "Xuan He 2025",
      "tenThuongHieu": "Nike"
    }
  ]
}
```

**Kieu du lieu:**

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
      "tenSanPham": "String",
      "giaVon": "Double",
      "giaBan": "Double",
      "giaGiam": "Integer",
      "hinhAnhChinh": "String",
      "moTa": "String",
      "soLuong": "Integer",
      "trangThai": "Integer",
      "tenKieuSanPham": "String",
      "tenBoSuuTap": "String",
      "tenThuongHieu": "String"
    }
  ]
}
```

---

## 2. Xem chi tiet san pham

| Thuoc tinh   | Chi tiet                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/san-pham/{id}` |
| **Method**   | `GET`                       |
| **Xac thuc** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham so | Kieu | Mo ta       |
| ------- | ---- | ----------- |
| `id`    | Long | Ma san pham |

**Response:** `200 OK` — Tra ve `ResSanPhamDTO`

```json
{
  "id": 1,
  "tenSanPham": "Ao Polo Classic",
  "giaVon": 120000,
  "giaBan": 250000,
  "giaGiam": 10,
  "hinhAnhChinh": "polo-classic.jpg",
  "moTa": "Ao polo nam",
  "soLuong": 50,
  "trangThai": 1,
  "tenKieuSanPham": "Ao",
  "tenBoSuuTap": "Xuan He 2025",
  "tenThuongHieu": "Nike"
}
```

**Kieu du lieu:**

```json
{
  "id": "Long",
  "tenSanPham": "String",
  "giaVon": "Double",
  "giaBan": "Double",
  "giaGiam": "Integer",
  "hinhAnhChinh": "String",
  "moTa": "String",
  "soLuong": "Integer",
  "trangThai": "Integer",
  "tenKieuSanPham": "String",
  "tenBoSuuTap": "String",
  "tenThuongHieu": "String"
}
```

---

# Chi Tiet San Pham Controller

> Base Path: `/api/v1/chi-tiet-san-pham`
> File: `ChiTietSanPhamController.java`

---

## 1. Lay danh sach chi tiet san pham (loc)

| Thuoc tinh   | Chi tiet                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham` |
| **Method**   | `GET`                           |
| **Xac thuc** | Bearer Token (JWT)              |

**Query Parameters:**

| Tham so       | Kieu    | Bat buoc | Mo ta               |
| ------------- | ------- | -------- | ------------------- |
| `sanPhamId`   | Long    | Khong    | Loc theo san pham   |
| `mauSacId`    | Long    | Khong    | Loc theo mau sac    |
| `kichThuocId` | Long    | Khong    | Loc theo kich thuoc |
| `maCuaHang`   | Long    | Khong    | Loc theo cua hang   |
| `trangThai`   | Integer | Khong    | Loc theo trang thai |

**Response:** `200 OK` — Tra ve `List<ResChiTietSanPhamDTO>`

```json
[
  {
    "id": 1,
    "sanPhamId": 1,
    "maPhieuNhap": null,
    "maCuaHang": 1,
    "tenCuaHang": "Chi nhanh Q1",
    "soLuong": 15,
    "giaBan": 250000,
    "trangThai": 1,
    "moTa": "Ao polo den size M",
    "ghiTru": null,
    "maVach": "8938501234567",
    "tenSanPham": "Ao Polo Classic",
    "tenMauSac": "Den",
    "tenKichThuoc": "M",
    "hinhAnhUrls": ["polo-den-1.jpg", "polo-den-2.jpg"]
  }
]
```

**Kieu du lieu:**

```json
[
  {
    "id": "Long",
    "sanPhamId": "Long",
    "maPhieuNhap": "Long | null",
    "maCuaHang": "Long",
    "tenCuaHang": "String",
    "soLuong": "Integer",
    "giaBan": "Double",
    "trangThai": "Integer",
    "moTa": "String",
    "ghiTru": "String",
    "maVach": "String",
    "tenSanPham": "String",
    "tenMauSac": "String",
    "tenKichThuoc": "String",
    "hinhAnhUrls": "List<String>"
  }
]
```

---

## 2. Xem chi tiet san pham theo id

| Thuoc tinh   | Chi tiet                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/{id}` |
| **Method**   | `GET`                                |
| **Xac thuc** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham so | Kieu | Mo ta                |
| ------- | ---- | -------------------- |
| `id`    | Long | Ma chi tiet san pham |

**Response:** `200 OK` — Tra ve `ResChiTietSanPhamDTO`

---

## 3. Danh sach chi tiet theo san pham

| Thuoc tinh   | Chi tiet                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}` |
| **Method**   | `GET`                                                |
| **Xac thuc** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham so     | Kieu | Mo ta       |
| ----------- | ---- | ----------- |
| `sanPhamId` | Long | Ma san pham |

**Response:** `200 OK` — Tra ve `List<ResChiTietSanPhamDTO>`

---

## 4. Danh sach chi tiet theo cua hang cua nhan vien

| Thuoc tinh   | Chi tiet                                              |
| ------------ | ----------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-san-pham/san-pham-tai-cua-hang` |
| **Method**   | `GET`                                                 |
| **Xac thuc** | Bearer Token (JWT)                                    |

**Response:** `200 OK` — Tra ve `List<ResChiTietSanPhamDTO>`

---

## 5. Tim san pham theo anh ma vach

| Thuoc tinh       | Chi tiet                                    |
| ---------------- | ------------------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-san-pham/scan-image` |
| **Method**       | `POST`                                      |
| **Xac thuc**     | Bearer Token (JWT)                          |
| **Content-Type** | `multipart/form-data`                       |

**Form Data:**

| Tham so | Kieu | Bat buoc | Mo ta       |
| ------- | ---- | -------- | ----------- |
| `file`  | File | Co       | Anh ma vach |

**Response:** `200 OK` — Tra ve `ResChiTietSanPhamDTO`

---

# Van Chuyen Controller

> Base Path: `/api/v1/van-chuyen`
> File: `VanChuyenController.java`

---

## 1. Lay danh sach nha van chuyen

| Thuoc tinh   | Chi tiet                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/van-chuyen` |
| **Method**   | `GET`                    |
| **Xac thuc** | Bearer Token (JWT)       |

**Response:** `200 OK` — Tra ve `List<VanChuyen>`

```json
[
  {
    "id": 1,
    "tenVanChuyen": "GHN",
    "soDienThoai": "0280000000",
    "website": "https://ghn.vn",
    "ghiTru": "",
    "trangThai": 1,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

**Kieu du lieu:**

```json
[
  {
    "id": "Long",
    "tenVanChuyen": "String",
    "soDienThoai": "String",
    "website": "String",
    "ghiTru": "String",
    "trangThai": "Integer",
    "ngayTao": "LocalDateTime",
    "ngayCapNhat": "LocalDateTime"
  }
]
```

---

## 2. Lay nha van chuyen theo id

| Thuoc tinh   | Chi tiet                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/van-chuyen/{id}` |
| **Method**   | `GET`                         |
| **Xac thuc** | Bearer Token (JWT)            |

**Path Parameters:**

| Tham so | Kieu | Mo ta         |
| ------- | ---- | ------------- |
| `id`    | Long | Ma van chuyen |

**Response:** `200 OK` — Tra ve `VanChuyen`

---

# Don Luan Chuyen Controller

> Base Path: `/api/v1/don-luan-chuyen`
> File: `DonLuanChuyenController.java`

---

## 1. Tao don luan chuyen

| Thuoc tinh   | Chi tiet                       |
| ------------ | ------------------------------ |
| **URL**      | `POST /api/v1/don-luan-chuyen` |
| **Method**   | `POST`                         |
| **Xac thuc** | Bearer Token (JWT)             |

**Request Body:**

```json
{
  "cuaHangDatId": 1,
  "cuaHangGuiId": 2,
  "loaiDonLuanChuyenId": 1,
  "tenDon": "LC-20260316-001",
  "ghiTru": "Dieu chuyen hang",
  "chiTietDonLuanChuyens": [
    {
      "chiTietSanPhamId": 101,
      "soLuong": 5,
      "ghiTru": "Uu tien size M"
    }
  ]
}
```

**Kieu du lieu:**

```json
{
  "cuaHangDatId": "Long",
  "cuaHangGuiId": "Long",
  "loaiDonLuanChuyenId": "Long",
  "tenDon": "String",
  "ghiTru": "String",
  "chiTietDonLuanChuyens": [
    {
      "chiTietSanPhamId": "Long",
      "soLuong": "Integer",
      "ghiTru": "String"
    }
  ]
}
```

**Response:** `201 Created` — Tra ve `ResDonLuanChuyenDTO`

---

## 2. Lay danh sach don luan chuyen

| Thuoc tinh   | Chi tiet                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/don-luan-chuyen` |
| **Method**   | `GET`                         |
| **Xac thuc** | Bearer Token (JWT)            |

**Query Parameters:**

| Tham so        | Kieu    | Bat buoc | Mo ta                 |
| -------------- | ------- | -------- | --------------------- |
| `cuaHangGuiId` | Long    | Khong    | Loc theo cua hang gui |
| `cuaHangDatId` | Long    | Khong    | Loc theo cua hang dat |
| `page`         | Integer | Khong    | Trang                 |
| `size`         | Integer | Khong    | Kich thuoc trang      |
| `sort`         | String  | Khong    | VD: `ngayTao,desc`    |

**Response:** `200 OK` — Tra ve `ResultPaginationDTO`

---

## 3. Lay don luan chuyen theo id

| Thuoc tinh   | Chi tiet                           |
| ------------ | ---------------------------------- |
| **URL**      | `GET /api/v1/don-luan-chuyen/{id}` |
| **Method**   | `GET`                              |
| **Xac thuc** | Bearer Token (JWT)                 |

**Path Parameters:**

| Tham so | Kieu | Mo ta              |
| ------- | ---- | ------------------ |
| `id`    | Long | Ma don luan chuyen |

**Response:** `200 OK` — Tra ve `ResDonLuanChuyenDTO`

---

## 4. Lay danh sach theo cua hang dat

| Thuoc tinh   | Chi tiet                                               |
| ------------ | ------------------------------------------------------ |
| **URL**      | `GET /api/v1/don-luan-chuyen/cua-hang-dat/{cuaHangId}` |
| **Method**   | `GET`                                                  |
| **Xac thuc** | Bearer Token (JWT)                                     |

**Response:** `200 OK` — Tra ve `List<ResDonLuanChuyenDTO>`

---

## 5. Lay danh sach theo cua hang gui

| Thuoc tinh   | Chi tiet                                               |
| ------------ | ------------------------------------------------------ |
| **URL**      | `GET /api/v1/don-luan-chuyen/cua-hang-gui/{cuaHangId}` |
| **Method**   | `GET`                                                  |
| **Xac thuc** | Bearer Token (JWT)                                     |

**Response:** `200 OK` — Tra ve `List<ResDonLuanChuyenDTO>`

---

## 6. Cap nhat trang thai

| Thuoc tinh   | Chi tiet                                      |
| ------------ | --------------------------------------------- |
| **URL**      | `PUT /api/v1/don-luan-chuyen/{id}/trang-thai` |
| **Method**   | `PUT`                                         |
| **Xac thuc** | Bearer Token (JWT)                            |

**Query Parameters:**

| Tham so     | Kieu    | Bat buoc | Mo ta          |
| ----------- | ------- | -------- | -------------- |
| `trangThai` | Integer | Co       | Trang thai moi |

**Response:** `200 OK` — Tra ve `ResDonLuanChuyenDTO`

### Trang thai don luan chuyen (trangThai)

#### 0 - Cho xac nhan

Don moi tao, cho cua hang gui xac nhan.

#### 4 - Da xac nhan

Cua hang gui da xac nhan don luan chuyen.

#### 1 - Dang giao

Cua hang gui bat dau giao hang.

#### 2 - Da nhan

Cua hang dat xac nhan da nhan hang.

#### 3 - Tu choi

Don bi tu choi.

### Luong cap nhat trang thai

#### Cua hang gui

0 -> 4 (da xac nhan) hoac 0 -> 3 (tu choi)
4 -> 1 (dang giao) hoac 4 -> 3 (tu choi)

#### Cua hang dat

1 -> 2 (da nhan)

---

# Kiem Ke Hang Hoa Controller

> Base Path: `/api/v1/kiem-ke-hang-hoa`
> File: `KiemKeHangHoaController.java`

---

## 1. Lay danh sach phieu kiem ke

| Thuoc tinh   | Chi tiet                       |
| ------------ | ------------------------------ |
| **URL**      | `GET /api/v1/kiem-ke-hang-hoa` |
| **Method**   | `GET`                          |
| **Xac thuc** | Bearer Token (JWT)             |

**Response:** `200 OK` — Tra ve `List<ResKiemKeHangHoaDTO>`

---

## 2. Xem phieu kiem ke theo id

| Thuoc tinh   | Chi tiet                            |
| ------------ | ----------------------------------- |
| **URL**      | `GET /api/v1/kiem-ke-hang-hoa/{id}` |
| **Method**   | `GET`                               |
| **Xac thuc** | Bearer Token (JWT)                  |

**Path Parameters:**

| Tham so | Kieu | Mo ta            |
| ------- | ---- | ---------------- |
| `id`    | Long | Ma phieu kiem ke |

**Response:** `200 OK` — Tra ve `ResKiemKeHangHoaDTO`

---

## 3. Tao phieu kiem ke

| Thuoc tinh   | Chi tiet                        |
| ------------ | ------------------------------- |
| **URL**      | `POST /api/v1/kiem-ke-hang-hoa` |
| **Method**   | `POST`                          |
| **Xac thuc** | Bearer Token (JWT)              |

**Request Body:**

```json
{
  "tenPhieuKiemKe": "Kiem ke cuoi thang",
  "loaiKiemKeId": 1,
  "cuaHangId": 1,
  "ghiChu": "Kiem ke dinh ky",
  "ngayKiemKe": "2026-03-15T08:00:00",
  "chiTietKiemKes": [
    {
      "chiTietSanPhamId": 1,
      "soLuongThucTe": 120,
      "ghiChu": "Day du"
    }
  ]
}
```

**Kieu du lieu:**

```json
{
  "id": "Long",
  "loaiKiemKeId": "Long",
  "cuaHangId": "Long",
  "tenPhieuKiemKe": "String",
  "ghiChu": "String",
  "ngayKiemKe": "LocalDateTime",
  "chiTietKiemKes": [
    {
      "id": "Long",
      "chiTietSanPhamId": "Long",
      "soLuongThucTe": "Integer",
      "ghiChu": "String"
    }
  ]
}
```

**Response:** `201 Created` — Tra ve `ResKiemKeHangHoaDTO`

---

## 4. Cap nhat phieu kiem ke

| Thuoc tinh   | Chi tiet                       |
| ------------ | ------------------------------ |
| **URL**      | `PUT /api/v1/kiem-ke-hang-hoa` |
| **Method**   | `PUT`                          |
| **Xac thuc** | Bearer Token (JWT)             |

**Request Body:** `ReqKiemKeHangHoaDTO` (giong muc 3, bat buoc co `id`)

**Response:** `200 OK` — Tra ve `ResKiemKeHangHoaDTO`

---

## 5. Gui duyet phieu kiem ke

| Thuoc tinh   | Chi tiet                                      |
| ------------ | --------------------------------------------- |
| **URL**      | `PUT /api/v1/kiem-ke-hang-hoa/{id}/gui-duyet` |
| **Method**   | `PUT`                                         |
| **Xac thuc** | Bearer Token (JWT)                            |

**Response:** `200 OK` — Tra ve `ResKiemKeHangHoaDTO`

---

## 6. Duyet / yeu cau kiem ke lai

| Thuoc tinh   | Chi tiet                                  |
| ------------ | ----------------------------------------- |
| **URL**      | `PUT /api/v1/kiem-ke-hang-hoa/{id}/duyet` |
| **Method**   | `PUT`                                     |
| **Xac thuc** | Bearer Token (JWT)                        |

**Request Body:**

```json
{
  "hanhDong": "XAC_NHAN",
  "lyDo": ""
}
```

**Kieu du lieu:**

```json
{
  "hanhDong": "String",
  "lyDo": "String"
}
```

**Response:** `200 OK` — Tra ve `ResKiemKeHangHoaDTO`

### Trang thai phieu kiem ke

#### 0 - Nhap

Phieu nhap/soan dang dang.

#### 1 - Cho duyet

Phieu da gui duyet.

#### 2 - Yeu cau kiem ke lai

Admin yeu cau kiem ke lai, can sua va gui lai.

#### 3 - Da xac nhan

Admin xac nhan, ton kho duoc cap nhat.

### Luong cap nhat trang thai

#### Gui duyet

0 -> 1, 2 -> 1

#### Duyet

1 -> 2 (YEU_CAU_KIEM_KE_LAI) hoac 1 -> 3 (XAC_NHAN)

---

## 7. Xoa phieu kiem ke

| Thuoc tinh   | Chi tiet                               |
| ------------ | -------------------------------------- |
| **URL**      | `DELETE /api/v1/kiem-ke-hang-hoa/{id}` |
| **Method**   | `DELETE`                               |
| **Xac thuc** | Bearer Token (JWT)                     |

**Path Parameters:**

| Tham so | Kieu | Mo ta            |
| ------- | ---- | ---------------- |
| `id`    | Long | Ma phieu kiem ke |

**Response:** `204 No Content`

---

# Barcode (Code 128) cho mobile

Backend tu dong tao `maVach` cho chi tiet san pham. Mobile chi can hien thi ma nay bang barcode Code 128.

## Cong thuc tao ma

```
maVach = "SP" + base36(sanPhamId).toUpperCase()
         + "CT" + base36(chiTietSanPhamId).toUpperCase()
         + "CH" + base36(cuaHangId).toUpperCase()
```

Neu do dai ma > 32 ky tu, backend cat 32 ky tu dau tien.

## Vi du

```
sanPhamId = 125     -> base36 = 3H
chiTietSanPhamId = 1024 -> base36 = SG
cuaHangId = 2       -> base36 = 2
maVach = SP3HCTSGCH2
```

## Luu y cho mobile

- Lay `maVach` tu API chi tiet san pham (`ResChiTietSanPhamDTO.maVach`).
- Hien thi ma bang barcode Code 128.
- Khi quet anh, backend giai ma Code 128 de tim san pham.
