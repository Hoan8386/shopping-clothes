# NhÃ  Cung Cáº¥p Controller

> **Base Path:** `/api/v1/nha-cung-cap`  
> **File:** `NhaCungCapController.java`  
> Quáº£n lÃ½ danh sÃ¡ch nhÃ  cung cáº¥p hÃ ng hÃ³a.

---

## Cáº¥u trÃºc dá»¯ liá»‡u `NhaCungCap`

| TrÆ°á»ng          | Kiá»ƒu          | MÃ´ táº£                            |
| --------------- | ------------- | -------------------------------- |
| `id`            | Long          | MÃ£ nhÃ  cung cáº¥p (auto-increment) |
| `tenNhaCungCap` | String(255)   | TÃªn nhÃ  cung cáº¥p                 |
| `soDienThoai`   | String(255)   | Sá»‘ Ä‘iá»‡n thoáº¡i                    |
| `email`         | String(255)   | Email liÃªn há»‡                    |
| `diaChi`        | String(255)   | Äá»‹a chá»‰                          |
| `ghiTru`        | String(255)   | Ghi chÃº                          |
| `trangThai`     | Integer       | Tráº¡ng thÃ¡i (0: ngá»«ng, 1: HÄ)     |
| `ngayTao`       | LocalDateTime | NgÃ y táº¡o (tá»± Ä‘á»™ng)               |
| `ngayCapNhat`   | LocalDateTime | NgÃ y cáº­p nháº­t (tá»± Ä‘á»™ng)          |

---

## 1. Láº¥y danh sÃ¡ch nhÃ  cung cáº¥p

| Thuá»™c tÃ­nh   | Chi tiáº¿t                   |
| ------------ | -------------------------- |
| **URL**      | `GET /api/v1/nha-cung-cap` |
| **Method**   | `GET`                      |
| **XÃ¡c thá»±c** | Bearer Token (JWT)         |

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "tenNhaCungCap": "CÃ´ng ty TNHH ABC",
    "soDienThoai": "028-9876-5432",
    "email": "abc@supplier.vn",
    "diaChi": "456 LÃª Lá»£i, Q.1, TP.HCM",
    "trangThai": 1
  }
]
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenNhaCungCap": "String",
  "soDienThoai": "String",
  "email": "String",
  "diaChi": "String",
  "trangThai": "Integer"
}
```

---

## 2-5. CRUD tiÃªu chuáº©n

| Endpoint                           | Method   | MÃ´ táº£           |
| ---------------------------------- | -------- | --------------- |
| `GET /api/v1/nha-cung-cap/{id}`    | `GET`    | Láº¥y NCC theo ID |
| `POST /api/v1/nha-cung-cap`        | `POST`   | Táº¡o NCC má»›i     |
| `PUT /api/v1/nha-cung-cap`         | `PUT`    | Cáº­p nháº­t NCC    |
| `DELETE /api/v1/nha-cung-cap/{id}` | `DELETE` | XÃ³a NCC         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenNhaCungCap": "CÃ´ng ty TNHH ABC",
  "soDienThoai": "028-9876-5432",
  "email": "abc@supplier.vn",
  "diaChi": "456 LÃª Lá»£i, Q.1, TP.HCM",
  "ghiTru": "NCC uy tÃ­n",
  "trangThai": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "id": "Long",
  "tenNhaCungCap": "String",
  "soDienThoai": "String",
  "email": "String",
  "diaChi": "String",
  "ghiTru": "String",
  "trangThai": "Integer"
}
```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                               |
| ----------- | ----------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y nhÃ  cung cáº¥p         |
| `400`       | MÃ£ nhÃ  cung cáº¥p khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âŒ  | âŒ   | âŒ  | âŒ     |


