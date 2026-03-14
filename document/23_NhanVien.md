# NhÃ¢n ViÃªn Controller

> **Base Path:** `/api/v1/nhan-vien`  
> **File:** `NhanVienController.java`  
> Quáº£n lÃ½ thÃ´ng tin nhÃ¢n viÃªn (xem danh sÃ¡ch, thÃªm, cáº­p nháº­t, xÃ³a).

---

## Cáº¥u trÃºc dá»¯ liá»‡u tráº£ vá» `ResNhanVienDTO`

| TrÆ°á»ng           | Kiá»ƒu          | MÃ´ táº£                            |
| ---------------- | ------------- | -------------------------------- |
| `id`             | Long          | MÃ£ nhÃ¢n viÃªn                     |
| `tenNhanVien`    | String        | TÃªn nhÃ¢n viÃªn                    |
| `email`          | String        | Email                            |
| `soDienThoai`    | String        | Sá»‘ Ä‘iá»‡n thoáº¡i                    |
| `ngayBatDauLam`  | LocalDateTime | NgÃ y báº¯t Ä‘áº§u lÃ m                 |
| `ngayKetThucLam` | LocalDateTime | NgÃ y káº¿t thÃºc lÃ m                |
| `trangThai`      | Integer       | Tráº¡ng thÃ¡i nhÃ¢n viÃªn             |
| `cuaHang`        | Object        | ThÃ´ng tin cá»­a hÃ ng cá»§a nhÃ¢n viÃªn |
| `role`           | Object        | ThÃ´ng tin vai trÃ²                |

**`cuaHang` gá»“m:** `id`, `tenCuaHang`, `diaChi`, `soDienThoai`, `email`, `trangThai`  
**`role` gá»“m:** `id`, `name`, `description`, `active`

---

## 1. Xem danh sÃ¡ch nhÃ¢n viÃªn

| Thuá»™c tÃ­nh   | Chi tiáº¿t                |
| ------------ | ----------------------- |
| **URL**      | `GET /api/v1/nhan-vien` |
| **Method**   | `GET`                   |
| **XÃ¡c thá»±c** | Bearer Token (JWT)      |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenNhanVien": "An",
    "email": "an@s.com",
    "soDienThoai": "0901000001",
    "ngayBatDauLam": null,
    "ngayKetThucLam": null,
    "trangThai": 1,
    "cuaHang": {
      "id": 1,
      "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1",
      "diaChi": "123 Nguyá»…n Huá»‡, Q.1, TP.HCM",
      "soDienThoai": "02812345678",
      "email": "q1@shop.com",
      "trangThai": 1
    },
    "role": {
      "id": 2,
      "name": "NHAN_VIEN",
      "description": "NhÃ¢n viÃªn bÃ¡n hÃ ng",
      "active": true
    }
  }
]
```

**Quy táº¯c dá»¯ liá»‡u tráº£ vá» theo vai trÃ²:**

- `ADMIN`: xem toÃ n bá»™ nhÃ¢n viÃªn.
- `NHAN_VIEN`: chá»‰ xem nhÃ¢n viÃªn cÃ¹ng cá»­a hÃ ng vá»›i tÃ i khoáº£n Ä‘ang Ä‘Äƒng nháº­p.

---

## 2. Táº¡o nhÃ¢n viÃªn

| Thuá»™c tÃ­nh   | Chi tiáº¿t                 |
| ------------ | ------------------------ |
| **URL**      | `POST /api/v1/nhan-vien` |
| **Method**   | `POST`                   |
| **XÃ¡c thá»±c** | Bearer Token (JWT)       |

**Request Body:**

```json
{
  "cuaHang": { "id": 1 },
  "tenNhanVien": "Nguyen Van A",
  "email": "nva@s.com",
  "soDienThoai": "0901234567",
  "matKhau": "123456",
  "ngayBatDauLam": "2026-03-12T08:00:00",
  "trangThai": 1,
  "role": { "id": 2 }
}
```

> Náº¿u khÃ´ng truyá»n `role`, há»‡ thá»‘ng sáº½ tá»± gÃ¡n role máº·c Ä‘á»‹nh nhÃ¢n viÃªn.

**Response:** `201 Created`

```json
{
  "id": 10,
  "tenNhanVien": "Nguyen Van A",
  "email": "nva@s.com",
  "soDienThoai": "0901234567",
  "ngayBatDauLam": "2026-03-12T08:00:00",
  "ngayKetThucLam": null,
  "trangThai": 1,
  "cuaHang": {
    "id": 1,
    "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1",
    "diaChi": "123 Nguyá»…n Huá»‡, Q.1, TP.HCM",
    "soDienThoai": "02812345678",
    "email": "q1@shop.com",
    "trangThai": 1
  },
  "role": {
    "id": 2,
    "name": "NHAN_VIEN",
    "description": "NhÃ¢n viÃªn bÃ¡n hÃ ng",
    "active": true
  }
}
```

---

## 3. Cáº­p nháº­t nhÃ¢n viÃªn

| Thuá»™c tÃ­nh   | Chi tiáº¿t                |
| ------------ | ----------------------- |
| **URL**      | `PUT /api/v1/nhan-vien` |
| **Method**   | `PUT`                   |
| **XÃ¡c thá»±c** | Bearer Token (JWT)      |

**Request Body:**

```json
{
  "id": 10,
  "cuaHang": { "id": 2 },
  "tenNhanVien": "Nguyen Van A Updated",
  "email": "nva@s.com",
  "soDienThoai": "0901234568",
  "matKhau": "123456",
  "ngayBatDauLam": "2026-03-12T08:00:00",
  "ngayKetThucLam": null,
  "trangThai": 1,
  "role": { "id": 2 }
}
```

**Response:** `200 OK` (dá»¯ liá»‡u `ResNhanVienDTO`)

---

## 4. XÃ³a nhÃ¢n viÃªn

| Thuá»™c tÃ­nh   | Chi tiáº¿t                        |
| ------------ | ------------------------------- |
| **URL**      | `DELETE /api/v1/nhan-vien/{id}` |
| **Method**   | `DELETE`                        |
| **XÃ¡c thá»±c** | Bearer Token (JWT)              |

**Response:** `204 No Content`

---

## Lá»—i thÆ°á»ng gáº·p

| HTTP Status | MÃ´ táº£                            |
| ----------- | -------------------------------- |
| `400`       | MÃ£ nhÃ¢n viÃªn khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |
| `400`       | KhÃ´ng tÃ¬m tháº¥y nhÃ¢n viÃªn theo ID |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âŒ  | âŒ   | âŒ  | âŒ     |


