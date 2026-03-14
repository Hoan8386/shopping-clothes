# Phiáº¿u Nháº­p Controller

> **Base Path:** `/api/v1/phieu-nhap`  
> **File:** `PhieuNhapController.java`  
> Quáº£n lÃ½ phiáº¿u nháº­p hÃ ng tá»« nhÃ  cung cáº¥p.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `PhieuNhap`

| TrÆ°á»ng              | Kiá»ƒu          | MÃ´ táº£                          |
| ------------------- | ------------- | ------------------------------ |
| `id`                | Long          | MÃ£ phiáº¿u nháº­p (auto-increment) |
| `cuaHang`           | CuaHang       | Cá»­a hÃ ng nháº­p hÃ ng (FK)        |
| `nhaCungCap`        | NhaCungCap    | NhÃ  cung cáº¥p (FK)              |
| `tenPhieuNhap`      | String(255)   | TÃªn phiáº¿u nháº­p                 |
| `trangThai`         | Integer       | Tráº¡ng thÃ¡i phiáº¿u nháº­p          |
| `ngayDatHang`       | LocalDateTime | NgÃ y Ä‘áº·t hÃ ng                  |
| `ngayNhanHang`      | LocalDateTime | NgÃ y thá»±c nháº­n hÃ ng            |
| `chiTietPhieuNhaps` | List          | Danh sÃ¡ch chi tiáº¿t phiáº¿u nháº­p  |
| `ngayTao`           | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)             |
| `ngayCapNhat`       | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)        |

### MÃ£ tráº¡ng thÃ¡i phiáº¿u nháº­p (`trangThai`)

| GiÃ¡ trá»‹ | Ã nghÄ©a        | MÃ´ táº£                                          |
| ------- | -------------- | ---------------------------------------------- |
| `0`     | ÄÃ£ Ä‘áº·t         | Phiáº¿u nháº­p má»›i táº¡o, chá» nhÃ  cung cáº¥p giao hÃ ng |
| `1`     | ÄÃ£ nháº­n        | HÃ ng Ä‘Ã£ Ä‘Æ°á»£c nháº­n, chá» kiá»ƒm kÃª                 |
| `2`     | Cháº­m giao      | NhÃ  cung cáº¥p giao cháº­m                         |
| `3`     | Há»§y            | Phiáº¿u nháº­p bá»‹ há»§y                              |
| `4`     | Thiáº¿u hÃ ng     | Kiá»ƒm kÃª xong, cÃ³ Ã­t nháº¥t 1 chi tiáº¿t thiáº¿u hÃ ng |
| `5`     | **HoÃ n thÃ nh** | Kiá»ƒm kÃª xong, táº¥t cáº£ chi tiáº¿t Ä‘áº§y Ä‘á»§           |

### Luá»“ng tráº¡ng thÃ¡i

```
Táº¡o phiáº¿u â†’ 0 (ÄÃ£ Ä‘áº·t)
  â”œâ”€â”€ Cáº­p nháº­t â†’ 1 (ÄÃ£ nháº­n) â†’ Kiá»ƒm kÃª â†’ 4 (Thiáº¿u hÃ ng) hoáº·c 5 (HoÃ n thÃ nh)
  â”‚                               â””â”€â”€ (Náº¿u 4) Cáº­p nháº­t chi tiáº¿t â†’ Kiá»ƒm kÃª láº¡i â†’ 5 (HoÃ n thÃ nh)
  â”œâ”€â”€ Cáº­p nháº­t â†’ 2 (Cháº­m giao) â†’ 1 (ÄÃ£ nháº­n) â†’ Kiá»ƒm kÃª
  â””â”€â”€ Cáº­p nháº­t â†’ 3 (Há»§y) â† KhÃ´ng thá»ƒ cáº­p nháº­t sau khi há»§y
```

**Quy táº¯c:**

