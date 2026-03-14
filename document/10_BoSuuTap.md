# Bá»™ SÆ°u Táº­p Controller

> **Base Path:** `/api/v1/bo-suu-tap`  
> **File:** `BoSuuTapController.java`  
> Quáº£n lÃ½ bá»™ sÆ°u táº­p sáº£n pháº©m (vÃ­ dá»¥: XuÃ¢n HÃ¨ 2025, Thu ÄÃ´ng 2025, ...).

---

## Cáº¥u trÃºc dá»¯ liá»‡u `BoSuuTap`

| TrÆ°á»ng        | Kiá»ƒu          | MÃ´ táº£                          |
| ------------- | ------------- | ------------------------------ |
| `id`          | Long          | MÃ£ bá»™ sÆ°u táº­p (auto-increment) |
| `tenSuuTap`   | String(255)   | TÃªn bá»™ sÆ°u táº­p                 |
| `moTa`        | String(255)   | MÃ´ táº£                          |
| `ngayTao`     | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)             |
| `ngayCapNhat` | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)        |

---

## 1. Láº¥y danh sÃ¡ch bá»™ sÆ°u táº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/bo-suu-tap` |
| **Method**   | `GET`                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT)       |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenSuuTap": "XuÃ¢n HÃ¨ 2025",
    "moTa": "BST mÃ¹a xuÃ¢n hÃ¨",
    "ngayTao": "2026-01-01T00:00:00"
  },
  {
    "id": 2,
    "tenSuuTap": "Thu ÄÃ´ng 2025",
    "moTa": "BST mÃ¹a thu Ä‘Ã´ng",
    "ngayTao": "2026-01-01T00:00:00"
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenSuuTap": "String",
  "moTa": "String",
  "ngayTao": "LocalDateTime"
}
```

---

## 2. Láº¥y bá»™ sÆ°u táº­p theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                      |
| ------------ | ----------------------------- |
| **URL**      | `GET /api/v1/bo-suu-tap/{id}` |
| **Method**   | `GET`                         |
| **XÃ¡c thá»±c** | Bearer Token (JWT)            |

**Lá»—i:**

| HTTP Status | MÃ´ táº£                     |
| ----------- | ------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y bá»™ sÆ°u táº­p |

---

## 3. Táº¡o bá»™ sÆ°u táº­p

| Thuá»™c tÃ­nh       | Chi tiáº¿t                  |
| ---------------- | ------------------------- |
| **URL**          | `POST /api/v1/bo-suu-tap` |
| **Method**       | `POST`                    |
| **Content-Type** | `application/json`        |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)        |

**Request Body:**

```json
{ "tenSuuTap": "XuÃ¢n HÃ¨ 2026", "moTa": "Bá»™ sÆ°u táº­p má»›i" }
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "tenSuuTap": "String",
  "moTa": "String"
}
```

**Response:** `201 Created`

---

## 4. Cáº­p nháº­t bá»™ sÆ°u táº­p

| Thuá»™c tÃ­nh       | Chi tiáº¿t                 |
| ---------------- | ------------------------ |
| **URL**          | `PUT /api/v1/bo-suu-tap` |
| **Method**       | `PUT`                    |
| **Content-Type** | `application/json`       |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)       |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{ "id": 1, "tenSuuTap": "XuÃ¢n HÃ¨ 2025 - Updated", "moTa": "MÃ´ táº£ má»›i" }
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenSuuTap": "String",
  "moTa": "String"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                             |
| ----------- | --------------------------------- |
| `400`       | MÃ£ bá»™ sÆ°u táº­p khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## 5. XÃ³a bá»™ sÆ°u táº­p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                         |
| ------------ | -------------------------------- |
| **URL**      | `DELETE /api/v1/bo-suu-tap/{id}` |
| **Method**   | `DELETE`                         |
| **XÃ¡c thá»±c** | Bearer Token (JWT)               |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                     |
| ----------- | ------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y bá»™ sÆ°u táº­p |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âœ…  | âŒ   | âŒ  | âŒ     |


