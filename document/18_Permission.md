# Permission Controller

> **Base Path:** `/api/v1/permissions`  
> **File:** `PermissionController.java`  
> Quáº£n lÃ½ quyá»n háº¡n (Permission) trong há»‡ thá»‘ng.

---

## Tá»•ng quan

Má»—i **Permission** Ä‘áº¡i diá»‡n cho 1 quyá»n truy cáº­p vÃ o 1 API endpoint cá»¥ thá»ƒ. Quyá»n Ä‘Æ°á»£c gÃ¡n cho **Role** qua quan há»‡ nhiá»u-nhiá»u.

### Cáº¥u trÃºc dá»¯ liá»‡u `Permission`

| TrÆ°á»ng      | Kiá»ƒu              | MÃ´ táº£                                  |
| ----------- | ----------------- | -------------------------------------- |
| `id`        | Long              | MÃ£ quyá»n (auto-increment)              |
| `name`      | String (NotBlank) | TÃªn quyá»n (vd: "Xem sáº£n pháº©m")         |
| `apiPath`   | String (NotBlank) | ÄÆ°á»ng dáº«n API (vd: `/api/v1/san-pham`) |
| `method`    | String (NotBlank) | HTTP method (GET/POST/PUT/DELETE)      |
| `module`    | String (NotBlank) | Module (vd: SAN_PHAM, DON_HANG, ...)   |
| `createdAt` | Instant           | NgÃ y táº¡o (tá»± Ä‘á»™ng â€” audit)             |
| `updatedAt` | Instant           | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng â€” audit)        |
| `createdBy` | String            | NgÆ°á»i táº¡o (tá»± Ä‘á»™ng â€” audit)            |
| `updatedBy` | String            | NgÆ°á»i cáº­p nháº­t (tá»± Ä‘á»™ng â€” audit)       |

### Danh sÃ¡ch module

| Module                | MÃ´ táº£                   |
| --------------------- | ----------------------- |
| `SAN_PHAM`            | Sáº£n pháº©m                |
| `CHI_TIET_SAN_PHAM`   | Chi tiáº¿t sáº£n pháº©m       |
| `DON_HANG`            | ÄÆ¡n hÃ ng                |
| `CHI_TIET_DON_HANG`   | Chi tiáº¿t Ä‘Æ¡n hÃ ng       |
| `GIO_HANG`            | Giá» hÃ ng                |
| `PHIEU_NHAP`          | Phiáº¿u nháº­p              |
| `CHI_TIET_PHIEU_NHAP` | Chi tiáº¿t phiáº¿u nháº­p     |
| `HINH_ANH`            | HÃ¬nh áº£nh                |
| `BO_SUU_TAP`          | Bá»™ sÆ°u táº­p              |
| `KIEU_SAN_PHAM`       | Kiá»ƒu sáº£n pháº©m           |
| `THUONG_HIEU`         | ThÆ°Æ¡ng hiá»‡u             |
| `MAU_SAC`             | MÃ u sáº¯c                 |
| `KICH_THUOC`          | KÃ­ch thÆ°á»›c              |
| `CUA_HANG`            | Cá»­a hÃ ng                |
| `NHA_CUNG_CAP`        | NhÃ  cung cáº¥p            |
| `ROLE`                | Vai trÃ²                 |
| `PERMISSION`          | Quyá»n háº¡n               |
| `KHUYEN_MAI_HOA_DON`  | Khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n |
| `KHUYEN_MAI_DIEM`     | Khuyáº¿n mÃ£i theo Ä‘iá»ƒm    |

---

## 1. Láº¥y danh sÃ¡ch quyá»n

| Thuá»™c tÃ­nh   | Chi tiáº¿t                  |
| ------------ | ------------------------- |
| **URL**      | `GET /api/v1/permissions` |
| **Method**   | `GET`                     |
| **XÃ¡c thá»±c** | Bearer Token (JWT)        |

**Response:** `200 OK` â€” Tráº£ vá» `List<Permission>`

```json
[
  {
    "id": 1,
    "name": "Xem sáº£n pháº©m",
    "apiPath": "/api/v1/san-pham",
    "method": "GET",
    "module": "SAN_PHAM",
    "createdAt": "2026-01-01T00:00:00Z"
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
[
  {
    "id": "Long",
    "name": "String",
    "apiPath": "String",
    "method": "String",
    "module": "String",
    "createdAt": "Instant"
  }
]
```

---

## 2. Láº¥y quyá»n theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                       |
| ------------ | ------------------------------ |
| **URL**      | `GET /api/v1/permissions/{id}` |
| **Method**   | `GET`                          |
| **XÃ¡c thá»±c** | Bearer Token (JWT)             |

**Response:** `200 OK` â€” Tráº£ vá» `Permission`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                |
| ----------- | -------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y quyá»n |

---

## 3. Táº¡o quyá»n

| Thuá»™c tÃ­nh       | Chi tiáº¿t                   |
| ---------------- | -------------------------- |
| **URL**          | `POST /api/v1/permissions` |
| **Method**       | `POST`                     |
| **Content-Type** | `application/json`         |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)         |

**Request Body:**

```json
{
  "name": "Xem bÃ¡o cÃ¡o",
  "apiPath": "/api/v1/bao-cao",
  "method": "GET",
  "module": "BAO_CAO"
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "name": "String",
  "apiPath": "String",
  "method": "String",
  "module": "String"
}
```

| TrÆ°á»ng    | Báº¯t buá»™c | MÃ´ táº£         |
| --------- | -------- | ------------- |
| `name`    | **CÃ³**   | TÃªn quyá»n     |
| `apiPath` | **CÃ³**   | ÄÆ°á»ng dáº«n API |
| `method`  | **CÃ³**   | HTTP method   |
| `module`  | **CÃ³**   | TÃªn module    |

**Response:** `201 Created` â€” Tráº£ vá» `Permission`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                                   |
| ----------- | --------------------------------------- |
| `400`       | Quyá»n Ä‘Ã£ tá»“n táº¡i (trÃ¹ng apiPath+method) |

---

## 4. Cáº­p nháº­t quyá»n

| Thuá»™c tÃ­nh       | Chi tiáº¿t                  |
| ---------------- | ------------------------- |
| **URL**          | `PUT /api/v1/permissions` |
| **Method**       | `PUT`                     |
| **Content-Type** | `application/json`        |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)        |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{
  "id": 99,
  "name": "Xem bÃ¡o cÃ¡o â€” updated",
  "apiPath": "/api/v1/bao-cao",
  "method": "GET",
  "module": "BAO_CAO"
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "name": "String",
  "apiPath": "String",
  "method": "String",
  "module": "String"
}
```

**Response:** `200 OK` â€” Tráº£ vá» `Permission`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                |
| ----------- | -------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y quyá»n |

---

## 5. XÃ³a quyá»n

| Thuá»™c tÃ­nh   | Chi tiáº¿t                          |
| ------------ | --------------------------------- |
| **URL**      | `DELETE /api/v1/permissions/{id}` |
| **Method**   | `DELETE`                          |
| **XÃ¡c thá»±c** | Bearer Token (JWT)                |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                |
| ----------- | -------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y quyá»n |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âŒ  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âŒ  | âŒ   | âŒ  | âŒ     |


