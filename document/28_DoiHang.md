# Đổi Hàng Controller

> Base Path: `/api/v1/doi-hang`
> File: `DoiHangController.java`

Module này xử lý nghiệp vụ đổi hàng sau bán.

## Trạng thái phiếu đổi

| Mã  | Tên trạng thái |
| --- | -------------- |
| 0   | Chờ xử lý      |
| 1   | Đã duyệt       |
| 2   | Từ chối        |

## Điều kiện tạo phiếu đổi

Theo service:

- Đơn hàng phải ở trạng thái `5` (Đã nhận hàng).
- Chỉ chủ đơn (khách hàng sở hữu đơn) mới được đổi.
- Phải có ít nhất 1 dòng chi tiết đổi.
- Sản phẩm đổi phải đủ tồn kho.

## DTO tạo phiếu đổi

```json
{
  "donHangId": 1001,
  "ghiTru": "Muốn đổi màu",
  "chiTietDoiHangs": [
    {
      "chiTietDonHangId": 5001,
      "chiTietSanPhamId": 7003,
      "ghiTru": "Đổi từ trắng sang đen"
    }
  ]
}
```

## 1) Tạo phiếu đổi hàng

- Method: `POST`
- Endpoint: `/api/v1/doi-hang`
- Body: `ReqDoiHangDTO`
- Response: `201 Created` + `ResDoiHangDTO`

Lưu ý nghiệp vụ:

- Hệ thống tính `tongTien` dựa trên chênh lệch giá giữa hàng đổi và hàng trả.
- Trạng thái mặc định lúc tạo là `0` (Chờ xử lý).

## 2) Lấy danh sách phiếu đổi

- Method: `GET`
- Endpoint: `/api/v1/doi-hang`
- Query: `page`, `size`, `sort`
- Response: `200 OK` + `ResultPaginationDTO`

## 3) Lấy phiếu đổi theo mã

- Method: `GET`
- Endpoint: `/api/v1/doi-hang/{id}`
- Response: `200 OK` + `ResDoiHangDTO`

## 4) Lấy danh sách theo đơn hàng

- Method: `GET`
- Endpoint: `/api/v1/doi-hang/don-hang/{donHangId}`
- Response: `200 OK` + `List<ResDoiHangDTO>`

## 5) Cập nhật trạng thái phiếu đổi

- Method: `PUT`
- Endpoint: `/api/v1/doi-hang/{id}/trang-thai`
- Query param: `trangThai` (`1` hoặc `2`)
- Response: `200 OK` + `ResDoiHangDTO`

Ví dụ:

```http
PUT /api/v1/doi-hang/12/trang-thai?trangThai=1
Authorization: Bearer <token>
```

Khi duyệt (`trangThai=1`):

- Hệ thống tăng tồn kho sản phẩm trả.
- Hệ thống giảm tồn kho sản phẩm đổi.

## Mã lỗi thường gặp

- `400`: Chỉ đổi hàng khi đơn ở trạng thái đã nhận.
- `400`: Bạn không có quyền đổi hàng cho đơn này.
- `400`: Trạng thái chuyển không hợp lệ.
- `400`: Không đủ số lượng sản phẩm đổi trong kho.

## Phân quyền gợi ý

- KHACH_HANG: tạo và theo dõi phiếu đổi của mình.
- NHAN_VIEN/ADMIN: duyệt hoặc từ chối phiếu đổi theo nghiệp vụ vận hành.
