# ÄÆ¡n HÃ ng Controller

> **Base Path:** `/api/v1/don-hang`  
> **File:** `DonHangController.java`  
> Quáº£n lÃ½ Ä‘Æ¡n hÃ ng: táº¡o Ä‘Æ¡n online (khÃ¡ch hÃ ng), táº¡o Ä‘Æ¡n táº¡i quáº§y (nhÃ¢n viÃªn), lá»c + phÃ¢n trang, cáº­p nháº­t tráº¡ng thÃ¡i.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `DonHang`

| TrÆ°á»ng               | Kiá»ƒu          | MÃ´ táº£                                     |
| -------------------- | ------------- | ----------------------------------------- |
| `id`                 | Long          | MÃ£ Ä‘Æ¡n hÃ ng (auto-increment)              |
| `cuaHang`            | CuaHang       | Cá»­a hÃ ng xá»­ lÃ½ Ä‘Æ¡n                        |
| `khachHang`          | KhachHang     | KhÃ¡ch hÃ ng Ä‘áº·t Ä‘Æ¡n                        |
| `nhanVien`           | NhanVien      | NhÃ¢n viÃªn xá»­ lÃ½ Ä‘Æ¡n                       |
| `maKhuyenMaiHoaDon`  | Long          | MÃ£ khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n (FK, nullable) |
| `maKhuyenMaiDiem`    | Long          | MÃ£ khuyáº¿n mÃ£i theo Ä‘iá»ƒm (FK, nullable)    |
| `diaChi`             | String(255)   | Äá»‹a chá»‰ giao hÃ ng                         |
| `sdt`                | String(255)   | Sá»‘ Ä‘iá»‡n thoáº¡i nháº­n hÃ ng                   |
| `tongTien`           | Integer       | Tá»•ng tiá»n trÆ°á»›c giáº£m (VND)                |
| `tienGiam`           | Integer       | Sá»‘ tiá»n giáº£m (VND)                        |
| `tongTienGiam`       | Integer       | Tá»•ng tiá»n giáº£m (VND)                      |
| `tongTienTra`        | Integer       | Tá»•ng tiá»n pháº£i tráº£ sau giáº£m (VND)         |
| `trangThai`          | Integer       | Tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng (xem báº£ng bÃªn dÆ°á»›i)   |
| `trangThaiThanhToan` | Integer       | Tráº¡ng thÃ¡i thanh toÃ¡n (xem báº£ng bÃªn dÆ°á»›i) |
| `hinhThucDonHang`    | Integer       | HÃ¬nh thá»©c Ä‘Æ¡n hÃ ng (xem báº£ng bÃªn dÆ°á»›i)    |
| `chiTietDonHangs`    | List          | Danh sÃ¡ch chi tiáº¿t Ä‘Æ¡n hÃ ng               |
| `ngayTao`            | LocalDateTime | NgÃ y táº¡o Ä‘Æ¡n (tá»± Ä‘á»™ng)                    |
| `ngayCapNhat`        | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)                   |

### MÃ£ tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng (`trangThai`)

| GiÃ¡ trá»‹ | Ã nghÄ©a          | MÃ´ táº£                                                  |
| ------- | ---------------- | ------------------------------------------------------ |
| `0`     | Chá» xÃ¡c nháº­n     | ÄÆ¡n má»›i táº¡o, chÆ°a Ä‘Æ°á»£c xá»­ lÃ½                           |
| `1`     | ÄÃ£ xÃ¡c nháº­n      | ÄÆ¡n Ä‘Ã£ Ä‘Æ°á»£c nhÃ¢n viÃªn xÃ¡c nháº­n                         |
| `2`     | Äang Ä‘Ã³ng gÃ³i    | ÄÆ¡n Ä‘ang Ä‘Æ°á»£c Ä‘Ã³ng gÃ³i                                 |
| `3`     | Äang giao hÃ ng   | ÄÆ¡n Ä‘ang trong quÃ¡ trÃ¬nh giao                          |
| `4`     | ÄÃ£ há»§y           | ÄÆ¡n bá»‹ há»§y                                             |
| `5`     | **ÄÃ£ nháº­n hÃ ng** | KhÃ¡ch hÃ ng xÃ¡c nháº­n nháº­n hÃ ng â†’ **cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y** |

### Tráº¡ng thÃ¡i thanh toÃ¡n (`trangThaiThanhToan`)

| GiÃ¡ trá»‹ | Ã nghÄ©a         |
| ------- | --------------- |
| `0`     | ChÆ°a thanh toÃ¡n |
| `1`     | ÄÃ£ thanh toÃ¡n   |

### HÃ¬nh thá»©c Ä‘Æ¡n hÃ ng (`hinhThucDonHang`)

