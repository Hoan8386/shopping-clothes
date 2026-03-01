# Chi Tiết Phiếu Nhập Controller

> **Base Path:** `/api/v1/chi-tiet-phieu-nhap`  
> **File:** `ChiTietPhieuNhapController.java`  
> Quản lý chi tiết phiếu nhập hàng (từng dòng sản phẩm trong phiếu nhập).

---

## Tổng quan

### Cấu trúc dữ liệu `ChiTietPhieuNhap`

| Trường           | Kiểu           | Mô tả                                   |
| ---------------- | -------------- | --------------------------------------- |
| `id`             | Long           | Mã chi tiết phiếu nhập (auto-increment) |
| `phieuNhap`      | PhieuNhap      | Phiếu nhập cha (FK, ẩn trong JSON)      |
| `chiTietSanPham` | ChiTietSanPham | Biến thể sản phẩm được nhập (FK)        |
| `soLuong`        | Integer        | Số lượng nhập                           |
| `ghiTru`         | String(255)    | Ghi chú trừ                             |
| `ghiTruKiemHang` | String(255)    | Ghi chú kiểm hàng                       |
| `trangThai`      | Integer        | Trạng thái                              |
| `ngayTao`        | LocalDateTime  | Ngày tạo (tự động)                      |
| `ngayCapNhat`    | LocalDateTime  | Ngày cập nhật (tự động)                 |

---

## 1. Lấy danh sách chi tiết phiếu nhập

| Thuộc tính   | Chi tiết                          |
| ------------ | --------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap` |
| **Method**   | `GET`                             |
| **Xác thực** | Bearer Token (JWT)                |

**Response:** `200 OK` — Trả về `List<ChiTietPhieuNhap>`

---

## 2. Lấy chi tiết phiếu nhập theo ID

| Thuộc tính   | Chi tiết                               |
| ------------ | -------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Method**   | `GET`                                  |
| **Xác thực** | Bearer Token (JWT)                     |

**Response:** `200 OK` — Trả về `ChiTietPhieuNhap`

**Lỗi:**

| HTTP Status | Mô tả                              |
| ----------- | ---------------------------------- |
| `400`       | Không tìm thấy chi tiết phiếu nhập |

---

## 3. Lấy chi tiết theo mã phiếu nhập

| Thuộc tính   | Chi tiết                                                   |
| ------------ | ---------------------------------------------------------- |
| **URL**      | `GET /api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}` |
| **Method**   | `GET`                                                      |
| **Xác thực** | Bearer Token (JWT)                                         |

**Path Parameters:**

| Tham số       | Kiểu | Mô tả         |
| ------------- | ---- | ------------- |
| `phieuNhapId` | Long | Mã phiếu nhập |

**Response:** `200 OK` — Trả về `List<ChiTietPhieuNhap>`

---

## 4. Tạo chi tiết phiếu nhập

| Thuộc tính       | Chi tiết                            |
| ---------------- | ----------------------------------- |
| **URL**          | `POST /api/v1/chi-tiet-phieu-nhap`  |
| **Method**       | `POST`                              |
| **Content-Type** | `application/x-www-form-urlencoded` |
| **Xác thực**     | Bearer Token (JWT)                  |

**Request Parameters:**

| Tham số            | Kiểu    | Bắt buộc | Mô tả                |
| ------------------ | ------- | -------- | -------------------- |
| `phieuNhapId`      | Long    | Không    | Mã phiếu nhập        |
| `chiTietSanPhamId` | Long    | Không    | Mã chi tiết sản phẩm |
| `soLuong`          | Integer | Không    | Số lượng nhập        |
| `ghiTru`           | String  | Không    | Ghi chú trừ          |
| `ghiTruKiemHang`   | String  | Không    | Ghi chú kiểm hàng    |
| `trangThai`        | Integer | Không    | Trạng thái           |

**Response:** `201 Created` — Trả về `ChiTietPhieuNhap`

---

## 5. Cập nhật chi tiết phiếu nhập

| Thuộc tính       | Chi tiết                          |
| ---------------- | --------------------------------- |
| **URL**          | `PUT /api/v1/chi-tiet-phieu-nhap` |
| **Method**       | `PUT`                             |
| **Content-Type** | `application/json`                |
| **Xác thực**     | Bearer Token (JWT)                |

**Request Body:** (phải có `id`)

```json
{
  "id": 1,
  "soLuong": 50,
  "ghiTruKiemHang": "Đã kiểm tra đủ",
  "trangThai": 1
}
```

**Response:** `200 OK` — Trả về `ChiTietPhieuNhap`

**Lỗi:**

| HTTP Status | Mô tả                                      |
| ----------- | ------------------------------------------ |
| `400`       | Mã chi tiết phiếu nhập không được để trống |

---

## 6. Xóa chi tiết phiếu nhập

| Thuộc tính   | Chi tiết                                  |
| ------------ | ----------------------------------------- |
| **URL**      | `DELETE /api/v1/chi-tiet-phieu-nhap/{id}` |
| **Method**   | `DELETE`                                  |
| **Xác thực** | Bearer Token (JWT)                        |

**Response:** `204 No Content`

**Lỗi:**

| HTTP Status | Mô tả                              |
| ----------- | ---------------------------------- |
| `400`       | Không tìm thấy chi tiết phiếu nhập |

---

## Phân quyền

| Vai trò    | GET (Xem) | POST (Tạo) | PUT (Sửa) | DELETE (Xóa) |
| ---------- | --------- | ---------- | --------- | ------------ |
| ADMIN      | ✅        | ✅         | ✅        | ✅           |
| NHAN_VIEN  | ✅        | ✅         | ✅        | ❌           |
| KHACH_HANG | ❌        | ❌         | ❌        | ❌           |
