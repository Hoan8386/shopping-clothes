# Chi Tiáº¿t Phiáº¿u Nháº­p Controller

> **Base Path:** `/api/v1/chi-tiet-phieu-nhap`  
> **File:** `ChiTietPhieuNhapController.java`  
> Quáº£n lÃ½ chi tiáº¿t phiáº¿u nháº­p hÃ ng (tá»«ng dÃ²ng sáº£n pháº©m trong phiáº¿u nháº­p).

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `ChiTietPhieuNhap`

| TrÆ°á»ng           | Kiá»ƒu           | MÃ´ táº£                                            |
| ---------------- | -------------- | ------------------------------------------------ |
| `id`             | Long           | MÃ£ chi tiáº¿t phiáº¿u nháº­p (auto-increment)          |
| `phieuNhap`      | PhieuNhap      | Phiáº¿u nháº­p cha (FK, áº©n trong JSON)               |
| `chiTietSanPham` | ChiTietSanPham | Biáº¿n thá»ƒ sáº£n pháº©m Ä‘Æ°á»£c nháº­p (FK)                 |
| `soLuong`        | Integer        | Sá»‘ lÆ°á»£ng nháº­p theo phiáº¿u                         |
| `soLuongThieu`   | Integer        | Sá»‘ lÆ°á»£ng thiáº¿u (dÃ¹ng khi kiá»ƒm kÃª)                |
| `soLuongDaNhap`  | Integer        | Sá»‘ lÆ°á»£ng Ä‘Ã£ thá»±c nháº­p vÃ o kho (tÃ­nh sau kiá»ƒm kÃª) |
| `ghiTru`         | String(255)    | Ghi chÃº trá»«                                      |
| `ghiTruKiemHang` | String(255)    | Ghi chÃº kiá»ƒm hÃ ng                                |
| `trangThai`      | Integer        | Tráº¡ng thÃ¡i (0: Äá»§, 1: Thiáº¿u)                     |
| `ngayTao`        | LocalDateTime  | NgÃ y táº¡o (tá»± Ä‘á»™ng)                               |
| `ngayCapNhat`    | LocalDateTime  | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)                          |

### MÃ£ tráº¡ng thÃ¡i chi tiáº¿t phiáº¿u nháº­p (`trangThai`)

| GiÃ¡ trá»‹ | Ã nghÄ©a | `trangThaiText` |
| ------- | ------- | --------------- |
| `0`     | Äá»§      | "Äá»§"            |
| `1`     | Thiáº¿u   | "Thiáº¿u"         |

---

## 1. Láº¥y danh sÃ¡ch chi tiáº¿t phiáº¿u nháº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                          |
| ------------ | --------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap` |
| **Method**   | `GET`                             |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                |

**Response:** `200 OK` â€” Tráº£ vá» `List<ResChiTietPhieuNhapDTO>`

```json
[
  {
    "id": 1,
    "phieuNhapId": 1,
    "tenPhieuNhap": "Nháº­p hÃ ng Ä‘á»£t 1 - CN Q.1",
    "chiTietSanPham": {
      "id": 1,
      "soLuong": 7,
      "tenSanPham": "Ão Oxford",
      "tenMauSac": "Tráº¯ng",
      "tenKichThuoc": "M"
    },
    "soLuong": 10,
    "soLuongThieu": 3,
    "soLuongDaNhap": 7,
    "ghiTru": null,
    "ghiTruKiemHang": "Thiáº¿u 3 cÃ¡i do hÆ° há»ng",
    "trangThai": 1,
    "trangThaiText": "Thiáº¿u",
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": "2026-03-03T10:00:00"
  }
]
```

---

## 2. Láº¥y chi tiáº¿t phiáº¿u nháº­p theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                               |
| ------------ | -------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Method**   | `GET`                                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                     |

**Response:** `200 OK` â€” Tráº£ vá» `ResChiTietPhieuNhapDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                              |
| ----------- | ---------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t phiáº¿u nháº­p |

---

## 3. Láº¥y chi tiáº¿t theo mÃ£ phiáº¿u nháº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                                   |
| ------------ | ---------------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}` |
| **Method**   | `GET`                                                      |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                         |

**Path Parameters:**

| Tham sá»‘       | Kiá»ƒu | MÃ´ táº£         |
| ------------- | ---- | ------------- |
| `phieuNhapId` | Long | MÃ£ phiáº¿u nháº­p |

**Response:** `200 OK` â€” Tráº£ vá» `List<ResChiTietPhieuNhapDTO>`

---

## 4. Táº¡o chi tiáº¿t phiáº¿u nháº­p

| Thuá»™c tÃ­nh       | Chi tiáº¿t                           |
| ---------------- | ---------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-phieu-nhap` |
| **Method**       | `POST`                             |
| **Content-Type** | `application/json`                 |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)                 |

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

