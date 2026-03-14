# ThÆ°Æ¡ng Hiá»‡u Controller

> **Base Path:** `/api/v1/thuong-hieu`  
> **File:** `ThuongHieuController.java`  
> Quáº£n lÃ½ thÆ°Æ¡ng hiá»‡u sáº£n pháº©m (vÃ­ dá»¥: Nike, Adidas, Uniqlo, ...).

---

## Cáº¥u trÃºc dá»¯ liá»‡u `ThuongHieu`

| TrÆ°á»ng              | Kiá»ƒu          | MÃ´ táº£                               |
| ------------------- | ------------- | ----------------------------------- |
| `id`                | Long          | MÃ£ thÆ°Æ¡ng hiá»‡u (auto-increment)     |
| `tenThuongHieu`     | String(255)   | TÃªn thÆ°Æ¡ng hiá»‡u                     |
| `trangThaiHoatDong` | Integer       | TT hoáº¡t Ä‘á»™ng (0: ngá»«ng, 1: Ä‘ang HÄ) |
| `trangThaiHienThi`  | Integer       | TT hiá»ƒn thá»‹ (0: áº©n, 1: hiá»ƒn thá»‹)    |
| `ngayTao`           | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)                  |
| `ngayCapNhat`       | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)             |

---

## 1. Láº¥y danh sÃ¡ch thÆ°Æ¡ng hiá»‡u

| Thuá»™c tÃ­nh   | Chi tiáº¿t                  |
| ------------ | ------------------------- |
| **URL**      | `GET /api/v1/thuong-hieu` |
| **Method**   | `GET`                     |
| **XÃ¡c thá»±c** | Bearer Token (JWT)        |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenThuongHieu": "Nike",
    "trangThaiHoatDong": 1,
    "trangThaiHienThi": 1
  },
  {
    "id": 2,
    "tenThuongHieu": "Adidas",
    "trangThaiHoatDong": 1,
    "trangThaiHienThi": 1
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenThuongHieu": "String",
  "trangThaiHoatDong": "Integer",
  "trangThaiHienThi": "Integer"
}
```

---

## 2-5. CRUD tiÃªu chuáº©n

| Endpoint                          | Method   | MÃ´ táº£                   |
| --------------------------------- | -------- | ----------------------- |
| `GET /api/v1/thuong-hieu/{id}`    | `GET`    | Láº¥y thÆ°Æ¡ng hiá»‡u theo ID |
| `POST /api/v1/thuong-hieu`        | `POST`   | Táº¡o thÆ°Æ¡ng hiá»‡u má»›i     |
| `PUT /api/v1/thuong-hieu`         | `PUT`    | Cáº­p nháº­t thÆ°Æ¡ng hiá»‡u    |
| `DELETE /api/v1/thuong-hieu/{id}` | `DELETE` | XÃ³a thÆ°Æ¡ng hiá»‡u         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenThuongHieu": "Nike Vietnam",
  "trangThaiHoatDong": 1,
  "trangThaiHienThi": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenThuongHieu": "String",
  "trangThaiHoatDong": "Integer",
  "trangThaiHienThi": "Integer"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                              |
| ----------- | ---------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y thÆ°Æ¡ng hiá»‡u         |
| `400`       | MÃ£ thÆ°Æ¡ng hiá»‡u khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âœ…  | âŒ   | âŒ  | âŒ     |


