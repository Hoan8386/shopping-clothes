# Loại Đơn Luân Chuyển Controller

> Base Path: `/api/v1/loai-don-luan-chuyen`
> File: `LoaiDonLuanChuyenController.java`

Danh mục loại đơn phục vụ module luân chuyển.

## Cấu trúc dữ liệu

Đối tượng `LoaiDonLuanChuyen` gồm các trường chính:

- `id`: Long
- `tenLoai`: String
- `moTa`: String
- `json`: String (TEXT)
- `ngayTao`: LocalDateTime (tự sinh khi tạo)
- `ngayCapNhat`: LocalDateTime (tự sinh khi cập nhật)

## 1) Lấy danh sách loại đơn

- Method: `GET`
- Endpoint: `/api/v1/loai-don-luan-chuyen`
- Response: `200 OK` + `List<LoaiDonLuanChuyen>`

## 2) Lấy chi tiết theo mã

- Method: `GET`
- Endpoint: `/api/v1/loai-don-luan-chuyen/{id}`
- Response: `200 OK` + `LoaiDonLuanChuyen`

## 3) Tạo mới

- Method: `POST`
- Endpoint: `/api/v1/loai-don-luan-chuyen`
- Body: JSON `LoaiDonLuanChuyen`
- Response: `201 Created` + `LoaiDonLuanChuyen`

Ví dụ body:

```json
{
  "tenLoai": "Điều chuyển theo yêu cầu",
  "moTa": "Luân chuyển nội bộ giữa cửa hàng",
  "json": "{}"
}
```

## 4) Cập nhật

- Method: `PUT`
- Endpoint: `/api/v1/loai-don-luan-chuyen`
- Body: JSON `LoaiDonLuanChuyen`
- Response: `200 OK` + `LoaiDonLuanChuyen`

Ràng buộc:

- `id` bắt buộc và khác 0.

## 5) Xóa

- Method: `DELETE`
- Endpoint: `/api/v1/loai-don-luan-chuyen/{id}`
- Response: `204 No Content`

## Mã lỗi thường gặp

- `400`: Mã loại đơn không được để trống khi cập nhật.
- `400`: Không tìm thấy loại đơn luân chuyển.

## Phân quyền gợi ý

- ADMIN: CRUD.
- NHAN_VIEN/KHACH_HANG: chỉ đọc hoặc không truy cập tùy theo thiết kế RBAC thực tế.
