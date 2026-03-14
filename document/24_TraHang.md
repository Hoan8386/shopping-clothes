# Tráº£ HÃ ng Controller

> **Base Path:** `/api/v1/tra-hang`  
> **File:** `TraHangController.java`  
> Quáº£n lÃ½ phiáº¿u tráº£ hÃ ng â€” KhÃ¡ch hÃ ng táº¡o yÃªu cáº§u tráº£ hÃ ng sau khi Ä‘Æ¡n hÃ ng Ä‘Ã£ nháº­n (tráº¡ng thÃ¡i 5), nhÃ¢n viÃªn/admin duyá»‡t hoáº·c tá»« chá»‘i.

---

## Tá»•ng quan

### Cáº¥u trÃºc dá»¯ liá»‡u `TraHang`

| TrÆ°á»ng            | Kiá»ƒu          | MÃ´ táº£                                          |
| ----------------- | ------------- | ---------------------------------------------- |
| `id`              | Long          | MÃ£ phiáº¿u tráº£ (PK, auto-increment)              |
| `donHang`         | DonHang       | ÄÆ¡n hÃ ng gá»‘c (FK)                              |
| `lyDoTraHang`     | String(255)   | LÃ½ do tráº£ hÃ ng                                 |
| `trangThai`       | Integer       | Tráº¡ng thÃ¡i phiáº¿u tráº£ (xem báº£ng bÃªn dÆ°á»›i)       |
| `tongTien`        | Double        | Tá»•ng tiá»n tráº£ (= tongTienTra âˆ’ giÃ¡ gá»‘c SP tráº£) |
| `lyDoTuChoi`      | String(255)   | LÃ½ do tá»« chá»‘i (náº¿u cÃ³)                         |
| `chiTietTraHangs` | List          | Danh sÃ¡ch chi tiáº¿t sáº£n pháº©m tráº£                |
| `ngayTao`         | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)                             |
| `ngayCapNhat`     | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)                        |

### Cáº¥u trÃºc dá»¯ liá»‡u `ChiTietTraHang`

| TrÆ°á»ng        | Kiá»ƒu           | MÃ´ táº£                                 |
| ------------- | -------------- | ------------------------------------- |
| `id`          | Long           | MÃ£ chi tiáº¿t tráº£ hÃ ng (auto-increment) |
| `traHang`     | TraHang        | Phiáº¿u tráº£ hÃ ng (FK)                   |
| `sanPhamTra`  | ChiTietDonHang | Chi tiáº¿t Ä‘Æ¡n hÃ ng Ä‘Æ°á»£c tráº£ (FK)       |
| `ghiTru`      | String(255)    | Ghi chÃº cho sáº£n pháº©m tráº£              |
| `trangThai`   | Integer        | Tráº¡ng thÃ¡i (Ä‘á»“ng bá»™ vá»›i phiáº¿u tráº£)    |
| `ngayTao`     | LocalDateTime  | NgÃ y táº¡o (tá»± Ä‘á»™ng)                    |
| `ngayCapNhat` | LocalDateTime  | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)               |

### MÃ£ tráº¡ng thÃ¡i phiáº¿u tráº£ hÃ ng (`trangThai`)

| GiÃ¡ trá»‹ | Ã nghÄ©a   | MÃ´ táº£                                              |
| ------- | --------- | -------------------------------------------------- |
| `0`     | Chá» xá»­ lÃ½ | Phiáº¿u tráº£ má»›i táº¡o, chá» nhÃ¢n viÃªn/admin duyá»‡t       |
| `1`     | ÄÃ£ duyá»‡t  | Phiáº¿u tráº£ Ä‘Æ°á»£c cháº¥p nháº­n, hoÃ n tiá»n cho khÃ¡ch hÃ ng |
| `2`     | Tá»« chá»‘i   | Phiáº¿u tráº£ bá»‹ tá»« chá»‘i                               |

### Luá»“ng chuyá»ƒn tráº¡ng thÃ¡i

- **KhÃ¡ch hÃ ng** táº¡o phiáº¿u: ÄÆ¡n hÃ ng (tráº¡ng thÃ¡i 5) â†’ Táº¡o phiáº¿u tráº£ (tráº¡ng thÃ¡i 0)
- **NhÃ¢n viÃªn/Admin** duyá»‡t: `0 â†’ 1` (ÄÃ£ duyá»‡t) hoáº·c `0 â†’ 2` (Tá»« chá»‘i)
- Chá»‰ cáº­p nháº­t Ä‘Æ°á»£c khi phiáº¿u Ä‘ang á»Ÿ tráº¡ng thÃ¡i `0` (Chá» xá»­ lÃ½)