- Phiáº¿u Ä‘Ã£ **há»§y** (3) â†’ khÃ´ng thá»ƒ cáº­p nháº­t
- Phiáº¿u Ä‘Ã£ **hoÃ n thÃ nh** (5) â†’ khÃ´ng thá»ƒ cáº­p nháº­t
- Phiáº¿u Ä‘ang **Ä‘Ã£ nháº­n** (1) hoáº·c **thiáº¿u hÃ ng** (4) â†’ cÃ³ thá»ƒ cáº­p nháº­t thÃ´ng tin (tÃªn, cá»­a hÃ ng, nhÃ  cung cáº¥p) nhÆ°ng **khÃ´ng thá»ƒ tá»± thay Ä‘á»•i tráº¡ng thÃ¡i thá»§ cÃ´ng** â€” tráº¡ng thÃ¡i chá»‰ thay Ä‘á»•i qua kiá»ƒm kÃª
- Khi chuyá»ƒn sang **ÄÃ£ nháº­n** (1), `ngayNhanHang` tá»± Ä‘á»™ng Ä‘Æ°á»£c gÃ¡n

---

## Response DTO: `ResPhieuNhapDTO`

```json
{
  "id": 1,
  "tenPhieuNhap": "Nháº­p hÃ ng Ä‘á»£t 1 - CN Q.1",
  "trangThai": 4,
  "trangThaiText": "Thiáº¿u hÃ ng",
  "ngayDatHang": "2026-03-01T10:00:00",
  "ngayNhanHang": "2026-03-03T10:00:00",
  "ngayTao": "2026-02-28T10:00:00",
  "ngayCapNhat": "2026-03-03T10:00:00",
  "cuaHang": {
    "id": 1,
    "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1",
    "diaChi": "123 Nguyá»…n Huá»‡, Q.1, TP.HCM",
    "soDienThoai": "02812345678"
  },
  "nhaCungCap": {
    "id": 1,
    "tenNhaCungCap": "CÃ´ng ty TNHH Váº£i Viá»‡t",
    "soDienThoai": "02838001001",
    "email": "vaiviet@ncc.com"
  },
  "chiTietPhieuNhaps": [
    {
      "id": 1,
      "chiTietSanPhamId": 1,
      "tenSanPham": "Ão Oxford",
      "tenMauSac": "Tráº¯ng",
      "tenKichThuoc": "M",
      "soLuong": 10,
      "soLuongThieu": 3,
      "soLuongDaNhap": 7,
      "ghiTru": null,
      "ghiTruKiemHang": "Thiáº¿u 3 cÃ¡i do hÆ° há»ng",
      "trangThai": 1,
      "trangThaiText": "Thiáº¿u"
    }
  ]
}
```

---

## 1. Láº¥y danh sÃ¡ch phiáº¿u nháº­p (cÃ³ lá»c + phÃ¢n trang)

| Thuá»™c tÃ­nh   | Chi tiáº¿t                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/phieu-nhap` |
| **Method**   | `GET`                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT)       |

**Query Parameters:**

| Tham sá»‘           | Kiá»ƒu          | Báº¯t buá»™c | MÃ´ táº£                        |
| ----------------- | ------------- | -------- | ---------------------------- |
| `tenPhieuNhap`    | String        | KhÃ´ng    | Lá»c theo tÃªn phiáº¿u nháº­p      |
| `trangThai`       | Integer       | KhÃ´ng    | Lá»c theo tráº¡ng thÃ¡i (0-5)    |
| `tenCuaHang`      | String        | KhÃ´ng    | Lá»c theo tÃªn cá»­a hÃ ng        |
| `tenNhaCungCap`   | String        | KhÃ´ng    | Lá»c theo tÃªn nhÃ  cung cáº¥p    |
| `ngayTaoTu`       | LocalDateTime | KhÃ´ng    | NgÃ y táº¡o tá»«                  |
| `ngayTaoDen`      | LocalDateTime | KhÃ´ng    | NgÃ y táº¡o Ä‘áº¿n                 |
| `ngayDatHangTu`   | LocalDateTime | KhÃ´ng    | NgÃ y Ä‘áº·t hÃ ng tá»«             |
| `ngayDatHangDen`  | LocalDateTime | KhÃ´ng    | NgÃ y Ä‘áº·t hÃ ng Ä‘áº¿n            |
| `ngayNhanHangTu`  | LocalDateTime | KhÃ´ng    | NgÃ y nháº­n hÃ ng tá»«            |
| `ngayNhanHangDen` | LocalDateTime | KhÃ´ng    | NgÃ y nháº­n hÃ ng Ä‘áº¿n           |
| `page`            | Integer       | KhÃ´ng    | Sá»‘ trang (máº·c Ä‘á»‹nh: 0)       |
| `size`            | Integer       | KhÃ´ng    | KÃ­ch thÆ°á»›c trang             |
| `sort`            | String        | KhÃ´ng    | Sáº¯p xáº¿p (vd: `ngayTao,desc`) |

**Response:** `200 OK` â€” Tráº£ vá» `ResultPaginationDTO`

```json
{
  "meta": {
    "page": 1,
    "pageSize": 20,
    "pages": 1,
    "total": 3
  },
  "result": [...]
}
```

---

## 2. Láº¥y phiáº¿u nháº­p theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/phieu-nhap/{id}` |
| **Method**   | `GET`                         |
| **XÃ¡c thá»±c** | Bearer Token (JWT)            |

