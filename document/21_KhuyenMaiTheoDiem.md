# Khuyáº¿n MÃ£i Theo Äiá»ƒm Controller

> **Base Path:** `/api/v1/khuyen-mai-theo-diem`  
> **File:** `KhuyenMaiTheoDiemController.java`  
> Quáº£n lÃ½ chÆ°Æ¡ng trÃ¬nh khuyáº¿n mÃ£i Ã¡p dá»¥ng báº±ng Ä‘iá»ƒm tÃ­ch lÅ©y cá»§a khÃ¡ch hÃ ng.

---

## Tá»•ng quan

Khuyáº¿n mÃ£i theo Ä‘iá»ƒm cho phÃ©p khÃ¡ch hÃ ng sá»­ dá»¥ng Ä‘iá»ƒm tÃ­ch lÅ©y Ä‘á»ƒ Ä‘á»•i mÃ£ giáº£m giÃ¡ Ã¡p dá»¥ng cho Ä‘Æ¡n hÃ ng. Má»—i chÆ°Æ¡ng trÃ¬nh cÃ³ thá»i gian hiá»‡u lá»±c vÃ  sá»‘ lÆ°á»£ng giá»›i háº¡n.

### Quy táº¯c tÃ­ch Ä‘iá»ƒm

- Khi Ä‘Æ¡n hÃ ng chuyá»ƒn sang tráº¡ng thÃ¡i **"ThÃ nh cÃ´ng" (trangThai = 3)**, há»‡ thá»‘ng tá»± Ä‘á»™ng cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y cho khÃ¡ch hÃ ng.
- **CÃ´ng thá»©c:** Má»—i **100.000 VND** tiá»n thanh toÃ¡n (`tongTienTra`) = **10 Ä‘iá»ƒm**.
- VÃ­ dá»¥: ÄÆ¡n 350.000Ä‘ â†’ 30 Ä‘iá»ƒm, Ä‘Æ¡n 1.200.000Ä‘ â†’ 120 Ä‘iá»ƒm.

### Cáº¥u trÃºc dá»¯ liá»‡u `KhuyenMaiTheoDiem`

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

## 1. Láº¥y danh sÃ¡ch khuyáº¿n mÃ£i theo Ä‘iá»ƒm

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/khuyen-mai-theo-diem`       |
| **Method**   | `GET`                                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” Táº¥t cáº£ Ä‘Ã£ Ä‘Äƒng nháº­p |

**Response:** `200 OK` â€” Tráº£ vá» `List<KhuyenMaiTheoDiem>`

```json
[
  {
    "id": 1,
    "tenKhuyenMai": "Äá»•i 50 Ä‘iá»ƒm giáº£m 15%",
    "giamToiDa": 200000,
    "hoaDonToiDa": 800000,
    "phanTramGiam": 15.0,
    "hinhThuc": 1,
    "thoiGianBatDau": "2026-01-01T00:00:00",
    "thoiGianKetThuc": "2026-12-31T23:59:59",
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

## 2. Láº¥y khuyáº¿n mÃ£i theo Ä‘iá»ƒm theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                 |
| ------------ | ---------------------------------------- |
| **URL**      | `GET /api/v1/khuyen-mai-theo-diem/{id}`  |
| **Method**   | `GET`                                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” Táº¥t cáº£ Ä‘Ã£ Ä‘Äƒng nháº­p |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£              |
| ------- | ---- | ------------------ |
| `id`    | Long | MÃ£ khuyáº¿n mÃ£i Ä‘iá»ƒm |

**Response:** `200 OK` â€” Tráº£ vá» `KhuyenMaiTheoDiem`

```json
{
  "id": 1,
  "tenKhuyenMai": "Äá»•i 50 Ä‘iá»ƒm giáº£m 15%",
  "giamToiDa": 200000,
  "hoaDonToiDa": 800000,
  "phanTramGiam": 15.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-01-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
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

| HTTP Status | MÃ´ táº£                               |
| ----------- | ----------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y khuyáº¿n mÃ£i theo Ä‘iá»ƒm |

---

## 3. Táº¡o khuyáº¿n mÃ£i theo Ä‘iá»ƒm

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `POST /api/v1/khuyen-mai-theo-diem`      |
| **Method**       | `POST`                                   |
| **Content-Type** | `application/json`                       |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n ADMIN |

**Request Body:**

```json
{
  "tenKhuyenMai": "Äá»•i 30 Ä‘iá»ƒm giáº£m 10%",
  "giamToiDa": 100000,
  "hoaDonToiDa": 500000,
  "phanTramGiam": 10.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
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

**Response:** `201 Created` â€” Tráº£ vá» `KhuyenMaiTheoDiem`

> **LÆ°u Ã½:** TrÆ°á»ng `ngayTao` Ä‘Æ°á»£c tá»± Ä‘á»™ng gÃ¡n khi táº¡o má»›i.

---

## 4. Cáº­p nháº­t khuyáº¿n mÃ£i theo Ä‘iá»ƒm

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                 |
| ---------------- | ---------------------------------------- |
| **URL**          | `PUT /api/v1/khuyen-mai-theo-diem`       |
| **Method**       | `PUT`                                    |
| **Content-Type** | `application/json`                       |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n ADMIN |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{
  "id": 1,
  "tenKhuyenMai": "Äá»•i 50 Ä‘iá»ƒm giáº£m 20% (cáº­p nháº­t)",
  "giamToiDa": 300000,
  "hoaDonToiDa": 1000000,
  "phanTramGiam": 20.0,
  "hinhThuc": 1,
  "thoiGianBatDau": "2026-03-01T00:00:00",
  "thoiGianKetThuc": "2026-12-31T23:59:59",
  "soLuong": 150,
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

**Response:** `200 OK` â€” Tráº£ vá» `KhuyenMaiTheoDiem`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                       |
| ----------- | ------------------------------------------- |
| `400`       | MÃ£ khuyáº¿n mÃ£i theo Ä‘iá»ƒm khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |
| `500`       | KhÃ´ng tÃ¬m tháº¥y khuyáº¿n mÃ£i Ä‘á»ƒ cáº­p nháº­t       |

> **LÆ°u Ã½:** TrÆ°á»ng `ngayCapNhat` Ä‘Æ°á»£c tá»± Ä‘á»™ng cáº­p nháº­t.

---

## 5. XÃ³a khuyáº¿n mÃ£i theo Ä‘iá»ƒm

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                   |
| ------------ | ------------------------------------------ |
| **URL**      | `DELETE /api/v1/khuyen-mai-theo-diem/{id}` |
| **Method**   | `DELETE`                                   |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” YÃªu cáº§u quyá»n ADMIN   |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£                 |
| ------- | ---- | --------------------- |
| `id`    | Long | MÃ£ khuyáº¿n mÃ£i cáº§n xÃ³a |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                               |
| ----------- | ----------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y khuyáº¿n mÃ£i theo Ä‘iá»ƒm |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | âœ…        | âœ…         | âœ…        | âœ…           |
| NHAN_VIEN  | âœ…        | âŒ         | âŒ        | âŒ           |
| KHACH_HANG | âœ…        | âŒ         | âŒ        | âŒ           |


