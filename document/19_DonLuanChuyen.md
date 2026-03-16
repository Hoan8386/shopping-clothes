# Đơn Luân Chuyển Controller

> Base Path: `/api/v1/don-luan-chuyen`
> File: `DonLuanChuyenController.java`

Module này xử lý luân chuyển tồn kho giữa các cửa hàng.

## Trạng thái đơn luân chuyển

| Mã  | Tên trạng thái |
| --- | -------------- |
| 0   | Chờ xác nhận   |
| 1   | Đang giao      |
| 2   | Đã nhận        |
| 3   | Từ chối        |
| 4   | Đã xác nhận    |

Luồng nghiệp vụ chính theo service:

- Cửa hàng gửi: `0 -> 4` hoặc `0 -> 3`
- Cửa hàng gửi: `4 -> 1` hoặc `4 -> 3`
- Cửa hàng nhận: `1 -> 2`

Khi chuyển sang trạng thái `2`, hệ thống tự động cập nhật tồn kho:

- Trừ số lượng ở cửa hàng gửi.
- Cộng số lượng ở cửa hàng nhận (tạo biến thể mới nếu chưa có).

## DTO tạo đơn

```json
{
  "cuaHangDatId": 1,
  "cuaHangGuiId": 2,
  "loaiDonLuanChuyenId": 1,
  "tenDon": "LC-20260316-001",
  "ghiTru": "Điều chuyển hàng theo nhu cầu",
  "chiTietDonLuanChuyens": [
    {
      "chiTietSanPhamId": 101,
      "soLuong": 5,
      "ghiTru": "Ưu tiên size M"
    }
  ]
}
```

## 1) Tạo đơn luân chuyển

- Method: `POST`
- Endpoint: `/api/v1/don-luan-chuyen`
- Body: `ReqDonLuanChuyenDTO`
- Response: `201 Created` + `ResDonLuanChuyenDTO`

Ràng buộc chính:

- `cuaHangDatId`, `cuaHangGuiId`, `loaiDonLuanChuyenId` bắt buộc.
- Cửa hàng đặt và gửi phải khác nhau.
- Danh sách chi tiết không được rỗng.
- Mỗi item phải có `chiTietSanPhamId`, `soLuong > 0`.
- Chi tiết sản phẩm phải thuộc cửa hàng gửi và đủ tồn kho.

## 2) Lấy danh sách đơn

- Method: `GET`
- Endpoint: `/api/v1/don-luan-chuyen`
- Query params:

| Param          | Kiểu    | Bắt buộc | Mô tả                 |
| -------------- | ------- | -------- | --------------------- |
| `cuaHangGuiId` | Long    | Không    | Lọc theo cửa hàng gửi |
| `cuaHangDatId` | Long    | Không    | Lọc theo cửa hàng đặt |
| `page`         | Integer | Không    | Trang                 |
| `size`         | Integer | Không    | Kích thước trang      |
| `sort`         | String  | Không    | Sắp xếp               |

- Response: `200 OK` + `ResultPaginationDTO`

## 3) Lấy chi tiết theo mã

- Method: `GET`
- Endpoint: `/api/v1/don-luan-chuyen/{id}`
- Response: `200 OK` + `ResDonLuanChuyenDTO`

## 4) Lấy danh sách theo cửa hàng đặt

- Method: `GET`
- Endpoint: `/api/v1/don-luan-chuyen/cua-hang-dat/{cuaHangId}`
- Response: `200 OK` + `List<ResDonLuanChuyenDTO>`

## 5) Lấy danh sách theo cửa hàng gửi

- Method: `GET`
- Endpoint: `/api/v1/don-luan-chuyen/cua-hang-gui/{cuaHangId}`
- Response: `200 OK` + `List<ResDonLuanChuyenDTO>`

## 6) Cập nhật trạng thái

- Method: `PUT`
- Endpoint: `/api/v1/don-luan-chuyen/{id}/trang-thai`
- Query param: `trangThai` (Integer)
- Response: `200 OK` + `ResDonLuanChuyenDTO`

Ví dụ:

```http
PUT /api/v1/don-luan-chuyen/15/trang-thai?trangThai=1
Authorization: Bearer <token>
```

## Mã lỗi thường gặp

- `400`: Không tìm thấy đơn/cửa hàng/loại đơn/chi tiết sản phẩm.
- `400`: Trạng thái không hợp lệ theo nghiệp vụ.
- `400`: Không đủ tồn kho tại cửa hàng gửi.
- `400`: Nhân viên không thuộc cửa hàng có quyền thao tác đơn.

## Phân quyền gợi ý

- ADMIN: toàn quyền.
- NHAN_VIEN: thao tác theo phạm vi cửa hàng được gán.
- KHACH_HANG: không sử dụng module này.
