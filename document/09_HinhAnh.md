# HÃ¬nh áº¢nh Controller

> **Base Path:** `/api/v1/hinh-anh`  
> **File:** `HinhAnhController.java`  
> Quáº£n lÃ½ hÃ¬nh áº£nh sáº£n pháº©m, há»— trá»£ upload lÃªn Cloudinary.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `HinhAnh`

| TrÆ°á»ng           | Kiá»ƒu           | MÃ´ táº£                           |
| ---------------- | -------------- | ------------------------------- |
| `id`             | Long           | MÃ£ hÃ¬nh áº£nh (auto-increment)    |
| `chiTietSanPham` | ChiTietSanPham | Chi tiáº¿t sáº£n pháº©m liÃªn káº¿t (FK) |
| `tenHinhAnh`     | String(255)    | TÃªn file áº£nh (trÃªn Cloudinary)       |
| `ngayTao`        | LocalDateTime  | NgÃ y táº¡o (tá»± Ä‘á»™ng)              |
| `ngayCapNhat`    | LocalDateTime  | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)         |

> **Xem áº£nh:** Truy cáº­p `GET {secure_url_cloudinary}` Ä‘á»ƒ láº¥y file áº£nh (khÃ´ng cáº§n xÃ¡c thá»±c).

---

## 1. Láº¥y danh sÃ¡ch hÃ¬nh áº£nh

| Thuá»™c tÃ­nh   | Chi tiáº¿t               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/hinh-anh` |
| **Method**   | `GET`                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)     |

**Response:** `200 OK` â€” Tráº£ vá» `List<HinhAnh>`

```json
[
  {
    "id": 1,
    "chiTietSanPham": { "id": 1 },
    "tenHinhAnh": "polo-den-m-1.jpg",
    "ngayTao": "2026-03-01T10:00:00"
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "chiTietSanPham": {
    "id": "Long"
  },
  "tenHinhAnh": "String",
  "ngayTao": "LocalDateTime"
}
```

---

## 2. Láº¥y hÃ¬nh áº£nh theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/hinh-anh/{id}` |
| **Method**   | `GET`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)          |

**Response:** `200 OK` â€” Tráº£ vá» `HinhAnh`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y hÃ¬nh áº£nh |

---

## 3. Láº¥y hÃ¬nh áº£nh theo chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                                    |
| ------------ | ----------------------------------------------------------- |
| **URL**      | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}` |
| **Method**   | `GET`                                                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                          |

**Path Parameters:**

| Tham sá»‘            | Kiá»ƒu | MÃ´ táº£                |
| ------------------ | ---- | -------------------- |
| `chiTietSanPhamId` | Long | MÃ£ chi tiáº¿t sáº£n pháº©m |

**Response:** `200 OK` â€” Tráº£ vá» `List<HinhAnh>`

---

## 4. Upload hÃ¬nh áº£nh cho chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh       | Chi tiáº¿t                                          |
| ---------------- | ------------------------------------------------- |
| **URL**          | `POST /api/v1/hinh-anh/upload/{chiTietSanPhamId}` |
| **Method**       | `POST`                                            |
| **Content-Type** | `multipart/form-data`                             |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)                                |

**Path Parameters:**

| Tham sá»‘            | Kiá»ƒu | MÃ´ táº£                |
| ------------------ | ---- | -------------------- |
| `chiTietSanPhamId` | Long | MÃ£ chi tiáº¿t sáº£n pháº©m |

**Form Data:**

| Tham sá»‘ | Kiá»ƒu         | Báº¯t buá»™c | MÃ´ táº£                         |
| ------- | ------------ | -------- | ----------------------------- |
| `files` | List\<File\> | **CÃ³**   | Danh sÃ¡ch file áº£nh cáº§n upload |

**Response:** `201 Created` â€” Tráº£ vá» `List<HinhAnh>`

> **LÆ°u Ã½:** áº¢nh Ä‘Æ°á»£c upload lÃªn Cloudinary storage. URL truy cáº­p: `GET {secure_url_cloudinary}`.

**Lá»—i:**

| HTTP Status | MÃ´ táº£                            |
| ----------- | -------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t sáº£n pháº©m |

---

## 5. Táº¡o hÃ¬nh áº£nh (JSON)

| Thuá»™c tÃ­nh       | Chi tiáº¿t                |
| ---------------- | ----------------------- |
| **URL**          | `POST /api/v1/hinh-anh` |
| **Method**       | `POST`                  |
| **Content-Type** | `application/json`      |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)      |

**Request Body:**

```json
{
  "chiTietSanPham": { "id": 1 },
  "tenHinhAnh": "custom-image.jpg"
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "chiTietSanPham": {
    "id": "Long"
  },
  "tenHinhAnh": "String"
}
```

**Response:** `201 Created` â€” Tráº£ vá» `HinhAnh`

---

## 6. Cáº­p nháº­t hÃ¬nh áº£nh

| Thuá»™c tÃ­nh       | Chi tiáº¿t               |
| ---------------- | ---------------------- |
| **URL**          | `PUT /api/v1/hinh-anh` |
| **Method**       | `PUT`                  |
| **Content-Type** | `application/json`     |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)     |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{
  "id": 1,
  "tenHinhAnh": "updated-image.jpg"
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenHinhAnh": "String"
}
```

**Response:** `200 OK` â€” Tráº£ vá» `HinhAnh`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                           |
| ----------- | ------------------------------- |
| `400`       | MÃ£ hÃ¬nh áº£nh khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## 7. XÃ³a hÃ¬nh áº£nh

| Thuá»™c tÃ­nh   | Chi tiáº¿t                       |
| ------------ | ------------------------------ |
| **URL**      | `DELETE /api/v1/hinh-anh/{id}` |
| **Method**   | `DELETE`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)             |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y hÃ¬nh áº£nh |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST/Upload | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ----------- | --------- | ------------ |
| ADMIN      | âœ…        | âœ…          | âœ…        | âœ…           |
| NHAN_VIEN  | âœ…        | âœ…          | âœ…        | âŒ           |
| KHACH_HANG | âœ…        | âŒ          | âŒ        | âŒ           |