**Kiá»ƒu dá»¯ liá»‡u:**

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

**Response:** `201 Created` â€” Tráº£ vá» `ResChiTietPhieuNhapDTO`

**Quy táº¯c:**

- Chá»‰ Ä‘Æ°á»£c thÃªm chi tiáº¿t khi phiáº¿u nháº­p Ä‘ang á»Ÿ tráº¡ng thÃ¡i **ÄÃ£ Ä‘áº·t** (0) hoáº·c **Cháº­m giao** (2)
- KhÃ´ng Ä‘Æ°á»£c thÃªm chi tiáº¿t khi phiáº¿u Ä‘Ã£ á»Ÿ tráº¡ng thÃ¡i **ÄÃ£ nháº­n** (1), **Thiáº¿u hÃ ng** (4), **HoÃ n thÃ nh** (5) hoáº·c **Há»§y** (3)

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                        |
| ----------- | ------------------------------------------------------------ |
| `400`       | KhÃ´ng tÃ¬m tháº¥y phiáº¿u nháº­p                                    |
| `400`       | KhÃ´ng thá»ƒ thÃªm chi tiáº¿t cho phiáº¿u nháº­p á»Ÿ tráº¡ng thÃ¡i hiá»‡n táº¡i |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t sáº£n pháº©m                             |

---

## 5. Cáº­p nháº­t chi tiáº¿t phiáº¿u nháº­p

| Thuá»™c tÃ­nh       | Chi tiáº¿t                          |
| ---------------- | --------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-phieu-nhap` |
| **Method**       | `PUT`                             |
| **Content-Type** | `application/json`                |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)                |

**Request Body:** `ReqChiTietPhieuNhapDTO` (pháº£i cÃ³ `id`)

```json
{
  "id": 1,
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "soLuongThieu": 0,
  "ghiTru": null,
  "ghiTruKiemHang": "ÄÃ£ kiá»ƒm tra Ä‘á»§",
  "trangThai": 0
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long (báº¯t buá»™c)",
  "phieuNhapId": "Long",
  "chiTietSanPhamId": "Long",
  "soLuong": "Integer",
  "soLuongThieu": "Integer",
  "ghiTru": "String",
  "ghiTruKiemHang": "String",
  "trangThai": "Integer"
}
```

**Response:** `200 OK` â€” Tráº£ vá» `ResChiTietPhieuNhapDTO`

**Quy táº¯c:**

- Chá»‰ Ä‘Æ°á»£c cáº­p nháº­t khi phiáº¿u nháº­p cha Ä‘ang á»Ÿ tráº¡ng thÃ¡i **ÄÃ£ Ä‘áº·t** (0), **ÄÃ£ nháº­n** (1), **Cháº­m giao** (2) hoáº·c **Thiáº¿u hÃ ng** (4)
- KhÃ´ng Ä‘Æ°á»£c cáº­p nháº­t khi phiáº¿u Ä‘Ã£ **HoÃ n thÃ nh** (5) hoáº·c **Há»§y** (3)
- Sau khi cáº­p nháº­t chi tiáº¿t á»Ÿ phiáº¿u **Thiáº¿u hÃ ng** (4), gá»i kiá»ƒm kÃª láº¡i Ä‘á»ƒ cáº­p nháº­t tá»“n kho

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                                   |
| ----------- | ----------------------------------------------------------------------- |
| `400`       | MÃ£ chi tiáº¿t phiáº¿u nháº­p khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng                              |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t phiáº¿u nháº­p                                      |
| `400`       | KhÃ´ng thá»ƒ cáº­p nháº­t chi tiáº¿t phiáº¿u nháº­p khi phiáº¿u Ä‘Ã£ HoÃ n thÃ nh hoáº·c Há»§y |
| `400`       | KhÃ´ng tÃ¬m tháº¥y phiáº¿u nháº­p                                               |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t sáº£n pháº©m                                        |

---

## 6. XÃ³a chi tiáº¿t phiáº¿u nháº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                  |
| ------------ | ----------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Method**   | `DELETE`                                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                        |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                              |
| ----------- | ---------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t phiáº¿u nháº­p |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | âœ…        | âœ…         | âœ…        | âœ…           |
| NHAN_VIEN  | âœ…        | âœ…         | âœ…        | âŒ           |
| KHACH_HANG | âŒ        | âŒ         | âŒ        | âŒ           |


