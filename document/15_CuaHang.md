# Cửa Hàng Controller

> **Base Path:** `/api/v1/cua-hang`  
> **File:** `CuaHangController.java`  
> Quản lý danh sách cửa hàng / chi nhánh.

---

## Cấu trúc dữ liệu `CuaHang`

| Trường        | Kiểu        | Mô tả                        |
| ------------- | ----------- | ---------------------------- |
| `id`          | Long        | Mã cửa hàng (auto-increment) |
| `tenCuaHang`  | String(255) | Tên cửa hàng / chi nhánh     |
| `diaChi`      | String(255) | Địa chỉ                      |
| `viTri`       | String(255) | Vị trí (tọa độ / khu vực)    |
| `soDienThoai` | String(255) | Số điện thoại liên hệ        |
| `email`       | String(255) | Email liên hệ                |
| `trangThai`   | Integer     | Trạng thái (0: đóng, 1: mở)  |

---

## 1. Lấy danh sách cửa hàng (có lọc + phân trang)

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/cua-hang` |
| **Method**   | `GET`                  |
| **Xác thực** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham số   | Kiểu    | Bắt buộc | Mô tả                                |
| --------- | ------- | -------- | ------------------------------------ |
| `name`    | String  | Không    | Lọc theo tên cửa hàng (like)         |
| `address` | String  | Không    | Lọc theo địa chỉ (like)              |
| `status`  | Integer | Không    | Lọc theo trạng thái (0: đóng, 1: mở) |
| `page`    | Integer | Không    | Số trang (mặc định: 0)               |
| `size`    | Integer | Không    | Kích thước trang (mặc định: 20)      |
| `sort`    | String  | Không    | Sắp xếp (vd: `tenCuaHang,asc`)       |

**Ví dụ request:**

```
GET /api/v1/cua-hang?name=Q1&status=1&page=0&size=10
```

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

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
      "tenCuaHang": "Chi nhánh Quận 1",
      "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
      "viTri": "10.7769, 106.7009",
      "soDienThoai": "028-1234-5678",
      "email": "q1@shop.vn",
      "trangThai": 1
    }
  ]
}
```

**Kiểu dữ liệu:**

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
      "soDienThoai": "String",
      "email": "String",
      "trangThai": "Integer"
    }
  ]
}
```

> **Lưu ý:** Nếu không truyền bất kỳ tham số lọc nào → trả về tất cả cửa hàng (có phân trang).

---

## 2-5. CRUD tiêu chuẩn

| Endpoint                       | Method   | Mô tả                |
| ------------------------------ | -------- | -------------------- |
| `GET /api/v1/cua-hang/{id}`    | `GET`    | Lấy cửa hàng theo ID |
| `POST /api/v1/cua-hang`        | `POST`   | Tạo cửa hàng mới     |
| `PUT /api/v1/cua-hang`         | `PUT`    | Cập nhật cửa hàng    |
| `DELETE /api/v1/cua-hang/{id}` | `DELETE` | Xóa cửa hàng         |

**Request Body (POST/PUT):**

```json
{
  "id": 1,
  "tenCuaHang": "Chi nhánh Quận 1",
  "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
  "soDienThoai": "028-1234-5678",
  "email": "q1@shop.vn",
  "trangThai": 1
}
```

**Kiểu dữ liệu:**

```json
{
  "id": "Long",
  "tenCuaHang": "String",
  "diaChi": "String",
  "soDienThoai": "String",
  "email": "String",
  "trangThai": "Integer"
}
```

**Lỗi:**

| HTTP Status | Mô tả                           |
| ----------- | ------------------------------- |
| `400`       | Không tìm thấy cửa hàng         |
| `400`       | Mã cửa hàng không được để trống |

---

## Phân quyền

| Vai trò    | GET | POST | PUT | DELETE |
| ---------- | --- | ---- | --- | ------ |
| ADMIN      | ✅  | ✅   | ✅  | ✅     |
| NHAN_VIEN  | ✅  | ❌   | ❌  | ❌     |
| KHACH_HANG | ✅  | ❌   | ❌  | ❌     |