### CÃ´ng thá»©c tÃ­nh tá»•ng tiá»n tráº£

```
tongTien = tongTienTra (Ä‘Æ¡n hÃ ng) âˆ’ Î£(giaSanPham Ã— soLuong) cá»§a cÃ¡c sáº£n pháº©m tráº£
```

- `tongTienTra`: Tá»•ng tiá»n khÃ¡ch hÃ ng Ä‘Ã£ thanh toÃ¡n cho Ä‘Æ¡n hÃ ng
- `giaSanPham Ã— soLuong`: GiÃ¡ gá»‘c (chÆ°a giáº£m) Ã— sá»‘ lÆ°á»£ng cá»§a tá»«ng sáº£n pháº©m tráº£

**VÃ­ dá»¥:** ÄÆ¡n hÃ ng cÃ³ tongTienTra = 900.000Ä‘. KhÃ¡ch tráº£ 1 sáº£n pháº©m (giaSanPham = 200.000Ä‘, soLuong = 3):

```
tongTien = 900.000 âˆ’ (200.000 Ã— 3) = 900.000 âˆ’ 600.000 = 300.000Ä‘
```

### Äiá»u kiá»‡n táº¡o phiáº¿u tráº£ hÃ ng

| Äiá»u kiá»‡n                                        | MÃ´ táº£                                       |
| ------------------------------------------------ | ------------------------------------------- |
| âœ… ÄÃ£ Ä‘Äƒng nháº­p                                  | Láº¥y thÃ´ng tin KH tá»« JWT token               |
| âœ… ÄÆ¡n hÃ ng **tráº¡ng thÃ¡i = 5** (ÄÃ£ nháº­n hÃ ng)    | Chá»‰ tráº£ Ä‘Æ°á»£c Ä‘Æ¡n Ä‘Ã£ nháº­n                    |
| âœ… KhÃ¡ch hÃ ng lÃ  **chá»§ Ä‘Æ¡n hÃ ng**                | KhÃ´ng Ä‘Æ°á»£c phÃ©p tráº£ Ä‘Æ¡n hÃ ng cá»§a ngÆ°á»i khÃ¡c |
| âœ… Chá»n **Ã­t nháº¥t 1 sáº£n pháº©m** Ä‘á»ƒ tráº£            | Pháº£i chá»n sáº£n pháº©m cáº§n tráº£                  |
| âœ… Chi tiáº¿t Ä‘Æ¡n hÃ ng **thuá»™c Ä‘Æ¡n hÃ ng Ä‘ang tráº£** | KhÃ´ng trá»™n sáº£n pháº©m tá»« Ä‘Æ¡n khÃ¡c             |

---

## 1. Táº¡o phiáº¿u tráº£ hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                |
| ------------ | ----------------------- |
| **URL**      | `POST /api/v1/tra-hang` |
| **Method**   | `POST`                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)      |

**Request Body:**

```json
{
  "donHangId": 5,
  "lyDoTraHang": "Sáº£n pháº©m bá»‹ lá»—i, khÃ´ng Ä‘Ãºng mÃ´ táº£",
  "chiTietTraHangs": [
    {
      "chiTietDonHangId": 6,
      "ghiTru": "Ão bá»‹ rÃ¡ch á»Ÿ tay"
    },
    {
      "chiTietDonHangId": 7,
      "ghiTru": "Quáº§n khÃ´ng Ä‘Ãºng mÃ u"
    }
  ]
}
```

| TrÆ°á»ng                               | Kiá»ƒu   | Báº¯t buá»™c | MÃ´ táº£                        |
| ------------------------------------ | ------ | -------- | ---------------------------- |
| `donHangId`                          | Long   | CÃ³       | MÃ£ Ä‘Æ¡n hÃ ng cáº§n tráº£          |
| `lyDoTraHang`                        | String | CÃ³       | LÃ½ do tráº£ hÃ ng               |
| `chiTietTraHangs`                    | Array  | CÃ³       | Danh sÃ¡ch sáº£n pháº©m tráº£       |
| `chiTietTraHangs[].chiTietDonHangId` | Long   | CÃ³       | MÃ£ chi tiáº¿t Ä‘Æ¡n hÃ ng cáº§n tráº£ |
| `chiTietTraHangs[].ghiTru`           | String | KhÃ´ng    | Ghi chÃº cho sáº£n pháº©m tráº£     |

