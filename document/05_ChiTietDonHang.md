# Chi Tiáº¿t ÄÆ¡n HÃ ng Controller

> **Base Path:** `/api/v1/chi-tiet-don-hang`  
> **File:** `ChiTietDonHangController.java`  
> Quáº£n lÃ½ chi tiáº¿t (dÃ²ng sáº£n pháº©m) cá»§a Ä‘Æ¡n hÃ ng.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `ChiTietDonHang`

| TrÆ°á»ng           | Kiá»ƒu           | MÃ´ táº£                                     |
| ---------------- | -------------- | ----------------------------------------- |
| `id`             | Long           | MÃ£ chi tiáº¿t Ä‘Æ¡n hÃ ng (auto-increment)     |
| `donHang`        | DonHang        | ÄÆ¡n hÃ ng cha (FK, áº©n trong JSON response) |
| `chiTietSanPham` | ChiTietSanPham | Biáº¿n thá»ƒ sáº£n pháº©m (FK)                    |
| `giaSanPham`     | Double         | GiÃ¡ sáº£n pháº©m táº¡i thá»i Ä‘iá»ƒm mua (VND)      |
| `giamGia`        | Double         | Pháº§n trÄƒm giáº£m giÃ¡ (%)                    |
| `giaGiam`        | Double         | Sá»‘ tiá»n giáº£m (VND)                        |
| `soLuong`        | Integer        | Sá»‘ lÆ°á»£ng mua                              |
| `thanhTien`      | Double         | ThÃ nh tiá»n (VND)                          |
| `ngayTao`        | LocalDateTime  | NgÃ y táº¡o (tá»± Ä‘á»™ng)                        |
| `ngayCapNhat`    | LocalDateTime  | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)                   |

---

## 1. Láº¥y táº¥t cáº£ chi tiáº¿t Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-don-hang` |
| **Method**   | `GET`                           |
| **XÃ¡c thá»±c** | Bearer Token (JWT)              |

**Response:** `200 OK` â€” Tráº£ vá» `List<ChiTietDonHang>`

```json
[
  {
    "id": 1,
    "chiTietSanPham": {
      "id": 1,
      "soLuong": 15,
      "mauSac": { "tenMauSac": "Äen" },
      "kichThuoc": { "tenKichThuoc": "M" }
    },
    "giaSanPham": 250000,
    "giamGia": 10,
    "giaGiam": 25000,
    "soLuong": 2,
    "thanhTien": 450000,
    "ngayTao": "2026-03-01T10:00:00"
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "chiTietSanPham": {
    "id": "Long",
    "soLuong": "Integer",
    "mauSac": {
      "tenMauSac": "String"
    },
    "kichThuoc": {
      "tenKichThuoc": "String"
    }
  },
  "giaSanPham": "Double",
  "giamGia": "Double",
  "giaGiam": "Double",
  "soLuong": "Integer",
  "thanhTien": "Double",
  "ngayTao": "LocalDateTime"
}
```

---

## 2. Láº¥y chi tiáº¿t Ä‘Æ¡n hÃ ng theo mÃ£ Ä‘Æ¡n

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}` |
| **Method**   | `GET`                                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham sá»‘     | Kiá»ƒu | MÃ´ táº£       |
| ----------- | ---- | ----------- |
| `donHangId` | Long | MÃ£ Ä‘Æ¡n hÃ ng |

**Response:** `200 OK` â€” Tráº£ vá» `List<ChiTietDonHang>`

---

## 3. Láº¥y chi tiáº¿t Ä‘Æ¡n hÃ ng theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/chi-tiet-don-hang/{id}` |
| **Method**   | `GET`                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£                |
| ------- | ---- | -------------------- |
| `id`    | Long | MÃ£ chi tiáº¿t Ä‘Æ¡n hÃ ng |

**Response:** `200 OK` â€” Tráº£ vá» `ChiTietDonHang`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                            |
| ----------- | -------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t Ä‘Æ¡n hÃ ng |

---

## 4. Táº¡o chi tiáº¿t Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh       | Chi tiáº¿t                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-don-hang` |
| **Method**       | `POST`                           |
| **Content-Type** | `application/json`               |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)               |

**Request Body:**

```json
{
  "donHang": { "id": 1 },
  "chiTietSanPham": { "id": 1 },
  "giaSanPham": 250000,
  "giamGia": 10,
  "giaGiam": 25000,
  "soLuong": 2,
  "thanhTien": 450000
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "donHang": {
    "id": "Long"
  },
  "chiTietSanPham": {
    "id": "Long"
  },
  "giaSanPham": "Double",
  "giamGia": "Double",
  "giaGiam": "Double",
  "soLuong": "Integer",
  "thanhTien": "Double"
}
```

**Response:** `201 Created` â€” Tráº£ vá» `ChiTietDonHang`

---

## 5. Cáº­p nháº­t chi tiáº¿t Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh       | Chi tiáº¿t                        |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-don-hang` |
| **Method**       | `PUT`                           |
| **Content-Type** | `application/json`              |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)              |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{
  "id": 1,
  "soLuong": 3,
  "thanhTien": 675000
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "soLuong": "Integer",
  "thanhTien": "Double"
}
```

**Response:** `200 OK` â€” Tráº£ vá» `ChiTietDonHang`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                    |
| ----------- | ---------------------------------------- |
| `400`       | MÃ£ chi tiáº¿t Ä‘Æ¡n hÃ ng khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## 6. XÃ³a chi tiáº¿t Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-don-hang/{id}` |
| **Method**   | `DELETE`                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                      |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                            |
| ----------- | -------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t Ä‘Æ¡n hÃ ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | âœ…        | âœ…         | âœ…        | âœ…           |
| NHAN_VIEN  | âœ…        | âœ…         | âŒ        | âŒ           |
| KHACH_HANG | âœ…        | âŒ         | âŒ        | âŒ           |