**Response:** `200 OK` â€” Tráº£ vá» `ResPhieuNhapDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                     |
| ----------- | ------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y phiáº¿u nháº­p |

---

## 3. Táº¡o phiáº¿u nháº­p

| Thuá»™c tÃ­nh       | Chi tiáº¿t                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/phieu-nhap` |
| **Method**       | `POST`                    |
| **Content-Type** | `application/json`        |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)        |

**Request Body:** `ReqPhieuNhapDTO`

```json
{
  "tenPhieuNhap": "Nháº­p hÃ ng thÃ¡ng 3",
  "cuaHangId": 1,
  "nhaCungCapId": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "tenPhieuNhap": "String",
  "cuaHangId": "Long",
  "nhaCungCapId": "Long"
}
```

**Logic:**

- `trangThai` tá»± Ä‘á»™ng gÃ¡n = `0` (ÄÃ£ Ä‘áº·t)
- `ngayDatHang` tá»± Ä‘á»™ng gÃ¡n = thá»i Ä‘iá»ƒm hiá»‡n táº¡i

**Response:** `201 Created` â€” Tráº£ vá» `ResPhieuNhapDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                       |
| ----------- | --------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y cá»­a hÃ ng     |
| `400`       | KhÃ´ng tÃ¬m tháº¥y nhÃ  cung cáº¥p |

---

## 4. Cáº­p nháº­t phiáº¿u nháº­p

| Thuá»™c tÃ­nh       | Chi tiáº¿t                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/phieu-nhap` |
| **Method**       | `PUT`                    |
| **Content-Type** | `application/json`       |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)       |

**Request Body:** `ReqPhieuNhapDTO` (pháº£i cÃ³ `id`)

```json
{
  "id": 1,
  "tenPhieuNhap": "Nháº­p hÃ ng Ä‘á»£t 1 (cáº­p nháº­t)",
  "cuaHangId": 1,
  "nhaCungCapId": 1,
  "trangThai": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long (báº¯t buá»™c)",
  "tenPhieuNhap": "String",
  "cuaHangId": "Long",
  "nhaCungCapId": "Long",
  "trangThai": "Integer (0-5)"
}
```

**Logic:**

- Khi chuyá»ƒn sang **ÄÃ£ nháº­n** (1): `ngayNhanHang` tá»± Ä‘á»™ng gÃ¡n
- KhÃ´ng cho cáº­p nháº­t phiáº¿u Ä‘Ã£ **Há»§y** (3) hoáº·c **HoÃ n thÃ nh** (5)
- KhÃ´ng cho thay Ä‘á»•i tráº¡ng thÃ¡i thá»§ cÃ´ng khi Ä‘Ã£ **ÄÃ£ nháº­n** (1) hoáº·c **Thiáº¿u hÃ ng** (4) â€” tráº¡ng thÃ¡i chá»‰ thay Ä‘á»•i qua kiá»ƒm kÃª
- Khi phiáº¿u Ä‘ang **Thiáº¿u hÃ ng** (4), váº«n cÃ³ thá»ƒ cáº­p nháº­t thÃ´ng tin (tÃªn, cá»­a hÃ ng, nhÃ  cung cáº¥p)

