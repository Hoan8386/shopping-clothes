# Giá» HÃ ng Controller

> **Base Path:** `/api/v1/gio-hang`  
> **File:** `GioHangController.java`  
> Quáº£n lÃ½ giá» hÃ ng cá»§a khÃ¡ch hÃ ng: thÃªm sáº£n pháº©m, xem giá», xÃ³a sáº£n pháº©m.

---

## Tá»•ng quan

Má»—i khÃ¡ch hÃ ng cÃ³ **1 giá» hÃ ng duy nháº¥t** (quan há»‡ 1-1 vá»›i `KhachHang`). Giá» hÃ ng chá»©a danh sÃ¡ch `ChiTietGioHang` (cÃ¡c biáº¿n thá»ƒ sáº£n pháº©m Ä‘Ã£ thÃªm).

### Cáº¥u trÃºc dá»¯ liá»‡u `GioHang`

| TrÆ°á»ng            | Kiá»ƒu                 | MÃ´ táº£                              |
| ----------------- | -------------------- | ---------------------------------- |
| `maGioHang`       | Long                 | MÃ£ giá» hÃ ng (auto-increment)       |
| `khachHang`       | KhachHang            | KhÃ¡ch hÃ ng sá»Ÿ há»¯u giá» (FK, unique) |
| `ngayTao`         | LocalDateTime        | NgÃ y táº¡o (tá»± Ä‘á»™ng)                 |
| `chiTietGioHangs` | List<ChiTietGioHang> | Danh sÃ¡ch sáº£n pháº©m trong giá»       |

---

## 1. ThÃªm sáº£n pháº©m vÃ o giá» hÃ ng

| Thuá»™c tÃ­nh       | Chi tiáº¿t                              |
| ---------------- | ------------------------------------- |
| **URL**          | `POST /api/v1/gio-hang/them-san-pham` |
| **Method**       | `POST`                                |
| **Content-Type** | `application/json`                    |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” KHACH_HANG       |

**Request Body:** `ReqThemGioHangDTO`

```json
{
  "maChiTietSanPham": 1,
  "soLuong": 2
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "maChiTietSanPham": "Long",
  "soLuong": "Integer"
}
```

| TrÆ°á»ng             | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                |
| ------------------ | ------- | -------- | -------------------- |
| `maChiTietSanPham` | Long    | **CÃ³**   | MÃ£ biáº¿n thá»ƒ sáº£n pháº©m |
| `soLuong`          | Integer | **CÃ³**   | Sá»‘ lÆ°á»£ng cáº§n thÃªm    |

**Response:** `201 Created` â€” Tráº£ vá» `ChiTietGioHang`

> **LÆ°u Ã½:** Náº¿u sáº£n pháº©m Ä‘Ã£ cÃ³ trong giá» â†’ tÄƒng sá»‘ lÆ°á»£ng. Náº¿u chÆ°a cÃ³ â†’ táº¡o dÃ²ng má»›i.

---

## 2. Láº¥y giá» hÃ ng cá»§a tÃ´i

| Thuá»™c tÃ­nh   | Chi tiáº¿t                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/gio-hang/cua-toi`  |
| **Method**   | `GET`                           |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” KHACH_HANG |

**Response:** `200 OK` â€” Tráº£ vá» `ResGioHangDTO`

```json
{
  "maGioHang": 1,
  "tongSoLuong": 3,
  "tongTien": 750000,
  "chiTietGioHangs": [
    {
      "maChiTietGioHang": 1,
      "maChiTietSanPham": 1,
      "tenSanPham": "Ão Polo Classic",
      "kichThuoc": "M",
      "mauSac": "Äen",
      "giaBan": 250000,
      "soLuong": 2,
      "thanhTien": 500000
    }
  ]
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "maGioHang": "Long",
  "tongSoLuong": "Integer",
  "tongTien": "Double",
  "chiTietGioHangs": [
    {
      "maChiTietGioHang": "Long",
      "maChiTietSanPham": "Long",
      "tenSanPham": "String",
      "kichThuoc": "String",
      "mauSac": "String",
      "giaBan": "Double",
      "soLuong": "Integer",
      "thanhTien": "Double"
    }
  ]
}
```

> **LÆ°u Ã½:** Há»‡ thá»‘ng tá»± xÃ¡c Ä‘á»‹nh khÃ¡ch hÃ ng tá»« JWT token (SecurityContext).

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y giá» hÃ ng |

---

## 3. XÃ³a sáº£n pháº©m khá»i giá» hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                              |
| ------------ | ----------------------------------------------------- |
| **URL**      | `DELETE /api/v1/gio-hang/chi-tiet/{maChiTietGioHang}` |
| **Method**   | `DELETE`                                              |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” KHACH_HANG                       |

**Path Parameters:**

| Tham sá»‘            | Kiá»ƒu | MÃ´ táº£                        |
| ------------------ | ---- | ---------------------------- |
| `maChiTietGioHang` | Long | MÃ£ chi tiáº¿t giá» hÃ ng cáº§n xÃ³a |

**Response:** `204 No Content`

---

## 4. Láº¥y danh sÃ¡ch khuyáº¿n mÃ£i há»£p lá»‡ cho giá» hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/gio-hang/khuyen-mai-hop-le` |
| **Method**   | `GET`                                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” KHACH_HANG          |

