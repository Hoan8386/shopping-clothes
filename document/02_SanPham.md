# Sáº£n Pháº©m Controller

> **Base Path:** `/api/v1/san-pham`  
> **File:** `SanPhamController.java`  
> Quáº£n lÃ½ sáº£n pháº©m: CRUD + tÃ¬m kiáº¿m/lá»c + phÃ¢n trang.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `SanPham`

| TrÆ°á»ng         | Kiá»ƒu          | MÃ´ táº£                               |
| -------------- | ------------- | ----------------------------------- |
| `id`           | Long          | MÃ£ sáº£n pháº©m (auto-increment)        |
| `kieuSanPham`  | KieuSanPham   | Kiá»ƒu/loáº¡i sáº£n pháº©m (FK)             |
| `boSuuTap`     | BoSuuTap      | Bá»™ sÆ°u táº­p (FK)                     |
| `thuongHieu`   | ThuongHieu    | ThÆ°Æ¡ng hiá»‡u (FK)                    |
| `tenSanPham`   | String(255)   | TÃªn sáº£n pháº©m                        |
| `giaVon`       | Double        | GiÃ¡ vá»‘n (VND)                       |
| `giaBan`       | Double        | GiÃ¡ bÃ¡n (VND)                       |
| `giaGiam`      | Integer       | Pháº§n trÄƒm giáº£m giÃ¡ (%)              |
| `hinhAnhChinh` | String(255)   | TÃªn file áº£nh chÃ­nh (lÆ°u trÃªn Cloudinary) |
| `moTa`         | String(255)   | MÃ´ táº£ sáº£n pháº©m                      |
| `soLuong`      | Integer       | Tá»•ng sá»‘ lÆ°á»£ng tá»“n kho               |
| `trangThai`    | Integer       | Tráº¡ng thÃ¡i (0: áº©n, 1: hiá»ƒn thá»‹)     |
| `ngayTao`      | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)                  |
| `ngayCapNhat`  | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)             |

---

## 1. Láº¥y danh sÃ¡ch sáº£n pháº©m (cÃ³ lá»c + phÃ¢n trang)

| Thuá»™c tÃ­nh   | Chi tiáº¿t               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/san-pham` |
| **Method**   | `GET`                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham sá»‘         | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                           |
| --------------- | ------- | -------- | ------------------------------- |
| `tenSanPham`    | String  | KhÃ´ng    | Lá»c theo tÃªn sáº£n pháº©m (like)    |
| `kieuSanPhamId` | Long    | KhÃ´ng    | Lá»c theo mÃ£ kiá»ƒu sáº£n pháº©m       |
| `boSuuTapId`    | Long    | KhÃ´ng    | Lá»c theo mÃ£ bá»™ sÆ°u táº­p          |
| `thuongHieuId`  | Long    | KhÃ´ng    | Lá»c theo mÃ£ thÆ°Æ¡ng hiá»‡u         |
| `kichThuocId`   | Long    | KhÃ´ng    | Lá»c theo mÃ£ kÃ­ch thÆ°á»›c          |
| `mauSacId`      | Long    | KhÃ´ng    | Lá»c theo mÃ£ mÃ u sáº¯c             |
| `trangThai`     | Integer | KhÃ´ng    | Lá»c theo tráº¡ng thÃ¡i             |
| `giaMin`        | Double  | KhÃ´ng    | GiÃ¡ bÃ¡n tá»‘i thiá»ƒu               |
| `giaMax`        | Double  | KhÃ´ng    | GiÃ¡ bÃ¡n tá»‘i Ä‘a                  |
| `page`          | Integer | KhÃ´ng    | Sá»‘ trang (máº·c Ä‘á»‹nh: 0)          |
| `size`          | Integer | KhÃ´ng    | KÃ­ch thÆ°á»›c trang (máº·c Ä‘á»‹nh: 20) |
| `sort`          | String  | KhÃ´ng    | Sáº¯p xáº¿p (vd: `giaBan,asc`)      |

**VÃ­ dá»¥ request:**

```
GET /api/v1/san-pham?tenSanPham=ao&kieuSanPhamId=1&thuongHieuId=2&kichThuocId=2&mauSacId=1&trangThai=1&giaMin=100000&giaMax=500000&page=0&size=10&sort=giaBan,asc
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
      "tenSanPham": "Ão Polo Classic",
      "giaVon": 120000,
      "giaBan": 250000,
      "giaGiam": 10,
      "hinhAnhChinh": "polo-classic.jpg",
      "moTa": "Ão polo nam cao cáº¥p",
      "soLuong": 50,
      "trangThai": 1,
      "tenKieuSanPham": "Ão",
      "tenBoSuuTap": "XuÃ¢n HÃ¨ 2025",
      "tenThuongHieu": "Nike"
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

