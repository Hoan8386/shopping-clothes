# ÄÃ¡nh GiÃ¡ Sáº£n Pháº©m Controller

> **Base Path:** `/api/v1/danh-gia-san-pham`  
> **File:** `DanhGiaSanPhamController.java`  
> Quáº£n lÃ½ Ä‘Ã¡nh giÃ¡ sáº£n pháº©m â€” KhÃ¡ch hÃ ng Ä‘Ã¡nh giÃ¡ tá»«ng sáº£n pháº©m trong Ä‘Æ¡n hÃ ng sau khi Ä‘Ã£ nháº­n hÃ ng, há»— trá»£ upload áº£nh lÃªn Cloudinary.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `DanhGiaSanPham`

| TrÆ°á»ng           | Kiá»ƒu           | MÃ´ táº£                                                      |
| ---------------- | -------------- | ---------------------------------------------------------- |
| `id`             | Long           | MÃ£ Ä‘Ã¡nh giÃ¡ (auto-increment)                               |
| `khachHang`      | KhachHang      | KhÃ¡ch hÃ ng Ä‘Ã¡nh giÃ¡ (FK)                                   |
| `chiTietDonHang` | ChiTietDonHang | Chi tiáº¿t Ä‘Æ¡n hÃ ng Ä‘Æ°á»£c Ä‘Ã¡nh giÃ¡ (FK) â€” 1 Ä‘Ã¡nh giÃ¡ / 1 dÃ²ng |
| `soSao`          | Integer        | Sá»‘ sao Ä‘Ã¡nh giÃ¡ (1â€“5)                                      |
| `ghiTru`         | String(255)    | Ná»™i dung Ä‘Ã¡nh giÃ¡                                          |
| `hinhAnh`        | String(255)    | URL áº£nh Ä‘Ã¡nh giÃ¡ (upload lÃªn Cloudinary)                        |
| `ngayTao`        | LocalDateTime  | NgÃ y táº¡o (tá»± Ä‘á»™ng)                                         |
| `ngayCapNhat`    | LocalDateTime  | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)                                    |
| `json`           | String(TEXT)   | Dá»¯ liá»‡u má»Ÿ rá»™ng (tuá»³ chá»n)                                 |

### Äiá»u kiá»‡n Ä‘Ã¡nh giÃ¡

| Äiá»u kiá»‡n                                                | MÃ´ táº£                                                       |
| -------------------------------------------------------- | ----------------------------------------------------------- |
| âœ… ÄÃ£ Ä‘Äƒng nháº­p báº±ng tÃ i khoáº£n **khÃ¡ch hÃ ng**            | Láº¥y thÃ´ng tin KH tá»« JWT token                               |
| âœ… `ChiTietDonHang` thuá»™c **Ä‘Æ¡n hÃ ng cá»§a khÃ¡ch hÃ ng Ä‘Ã³** | KH chá»‰ Ä‘Ã¡nh giÃ¡ Ä‘Æ°á»£c Ä‘Æ¡n hÃ ng cá»§a mÃ¬nh                      |
| âœ… ÄÆ¡n hÃ ng **tráº¡ng thÃ¡i = 5** (ÄÃ£ nháº­n hÃ ng)            | Pháº£i nháº­n hÃ ng xong má»›i Ä‘Æ°á»£c Ä‘Ã¡nh giÃ¡                       |
| âœ… ChÆ°a Ä‘Ã¡nh giÃ¡ `ChiTietDonHang` nÃ y                    | Má»—i dÃ²ng sáº£n pháº©m trong Ä‘Æ¡n chá»‰ Ä‘Ã¡nh giÃ¡ **1 láº§n duy nháº¥t** |

> Sau khi Ä‘Ã£ táº¡o Ä‘Ã¡nh giÃ¡, khÃ¡ch hÃ ng **chá»‰ cÃ³ thá»ƒ cáº­p nháº­t hoáº·c xÃ³a**, khÃ´ng thá»ƒ táº¡o thÃªm Ä‘Ã¡nh giÃ¡ cho cÃ¹ng 1 dÃ²ng sáº£n pháº©m.

### Tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng (tham kháº£o)

| GiÃ¡ trá»‹ | Ã nghÄ©a         |
| ------- | --------------- |
| `0`     | Chá» xÃ¡c nháº­n    |
| `1`     | ÄÃ£ xÃ¡c nháº­n     |
| `2`     | Äang Ä‘Ã³ng gÃ³i   |
| `3`     | Äang giao hÃ ng  |
| `4`     | ÄÃ£ há»§y          |
| `5`     | ÄÃ£ nháº­n hÃ ng âœ… |

---

## 1. Xem táº¥t cáº£ Ä‘Ã¡nh giÃ¡

| Thuá»™c tÃ­nh   | Chi tiáº¿t                        |
| ------------ | ------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham` |
| **Method**   | `GET`                           |
| **XÃ¡c thá»±c** | Bearer Token (JWT)              |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "khachHangId": 3,
    "tenKhachHang": "Hoa",
    "chiTietDonHangId": 7,
    "donHangId": 3,
    "sanPhamId": 3,
    "tenSanPham": "VÃ¡y Hoa",
    "soSao": 5,
    "ghiTru": "VÃ¡y ráº¥t Ä‘áº¹p, Ä‘Ãºng size, váº£i mÃ¡t",
    "hinhAnh": null,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

---

## 2. Xem Ä‘Ã¡nh giÃ¡ theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                             |
| ------------ | ------------------------------------ |
| **URL**      | `GET /api/v1/danh-gia-san-pham/{id}` |
| **Method**   | `GET`                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ Ä‘Ã¡nh giÃ¡ |

**Response:** `200 OK`

```json
{
  "id": 1,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "chiTietDonHangId": 7,
  "donHangId": 3,
  "sanPhamId": 3,
  "tenSanPham": "VÃ¡y Hoa",
  "soSao": 5,
  "ghiTru": "VÃ¡y ráº¥t Ä‘áº¹p, Ä‘Ãºng size, váº£i mÃ¡t",
  "hinhAnh": null,
  "ngayTao": "2026-03-01T10:00:00",
  "ngayCapNhat": null
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                   |
| ----------- | ----------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y Ä‘Ã¡nh giÃ¡ |

---

## 3. Xem Ä‘Ã¡nh giÃ¡ theo sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                             |
| ------------ | ---------------------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}` |
| **Method**   | `GET`                                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                   |

> Truy váº¥n táº¥t cáº£ Ä‘Ã¡nh giÃ¡ cá»§a má»™t sáº£n pháº©m thÃ´ng qua Ä‘Æ°á»ng dáº«n: `DanhGia â†’ ChiTietDonHang â†’ ChiTietSanPham â†’ SanPham`.  
> DÃ¹ng khi hiá»ƒn thá»‹ danh sÃ¡ch Ä‘Ã¡nh giÃ¡ trÃªn trang chi tiáº¿t sáº£n pháº©m.

**Path Parameters:**

| Tham sá»‘     | Kiá»ƒu | MÃ´ táº£       |
| ----------- | ---- | ----------- |
| `sanPhamId` | Long | MÃ£ sáº£n pháº©m |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "khachHangId": 3,
    "tenKhachHang": "Hoa",
    "chiTietDonHangId": 7,
    "donHangId": 3,
    "sanPhamId": 3,
    "tenSanPham": "VÃ¡y Hoa",
    "soSao": 5,
    "ghiTru": "VÃ¡y ráº¥t Ä‘áº¹p, Ä‘Ãºng size, váº£i mÃ¡t",
    "hinhAnh": null,
    "ngayTao": "2026-03-01T10:00:00",
    "ngayCapNhat": null
  }
]
```

---

