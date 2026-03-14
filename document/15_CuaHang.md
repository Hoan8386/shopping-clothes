# Cá»­a HÃ ng Controller

> **Base Path:** `/api/v1/cua-hang`  
> **File:** `CuaHangController.java`  
> Quáº£n lÃ½ danh sÃ¡ch cá»­a hÃ ng / chi nhÃ¡nh.

---

## Cáº¥u trÃºc dá»¯ liá»‡u `CuaHang`

| TrÆ°á»ng        | Kiá»ƒu        | MÃ´ táº£                        |
| ------------- | ----------- | ---------------------------- |
| `id`          | Long        | MÃ£ cá»­a hÃ ng (auto-increment) |
| `tenCuaHang`  | String(255) | TÃªn cá»­a hÃ ng / chi nhÃ¡nh     |
| `diaChi`      | String(255) | Äá»‹a chá»‰                      |
| `viTri`       | String(255) | Vá»‹ trÃ­ (tá»a Ä‘á»™ / khu vá»±c)    |
| `soDienThoai` | String(255) | Sá»‘ Ä‘iá»‡n thoáº¡i liÃªn há»‡        |
| `email`       | String(255) | Email liÃªn há»‡                |
| `trangThai`   | Integer     | Tráº¡ng thÃ¡i (0: Ä‘Ã³ng, 1: má»Ÿ)  |

### Quy Æ°á»›c vá»‹ trÃ­

- Dá»¯ liá»‡u vá»‹ trÃ­ khi lÆ°u DB váº«n náº±m trong `viTri` theo Ä‘á»‹nh dáº¡ng: `"latitude,longitude"`
  - VÃ­ dá»¥: `"10.7769,106.7009"`
- Khi tráº£ API, há»‡ thá»‘ng tÃ¡ch `viTri` thÃ nh 2 trÆ°á»ng:
  - `latitude` (Double)
  - `longitude` (Double)

---

## 1. Láº¥y danh sÃ¡ch cá»­a hÃ ng (cÃ³ lá»c + phÃ¢n trang)

| Thuá»™c tÃ­nh   | Chi tiáº¿t               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/cua-hang` |
| **Method**   | `GET`                  |
| **XÃ¡c thá»±c** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham sá»‘   | Kiá»ƒu    | Báº¯t buá»™c | MÃ´ táº£                                |
| --------- | ------- | -------- | ------------------------------------ |
| `name`    | String  | KhÃ´ng    | Lá»c theo tÃªn cá»­a hÃ ng (like)         |
| `address` | String  | KhÃ´ng    | Lá»c theo Ä‘á»‹a chá»‰ (like)              |
| `status`  | Integer | KhÃ´ng    | Lá»c theo tráº¡ng thÃ¡i (0: Ä‘Ã³ng, 1: má»Ÿ) |
| `page`    | Integer | KhÃ´ng    | Sá»‘ trang (máº·c Ä‘á»‹nh: 0)               |
| `size`    | Integer | KhÃ´ng    | KÃ­ch thÆ°á»›c trang (máº·c Ä‘á»‹nh: 20)      |
| `sort`    | String  | KhÃ´ng    | Sáº¯p xáº¿p (vd: `tenCuaHang,asc`)       |

**VÃ­ dá»¥ request:**

```
GET /api/v1/cua-hang?name=Q1&status=1&page=0&size=10
```

**Response:** `200 OK` â€” Tráº£ vá» `ResultPaginationDTO`

```json
{
  "meta": {
    "page": 1,
    "pageSize": 10,
    "pages": 1,
    "total": 2
  },
  "result": [
    {
      "id": 1,
      "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1",
      "diaChi": "123 Nguyá»…n TrÃ£i, Q.1, TP.HCM",
      "viTri": "10.7769,106.7009",
      "latitude": 10.7769,
      "longitude": 106.7009,
      "soDienThoai": "028-1234-5678",
      "email": "q1@shop.vn",
      "trangThai": 1
    }
  ]
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

```json
{
  "meta": {
    "page": "Integer",
    "pageSize": "Integer",
    "pages": "Integer",
    "total": "Long"
  },
  "result": [
    {
      "id": "Long",
      "tenCuaHang": "String",
      "diaChi": "String",
      "viTri": "String",
      "latitude": "Double | null",
      "longitude": "Double | null",
      "soDienThoai": "String",
      "email": "String",
      "trangThai": "Integer"
    }
  ]
}
```

> **LÆ°u Ã½:** Náº¿u khÃ´ng truyá»n báº¥t ká»³ tham sá»‘ lá»c nÃ o â†’ tráº£ vá» táº¥t cáº£ cá»­a hÃ ng (cÃ³ phÃ¢n trang).

---

## 2-5. CRUD tiÃªu chuáº©n

| Endpoint                       | Method   | MÃ´ táº£                |
| ------------------------------ | -------- | -------------------- |
| `GET /api/v1/cua-hang/{id}`    | `GET`    | Láº¥y cá»­a hÃ ng theo ID |
| `POST /api/v1/cua-hang`        | `POST`   | Táº¡o cá»­a hÃ ng má»›i     |
| `PUT /api/v1/cua-hang`         | `PUT`    | Cáº­p nháº­t cá»­a hÃ ng    |
| `DELETE /api/v1/cua-hang/{id}` | `DELETE` | XÃ³a cá»­a hÃ ng         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenCuaHang": "Chi nhÃ¡nh Quáº­n 1",
  "diaChi": "123 Nguyá»…n TrÃ£i, Q.1, TP.HCM",
  "viTri": "10.7769,106.7009",
  "soDienThoai": "028-1234-5678",
  "email": "q1@shop.vn",
  "trangThai": 1
}
```

**Kiá»ƒu dá»¯ liá»‡u:**

````json
{
  "id": "Long",
  "tenCuaHang": "String",
  "diaChi": "String",
  "viTri": "String (latitude,longitude)",
  "soDienThoai": "String",
  "email": "String",
  "trangThai": "Integer"
}

**Response Body (GET by id / POST / PUT):**

```json
{
  "id": 1,
  "tenCuaHang": "Shop Quáº­n 1",
  "diaChi": "123 Nguyá»…n TrÃ£i, Q.1, TP.HCM",
  "viTri": "10.7769,106.7009",
  "latitude": 10.7769,
  "longitude": 106.7009,
  "soDienThoai": "028-1234-5678",
  "email": "q1@shop.vn",
  "trangThai": 1
}
````

```

**Lá»—i:**

| HTTP Status | MÃ´ táº£                           |
| ----------- | ------------------------------- |
| `400`       | KhÃ´ng tÃ¬m tháº¥y cá»­a hÃ ng         |
| `400`       | MÃ£ cá»­a hÃ ng khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng |

---

## PhÃ¢n quyá»n

| Vai trÃ²    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | âœ…  | âœ…   | âœ…  | âœ…     |
| NHAN_VIEN  | âœ…  | âŒ   | âŒ  | âŒ     |
| KHACH_HANG | âœ…  | âŒ   | âŒ  | âŒ     |
```


