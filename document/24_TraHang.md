# Trả Hàng Controller

> **Base Path:** `/api/v1/tra-hang`  
> **File:** `TraHangController.java`  
> Quản lý phiếu trả hàng — Khách hàng tạo yêu cầu trả hàng sau khi đơn hàng đã nhận (trạng thái 5), nhân viên/admin duyệt hoặc từ chối.

---

## Tổng quan

### Cấu trúc dữ liệu `TraHang`

| Trường            | Kiểu          | Mô tả                                          |
| ----------------- | ------------- | ---------------------------------------------- |
| `id`              | Long          | Mã phiếu trả (PK, auto-increment)              |
| `donHang`         | DonHang       | Đơn hàng gốc (FK)                              |
| `lyDoTraHang`     | String(255)   | Lý do trả hàng                                 |
| `trangThai`       | Integer       | Trạng thái phiếu trả (xem bảng bên dưới)       |
| `tongTien`        | Double        | Tổng tiền trả (= tongTienTra − giá gốc SP trả) |
| `lyDoTuChoi`      | String(255)   | Lý do từ chối (nếu có)                         |
| `chiTietTraHangs` | List          | Danh sách chi tiết sản phẩm trả                |
| `ngayTao`         | LocalDateTime | Ngày tạo (tự động)                             |
| `ngayCapNhat`     | LocalDateTime | Ngày cập nhật (tự động)                        |

### Cấu trúc dữ liệu `ChiTietTraHang`

| Trường        | Kiểu           | Mô tả                                 |
| ------------- | -------------- | ------------------------------------- |
| `id`          | Long           | Mã chi tiết trả hàng (auto-increment) |
| `traHang`     | TraHang        | Phiếu trả hàng (FK)                   |
| `sanPhamTra`  | ChiTietDonHang | Chi tiết đơn hàng được trả (FK)       |
| `ghiTru`      | String(255)    | Ghi chú cho sản phẩm trả              |
| `trangThai`   | Integer        | Trạng thái (đồng bộ với phiếu trả)    |
| `ngayTao`     | LocalDateTime  | Ngày tạo (tự động)                    |
| `ngayCapNhat` | LocalDateTime  | Ngày cập nhật (tự động)               |

### Mã trạng thái phiếu trả hàng (`trangThai`)

| Giá trị | Ý nghĩa   | Mô tả                                              |
| ------- | --------- | -------------------------------------------------- |
| `0`     | Chờ xử lý | Phiếu trả mới tạo, chờ nhân viên/admin duyệt       |
| `1`     | Đã duyệt  | Phiếu trả được chấp nhận, hoàn tiền cho khách hàng |
| `2`     | Từ chối   | Phiếu trả bị từ chối                               |

### Luồng chuyển trạng thái

- **Khách hàng** tạo phiếu: Đơn hàng (trạng thái 5) → Tạo phiếu trả (trạng thái 0)
- **Nhân viên/Admin** duyệt: `0 → 1` (Đã duyệt) hoặc `0 → 2` (Từ chối)
- Chỉ cập nhật được khi phiếu đang ở trạng thái `0` (Chờ xử lý)

### Công thức tính tổng tiền trả

```
tongTien = tongTienTra (đơn hàng) − Σ(giaSanPham × soLuong) của các sản phẩm trả
```

- `tongTienTra`: Tổng tiền khách hàng đã thanh toán cho đơn hàng
- `giaSanPham × soLuong`: Giá gốc (chưa giảm) × số lượng của từng sản phẩm trả

**Ví dụ:** Đơn hàng có tongTienTra = 900.000đ. Khách trả 1 sản phẩm (giaSanPham = 200.000đ, soLuong = 3):

```
tongTien = 900.000 − (200.000 × 3) = 900.000 − 600.000 = 300.000đ
```

### Điều kiện tạo phiếu trả hàng

| Điều kiện                                        | Mô tả                                       |
| ------------------------------------------------ | ------------------------------------------- |
| ✅ Đã đăng nhập                                  | Lấy thông tin KH từ JWT token               |
| ✅ Đơn hàng **trạng thái = 5** (Đã nhận hàng)    | Chỉ trả được đơn đã nhận                    |
| ✅ Khách hàng là **chủ đơn hàng**                | Không được phép trả đơn hàng của người khác |
| ✅ Chọn **ít nhất 1 sản phẩm** để trả            | Phải chọn sản phẩm cần trả                  |
| ✅ Chi tiết đơn hàng **thuộc đơn hàng đang trả** | Không trộn sản phẩm từ đơn khác             |

