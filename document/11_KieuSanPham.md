# Kiá»ƒu Sáº£n Pháº©m Controller

> **Base Path:** `/api/v1/kieu-san-pham`  
> **File:** `KieuSanPhamController.java`  
> Quáº£n lÃ½ kiá»ƒu/loáº¡i sáº£n pháº©m (vÃ­ dá»¥: Ão, Quáº§n, GiÃ y, Phá»¥ kiá»‡n, ...).

---

## Cáº¥u trÃºc dá»¯ liá»‡u `KieuSanPham`

| TrÆ°á»ng           | Kiá»ƒu          | MÃ´ táº£                             |
| ---------------- | ------------- | --------------------------------- |
| `id`             | Long          | MÃ£ kiá»ƒu sáº£n pháº©m (auto-increment) |
| `tenKieuSanPham` | String(255)   | TÃªn kiá»ƒu sáº£n pháº©m                 |
| `ngayTao`        | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)                |
| `ngayCapNhat`    | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)           |

---

## 1. Láº¥y danh sÃ¡ch kiá»ƒu sáº£n pháº©m

| Thuá»™c tÃ­nh   | Chi tiáº¿t                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/kieu-san-pham` |
| **Method**   | `GET`                       |
| **XÃ¡c thá»±c** | Bearer Token (JWT)          |

**Response:** `200 OK`

```json
[
  { "id": 1, "tenKieuSanPham": "Ão", "ngayTao": "2026-01-01T00:00:00" },
  { "id": 2, "tenKieuSanPham": "Quáº§n", "ngayTao": "2026-01-01T00:00:00" }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKieuSanPham": "String",
  "ngayTao": "LocalDateTime"
}
```

---

## 2-5. CRUD tiÃªu chuáº©n

| Endpoint                            | Method   | MÃ´ táº£               |
| ----------------------------------- | -------- | ------------------- |
| `GET /api/v1/kieu-san-pham/{id}`    | `GET`    | Láº¥y kiá»ƒu SP theo ID |
| `POST /api/v1/kieu-san-pham`        | `POST`   | Táº¡o kiá»ƒu SP má»›i     |
| `PUT /api/v1/kieu-san-pham`         | `PUT`    | Cáº­p nháº­t kiá»ƒu SP    |
| `DELETE /api/v1/kieu-san-pham/{id}` | `DELETE` | XÃ³a kiá»ƒu SP         |

**Request Body (POST/PUT):**

```json
{ "id": 1, "tenKieuSanPham": "Ão khoÃ¡c" }
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenKieuSanPham": "String"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                |
| ----------- | ------------------------------------ |
| `400`       | KhÃ´ng tÃ¬m tháº¥y kiá»ƒu sáº£n pháº©m         |
| `400`       | MÃ£ kiá»ƒu sáº£n pháº©m khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âœ…  | âŒ   | âŒ  | âŒ     |