**Response:** `201 Created`

```json
{
  "statusCode": 201,
  "error": null,
  "message": "Táº¡o phiáº¿u tráº£ hÃ ng",
  "data": {
    "id": 1,
    "donHangId": 5,
    "lyDoTraHang": "Sáº£n pháº©m bá»‹ lá»—i, khÃ´ng Ä‘Ãºng mÃ´ táº£",
    "trangThai": "Chá» xá»­ lÃ½",
    "tongTien": 0.0,
    "ngayTao": "2026-03-12T10:30:00",
    "ngayCapNhat": null,
    "chiTietTraHangs": [
      {
        "id": 1,
        "chiTietDonHangId": 6,
        "tenSanPham": "Ão Oxford",
        "hinhAnhChinh": "ao-oxford.jpg",
        "tenMauSac": "Tráº¯ng",
        "tenKichThuoc": "M",
        "giaSanPham": 200.0,
        "soLuong": 3,
        "thanhTien": 600.0,
        "ghiTru": "Ão bá»‹ rÃ¡ch á»Ÿ tay",
        "trangThai": "Chá» xá»­ lÃ½"
      },
      {
        "id": 2,
        "chiTietDonHangId": 7,
        "tenSanPham": "Quáº§n Jean",
        "hinhAnhChinh": null,
        "tenMauSac": "Äen",
        "tenKichThuoc": "M",
        "giaSanPham": 400.0,
        "soLuong": 1,
        "thanhTien": 400.0,
        "ghiTru": "Quáº§n khÃ´ng Ä‘Ãºng mÃ u",
        "trangThai": "Chá» xá»­ lÃ½"
      }
    ]
  }
}
```

**Lá»—i thÆ°á»ng gáº·p:**

| MÃ£ lá»—i | NguyÃªn nhÃ¢n                                     |
| ------ | ----------------------------------------------- |
| `400`  | ÄÆ¡n hÃ ng khÃ´ng á»Ÿ tráº¡ng thÃ¡i "ÄÃ£ nháº­n hÃ ng"      |
| `400`  | KhÃ´ng tÃ¬m tháº¥y Ä‘Æ¡n hÃ ng                         |
| `400`  | KhÃ´ng cÃ³ quyá»n tráº£ Ä‘Æ¡n hÃ ng nÃ y                 |
| `400`  | KhÃ´ng chá»n sáº£n pháº©m Ä‘á»ƒ tráº£                      |
| `400`  | Chi tiáº¿t Ä‘Æ¡n hÃ ng khÃ´ng thuá»™c Ä‘Æ¡n hÃ ng Ä‘ang tráº£ |

---

## 2. Láº¥y danh sÃ¡ch phiáº¿u tráº£ hÃ ng (phÃ¢n trang)

| Thuá»™c tÃ­nh   | Chi tiáº¿t               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/tra-hang` |
| **Method**   | `GET`                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham sá»‘ | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                           |
| ------- | ------- | -------- | ------------------------------- |
| `page`  | Integer | KhÃ´ng    | Sá»‘ trang (máº·c Ä‘á»‹nh: 0)          |
| `size`  | Integer | KhÃ´ng    | KÃ­ch thÆ°á»›c trang (máº·c Ä‘á»‹nh: 20) |
| `sort`  | String  | KhÃ´ng    | Sáº¯p xáº¿p (vd: `ngayTao,desc`)    |

**VÃ­ dá»¥ request:**

```
GET /api/v1/tra-hang?page=0&size=10&sort=ngayTao,desc
```

**Response:** `200 OK` â€” Tráº£ vá» `ResultPaginationDTO`

```json
{
    "statusCode": 200,
    "error": null,
    "message": "Láº¥y danh sÃ¡ch phiáº¿u tráº£ hÃ ng",
    "data": {
        "meta": {
            "page": 1,
            "pageSize": 10,
            "pages": 1,
            "total": 2
        },
        "result": [
            {
                "id": 1,
                "donHangId": 5,
                "lyDoTraHang": "Sáº£n pháº©m bá»‹ lá»—i",
                "trangThai": "Chá» xá»­ lÃ½",
                "tongTien": 0.0,
                "ngayTao": "2026-03-12T10:30:00",
                "ngayCapNhat": null,
                "chiTietTraHangs": [...]
            }
        ]
    }
}
```

---

## 3. Láº¥y phiáº¿u tráº£ hÃ ng theo mÃ£

| Thuá»™c tÃ­nh   | Chi tiáº¿t                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/tra-hang/{id}` |
| **Method**   | `GET`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | Báº¯t buá»™c | MÃ´ táº£             |
| ------- | ---- | -------- | ----------------- |
| `id`    | Long | CÃ³       | MÃ£ phiáº¿u tráº£ hÃ ng |

