# Loại Kiểm Kê Controller

> Base Path: `/api/v1/loai-kiem-ke`
> File: `LoaiKiemKeController.java`
> Module quản lý danh mục loại phiếu kiểm kê.

---

## Cấu trúc dữ liệu

| Trường          | Kiểu          | Mô tả                                   |
| --------------- | ------------- | --------------------------------------- |
| `id`            | Long          | Mã loại kiểm kê (auto-increment)        |
| `tenLoaiKiemKe` | String(255)   | Tên loại kiểm kê                        |
| `moTa`          | String(255)   | Mô tả                                   |
| `trangThai`     | Integer       | Trạng thái (`1`: hoạt động, `0`: ngừng) |
| `ngayTao`       | LocalDateTime | Tự động sinh khi tạo                    |
| `ngayCapNhat`   | LocalDateTime | Tự động sinh khi cập nhật               |

---

## 1. Lấy danh sách loại kiểm kê

- URL: `GET /api/v1/loai-kiem-ke`
- Quyền: `ADMIN`, `NHAN_VIEN`
- Response: `200 OK` + danh sách `LoaiKiemKe[]`

## 2. Lấy loại kiểm kê theo id

- URL: `GET /api/v1/loai-kiem-ke/{id}`
- Quyền: `ADMIN`, `NHAN_VIEN`
- Response: `200 OK`
- Lỗi: `400` nếu không tìm thấy id

## 3. Tạo loại kiểm kê

- URL: `POST /api/v1/loai-kiem-ke`
- Quyền: `ADMIN`
- Body:

```json
{
  "tenLoaiKiemKe": "Kiểm kê định kỳ",
  "moTa": "Kiểm kê hàng tháng",
  "trangThai": 1
}
```

- Response: `201 Created`

## 4. Cập nhật loại kiểm kê

- URL: `PUT /api/v1/loai-kiem-ke`
- Quyền: `ADMIN`
- Body:

```json
{
  "id": 1,
  "tenLoaiKiemKe": "Kiểm kê định kỳ",
  "moTa": "Kiểm kê mỗi tháng",
  "trangThai": 1
}
```

- Response: `200 OK`

## 5. Xóa loại kiểm kê

- URL: `DELETE /api/v1/loai-kiem-ke/{id}`
- Quyền: `ADMIN`
- Response: `204 No Content`
