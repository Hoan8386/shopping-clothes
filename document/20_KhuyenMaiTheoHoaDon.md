# Khuyáº¿n MÃ£i Theo HÃ³a ÄÆ¡n Controller

> **Base Path:** `/api/v1/khuyen-mai-theo-hoa-don`  
> **File:** `KhuyenMaiTheoHoaDonController.java`  
> Quáº£n lÃ½ chÆ°Æ¡ng trÃ¬nh khuyáº¿n mÃ£i Ã¡p dá»¥ng cho hÃ³a Ä‘Æ¡n (giáº£m giÃ¡ theo tá»•ng giÃ¡ trá»‹ Ä‘Æ¡n hÃ ng).

---

## Tá»•ng quan

Khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n cho phÃ©p giáº£m giÃ¡ trá»±c tiáº¿p trÃªn tá»•ng giÃ¡ trá»‹ Ä‘Æ¡n hÃ ng. Má»—i chÆ°Æ¡ng trÃ¬nh khuyáº¿n mÃ£i cÃ³ thá»i gian hiá»‡u lá»±c, sá»‘ lÆ°á»£ng sá»­ dá»¥ng giá»›i háº¡n, vÃ  cÃ¡c Ä‘iá»u kiá»‡n Ã¡p dá»¥ng (hÃ³a Ä‘Æ¡n tá»‘i Ä‘a, giáº£m tá»‘i Ä‘a).

### Cáº¥u trÃºc dá»¯ liá»‡u `KhuyenMaiTheoHoaDon`

| TrÆ°á»ng            | Kiá»ƒu          | MÃ´ táº£                                          |
| ----------------- | ------------- | ---------------------------------------------- |
| `id`              | Long          | MÃ£ khuyáº¿n mÃ£i (auto-increment)                 |
| `tenKhuyenMai`    | String(255)   | TÃªn chÆ°Æ¡ng trÃ¬nh khuyáº¿n mÃ£i                    |
| `giamToiDa`       | Integer       | Sá»‘ tiá»n giáº£m tá»‘i Ä‘a (VND)                      |
| `hoaDonToiDa`     | Integer       | GiÃ¡ trá»‹ hÃ³a Ä‘Æ¡n tá»‘i thiá»ƒu Ä‘á»ƒ Ã¡p dá»¥ng (VND)     |
| `phanTramGiam`    | Double        | Pháº§n trÄƒm giáº£m giÃ¡ (%)                         |
| `hinhThuc`        | Integer       | HÃ¬nh thá»©c Ã¡p dá»¥ng (0: táº¥t cáº£, 1: cÃ³ Ä‘iá»u kiá»‡n) |
| `thoiGianBatDau`  | LocalDateTime | Thá»i gian báº¯t Ä‘áº§u hiá»‡u lá»±c                     |
| `thoiGianKetThuc` | LocalDateTime | Thá»i gian káº¿t thÃºc hiá»‡u lá»±c                    |
| `soLuong`         | Integer       | Sá»‘ lÆ°á»£ng mÃ£ khuyáº¿n mÃ£i cÃ²n láº¡i                 |
| `trangThai`       | Integer       | Tráº¡ng thÃ¡i (0: ngá»«ng, 1: hoáº¡t Ä‘á»™ng)            |
| `ngayTao`         | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)                             |
| `ngayCapNhat`     | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)                        |

---

## 1. Láº¥y danh sÃ¡ch khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/khuyen-mai-theo-hoa-don`    |
| **Method**   | `GET`                                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” Táº¥t cáº£ Ä‘Ã£ Ä‘Äƒng nháº­p |

**Response:** `200 OK` â€” Tráº£ vá» `List<KhuyenMaiTheoHoaDon>`

```json
[
  {
    "id": 1,
    "tenKhuyenMai": "Giáº£m 10% Ä‘Æ¡n tá»« 500K",
    "giamToiDa": 100000,
    "hoaDonToiDa": 500000,
    "phanTramGiam": 10.0,
    "hinhThuc": 1,
    "thoiGianBatDau": "2026-01-01T00:00:00",
    "thoiGianKetThuc": "2026-06-30T23:59:59",
    "soLuong": 100,
    "trangThai": 1,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKhuyenMai": "String",
  "giamToiDa": "Integer",
  "hoaDonToiDa": "Integer",
  "phanTramGiam": "Double",
  "hinhThuc": "Integer",
  "thoiGianBatDau": "LocalDateTime",
  "thoiGianKetThuc": "LocalDateTime",
  "soLuong": "Integer",
  "trangThai": "Integer",
  "ngayTao": "LocalDateTime",
  "ngayCapNhat": "LocalDateTime | null"
}
```

---

