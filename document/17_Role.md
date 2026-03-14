# Role Controller

> **Base Path:** `/api/v1/roles`  
> **File:** `RoleController.java`  
> Quáº£n lÃ½ vai trÃ² (Role) trong há»‡ thá»‘ng phÃ¢n quyá»n.

---

## Tá»•ng quan

Há»‡ thá»‘ng sá»­ dá»¥ng **RBAC** (Role-Based Access Control). Má»—i vai trÃ² gáº¯n vá»›i cÃ¡c quyá»n (Permission) qua quan há»‡ **nhiá»u-nhiá»u**.

### Vai trÃ² máº·c Ä‘á»‹nh

| ID  | Name       | MÃ´ táº£              |
| --- | ---------- | ------------------ |
| 1   | ADMIN      | Quáº£n trá»‹ há»‡ thá»‘ng  |
| 2   | NHAN_VIEN  | NhÃ¢n viÃªn bÃ¡n hÃ ng |
| 3   | KHACH_HANG | KhÃ¡ch hÃ ng         |

### Cáº¥u trÃºc dá»¯ liá»‡u `Role`

| TrÆ°á»ng        | Kiá»ƒu               | MÃ´ táº£                            |
| ------------- | ------------------ | -------------------------------- |
| `id`          | Long               | MÃ£ vai trÃ² (auto-increment)      |
| `name`        | String (NotBlank)  | TÃªn vai trÃ² (unique)             |
| `description` | String             | MÃ´ táº£                            |
| `active`      | boolean            | Tráº¡ng thÃ¡i kÃ­ch hoáº¡t             |
| `permissions` | List\<Permission\> | Danh sÃ¡ch quyá»n (ManyToMany)     |
| `createdAt`   | Instant            | NgÃ y táº¡o (tá»± Ä‘á»™ng â€” audit)       |
| `updatedAt`   | Instant            | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng â€” audit)  |
| `createdBy`   | String             | NgÆ°á»i táº¡o (tá»± Ä‘á»™ng â€” audit)      |
| `updatedBy`   | String             | NgÆ°á»i cáº­p nháº­t (tá»± Ä‘á»™ng â€” audit) |

> **LÆ°u Ã½:** TrÆ°á»ng `createdBy`/`updatedBy` tá»± Ä‘á»™ng láº¥y tá»« JWT token (SecurityContext) qua `SecurityUtil.getCurrentUserLogin()`.

---

## 1. Láº¥y danh sÃ¡ch vai trÃ²

| Thuá»™c tÃ­nh   | Chi tiáº¿t            |
| ------------ | ------------------- |
| **URL**      | `GET /api/v1/roles` |
| **Method**   | `GET`               |
| **XÃ¡c thá»±c** | Bearer Token (JWT)  |

**Response:** `200 OK` â€” Tráº£ vá» `List<Role>`

```json
[
  {
    "id": 1,
    "name": "ADMIN",
    "description": "Quáº£n trá»‹ há»‡ thá»‘ng",
    "active": true,
    "permissions": [
      {
        "id": 1,
        "name": "Xem sáº£n pháº©m",
        "apiPath": "/api/v1/san-pham",
        "method": "GET",
        "module": "SAN_PHAM"
      }
    ],
    "createdAt": "2026-01-01T00:00:00Z",
    "createdBy": "admin@gmail.com"
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
[
  {
    "id": "Long",
    "name": "String",
    "description": "String",
    "active": "boolean",
    "permissions": [
      {
        "id": "Long",
        "name": "String",
        "apiPath": "String",
        "method": "String",
        "module": "String"
      }
    ],
    "createdAt": "Instant",
    "createdBy": "String"
  }
]
```

---

## 2. Láº¥y vai trÃ² theo ID

| Thuá»™c tÃ­nh   | Chi tiáº¿t                 |
| ------------ | ------------------------ |
| **URL**      | `GET /api/v1/roles/{id}` |
| **Method**   | `GET`                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT)       |

**Response:** `200 OK` â€” Tráº£ vá» `Role`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                  |
| ----------- | ---------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y vai trÃ² |

---

## 3. Táº¡o vai trÃ²

| Thuá»™c tÃ­nh       | Chi tiáº¿t             |
| ---------------- | -------------------- |
| **URL**          | `POST /api/v1/roles` |
| **Method**       | `POST`               |
| **Content-Type** | `application/json`   |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)   |

**Request Body:**

```json
{
  "name": "SUPERVISOR",
  "description": "GiÃ¡m sÃ¡t viÃªn",
  "active": true,
  "permissions": [{ "id": 1 }, { "id": 2 }, { "id": 3 }]
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "name": "String",
  "description": "String",
  "active": "boolean",
  "permissions": [
    {
      "id": "Long"
    }
  ]
}
```

| TrÆ°á»ng        | Báº¯t buá»™c | MÃ´ táº£                   |
| ------------- | -------- | ----------------------- |
| `name`        | **CÃ³**   | TÃªn vai trÃ² (unique)    |
| `description` | KhÃ´ng    | MÃ´ táº£                   |
| `active`      | KhÃ´ng    | Tráº¡ng thÃ¡i              |
| `permissions` | KhÃ´ng    | Ds quyá»n (chá»‰ cáº§n `id`) |

**Response:** `201 Created` â€” Tráº£ vá» `Role`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                  |
| ----------- | ---------------------- |
| `400`       | TÃªn vai trÃ² Ä‘Ã£ tá»“n táº¡i |

---

## 4. Cáº­p nháº­t vai trÃ²

| Thuá»™c tÃ­nh       | Chi tiáº¿t            |
| ---------------- | ------------------- |
| **URL**          | `PUT /api/v1/roles` |
| **Method**       | `PUT`               |
| **Content-Type** | `application/json`  |
| **XÃ¡c thá»±c**     | Bearer Token (JWT)  |

**Request Body:** (pháº£i cÃ³ `id`)

```json
{
  "id": 5,
  "name": "SUPERVISOR",
  "description": "GiÃ¡m sÃ¡t viÃªn â€” cáº­p nháº­t",
  "active": true,
  "permissions": [{ "id": 1 }, { "id": 2 }]
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "name": "String",
  "description": "String",
  "active": "boolean",
  "permissions": [
    {
      "id": "Long"
    }
  ]
}
```

**Response:** `200 OK` â€” Tráº£ vá» `Role`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                  |
| ----------- | ---------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y vai trÃ² |

---

## 5. XÃ³a vai trÃ²

| Thuá»™c tÃ­nh   | Chi tiáº¿t                    |
| ------------ | --------------------------- |
| **URL**      | `DELETE /api/v1/roles/{id}` |
| **Method**   | `DELETE`                    |
| **XÃ¡c thá»±c** | Bearer Token (JWT)          |

**Response:** `204 No Content`

**Lá»—i:**

| HTTP Status | MÃ´ táº£                  |
| ----------- | ---------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y vai trÃ² |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âŒ  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âŒ  | âŒ   | âŒ  | âŒ     |