> **LÆ°u Ã½:** Náº¿u khÃ´ng truyá»n báº¥t ká»³ tham sá»‘ lá»c nÃ o â†’ tráº£ vá» táº¥t cáº£ sáº£n pháº©m (cÃ³ phÃ¢n trang).

---

## 2. Xem chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/san-pham/{id}` |
| **Method**   | `GET`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ sáº£n pháº©m |

**Response:** `200 OK` â€” Tráº£ vá» `ResSanPhamDTO`

```json
{
  "id": 1,
  "tenSanPham": "Ão Polo Classic",
  "giaVon": 120000,
  "giaBan": 250000,
  "giaGiam": 10,
  "hinhAnhChinh": "polo-classic.jpg",
  "moTa": "Ão polo nam cao cáº¥p",
  "soLuong": 50,
  "trangThai": 1,
  "tenKieuSanPham": "Ão",
  "tenBoSuuTap": "XuÃ¢n HÃ¨ 2025",
  "tenThuongHieu": "Nike"
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

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

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y sáº£n pháº©m |

---

## 3. Táº¡o sáº£n pháº©m má»›i

| Thuá»™c tÃ­nh       | Chi tiáº¿t                |
| ---------------- | ----------------------- |
| **URL**          | `POST /api/v1/san-pham` |
| **Method**       | `POST`                  |
| **Content-Type** | `multipart/form-data`   |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)      |

**Form Data:**

| Tham sá»‘         | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                           |
| --------------- | ------- | -------- | ------------------------------- |
| `tenSanPham`    | String  | KhÃ´ng    | TÃªn sáº£n pháº©m                    |
| `giaVon`        | Double  | KhÃ´ng    | GiÃ¡ vá»‘n (VND)                   |
| `giaBan`        | Double  | KhÃ´ng    | GiÃ¡ bÃ¡n (VND)                   |
| `giaGiam`       | Integer | KhÃ´ng    | Pháº§n trÄƒm giáº£m giÃ¡ (%)          |
| `moTa`          | String  | KhÃ´ng    | MÃ´ táº£                           |
| `soLuong`       | Integer | KhÃ´ng    | Sá»‘ lÆ°á»£ng                        |
| `trangThai`     | Integer | KhÃ´ng    | Tráº¡ng thÃ¡i (0/1)                |
| `kieuSanPhamId` | Long    | KhÃ´ng    | MÃ£ kiá»ƒu sáº£n pháº©m                |
| `boSuuTapId`    | Long    | KhÃ´ng    | MÃ£ bá»™ sÆ°u táº­p                   |
| `thuongHieuId`  | Long    | KhÃ´ng    | MÃ£ thÆ°Æ¡ng hiá»‡u                  |
| `file`          | File    | KhÃ´ng    | áº¢nh sáº£n pháº©m (upload lÃªn Cloudinary) |

**Response:** `201 Created` â€” Tráº£ vá» `ResSanPhamDTO`

> **LÆ°u Ã½:** áº¢nh sáº£n pháº©m Ä‘Æ°á»£c upload lÃªn Cloudinary. URL áº£nh cÃ³ thá»ƒ truy cáº­p qua `GET {secure_url_cloudinary}`.

---

## 4. Cáº­p nháº­t sáº£n pháº©m

| Thuá»™c tÃ­nh       | Chi tiáº¿t               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/san-pham` |
| **Method**       | `PUT`                  |
| **Content-Type** | `multipart/form-data`  |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)     |

**Form Data:** Giá»‘ng táº¡o má»›i, thÃªm trÆ°á»ng:

| Tham sá»‘ | Kiá»ƒu | Báº¯t buá»™c | MÃ´ táº£                    |
| ------- | ---- | -------- | ------------------------ |
| `id`    | Long | **CÃ³**   | MÃ£ sáº£n pháº©m cáº§n cáº­p nháº­t |

**Response:** `200 OK` â€” Tráº£ vá» `ResSanPhamDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                           |
| ----------- | ------------------------------- |
| `400`       | MÃ£ sáº£n pháº©m khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## 5. XÃ³a sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/san-pham/{id}` |
| **Method**   | `DELETE`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)             |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ sáº£n pháº©m |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y sáº£n pháº©m |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | âœ…        | âœ…         | âœ…        | âœ…           |
| NHAN_VIEN  | âœ…        | âŒ         | âŒ        | âŒ           |
| KHACH_HANG | âœ…        | âŒ         | âŒ        | âŒ           |


