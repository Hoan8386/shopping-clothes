# KÃ­ch ThÆ°á»›c Controller

> **Base Path:** `/api/v1/kich-thuoc`  
> **File:** `KichThuocController.java`  
> Quáº£n lÃ½ danh sÃ¡ch kÃ­ch thÆ°á»›c sáº£n pháº©m (vÃ­ dá»¥: S, M, L, XL, XXL, ...).

---

## Cáº¥u trÃºc dá»¯ liá»‡u `KichThuoc`

| TrÆ°á»ng         | Kiá»ƒu          | MÃ´ táº£                          |
| -------------- | ------------- | ------------------------------ |
| `id`           | Long          | MÃ£ kÃ­ch thÆ°á»›c (auto-increment) |
| `tenKichThuoc` | String(255)   | TÃªn kÃ­ch thÆ°á»›c                 |
| `ngayTao`      | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)             |
| `ngayCapNhat`  | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)        |

---

## 1. Láº¥y danh sÃ¡ch kÃ­ch thÆ°á»›c

| Thuá»™c tÃ­nh   | Chi tiáº¿t                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/kich-thuoc` |
| **Method**   | `GET`                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT)       |

**Response:** `200 OK`

```json
[
  { "id": 1, "tenKichThuoc": "S", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 2, "tenKichThuoc": "M", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 3, "tenKichThuoc": "L", "ngayTao": "2026-01-01T00:00:00" }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKichThuoc": "String",
  "ngayTao": "LocalDateTime"
}
```

---

## 2-5. CRUD tiÃªu chuáº©n

| Endpoint                         | Method   | MÃ´ táº£                  |
| -------------------------------- | -------- | ---------------------- |
| `GET /api/v1/kich-thuoc/{id}`    | `GET`    | Láº¥y kÃ­ch thÆ°á»›c theo ID |
| `POST /api/v1/kich-thuoc`        | `POST`   | Táº¡o kÃ­ch thÆ°á»›c má»›i     |
| `PUT /api/v1/kich-thuoc`         | `PUT`    | Cáº­p nháº­t kÃ­ch thÆ°á»›c    |
| `DELETE /api/v1/kich-thuoc/{id}` | `DELETE` | XÃ³a kÃ­ch thÆ°á»›c         |

**Request Body (POST/PUT):**

```json
{ "id": 1, "tenKichThuoc": "XS" }
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKichThuoc": "String"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                             |
| ----------- | --------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y kÃ­ch thÆ°á»›c         |
| `400`       | MÃ£ kÃ­ch thÆ°á»›c khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âœ…  | âŒ   | âŒ  | âŒ     |