| GiÃ¡ trá»‹ | Ã nghÄ©a  |
| ------- | -------- |
| `0`     | Táº¡i quáº§y |
| `1`     | Online   |

### Luá»“ng chuyá»ƒn tráº¡ng thÃ¡i

- **NhÃ¢n viÃªn:** `0 â†’ 1 â†’ 2 â†’ 3` (xÃ¡c nháº­n â†’ Ä‘Ã³ng gÃ³i â†’ gá»­i hÃ ng)
- **KhÃ¡ch hÃ ng:** `3 â†’ 5` (xÃ¡c nháº­n Ä‘Ã£ nháº­n hÃ ng)
- **Há»§y Ä‘Æ¡n:** `0 â†’ 4` hoáº·c `1 â†’ 4` (chá»‰ khi chÆ°a Ä‘Ã³ng gÃ³i)
- **Cáº­p nháº­t Ä‘á»‹a chá»‰/SÄT:** Chá»‰ cho phÃ©p khi `trangThai < 2` (chÆ°a Ä‘Ã³ng gÃ³i)

### Quy táº¯c cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y

Khi cáº­p nháº­t Ä‘Æ¡n hÃ ng vÃ  `trangThai` chuyá»ƒn sang **5 (ÄÃ£ nháº­n hÃ ng)**:

- Há»‡ thá»‘ng tá»± Ä‘á»™ng cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y cho khÃ¡ch hÃ ng.
- **CÃ´ng thá»©c:** `(tongTienTra / 100.000) Ã— 10 Ä‘iá»ƒm`
- VÃ­ dá»¥: ÄÆ¡n 350.000Ä‘ â†’ 30 Ä‘iá»ƒm, Ä‘Æ¡n 1.200.000Ä‘ â†’ 120 Ä‘iá»ƒm.
- Äiá»ƒm chá»‰ Ä‘Æ°á»£c cá»™ng **1 láº§n** khi chuyá»ƒn tráº¡ng thÃ¡i sang 5, khÃ´ng cá»™ng láº¡i náº¿u Ä‘Ã£ á»Ÿ tráº¡ng thÃ¡i 5.

---

## 1. Láº¥y danh sÃ¡ch Ä‘Æ¡n hÃ ng (cÃ³ lá»c + phÃ¢n trang)

| Thuá»™c tÃ­nh   | Chi tiáº¿t               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/don-hang` |
| **Method**   | `GET`                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham sá»‘              | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                                       |
| -------------------- | ------- | -------- | ------------------------------------------- |
| `cuaHangId`          | Long    | KhÃ´ng    | Lá»c theo mÃ£ cá»­a hÃ ng                        |
| `nhanVienId`         | Long    | KhÃ´ng    | Lá»c theo mÃ£ nhÃ¢n viÃªn                       |
| `trangThai`          | Integer | KhÃ´ng    | Lá»c theo tráº¡ng thÃ¡i Ä‘Æ¡n (0-5)               |
| `trangThaiThanhToan` | Integer | KhÃ´ng    | Lá»c theo tráº¡ng thÃ¡i thanh toÃ¡n (0-1)        |
| `hinhThucDonHang`    | Integer | KhÃ´ng    | Lá»c theo hÃ¬nh thá»©c Ä‘Æ¡n (0: quáº§y, 1: online) |
| `page`               | Integer | KhÃ´ng    | Sá»‘ trang (máº·c Ä‘á»‹nh: 0)                      |
| `size`               | Integer | KhÃ´ng    | KÃ­ch thÆ°á»›c trang (máº·c Ä‘á»‹nh: 20)             |
| `sort`               | String  | KhÃ´ng    | Sáº¯p xáº¿p (vd: `ngayTao,desc`)                |

**VÃ­ dá»¥ request:**

```
GET /api/v1/don-hang?trangThai=1&hinhThucDonHang=1&page=0&size=10&sort=ngayTao,desc
```

**Response:** `200 OK` â€” Tráº£ vá» `ResultPaginationDTO`

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
      "cuaHang": { "id": 1, "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1" },
      "khachHang": {
        "id": 1,
        "tenKhachHang": "Lan",
        "sdt": "0911000001",
        "email": "lan@g.com",
        "diemTichLuy": 10
      },
      "nhanVien": {
        "id": 5,
        "tenNhanVien": "HÃ¹ng",
        "email": "h@s.com",
        "soDienThoai": "0901000005"
      },
      "khuyenMaiHoaDon": null,
      "khuyenMaiDiem": null,
      "diaChi": "123 Nguyá»…n TrÃ£i, Q.1, TP.HCM",
      "sdt": "0911000001",
      "tongTien": 600,
      "tienGiam": 0,
      "tongTienGiam": 0,
      "tongTienTra": 600,
      "trangThai": "ÄÃ£ xÃ¡c nháº­n",
      "trangThaiThanhToan": "ÄÃ£ thanh toÃ¡n",
      "hinhThucDonHang": "Online",
      "chiTietDonHangs": [
        {
          "id": 1,
          "chiTietSanPhamId": 1,
          "tenSanPham": "Ão Oxford",
          "hinhAnhChinh": "ao-oxford.jpg",
          "tenMauSac": "Tráº¯ng",
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

**Kiá»ƒu dá»¯ liá»‡u:**

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
      "trangThai": "String (text mÃ´ táº£)",
      "trangThaiThanhToan": "String (text mÃ´ táº£)",
      "hinhThucDonHang": "String (text mÃ´ táº£)",
      "chiTietDonHangs": [
        {
          "id": "Long",
          "chiTietSanPhamId": "Long",
          "tenSanPham": "String",
          "hinhAnhChinh": "String",
          "tenMauSac": "String",
          "tenKichThuoc": "String",
          "giaSanPham": "Double",
          "giamGia": "Double (% giáº£m giÃ¡)",
          "giaGiam": "Double (sá»‘ tiá»n giáº£m)",
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

> **LÆ°u Ã½ phÃ¢n quyá»n:**
>
> - **KhÃ¡ch hÃ ng:** Chá»‰ xem Ä‘Æ°á»£c Ä‘Æ¡n hÃ ng cá»§a mÃ¬nh (há»‡ thá»‘ng tá»± lá»c theo `khachHangId`).
> - **NhÃ¢n viÃªn / Admin:** Xem Ä‘Æ°á»£c táº¥t cáº£ Ä‘Æ¡n hÃ ng, cÃ³ thá»ƒ lá»c tÃ¹y Ã½.

---

## 2. Láº¥y Ä‘Æ¡n hÃ ng theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/don-hang/{id}` |
| **Method**   | `GET`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ Ä‘Æ¡n hÃ ng |