## 2. Láº¥y khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                   |
| ------------ | ------------------------------------------ |
| **URL**      | `GET /api/v1/khuyen-mai-theo-hoa-don/{id}` |
| **Method**   | `GET`                                      |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” Táº¥t cáº£ Ä‘Ã£ Ä‘Äƒng nháº­p   |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£                 |
| ------- | ---- | --------------------- |
| `id`    | Long | MÃ£ khuyáº¿n mÃ£i hÃ³a Ä‘Æ¡n |

**Response:** `200 OK` â€” Tráº£ vá» `KhuyenMaiTheoHoaDon`

```json
{
  "id": 1,
  "tenKhuyenMai": "Giáº£m 10% Ä‘Æ¡n tá»« 500K",
  "giamToiDa": 100000,
  "hoaDonToiDa": 500000,
  "phanTramGiam": 10.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-01-01T00:00:00",
  "thoiGianKetThuc": "2026-06-30T23:59:59",
  "soLuong": 100,
  "trangThai": 1,
  "ngayTao": "2026-03-01T10:00:00",
  "ngayCapNhat": null
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKhuyenMai": "String",
  "giamToiDa": "Integer",
  "hoaDonToiDa": "Integer",
  "phanTramGiam": "Double",
  "hinhThuc": "Integer",
  "thoiGianBatDau": "LocalDateTime",
  "thoiGianKetThuc": "LocalDateTime",
  "soLuong": "Integer",
  "trangThai": "Integer",
  "ngayTao": "LocalDateTime",
  "ngayCapNhat": "LocalDateTime | null"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                  |
| ----------- | -------------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n |

---

## 3. Táº¡o khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `POST /api/v1/khuyen-mai-theo-hoa-don`   |
| **Method**       | `POST`                                   |
| **Content-Type** | `application/json`                       |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n ADMIN |

**Request Body:**

```json
{
  "tenKhuyenMai": "Giáº£m 10% Ä‘Æ¡n tá»« 300K",
  "giamToiDa": 50000,
  "hoaDonToiDa": 300000,
  "phanTramGiam": 10.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-06-30T23:59:59",
  "soLuong": 100,
  "trangThai": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "tenKhuyenMai": "String",
  "giamToiDa": "Integer",
  "hoaDonToiDa": "Integer",
  "phanTramGiam": "Double",
  "hinhThuc": "Integer",
  "thoiGianBatDau": "LocalDateTime",
  "thoiGianKetThuc": "LocalDateTime",
  "soLuong": "Integer",
  "trangThai": "Integer"
}
```

**Response:** `201 Created` â€” Tráº£ vá» `KhuyenMaiTheoHoaDon`

> **LÆ°u Ã½:** TrÆ°á»ng `ngayTao` Ä‘Æ°á»£c tá»± Ä‘á»™ng gÃ¡n khi táº¡o má»›i.

---

## 4. Cáº­p nháº­t khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `PUT /api/v1/khuyen-mai-theo-hoa-don`    |
| **Method**       | `PUT`                                    |
| **Content-Type** | `application/json`                       |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n ADMIN |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{
  "id": 1,
  "tenKhuyenMai": "Giáº£m 15% Ä‘Æ¡n tá»« 500K (cáº­p nháº­t)",
  "giamToiDa": 150000,
  "hoaDonToiDa": 500000,
  "phanTramGiam": 15.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
  "soLuong": 200,
  "trangThai": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKhuyenMai": "String",
  "giamToiDa": "Integer",
  "hoaDonToiDa": "Integer",
  "phanTramGiam": "Double",
  "hinhThuc": "Integer",
  "thoiGianBatDau": "LocalDateTime",
  "thoiGianKetThuc": "LocalDateTime",
  "soLuong": "Integer",
  "trangThai": "Integer"
}
```

**Response:** `200 OK` â€” Tráº£ vá» `KhuyenMaiTheoHoaDon`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                          |
| ----------- | ---------------------------------------------- |
| `400`       | MÃ£ khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |
| `500`       | KhÃ´ng tÃ¬m tháº¥y khuyáº¿n mÃ£i Ä‘á»ƒ cáº­p nháº­t          |

> **LÆ°u Ã½:** TrÆ°á»ng `ngayCapNhat` Ä‘Æ°á»£c tá»± Ä‘á»™ng cáº­p nháº­t.

---

## 5. XÃ³a khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                      |
| ------------ | --------------------------------------------- |
| **URL**      | `DELETE /api/v1/khuyen-mai-theo-hoa-don/{id}` |
| **Method**   | `DELETE`                                      |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n ADMIN      |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£                 |
| ------- | ---- | --------------------- |
| `id`    | Long | MÃ£ khuyáº¿n mÃ£i cáº§n xÃ³a |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                  |
| ----------- | -------------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | âœ…        | âœ…         | âœ…        | âœ…           |
| NHAN_VIEN  | âœ…        | âŒ         | âŒ        | âŒ           |
| KHACH_HANG | âœ…        | âŒ         | âŒ        | âŒ           |


