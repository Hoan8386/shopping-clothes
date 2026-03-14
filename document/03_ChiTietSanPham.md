# Chi Tiáº¿t Sáº£n Pháº©m Controller

> **Base Path:** `/api/v1/chi-tiet-san-pham`  
> **File:** `ChiTietSanPhamController.java`  
> Quáº£n lÃ½ chi tiáº¿t sáº£n pháº©m (biáº¿n thá»ƒ sáº£n pháº©m theo mÃ u sáº¯c, kÃ­ch thÆ°á»›c, cá»­a hÃ ng).

---

## Tá»•ng quan

Má»—i sáº£n pháº©m (`SanPham`) cÃ³ thá»ƒ cÃ³ nhiá»u biáº¿n thá»ƒ (`ChiTietSanPham`) khÃ¡c nhau theo **mÃ u sáº¯c**, **kÃ­ch thÆ°á»›c**, vÃ  phÃ¢n bá»• theo **cá»­a hÃ ng**.

### Cáº¥u trÃºc dá»¯ liá»‡u `ChiTietSanPham`

| TrÆ°á»ng      | Kiá»ƒu        | MÃ´ táº£                                     |
| ------------- | ------------- | -------------------------------------------- |
| `id`          | Long          | MÃ£ chi tiáº¿t sáº£n pháº©m (auto-increment) |
| `sanPham`     | SanPham       | Sáº£n pháº©m cha (FK)                        |
| `maPhieuNhap` | Long          | MÃ£ phiáº¿u nháº­p liÃªn quan (nullable)     |
| `mauSac`      | MauSac        | MÃ u sáº¯c (FK)                              |
| `kichThuoc`   | KichThuoc     | KÃ­ch thÆ°á»›c (FK)                          |
| `maCuaHang`   | Long          | MÃ£ cá»­a hÃ ng phÃ¢n bá»•                   |
| `soLuong`     | Integer       | Sá»‘ lÆ°á»£ng tá»“n kho                      |
| `trangThai`   | Integer       | Tráº¡ng thÃ¡i (0: áº©n, 1: hiá»ƒn thá»‹)     |
| `moTa`        | String(255)   | MÃ´ táº£ chi tiáº¿t                          |
| `ghiTru`      | String(255)   | Ghi chÃº trá»«                               |
| `ngayTao`     | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)                   |
| `ngayCapNhat` | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)            |

---

## 1. Láº¥y táº¥t cáº£ chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                      |
| --------------- | ------------------------------- |
| **URL**         | `GET /api/v1/chi-tiet-san-pham` |
| **Method**      | `GET`                           |
| **XÃ¡c thá»±c** | Bearer Token (JWT)              |

**Query Parameters (lá»c tÃ¹y chá»n):**

| Tham sá»‘     | Kiá»ƒu  | Báº¯t buá»™c | MÃ´ táº£                                     |
| ------------- | ------- | ------------ | -------------------------------------------- |
| `sanPhamId`   | Long    | KhÃ´ng       | Lá»c theo mÃ£ sáº£n pháº©m                   |
| `mauSacId`    | Long    | KhÃ´ng       | Lá»c theo mÃ£ mÃ u sáº¯c                     |
| `kichThuocId` | Long    | KhÃ´ng       | Lá»c theo mÃ£ kÃ­ch thÆ°á»›c                 |
| `maCuaHang`   | Long    | KhÃ´ng       | Lá»c theo mÃ£ cá»­a hÃ ng                    |
| `trangThai`   | Integer | KhÃ´ng       | Lá»c theo tráº¡ng thÃ¡i (0: áº©n, 1: hiá»ƒn) |

**VÃ­ dá»¥ request:**

```
GET /api/v1/chi-tiet-san-pham?sanPhamId=1&mauSacId=2&kichThuocId=3&maCuaHang=1&trangThai=1
```

**Response:** `200 OK` â€” Tráº£ vá» `List<ResChiTietSanPhamDTO>`