**Response:** `200 OK` â€” Tráº£ vá» `ResPhieuNhapDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                         |
| ----------- | ------------------------------------------------------------- |
| `400`       | MÃ£ phiáº¿u nháº­p khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng                             |
| `400`       | KhÃ´ng tÃ¬m tháº¥y phiáº¿u nháº­p                                     |
| `400`       | Phiáº¿u nháº­p Ä‘Ã£ há»§y, khÃ´ng thá»ƒ cáº­p nháº­t                         |
| `400`       | Phiáº¿u nháº­p Ä‘Ã£ hoÃ n thÃ nh, khÃ´ng thá»ƒ cáº­p nháº­t                  |
| `400`       | KhÃ´ng thá»ƒ thay Ä‘á»•i tráº¡ng thÃ¡i thá»§ cÃ´ng khi phiáº¿u Ä‘Ã£ nháº­n hÃ ng |
| `400`       | Tráº¡ng thÃ¡i khÃ´ng há»£p lá»‡                                       |

---

## 5. Kiá»ƒm kÃª phiáº¿u nháº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                              |
| ------------ | ------------------------------------- |
| **URL**      | `PUT /api/v1/phieu-nhap/kiem-ke/{id}` |
| **Method**   | `PUT`                                 |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                    |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | MÃ´ táº£         |
| ------- | ---- | ------------- |
| `id`    | Long | MÃ£ phiáº¿u nháº­p |

**Äiá»u kiá»‡n:** Phiáº¿u nháº­p pháº£i á»Ÿ tráº¡ng thÃ¡i **ÄÃ£ nháº­n** (1) hoáº·c **Thiáº¿u hÃ ng** (4).

**Logic kiá»ƒm kÃª (sá»­ dá»¥ng delta Ä‘á»ƒ trÃ¡nh cá»™ng trÃ¹ng):**

1. Duyá»‡t tá»«ng chi tiáº¿t phiáº¿u nháº­p:
   - `trangThai = 0` (Ä‘á»§ hÃ ng): sá»‘ lÆ°á»£ng cáº§n nháº­p (`soLuongCanNhap`) = `soLuong`
   - `trangThai = 1` (thiáº¿u hÃ ng): `soLuongCanNhap` = `soLuong - soLuongThieu`
2. TÃ­nh **delta** = `soLuongCanNhap - soLuongDaNhap` (sá»‘ lÆ°á»£ng Ä‘Ã£ nháº­p tá»« láº§n kiá»ƒm kÃª trÆ°á»›c)
3. Cáº­p nháº­t `ChiTietSanPham.soLuong` += delta
4. GÃ¡n `ChiTietSanPham.maPhieuNhap` vÃ  `ChiTietSanPham.maCuaHang`
5. LÆ°u láº¡i `soLuongDaNhap = soLuongCanNhap` vÃ o chi tiáº¿t phiáº¿u nháº­p (dÃ¹ng cho láº§n kiá»ƒm kÃª tiáº¿p)
6. TÃ­nh láº¡i tá»•ng `SanPham.soLuong` = tá»•ng soLuong cá»§a táº¥t cáº£ ChiTietSanPham
7. Káº¿t quáº£:
   - CÃ³ Ã­t nháº¥t 1 chi tiáº¿t thiáº¿u â†’ phiáº¿u nháº­p = **4 (Thiáº¿u hÃ ng)**
   - Táº¥t cáº£ Ä‘á»§ â†’ phiáº¿u nháº­p = **5 (HoÃ n thÃ nh)**

> **VÃ­ dá»¥ kiá»ƒm kÃª 2 láº§n:**
>
> - Láº§n 1: `soLuong=10`, `soLuongThieu=3` â†’ `soLuongCanNhap=7`, `delta=7-0=7` â†’ kho +7, `soLuongDaNhap=7`, phiáº¿u = 4
> - Cáº­p nháº­t chi tiáº¿t: `soLuongThieu=0`, `trangThai=0`
> - Láº§n 2: `soLuongCanNhap=10`, `delta=10-7=3` â†’ kho +3, `soLuongDaNhap=10`, phiáº¿u = 5
> - Tá»•ng kho = 10 âœ“

**Response:** `200 OK` â€” Tráº£ vá» `ResPhieuNhapDTO`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                                                  |
| ----------- | ---------------------------------------------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y phiáº¿u nháº­p                                              |
| `400`       | Chá»‰ cÃ³ thá»ƒ kiá»ƒm kÃª phiáº¿u nháº­p á»Ÿ tráº¡ng thÃ¡i 'ÄÃ£ nháº­n' hoáº·c 'Thiáº¿u hÃ ng' |

---

## 6. XÃ³a phiáº¿u nháº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/phieu-nhap/{id}` |
| **Method**   | `DELETE`                         |
| **XÃ¡c thá»±c** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                     |
| ----------- | ------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y phiáº¿u nháº­p |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET (Xem) | POST (Táº¡o) | PUT (Sá»­a) | PUT (Kiá»ƒm kÃª) | DELETE (XÃ³a) |
| ---------- | --------- | ---------- | --------- | ------------- | ------------ |
| ADMIN      | âœ…        | âœ…         | âœ…        | âœ…            | âœ…           |
| NHAN_VIEN  | âœ…        | âœ…         | âœ…        | âœ…            | âŒ           |
| KHACH_HANG | âŒ        | âŒ         | âŒ        | âŒ            | âŒ           |

