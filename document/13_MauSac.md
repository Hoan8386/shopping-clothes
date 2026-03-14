# MÃ u Sáº¯c Controller

> **Base Path:** `/api/v1/mau-sac`  
> **File:** `MauSacController.java`  
> Quáº£n lÃ½ danh sÃ¡ch mÃ u sáº¯c sáº£n pháº©m (vÃ­ dá»¥: Äen, Tráº¯ng, Äá», Xanh, ...).

---

## Cáº¥u trÃºc dá»¯ liá»‡u `MauSac`

| TrÆ°á»ng        | Kiá»ƒu          | MÃ´ táº£                       |
| ------------- | ------------- | --------------------------- |
| `id`          | Long          | MÃ£ mÃ u sáº¯c (auto-increment) |
| `tenMauSac`   | String(255)   | TÃªn mÃ u sáº¯c                 |
| `ngayTao`     | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)          |
| `ngayCapNhat` | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)     |

---

## 1. Láº¥y danh sÃ¡ch mÃ u sáº¯c

| Thuá»™c tÃ­nh   | Chi tiáº¿t              |
| ------------ | --------------------- |
| **URL**      | `GET /api/v1/mau-sac` |
| **Method**   | `GET`                 |
| **XÃ¡c thá»±c** | Bearer Token (JWT)    |

**Response:** `200 OK`

```json
[
  { "id": 1, "tenMauSac": "Äen", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 2, "tenMauSac": "Tráº¯ng", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 3, "tenMauSac": "Äá»", "ngayTao": "2026-01-01T00:00:00" }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenMauSac": "String",
  "ngayTao": "LocalDateTime"
}
```

---

## 2-5. CRUD tiÃªu chuáº©n

| Endpoint                      | Method   | MÃ´ táº£               |
| ----------------------------- | -------- | ------------------- |
| `GET /api/v1/mau-sac/{id}`    | `GET`    | Láº¥y mÃ u sáº¯c theo ID |
| `POST /api/v1/mau-sac`        | `POST`   | Táº¡o mÃ u sáº¯c má»›i     |
| `PUT /api/v1/mau-sac`         | `PUT`    | Cáº­p nháº­t mÃ u sáº¯c    |
| `DELETE /api/v1/mau-sac/{id}` | `DELETE` | XÃ³a mÃ u sáº¯c         |

**Request Body (POST/PUT):**

```json
{ "id": 1, "tenMauSac": "Äen nhÃ¡m" }
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenMauSac": "String"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                          |
| ----------- | ------------------------------ |
| `400`       | KhÃ´ng tÃ¬m tháº¥y mÃ u sáº¯c         |
| `400`       | MÃ£ mÃ u sáº¯c khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âœ…  | âŒ   | âŒ  | âŒ     |


