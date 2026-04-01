# Giỏ Hàng Nhân Viên Controller

> Base Path: `/api/v1/gio-hang-nhan-vien`  
> File: `GioHangNhanVienController.java`  
> Module dùng cho luồng bán tại quầy: tạo giỏ nháp, thêm sản phẩm, áp khuyến mãi và thanh toán.

---

## 1. Danh sách endpoint

| Method | Endpoint                                          | Mô tả                             |
| ------ | ------------------------------------------------- | --------------------------------- |
| GET    | `/api/v1/gio-hang-nhan-vien/danh-sach`            | Lấy các giỏ nháp chưa thanh toán  |
| GET    | `/api/v1/gio-hang-nhan-vien/{id}`                 | Lấy chi tiết giỏ nháp theo ID     |
| DELETE | `/api/v1/gio-hang-nhan-vien/{id}`                 | Xóa giỏ nháp                      |
| POST   | `/api/v1/gio-hang-nhan-vien/moi`                  | Tạo giỏ nháp mới                  |
| GET    | `/api/v1/gio-hang-nhan-vien/hien-tai`             | Lấy giỏ hiện tại của nhân viên    |
| PUT    | `/api/v1/gio-hang-nhan-vien/thong-tin-khach`      | Cập nhật thông tin người mua      |
| POST   | `/api/v1/gio-hang-nhan-vien/them-san-pham`        | Thêm sản phẩm vào giỏ             |
| PUT    | `/api/v1/gio-hang-nhan-vien/chi-tiet/{id}`        | Cập nhật số lượng 1 dòng sản phẩm |
| DELETE | `/api/v1/gio-hang-nhan-vien/chi-tiet/{id}`        | Xóa 1 dòng sản phẩm khỏi giỏ      |
| PUT    | `/api/v1/gio-hang-nhan-vien/khuyen-mai`           | Cập nhật khuyến mãi áp dụng       |
| POST   | `/api/v1/gio-hang-nhan-vien/thanh-toan`           | Thanh toán và tạo đơn             |
| POST   | `/api/v1/gio-hang-nhan-vien/thanh-toan/vnpay-url` | Tạo URL thanh toán VNPAY          |

---

## 2. Tham số và body mẫu

### 2.1 Cập nhật thông tin khách

- Endpoint: `PUT /api/v1/gio-hang-nhan-vien/thong-tin-khach`
- Query params: `cartId` (optional)
- Body (`ReqCapNhatKhachGioHangNhanVienDTO`):

```json
{
  "tenNguoiMua": "Nguyen Van A",
  "sdt": "0911000001"
}
```

### 2.2 Thêm sản phẩm vào giỏ

- Endpoint: `POST /api/v1/gio-hang-nhan-vien/them-san-pham`
- Query params: `cartId` (optional)
- Body (`ReqThemSanPhamGioHangNhanVienDTO`):

```json
{
  "chiTietSanPhamId": 1,
  "maVach": null,
  "soLuong": 2
}
```

### 2.3 Cập nhật số lượng

- Endpoint: `PUT /api/v1/gio-hang-nhan-vien/chi-tiet/{id}`
- Query params: `cartId` (optional)
- Body (`ReqCapNhatSoLuongGioHangNhanVienDTO`):

```json
{
  "soLuong": 3
}
```

### 2.4 Cập nhật khuyến mãi

- Endpoint: `PUT /api/v1/gio-hang-nhan-vien/khuyen-mai`
- Query params: `cartId` (optional)
- Body (`ReqCapNhatKhuyenMaiGioHangNhanVienDTO`):

```json
{
  "maKhuyenMaiHoaDon": 1,
  "maKhuyenMaiDiem": null
}
```

### 2.5 Thanh toán tại quầy

- Endpoint: `POST /api/v1/gio-hang-nhan-vien/thanh-toan`
- Query params: `cartId` (optional)
- Body (`ReqThanhToanGioHangNhanVienDTO`):

```json
{
  "hinhThucDonHang": 0
}
```

Ghi chú: `hinhThucDonHang` trong request hiện hỗ trợ `0` (tiền mặt/COD) và `1` (VNPAY).

### 2.6 Tạo URL VNPAY cho giỏ nhân viên

- Endpoint: `POST /api/v1/gio-hang-nhan-vien/thanh-toan/vnpay-url`
- Query params: `cartId` (optional)
- Response mẫu:

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?..."
}
```

---

## 3. Phân quyền

- ADMIN: toàn quyền
- NHAN_VIEN: thao tác bán tại quầy
- KHACH_HANG: không dùng module này

Quyền chi tiết map theo bảng `permissions` + `permission_role` trong `insert_data.sql`.