**Response:** `200 OK` â€” Tráº£ vá» `ResKhuyenMaiHopLeDTO`

```json
{
  "khuyenMaiHoaDon": [
    {
      "id": 1,
      "tenKhuyenMai": "Giáº£m 10% cho Ä‘Æ¡n tá»« 500k",
      "hoaDonToiThieu": 500000,
      "phanTramGiam": 10,
      "giamToiDa": 100000,
      "trangThai": 1
    }
  ],
  "khuyenMaiDiem": [
    {
      "id": 1,
      "tenKhuyenMai": "Äá»•i 50 Ä‘iá»ƒm giáº£m 15%",
      "diemToiThieu": 50,
      "hoaDonToiThieu": 300000,
      "phanTramGiam": 15,
      "giamToiDa": 200000,
      "trangThai": 1
    }
  ]
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "khuyenMaiHoaDon": "List<KhuyenMaiTheoHoaDon>",
  "khuyenMaiDiem": "List<KhuyenMaiTheoDiem>"
}
```

> **LÆ°u Ã½:** Há»‡ thá»‘ng tá»± lá»c chá»‰ tráº£ vá» cÃ¡c khuyáº¿n mÃ£i:
>
> - CÃ²n háº¡n (thá»i gian hiá»‡n táº¡i náº±m trong `thoiGianBatDau` â€“ `thoiGianKetThuc`)
> - Äá»§ Ä‘iá»u kiá»‡n (tá»•ng tiá»n giá» hÃ ng â‰¥ `hoaDonToiThieu`, Ä‘iá»ƒm tÃ­ch lÅ©y â‰¥ `diemToiThieu`)

---

## 5. Xem trÆ°á»›c giáº£m giÃ¡ khi Ã¡p dá»¥ng khuyáº¿n mÃ£i

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                   |
| ---------------- | ------------------------------------------ |
| **URL**          | `POST /api/v1/gio-hang/ap-dung-khuyen-mai` |
| **Method**       | `POST`                                     |
| **Content-Type** | `application/json`                         |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” KHACH_HANG            |

**Request Body:** `ReqApDungKhuyenMaiDTO`

```json
{
  "maKhuyenMaiHoaDon": 1,
  "maKhuyenMaiDiem": 2
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "maKhuyenMaiHoaDon": "Long | null",
  "maKhuyenMaiDiem": "Long | null"
}
```

| TrÆ°á»ng              | Kiá»ƒu | Báº¯t buá»™c | MÃ´ táº£                      |
| ------------------- | ---- | -------- | -------------------------- |
| `maKhuyenMaiHoaDon` | Long | KhÃ´ng    | MÃ£ khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n |
| `maKhuyenMaiDiem`   | Long | KhÃ´ng    | MÃ£ khuyáº¿n mÃ£i theo Ä‘iá»ƒm    |

**Response:** `200 OK` â€” Tráº£ vá» `ResApDungKhuyenMaiDTO`

```json
{
  "tongTienGoc": 750000,
  "maKhuyenMaiHoaDon": 1,
  "tenKhuyenMaiHoaDon": "Giáº£m 10% cho Ä‘Æ¡n tá»« 500k",
  "tienGiamHoaDon": 75000,
  "maKhuyenMaiDiem": 2,
  "tenKhuyenMaiDiem": "Äá»•i 50 Ä‘iá»ƒm giáº£m 15%",
  "tienGiamDiem": 101250,
  "tongTienGiam": 176250,
  "tongTienTra": 573750
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "tongTienGoc": "Integer",
  "maKhuyenMaiHoaDon": "Long | null",
  "tenKhuyenMaiHoaDon": "String | null",
  "tienGiamHoaDon": "Integer | null",
  "maKhuyenMaiDiem": "Long | null",
  "tenKhuyenMaiDiem": "String | null",
  "tienGiamDiem": "Integer | null",
  "tongTienGiam": "Integer",
  "tongTienTra": "Integer"
}
```

> **LÆ°u Ã½:** Endpoint nÃ y chá»‰ **xem trÆ°á»›c** káº¿t quáº£ giáº£m giÃ¡, khÃ´ng thá»±c sá»± táº¡o Ä‘Æ¡n hÃ ng. KhÃ¡ch hÃ ng dÃ¹ng thÃ´ng tin nÃ y Ä‘á»ƒ xÃ¡c nháº­n trÆ°á»›c khi Ä‘áº·t hÃ ng.

---

## PhÃ¢n quyá»n

| Vai trÃ²    | ThÃªm SP | Xem giá» | XÃ³a SP | KM há»£p lá»‡ | Ãp dá»¥ng KM |
| ---------- | ------- | ------- | ------ | --------- | ---------- |
| ADMIN      | âœ…      | âœ…      | âœ…     | âœ…        | âœ…         |
| NHAN_VIEN  | âŒ      | âŒ      | âŒ     | âŒ        | âŒ         |
| KHACH_HANG | âœ…      | âœ…      | âœ…     | âœ…        | âœ…         |