---

## 1. Tạo phiếu trả hàng

| Thuộc tính   | Chi tiết                |
| ------------ | ----------------------- |
| **URL**      | `POST /api/v1/tra-hang` |
| **Method**   | `POST`                  |
| **Xác thực** | Bearer Token (JWT)      |

**Request Body:**

```json
{
  "donHangId": 5,
  "lyDoTraHang": "Sản phẩm bị lỗi, không đúng mô tả",
  "chiTietTraHangs": [
    {
      "chiTietDonHangId": 6,
      "ghiTru": "Áo bị rách ở tay"
    },
    {
      "chiTietDonHangId": 7,
      "ghiTru": "Quần không đúng màu"
    }
  ]
}
```

| Trường                               | Kiểu   | Bắt buộc | Mô tả                        |
| ------------------------------------ | ------ | -------- | ---------------------------- |
| `donHangId`                          | Long   | Có       | Mã đơn hàng cần trả          |
| `lyDoTraHang`                        | String | Có       | Lý do trả hàng               |
| `chiTietTraHangs`                    | Array  | Có       | Danh sách sản phẩm trả       |
| `chiTietTraHangs[].chiTietDonHangId` | Long   | Có       | Mã chi tiết đơn hàng cần trả |
| `chiTietTraHangs[].ghiTru`           | String | Không    | Ghi chú cho sản phẩm trả     |

**Response:** `201 Created`

```json
{
  "statusCode": 201,
  "error": null,
  "message": "Tạo phiếu trả hàng",
  "data": {
    "id": 1,
    "donHangId": 5,
    "lyDoTraHang": "Sản phẩm bị lỗi, không đúng mô tả",
    "trangThai": "Chờ xử lý",
    "tongTien": 0.0,
    "ngayTao": "2026-03-12T10:30:00",
    "ngayCapNhat": null,
    "chiTietTraHangs": [
      {
        "id": 1,
        "chiTietDonHangId": 6,
        "tenSanPham": "Áo Oxford",
        "hinhAnhChinh": "ao-oxford.jpg",
        "tenMauSac": "Trắng",
        "tenKichThuoc": "M",
        "giaSanPham": 200.0,
        "soLuong": 3,
        "thanhTien": 600.0,
        "ghiTru": "Áo bị rách ở tay",
        "trangThai": "Chờ xử lý"
      },
      {
        "id": 2,
        "chiTietDonHangId": 7,
        "tenSanPham": "Quần Jean",
        "hinhAnhChinh": null,
        "tenMauSac": "Đen",
        "tenKichThuoc": "M",
        "giaSanPham": 400.0,
        "soLuong": 1,
        "thanhTien": 400.0,
        "ghiTru": "Quần không đúng màu",
        "trangThai": "Chờ xử lý"
      }
    ]
  }
}
```

**Lỗi thường gặp:**

| Mã lỗi | Nguyên nhân                                     |
| ------ | ----------------------------------------------- |
| `400`  | Đơn hàng không ở trạng thái "Đã nhận hàng"      |
| `400`  | Không tìm thấy đơn hàng                         |
| `400`  | Không có quyền trả đơn hàng này                 |
| `400`  | Không chọn sản phẩm để trả                      |
| `400`  | Chi tiết đơn hàng không thuộc đơn hàng đang trả |

---

## 2. Lấy danh sách phiếu trả hàng (phân trang)

| Thuộc tính   | Chi tiết               |
| ------------ | ---------------------- |
| **URL**      | `GET /api/v1/tra-hang` |
| **Method**   | `GET`                  |
| **Xác thực** | Bearer Token (JWT)     |

**Query Parameters:**

| Tham số | Kiểu    | Bắt buộc | Mô tả                           |
| ------- | ------- | -------- | ------------------------------- |
| `page`  | Integer | Không    | Số trang (mặc định: 0)          |
| `size`  | Integer | Không    | Kích thước trang (mặc định: 20) |
| `sort`  | String  | Không    | Sắp xếp (vd: `ngayTao,desc`)    |

**Ví dụ request:**

```
GET /api/v1/tra-hang?page=0&size=10&sort=ngayTao,desc
```

**Response:** `200 OK` — Trả về `ResultPaginationDTO`