```json
[
  {
    "id": 1,
    "maPhieuNhap": null,
    "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1",
    "soLuong": 15,
    "trangThai": 1,
    "moTa": "Ão polo Ä‘en size M",
    "ghiTru": null,
    "tenSanPham": "Ão Polo Classic",
    "tenMauSac": "Äen",
    "tenKichThuoc": "M",
    "hinhAnhUrls": ["polo-den-m-1.jpg", "polo-den-m-2.jpg"]
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "maPhieuNhap": "Long | null",
  "tenCuaHang": "String",
  "soLuong": "Integer",
  "trangThai": "Integer",
  "moTa": "String",
  "ghiTru": "String",
  "tenSanPham": "String",
  "tenMauSac": "String",
  "tenKichThuoc": "String",
  "hinhAnhUrls": "List<String>"
}
```

> **LÆ°u Ã½:** Náº¿u khÃ´ng truyá»n báº¥t ká»³ tham sá»‘ lá»c nÃ o â†’ tráº£ vá» táº¥t cáº£ chi tiáº¿t sáº£n pháº©m.

---

## 2. Láº¥y chi tiáº¿t sáº£n pháº©m theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                           |
| --------------- | ------------------------------------ |
| **URL**         | `GET /api/v1/chi-tiet-san-pham/{id}` |
| **Method**      | `GET`                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                   |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£                    |
| --------- | ------ | --------------------------- |
| `id`      | Long   | MÃ£ chi tiáº¿t sáº£n pháº©m |

**Response:** `200 OK` â€” Tráº£ vá» `ResChiTietSanPhamDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                   |
| ----------- | ------------------------------------------ |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t sáº£n pháº©m |

---

## 3. Láº¥y chi tiáº¿t sáº£n pháº©m theo mÃ£ sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                           |
| --------------- | ---------------------------------------------------- |
| **URL**         | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}` |
| **Method**      | `GET`                                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                   |

**Path Parameters:**

| Tham sá»‘   | Kiá»ƒu | MÃ´ táº£             |
| ----------- | ------ | -------------------- |
| `sanPhamId` | Long   | MÃ£ sáº£n pháº©m cha |

**Response:** `200 OK` â€” Tráº£ vá» `List<ResChiTietSanPhamDTO>`

> **LÆ°u Ã½:** Tráº£ vá» táº¥t cáº£ biáº¿n thá»ƒ (mÃ u sáº¯c, kÃ­ch thÆ°á»›c) cá»§a sáº£n pháº©m Ä‘Æ°á»£c chá»‰ Ä‘á»‹nh.

---

## 3.1 Láº¥y chi tiáº¿t sáº£n pháº©m theo cá»­a hÃ ng cá»§a nhÃ¢n viÃªn Ä‘ang Ä‘Äƒng nháº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                            |
| --------------- | ----------------------------------------------------- |
| **URL**         | `GET /api/v1/chi-tiet-san-pham/san-pham-tai-cua-hang` |
| **Method**      | `GET`                                                 |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                                    |

**MÃ´ táº£ nghiá»‡p vá»¥:**

- Endpoint dÃ nh cho `NHAN_VIEN` (vÃ  `ADMIN`) Ä‘á»ƒ xem tá»“n kho theo Ä‘Ãºng cá»­a hÃ ng Ä‘Æ°á»£c gÃ¡n trong tÃ i khoáº£n nhÃ¢n viÃªn.
- KhÃ´ng cáº§n truyá»n query/path param.
- Há»‡ thá»‘ng tá»± láº¥y email tá»« token Ä‘á»ƒ xÃ¡c Ä‘á»‹nh nhÃ¢n viÃªn vÃ  cá»­a hÃ ng.

**Response:** `200 OK` â€” Tráº£ vá» `List<ResChiTietSanPhamDTO>`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                |
| ----------- | ------------------------------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y nhÃ¢n viÃªn / chÆ°a gÃ¡n cá»­a hÃ ng |
| `403`       | KhÃ´ng cÃ³ quyá»n truy cáº­p endpoint nÃ y              |

---