## 4. Xem Ä‘Ã¡nh giÃ¡ theo chi tiáº¿t Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                                             |
| ------------ | -------------------------------------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` |
| **Method**   | `GET`                                                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                                   |

**Path Parameters:**

| Tham sá»‘            | Kiá»ƒu | MÃ´ táº£                |
| ------------------ | ---- | -------------------- |
| `chiTietDonHangId` | Long | MÃ£ chi tiáº¿t Ä‘Æ¡n hÃ ng |

**Response:** `200 OK` â€” TÆ°Æ¡ng tá»± má»¥c 3.

---

## 5. Xem Ä‘Ã¡nh giÃ¡ cá»§a tÃ´i

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                |
| ------------ | --------------------------------------- |
| **URL**      | `GET /api/v1/danh-gia-san-pham/cua-toi` |
| **Method**   | `GET`                                   |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” KhÃ¡ch hÃ ng         |

Tráº£ vá» táº¥t cáº£ Ä‘Ã¡nh giÃ¡ cá»§a khÃ¡ch hÃ ng Ä‘ang Ä‘Äƒng nháº­p.

**Response:** `200 OK` â€” TÆ°Æ¡ng tá»± má»¥c 3.

---

## 6. Táº¡o Ä‘Ã¡nh giÃ¡ sáº£n pháº©m

| Thuá»™c tÃ­nh       | Chi tiáº¿t                         |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/danh-gia-san-pham` |
| **Method**       | `POST`                           |
| **Content-Type** | `multipart/form-data`            |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” KhÃ¡ch hÃ ng  |

**Form Data:**

| Tham sá»‘            | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                                |
| ------------------ | ------- | -------- | ------------------------------------ |
| `chiTietDonHangId` | Long    | **CÃ³**   | MÃ£ chi tiáº¿t Ä‘Æ¡n hÃ ng (dÃ²ng sáº£n pháº©m) |
| `soSao`            | Integer | **CÃ³**   | Sá»‘ sao (1â€“5)                         |
| `ghiTru`           | String  | KhÃ´ng    | Ná»™i dung Ä‘Ã¡nh giÃ¡                    |
| `file`             | File    | KhÃ´ng    | áº¢nh Ä‘Ã¡nh giÃ¡ (upload lÃªn Cloudinary)      |

**Response:** `201 Created`