---

## HÆ°á»›ng dáº«n test luá»“ng nháº­p hÃ ng

> **Äiá»u kiá»‡n tiÃªn quyáº¿t:** Äáº£m báº£o Ä‘Ã£ cÃ³ dá»¯ liá»‡u **Cá»­a hÃ ng**, **NhÃ  cung cáº¥p**, vÃ  **Chi tiáº¿t sáº£n pháº©m** trong DB (cÃ³ thá»ƒ cháº¡y file `insert_data.sql`).

### BÆ°á»›c 1: ÄÄƒng nháº­p láº¥y Token

```
POST /api/v1/auth/login
```

```json
{
  "username": "lan@g.com",
  "password": "123456"
}
```

â†’ Copy `access_token` tá»« response, dÃ¹ng cho táº¥t cáº£ request sau:  
`Authorization: Bearer <access_token>`

---

### BÆ°á»›c 2: Táº¡o phiáº¿u nháº­p

```
POST /api/v1/phieu-nhap
```

```json
{
  "tenPhieuNhap": "Nháº­p hÃ ng thÃ¡ng 3",
  "cuaHangId": 1,
  "nhaCungCapId": 1
}
```

â†’ Há»‡ thá»‘ng tá»± gÃ¡n `trangThai = 0` (ÄÃ£ Ä‘áº·t) vÃ  `ngayDatHang` = thá»i Ä‘iá»ƒm hiá»‡n táº¡i.  
â†’ Ghi nhá»› `id` phiáº¿u nháº­p tráº£ vá» (vÃ­ dá»¥: `id = 1`).

---

### BÆ°á»›c 3: ThÃªm chi tiáº¿t phiáº¿u nháº­p (tá»«ng dÃ²ng sáº£n pháº©m)

```
POST /api/v1/chi-tiet-phieu-nhap
```

```json
{
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "trangThai": 0
}
```

- `chiTietSanPhamId`: ID biáº¿n thá»ƒ sáº£n pháº©m (vÃ­ dá»¥: Ão Oxford - Tráº¯ng - M)
- `trangThai = 0`: Ä‘á»§ hÃ ng | `trangThai = 1`: thiáº¿u hÃ ng
- Gá»i nhiá»u láº§n náº¿u phiáº¿u nháº­p cÃ³ nhiá»u sáº£n pháº©m

---

### BÆ°á»›c 4: Cáº­p nháº­t tráº¡ng thÃ¡i â†’ "ÄÃ£ nháº­n" (1)