## 4. Táº¡o chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh    | Chi tiáº¿t                       |
| ---------------- | -------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-san-pham` |
| **Method**       | `POST`                           |
| **Content-Type** | `multipart/form-data`            |
| **XÃ¡c thá»±c**  | Bearer Token (JWT)               |

**Form Data:**

| Tham sá»‘     | Kiá»ƒu       | Báº¯t buá»™c | MÃ´ táº£                                        |
| ------------- | ------------ | ------------ | ----------------------------------------------- |
| `sanPhamId`   | Long         | KhÃ´ng       | MÃ£ sáº£n pháº©m                                |
| `maPhieuNhap` | Long         | KhÃ´ng       | MÃ£ phiáº¿u nháº­p                              |
| `mauSacId`    | Long         | KhÃ´ng       | MÃ£ mÃ u sáº¯c                                  |
| `kichThuocId` | Long         | KhÃ´ng       | MÃ£ kÃ­ch thÆ°á»›c                              |
| `soLuong`     | Integer      | KhÃ´ng       | Sá»‘ lÆ°á»£ng                                   |
| `trangThai`   | Integer      | KhÃ´ng       | Tráº¡ng thÃ¡i                                   |
| `moTa`        | String       | KhÃ´ng       | MÃ´ táº£                                        |
| `ghiTru`      | String       | KhÃ´ng       | Ghi chÃº trá»«                                  |
| `files`       | List\<File\> | KhÃ´ng       | Danh sÃ¡ch hÃ¬nh áº£nh (upload lÃªn Cloudinary) |

**Response:** `201 Created` â€” Tráº£ vá» `List<ResChiTietSanPhamDTO>`

> **LÆ°u Ã½:** Khi táº¡o chi tiáº¿t sáº£n pháº©m, há»‡ thá»‘ng tá»± Ä‘á»™ng táº¡o cho **Táº¤T Cáº¢ cá»­a hÃ ng** hiá»‡n cÃ³ vÃ  cáº­p nháº­t tá»•ng `soLuong` cá»§a sáº£n pháº©m cha.

---

## 5. Cáº­p nháº­t chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh    | Chi tiáº¿t                      |
| ---------------- | ------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-san-pham` |
| **Method**       | `PUT`                           |
| **Content-Type** | `multipart/form-data`           |
| **XÃ¡c thá»±c**  | Bearer Token (JWT)              |

**Form Data:** Giá»‘ng táº¡o má»›i, thÃªm trÆ°á»ng:

| Tham sá»‘ | Kiá»ƒu | Báº¯t buá»™c | MÃ´ táº£                                       |
| --------- | ------ | ------------ | ---------------------------------------------- |
| `id`      | Long   | **CÃ³**      | MÃ£ chi tiáº¿t sáº£n pháº©m cáº§n cáº­p nháº­t |

**Response:** `200 OK` â€” Tráº£ vá» `ResChiTietSanPhamDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                  |
| ----------- | --------------------------------------------------------- |
| `400`       | MÃ£ chi tiáº¿t sáº£n pháº©m khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## 6. XÃ³a chi tiáº¿t sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                              |
| --------------- | --------------------------------------- |
| **URL**         | `DELETE /api/v1/chi-tiet-san-pham/{id}` |
| **Method**      | `DELETE`                                |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                      |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£                    |
| --------- | ------ | --------------------------- |
| `id`      | Long   | MÃ£ chi tiáº¿t sáº£n pháº©m |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                   |
| ----------- | ------------------------------------------ |
| `400`       | KhÃ´ng tÃ¬m tháº¥y chi tiáº¿t sáº£n pháº©m |

---

## PhÃ¢n quyá»n

| Vai trÃ²   | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | DELETE (XÃ³a) |
| ---------- | --------- | ------------ | ----------- | ------------- |
| ADMIN      | âœ…       | âœ…          | âœ…         | âœ…           |
| NHAN_VIEN  | âœ…       | âŒ           | âŒ          | âŒ            |
| KHACH_HANG | âœ…       | âŒ           | âŒ          | âŒ            |