**VÃ­ dá»¥ request:**

```
GET /api/v1/tra-hang/1
```

**Response:** `200 OK` â€” Tráº£ vá» `ResTraHangDTO` (cáº¥u trÃºc giá»‘ng response táº¡o phiáº¿u)

---

## 4. Láº¥y danh sÃ¡ch phiáº¿u tráº£ theo Ä‘Æ¡n hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                                    |
| ------------ | ------------------------------------------- |
| **URL**      | `GET /api/v1/tra-hang/don-hang/{donHangId}` |
| **Method**   | `GET`                                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                          |

**Path Parameters:**

| Tham sá»‘     | Kiá»ƒu | Báº¯t buá»™c | MÃ´ táº£       |
| ----------- | ---- | -------- | ----------- |
| `donHangId` | Long | CÃ³       | MÃ£ Ä‘Æ¡n hÃ ng |

**VÃ­ dá»¥ request:**

```
GET /api/v1/tra-hang/don-hang/5
```

**Response:** `200 OK` â€” Tráº£ vá» `List<ResTraHangDTO>`

```json
{
    "statusCode": 200,
    "error": null,
    "message": "Láº¥y danh sÃ¡ch phiáº¿u tráº£ hÃ ng theo Ä‘Æ¡n hÃ ng",
    "data": [
        {
            "id": 1,
            "donHangId": 5,
            "lyDoTraHang": "Sáº£n pháº©m bá»‹ lá»—i",
            "trangThai": "Chá» xá»­ lÃ½",
            "tongTien": 0.0,
            "ngayTao": "2026-03-12T10:30:00",
            "ngayCapNhat": null,
            "chiTietTraHangs": [...]
        }
    ]
}
```

---

## 5. Cáº­p nháº­t tráº¡ng thÃ¡i phiáº¿u tráº£ hÃ ng

| Thuá»™c tÃ­nh   | Chi tiáº¿t                               |
| ------------ | -------------------------------------- |
| **URL**      | `PUT /api/v1/tra-hang/{id}/trang-thai` |
| **Method**   | `PUT`                                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT) â€” Admin/NhÃ¢n viÃªn   |

**Path Parameters:**

| Tham sá»‘ | Kiá»ƒu | Báº¯t buá»™c | MÃ´ táº£             |
| ------- | ---- | -------- | ----------------- |
| `id`    | Long | CÃ³       | MÃ£ phiáº¿u tráº£ hÃ ng |

**Query Parameters:**

| Tham sá»‘     | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                                   |
| ----------- | ------- | -------- | --------------------------------------- |
| `trangThai` | Integer | CÃ³       | Tráº¡ng thÃ¡i má»›i (1 = Duyá»‡t, 2 = Tá»« chá»‘i) |

**VÃ­ dá»¥ request:**

```
PUT /api/v1/tra-hang/1/trang-thai?trangThai=1
```

**Response:** `200 OK`

```json
{
    "statusCode": 200,
    "error": null,
    "message": "Cáº­p nháº­t tráº¡ng thÃ¡i phiáº¿u tráº£ hÃ ng",
    "data": {
        "id": 1,
        "donHangId": 5,
        "lyDoTraHang": "Sáº£n pháº©m bá»‹ lá»—i",
        "trangThai": "ÄÃ£ duyá»‡t",
        "tongTien": 0.0,
        "ngayTao": "2026-03-12T10:30:00",
        "ngayCapNhat": "2026-03-12T14:00:00",
        "chiTietTraHangs": [...]
    }
}
```

**Lá»—i thÆ°á»ng gáº·p:**

| MÃ£ lá»—i | NguyÃªn nhÃ¢n                                      |
| ------ | ------------------------------------------------ |
| `400`  | KhÃ´ng tÃ¬m tháº¥y phiáº¿u tráº£ hÃ ng                    |
| `400`  | Phiáº¿u khÃ´ng á»Ÿ tráº¡ng thÃ¡i "Chá» xá»­ lÃ½"             |
| `400`  | Tráº¡ng thÃ¡i khÃ´ng há»£p lá»‡ (chá»‰ cháº¥p nháº­n 1 hoáº·c 2) |