```
PUT /api/v1/phieu-nhap
```

```json
{
  "id": 1,
  "tenPhieuNhap": "Nháº­p hÃ ng thÃ¡ng 3",
  "cuaHangId": 1,
  "nhaCungCapId": 1,
  "trangThai": 1
}
```

â†’ Há»‡ thá»‘ng tá»± gÃ¡n `ngayNhanHang`. Phiáº¿u chuyá»ƒn sang tráº¡ng thÃ¡i **ÄÃ£ nháº­n**.

---

### BÆ°á»›c 5 (tuá»³ chá»n): Cáº­p nháº­t chi tiáº¿t náº¿u cÃ³ thiáº¿u hÃ ng

```
PUT /api/v1/chi-tiet-phieu-nhap
```

```json
{
  "id": 1,
  "phieuNhapId": 1,
  "chiTietSanPhamId": 1,
  "soLuong": 50,
  "soLuongThieu": 5,
  "ghiTruKiemHang": "Thiáº¿u 5 cÃ¡i do hÆ° há»ng",
  "trangThai": 1
}
```

> CÃ³ thá»ƒ cáº­p nháº­t chi tiáº¿t khi phiáº¿u Ä‘ang á»Ÿ tráº¡ng thÃ¡i **ÄÃ£ nháº­n** (1) hoáº·c **Thiáº¿u hÃ ng** (4).

---

### BÆ°á»›c 6: Kiá»ƒm kÃª (nháº­p kho thá»±c táº¿)

```
PUT /api/v1/phieu-nhap/kiem-ke/1
```

â†’ Há»‡ thá»‘ng sáº½:

1. TÃ­nh `delta = soLuongCanNhap - soLuongDaNhap` cho má»—i chi tiáº¿t
2. Cá»™ng delta vÃ o tá»“n kho `ChiTietSanPham` (chá»‰ cá»™ng pháº§n chÃªnh lá»‡ch, khÃ´ng cá»™ng láº¡i tá»« Ä‘áº§u)
3. GÃ¡n `maPhieuNhap` vÃ  `maCuaHang` cho `ChiTietSanPham`
4. TÃ­nh láº¡i tá»•ng `SanPham.soLuong`
5. LÆ°u `soLuongDaNhap` = sá»‘ lÆ°á»£ng Ä‘Ã£ thá»±c nháº­p Ä‘á»ƒ dÃ¹ng cho láº§n kiá»ƒm kÃª tiáº¿p theo
6. Cáº­p nháº­t tráº¡ng thÃ¡i phiáº¿u:
   - **5 (HoÃ n thÃ nh)** náº¿u táº¥t cáº£ chi tiáº¿t Ä‘á»§ hÃ ng
   - **4 (Thiáº¿u hÃ ng)** náº¿u cÃ³ Ã­t nháº¥t 1 chi tiáº¿t thiáº¿u

> CÃ³ thá»ƒ gá»i kiá»ƒm kÃª nhiá»u láº§n khi phiáº¿u Ä‘ang **Thiáº¿u hÃ ng** (4) sau khi cáº­p nháº­t láº¡i chi tiáº¿t.

---

### TÃ³m táº¯t luá»“ng

```
Login â†’ Táº¡o phiáº¿u nháº­p (0-ÄÃ£ Ä‘áº·t)
      â†’ ThÃªm chi tiáº¿t sáº£n pháº©m
      â†’ Cáº­p nháº­t tráº¡ng thÃ¡i (1-ÄÃ£ nháº­n)
      â†’ (Tuá»³ chá»n) Cáº­p nháº­t chi tiáº¿t náº¿u thiáº¿u hÃ ng
      â†’ Kiá»ƒm kÃª â†’ (4-Thiáº¿u hÃ ng) hoáº·c (5-HoÃ n thÃ nh)
                      â†‘
      (Náº¿u 4) Cáº­p nháº­t chi tiáº¿t â†’ Kiá»ƒm kÃª láº¡i â†’ (5-HoÃ n thÃ nh)
```