**Response:** `200 OK` â€” Tráº£ vá» `DonHang` (bao gá»“m `chiTietDonHangs`)

**Lá»—i:**

| HTTP Status | MÃ´ táº£                               |
| ----------- | ----------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y Ä‘Æ¡n hÃ ng             |
| `400`       | Báº¡n khÃ´ng cÃ³ quyá»n xem Ä‘Æ¡n hÃ ng nÃ y |

---

## 3. Táº¡o Ä‘Æ¡n hÃ ng online (KhÃ¡ch hÃ ng)

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                      |
| ---------------- | --------------------------------------------- |
| **URL**          | `POST /api/v1/don-hang/online`                |
| **Method**       | `POST`                                        |
| **Content-Type** | `application/json`                            |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n KHACH_HANG |

**Request Body:** `ReqTaoDonHangDTO`

```json
{
  "diaChi": "123 Nguyá»…n TrÃ£i, Q.1, TP.HCM",
  "sdt": "0911000001",
  "cuaHangId": 1,
  "maKhuyenMaiHoaDon": null,
  "maKhuyenMaiDiem": null
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "diaChi": "String",
  "sdt": "String",
  "cuaHangId": "Long",
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null"
}
```

| TrÆ°á»ng              | Kiá»ƒu   | Báº¯t buá»™c | MÃ´ táº£                            |
| ------------------- | ------ | -------- | -------------------------------- |
| `diaChi`            | String | **CÃ³**   | Äá»‹a chá»‰ giao hÃ ng                |
| `sdt`               | String | KhÃ´ng    | Sá»‘ Ä‘iá»‡n thoáº¡i nháº­n hÃ ng          |
| `cuaHangId`         | Long   | KhÃ´ng    | MÃ£ cá»­a hÃ ng xá»­ lÃ½                |
| `maKhuyenMaiHoaDon` | Long   | KhÃ´ng    | MÃ£ khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n       |
| `maKhuyenMaiDiem`   | Long   | KhÃ´ng    | MÃ£ khuyáº¿n mÃ£i theo Ä‘iá»ƒm tÃ­ch lÅ©y |

**Response:** `201 Created` â€” Tráº£ vá» `DonHang`

**Logic xá»­ lÃ½:**

1. Láº¥y thÃ´ng tin khÃ¡ch hÃ ng tá»« JWT token
2. Láº¥y giá» hÃ ng â†’ táº¡o chi tiáº¿t Ä‘Æ¡n hÃ ng
3. TÃ­nh giÃ¡ bÃ¡n, giáº£m giÃ¡, thÃ nh tiá»n
4. Trá»« sá»‘ lÆ°á»£ng tá»“n kho (`ChiTietSanPham.soLuong`)
5. XÃ³a giá» hÃ ng sau khi táº¡o Ä‘Æ¡n thÃ nh cÃ´ng
6. GÃ¡n `hinhThucDonHang = 1`, `trangThai = 0`, `trangThaiThanhToan = 0`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                     |
| ----------- | ------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y khÃ¡ch hÃ ng |
| `400`       | Giá» hÃ ng trá»‘ng            |