```json
{
  "id": 4,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "chiTietDonHangId": 7,
  "donHangId": 3,
  "sanPhamId": 3,
  "tenSanPham": "VÃ¡y Hoa",
  "soSao": 5,
  "ghiTru": "Sáº£n pháº©m ráº¥t Ä‘áº¹p, cháº¥t lÆ°á»£ng tá»‘t!",
  "hinhAnh": "/storage/abc123-uuid.jpg",
  "ngayTao": "2026-03-01T15:30:00",
  "ngayCapNhat": null
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                       |
| ----------- | ----------------------------------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t Ä‘Æ¡n hÃ ng                            |
| `400`       | ÄÆ¡n hÃ ng khÃ´ng thuá»™c vá» báº¡n                                 |
| `400`       | ÄÆ¡n hÃ ng pháº£i á»Ÿ tráº¡ng thÃ¡i Ä‘Ã£ nháº­n hÃ ng má»›i cÃ³ thá»ƒ Ä‘Ã¡nh giÃ¡ |
| `400`       | Báº¡n Ä‘Ã£ Ä‘Ã¡nh giÃ¡ sáº£n pháº©m nÃ y rá»“i                            |
| `400`       | Sá»‘ sao pháº£i tá»« 1 Ä‘áº¿n 5                                      |

---

## 7. Cáº­p nháº­t Ä‘Ã¡nh giÃ¡

| Thuá»™c tÃ­nh       | Chi tiáº¿t                             |
| ---------------- | ------------------------------------ |
| **URL**          | `PUT /api/v1/danh-gia-san-pham/{id}` |
| **Method**       | `PUT`                                |
| **Content-Type** | `multipart/form-data`                |
| **XÃ¡c thá»±c**     | Bearer Token (JWT) â€” KhÃ¡ch hÃ ng      |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ Ä‘Ã¡nh giÃ¡ |

**Form Data:**

| Tham sá»‘  | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                      |
| -------- | ------- | -------- | -------------------------- |
| `soSao`  | Integer | KhÃ´ng    | Sá»‘ sao má»›i (1â€“5)           |
| `ghiTru` | String  | KhÃ´ng    | Ná»™i dung Ä‘Ã¡nh giÃ¡ má»›i      |
| `file`   | File    | KhÃ´ng    | áº¢nh má»›i (upload lÃªn Cloudinary) |

**Response:** `200 OK` â€” Tráº£ vá» `ResDanhGiaSanPhamDTO` Ä‘Ã£ cáº­p nháº­t.

**Lá»—i:**

| HTTP Status | MÃ´ táº£                               |
| ----------- | ----------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y Ä‘Ã¡nh giÃ¡             |
| `400`       | Báº¡n khÃ´ng cÃ³ quyá»n sá»­a Ä‘Ã¡nh giÃ¡ nÃ y |
| `400`       | Sá»‘ sao pháº£i tá»« 1 Ä‘áº¿n 5              |

---

## 8. XÃ³a Ä‘Ã¡nh giÃ¡

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                |
| ------------ | --------------------------------------- |
| **URL**      | `DELETE /api/v1/danh-gia-san-pham/{id}` |
| **Method**   | `DELETE`                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                      |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£       |
| ------- | ---- | ----------- |
| `id`    | Long | MÃ£ Ä‘Ã¡nh giÃ¡ |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                               |
| ----------- | ----------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y Ä‘Ã¡nh giÃ¡             |
| `400`       | Báº¡n khÃ´ng cÃ³ quyá»n xÃ³a Ä‘Ã¡nh giÃ¡ nÃ y |

> **LÆ°u Ã½:** KhÃ¡ch hÃ ng chá»‰ xÃ³a Ä‘Æ°á»£c Ä‘Ã¡nh giÃ¡ cá»§a mÃ¬nh. Admin/NhÃ¢n viÃªn cÃ³ thá»ƒ xÃ³a báº¥t ká»³ Ä‘Ã¡nh giÃ¡ nÃ o.

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Táº¥t cáº£) | GET (Theo SP) | GET (Theo CTDH) | GET (Cá»§a tÃ´i) | POST (Táº¡o) | PUT (Sá»­a)     | DELETE (XÃ³a)  |
| ---------- | ------------ | ------------- | --------------- | ------------- | ---------- | ------------- | ------------- |
| ADMIN      | âœ…           | âœ…            | âœ…              | âŒ            | âŒ         | âŒ            | âœ… (táº¥t cáº£)   |
| NHAN_VIEN  | âœ…           | âœ…            | âœ…              | âŒ            | âŒ         | âŒ            | âŒ            |
| KHACH_HANG | âœ…           | âœ…            | âœ…              | âœ…            | âœ…         | âœ… (cá»§a mÃ¬nh) | âœ… (cá»§a mÃ¬nh) |

---

## VÃ­ dá»¥ test thá»±c táº¿ (Postman / curl)

### BÆ°á»›c 1 â€” ÄÄƒng nháº­p láº¥y token

```
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "h@g.com",
  "password": "123456"
}
```

> LÆ°u láº¡i `access_token` tá»« response Ä‘á»ƒ dÃ¹ng á»Ÿ cÃ¡c bÆ°á»›c sau.

---

### BÆ°á»›c 2 â€” Xem Ä‘Æ¡n hÃ ng Ä‘Ã£ nháº­n (trangThai = 5)

```
GET /api/v1/don-hang/cua-toi
Authorization: Bearer <access_token>
```

> TÃ¬m Ä‘Æ¡n hÃ ng cÃ³ `trangThai = 5`. Ghi láº¡i `id` cá»§a Ä‘Æ¡n hÃ ng Ä‘Ã³.

---

### BÆ°á»›c 3 â€” Xem chi tiáº¿t Ä‘Æ¡n hÃ ng Ä‘á»ƒ láº¥y `chiTietDonHangId`

```
GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}
Authorization: Bearer <access_token>
```

> Má»—i dÃ²ng trong `result` lÃ  1 sáº£n pháº©m trong Ä‘Æ¡n hÃ ng.  
> Ghi láº¡i `id` cá»§a dÃ²ng sáº£n pháº©m muá»‘n Ä‘Ã¡nh giÃ¡ â€” Ä‘Ã¢y lÃ  `chiTietDonHangId`.

---

### BÆ°á»›c 4 â€” Táº¡o Ä‘Ã¡nh giÃ¡

```
POST /api/v1/danh-gia-san-pham
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