```json
{
    "statusCode": 200,
    "error": null,
    "message": "Lấy danh sách phiếu trả hàng",
    "data": {
        "meta": {
            "page": 1,
            "pageSize": 10,
            "pages": 1,
            "total": 2
        },
        "result": [
            {
                "id": 1,
                "donHangId": 5,
                "lyDoTraHang": "Sản phẩm bị lỗi",
                "trangThai": "Chờ xử lý",
                "tongTien": 0.0,
                "ngayTao": "2026-03-12T10:30:00",
                "ngayCapNhat": null,
                "chiTietTraHangs": [...]
            }
        ]
    }
}
```

---

## 3. Lấy phiếu trả hàng theo mã

| Thuộc tính   | Chi tiết                    |
| ------------ | --------------------------- |
| **URL**      | `GET /api/v1/tra-hang/{id}` |
| **Method**   | `GET`                       |
| **Xác thực** | Bearer Token (JWT)          |

**Path Parameters:**

| Tham số | Kiểu | Bắt buộc | Mô tả             |
| ------- | ---- | -------- | ----------------- |
| `id`    | Long | Có       | Mã phiếu trả hàng |

**Ví dụ request:**

```
GET /api/v1/tra-hang/1
```

**Response:** `200 OK` — Trả về `ResTraHangDTO` (cấu trúc giống response tạo phiếu)

---

## 4. Lấy danh sách phiếu trả theo đơn hàng

| Thuộc tính   | Chi tiết                                    |
| ------------ | ------------------------------------------- |
| **URL**      | `GET /api/v1/tra-hang/don-hang/{donHangId}` |
| **Method**   | `GET`                                       |
| **Xác thực** | Bearer Token (JWT)                          |

**Path Parameters:**

| Tham số     | Kiểu | Bắt buộc | Mô tả       |
| ----------- | ---- | -------- | ----------- |
| `donHangId` | Long | Có       | Mã đơn hàng |

**Ví dụ request:**

```
GET /api/v1/tra-hang/don-hang/5
```

**Response:** `200 OK` — Trả về `List<ResTraHangDTO>`

```json
{
    "statusCode": 200,
    "error": null,
    "message": "Lấy danh sách phiếu trả hàng theo đơn hàng",
    "data": [
        {
            "id": 1,
            "donHangId": 5,
            "lyDoTraHang": "Sản phẩm bị lỗi",
            "trangThai": "Chờ xử lý",
            "tongTien": 0.0,
            "ngayTao": "2026-03-12T10:30:00",
            "ngayCapNhat": null,
            "chiTietTraHangs": [...]
        }
    ]
}
```

---

## 5. Cập nhật trạng thái phiếu trả hàng

| Thuộc tính   | Chi tiết                               |
| ------------ | -------------------------------------- |
| **URL**      | `PUT /api/v1/tra-hang/{id}/trang-thai` |
| **Method**   | `PUT`                                  |
| **Xác thực** | Bearer Token (JWT) — Admin/Nhân viên   |

**Path Parameters:**

| Tham số | Kiểu | Bắt buộc | Mô tả             |
| ------- | ---- | -------- | ----------------- |
| `id`    | Long | Có       | Mã phiếu trả hàng |

**Query Parameters:**

| Tham số     | Kiểu    | Bắt buộc | Mô tả                                   |
| ----------- | ------- | -------- | --------------------------------------- |
| `trangThai` | Integer | Có       | Trạng thái mới (1 = Duyệt, 2 = Từ chối) |

**Ví dụ request:**

```
PUT /api/v1/tra-hang/1/trang-thai?trangThai=1
```

**Response:** `200 OK`

```json
{
    "statusCode": 200,
    "error": null,
    "message": "Cập nhật trạng thái phiếu trả hàng",
    "data": {
        "id": 1,
        "donHangId": 5,
        "lyDoTraHang": "Sản phẩm bị lỗi",
        "trangThai": "Đã duyệt",
        "tongTien": 0.0,
        "ngayTao": "2026-03-12T10:30:00",
        "ngayCapNhat": "2026-03-12T14:00:00",
        "chiTietTraHangs": [...]
    }
}
```

**Lỗi thường gặp:**

| Mã lỗi | Nguyên nhân                                      |
| ------ | ------------------------------------------------ |
| `400`  | Không tìm thấy phiếu trả hàng                    |
| `400`  | Phiếu không ở trạng thái "Chờ xử lý"             |
| `400`  | Trạng thái không hợp lệ (chỉ chấp nhận 1 hoặc 2) |