---

## 4. Táº¡o Ä‘Æ¡n hÃ ng táº¡i quáº§y (NhÃ¢n viÃªn)

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                     |
| ---------------- | -------------------------------------------- |
| **URL**          | `POST /api/v1/don-hang/tai-quay`             |
| **Method**       | `POST`                                       |
| **Content-Type** | `application/json`                           |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n NHAN_VIEN |

**Request Body:**

```json
{
  "khachHang": { "id": 1 },
  "diaChi": "Mua táº¡i cá»­a hÃ ng",
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

**Kiá»ƒu dá»¯ liá»‡u:**

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

**Lá»—i:**

| HTTP Status | MÃ´ táº£                    |
| ----------- | ------------------------ |
| `400`       | KhÃ´ng tÃ¬m tháº¥y nhÃ¢n viÃªn |

---

## 5. Cáº­p nháº­t Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh       | Chi tiáº¿t               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/don-hang` |
| **Method**       | `PUT`                  |
| **Content-Type** | `application/json`     |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)     |

**Request Body:** `ReqCapNhatDonHangDTO`

```json
{
  "id": 4,
  "trangThai": 1,
  "diaChi": "789 Tráº§n HÆ°ng Äáº¡o, Q.5, TP.HCM",
  "sdt": "0944000004"
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "trangThai": "Integer (0-5)",
  "diaChi": "String | null",
  "sdt": "String | null"
}
```

| TrÆ°á»ng      | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                                              |
| ----------- | ------- | -------- | -------------------------------------------------- |
| `id`        | Long    | **CÃ³**   | MÃ£ Ä‘Æ¡n hÃ ng cáº§n cáº­p nháº­t                           |
| `trangThai` | Integer | KhÃ´ng    | Tráº¡ng thÃ¡i má»›i (0-5, theo luá»“ng chuyá»ƒn tráº¡ng thÃ¡i) |
| `diaChi`    | String  | KhÃ´ng    | Äá»‹a chá»‰ má»›i (chá»‰ cáº­p nháº­t khi trangThai < 2)       |
| `sdt`       | String  | KhÃ´ng    | SÄT má»›i (chá»‰ cáº­p nháº­t khi trangThai < 2)           |

**Response:** `200 OK` â€” Tráº£ vá» `ResDonHangDTO`

> **Quan trá»ng - Cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y:**
> Khi `trangThai` Ä‘Æ°á»£c chuyá»ƒn sang **5 (ÄÃ£ nháº­n hÃ ng)** tá»« tráº¡ng thÃ¡i 3:
>
> - Há»‡ thá»‘ng tá»± Ä‘á»™ng tÃ­nh Ä‘iá»ƒm: `(tongTienTra / 100.000) Ã— 10`
> - Cá»™ng Ä‘iá»ƒm vÃ o `KhachHang.diemTichLuy`

> **GÃ¡n nhÃ¢n viÃªn tá»± Ä‘á»™ng:**
> Khi nhÃ¢n viÃªn cáº­p nháº­t tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng online (1â†’2â†’3), há»‡ thá»‘ng tá»± Ä‘á»™ng gÃ¡n nhÃ¢n viÃªn Ä‘ang thao tÃ¡c vÃ o Ä‘Æ¡n hÃ ng.

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                           |
| ----------- | --------------------------------------------------------------- |
| `400`       | MÃ£ Ä‘Æ¡n hÃ ng khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng                                 |
| `400`       | KhÃ´ng tÃ¬m tháº¥y Ä‘Æ¡n hÃ ng                                         |
| `400`       | KhÃ´ng thá»ƒ chuyá»ƒn tráº¡ng thÃ¡i tá»« X sang Y                         |
| `400`       | KhÃ´ng thá»ƒ cáº­p nháº­t Ä‘á»‹a chá»‰ khi Ä‘Æ¡n hÃ ng Ä‘Ã£ Ä‘Ã³ng gÃ³i hoáº·c Ä‘Ã£ gá»­i |

---

## 6. XÃ³a Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/don-hang/{id}` |
| **Method**   | `DELETE`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)             |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ Ä‘Æ¡n hÃ ng |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y Ä‘Æ¡n hÃ ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem)       | POST Online | POST Táº¡i quáº§y | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------------- | ----------- | ------------- | --------- | ------------ |
| ADMIN      | âœ… Táº¥t cáº£       | âŒ          | âœ…            | âœ…        | âœ…           |
| NHAN_VIEN  | âœ… Táº¥t cáº£       | âŒ          | âœ…            | âŒ        | âŒ           |
| KHACH_HANG | âœ… Chá»‰ Ä‘Æ¡n mÃ¬nh | âœ…          | âŒ            | âŒ        | âŒ           |