chiTietDonHangId = 7
soSao            = 5
ghiTru           = Sáº£n pháº©m ráº¥t Ä‘áº¹p, Ä‘Ãºng size!
file             = (Ä‘á»ƒ trá»‘ng hoáº·c chá»n áº£nh)
```

**Response `201 Created`:**

```json
{
  "id": 4,
  "khachHangId": 3,
  "tenKhachHang": "Hoa",
  "chiTietDonHangId": 7,
  "donHangId": 3,
  "sanPhamId": 3,
  "tenSanPham": "VÃ¡y Hoa",
  "soSao": 5,
  "ghiTru": "Sáº£n pháº©m ráº¥t Ä‘áº¹p, Ä‘Ãºng size!",
  "hinhAnh": null,
  "ngayTao": "2026-03-06T10:00:00",
  "ngayCapNhat": null
}
```

---

### BÆ°á»›c 5 â€” Xem Ä‘Ã¡nh giÃ¡ theo sáº£n pháº©m (khÃ´ng cáº§n token náº¿u public)

```
GET /api/v1/danh-gia-san-pham/san-pham/3
Authorization: Bearer <access_token>
```

> Tráº£ vá» táº¥t cáº£ Ä‘Ã¡nh giÃ¡ cá»§a sáº£n pháº©m `id = 3` tá»« táº¥t cáº£ khÃ¡ch hÃ ng.

---

### BÆ°á»›c 6 â€” Cáº­p nháº­t Ä‘Ã¡nh giÃ¡

```
PUT /api/v1/danh-gia-san-pham/4
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

soSao  = 4
ghiTru = Äáº¹p nhÆ°ng giao hÆ¡i cháº­m
```

---

### BÆ°á»›c 7 â€” XÃ³a Ä‘Ã¡nh giÃ¡

```
DELETE /api/v1/danh-gia-san-pham/4
Authorization: Bearer <access_token>
```

**Response:** `204 No Content`

---

### LÆ°u Ã½ khi test

| TrÆ°á»ng há»£p                                       | Káº¿t quáº£ mong Ä‘á»£i                                  |
| ------------------------------------------------ | ------------------------------------------------- |
| Táº¡o Ä‘Ã¡nh giÃ¡ láº§n 2 cho cÃ¹ng `chiTietDonHangId`   | `400` â€” "Báº¡n Ä‘Ã£ Ä‘Ã¡nh giÃ¡ sáº£n pháº©m nÃ y rá»“i"        |
| ÄÆ¡n hÃ ng `trangThai != 5`                        | `400` â€” "ÄÆ¡n hÃ ng pháº£i á»Ÿ tráº¡ng thÃ¡i Ä‘Ã£ nháº­n hÃ ng" |
| `chiTietDonHangId` khÃ´ng thuá»™c Ä‘Æ¡n hÃ ng cá»§a mÃ¬nh | `400` â€” "ÄÆ¡n hÃ ng khÃ´ng thuá»™c vá» báº¡n"             |
| `soSao = 0` hoáº·c `soSao = 6`                     | `400` â€” "Sá»‘ sao pháº£i tá»« 1 Ä‘áº¿n 5"                  |
| Sá»­a/xÃ³a Ä‘Ã¡nh giÃ¡ cá»§a ngÆ°á»i khÃ¡c                  | `400` â€” "Báº¡n khÃ´ng cÃ³ quyá»n..."                   |


