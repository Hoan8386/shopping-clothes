# Shopping API Documentation

Tài liệu này đã được rà soát lại theo mã nguồn backend hiện tại trong thư mục `src/main/java/com/vn/shopping/controller`.

- Base URL: `/api/v1`
- Chuẩn mã hóa tài liệu: `UTF-8`
- Cơ chế xác thực: `JWT + refresh token cookie`

---

## 1. Danh mục tài liệu chi tiết

| STT | Module                          | File tài liệu                                                        |
| --- | ------------------------------- | -------------------------------------------------------------------- |
| 00  | Tổng hợp chức năng theo vai trò | [00_TongHopChucNang_TheoVaiTro.md](00_TongHopChucNang_TheoVaiTro.md) |
| 01  | Auth                            | [01_Auth.md](01_Auth.md)                                             |
| 02  | Sản phẩm                        | [02_SanPham.md](02_SanPham.md)                                       |
| 03  | Chi tiết sản phẩm               | [03_ChiTietSanPham.md](03_ChiTietSanPham.md)                         |
| 04  | Đơn hàng                        | [04_DonHang.md](04_DonHang.md)                                       |
| 05  | Chi tiết đơn hàng               | [05_ChiTietDonHang.md](05_ChiTietDonHang.md)                         |
| 06  | Giỏ hàng                        | [06_GioHang.md](06_GioHang.md)                                       |
| 07  | Phiếu nhập                      | [07_PhieuNhap.md](07_PhieuNhap.md)                                   |
| 08  | Chi tiết phiếu nhập             | [08_ChiTietPhieuNhap.md](08_ChiTietPhieuNhap.md)                     |
| 09  | Hình ảnh                        | [09_HinhAnh.md](09_HinhAnh.md)                                       |
| 10  | Bộ sưu tập                      | [10_BoSuuTap.md](10_BoSuuTap.md)                                     |
| 11  | Kiểu sản phẩm                   | [11_KieuSanPham.md](11_KieuSanPham.md)                               |
| 12  | Thương hiệu                     | [12_ThuongHieu.md](12_ThuongHieu.md)                                 |
| 13  | Màu sắc                         | [13_MauSac.md](13_MauSac.md)                                         |
| 14  | Kích thước                      | [14_KichThuoc.md](14_KichThuoc.md)                                   |
| 15  | Cửa hàng                        | [15_CuaHang.md](15_CuaHang.md)                                       |
| 16  | Nhà cung cấp                    | [16_NhaCungCap.md](16_NhaCungCap.md)                                 |
| 17  | Role                            | [17_Role.md](17_Role.md)                                             |
| 18  | Permission                      | [18_Permission.md](18_Permission.md)                                 |
| 19  | Đơn luân chuyển                 | [19_DonLuanChuyen.md](19_DonLuanChuyen.md)                           |
| 20  | Khuyến mãi theo hóa đơn         | [20_KhuyenMaiTheoHoaDon.md](20_KhuyenMaiTheoHoaDon.md)               |
| 21  | Khuyến mãi theo điểm            | [21_KhuyenMaiTheoDiem.md](21_KhuyenMaiTheoDiem.md)                   |
| 22  | Đánh giá sản phẩm               | [22_DanhGiaSanPham.md](22_DanhGiaSanPham.md)                         |
| 23  | Nhân viên                       | [23_NhanVien.md](23_NhanVien.md)                                     |
| 24  | Trả hàng                        | [24_TraHang.md](24_TraHang.md)                                       |
| 25  | Loại kiểm kê                    | [25_LoaiKiemKe.md](25_LoaiKiemKe.md)                                 |
| 26  | Kiểm kê hàng hóa                | [26_KiemKeHangHoa.md](26_KiemKeHangHoa.md)                           |
| 27  | Chi tiết kiểm kê                | [27_ChiTietKiemKe.md](27_ChiTietKiemKe.md)                           |
| 28  | Đổi hàng                        | [28_DoiHang.md](28_DoiHang.md)                                       |
| 29  | Loại đơn luân chuyển            | [29_LoaiDonLuanChuyen.md](29_LoaiDonLuanChuyen.md)                   |
| 30  | Thanh toán VNPay                | [30_VNPay.md](30_VNPay.md)                                           |
| 31  | Ca làm việc                     | [31_CaLamViec.md](31_CaLamViec.md)                                   |
| 32  | Lịch làm việc                   | [32_LichLamViec.md](32_LichLamViec.md)                               |
| 33  | Chi tiết lịch làm               | [33_ChiTietLichLam.md](33_ChiTietLichLam.md)                         |
| 34  | Lương cơ bản                    | [34_LuongCoban.md](34_LuongCoban.md)                                 |
| 35  | Lương thưởng                    | [35_LuongThuong.md](35_LuongThuong.md)                               |
| 36  | Đổi ca                          | [36_DoiCa.md](36_DoiCa.md)                                           |
| 37  | Lỗi phát sinh                   | [37_LoiPhatSinh.md](37_LoiPhatSinh.md)                               |
| 38  | Giỏ hàng nhân viên              | [38_GioHangNhanVien.md](38_GioHangNhanVien.md)                       |
| 39  | Tra cứu khách hàng              | [39_KhachHangLookup.md](39_KhachHangLookup.md)                       |

---

## 2. Xác thực và phân quyền

- Access token được lấy từ `POST /api/v1/auth/login`.
- Gắn token vào header: `Authorization: Bearer <accessToken>`.
- Gia hạn bằng `GET /api/v1/auth/refresh` (sử dụng cookie refresh token).
- Thu hồi phiên bằng `POST /api/v1/auth/logout`.

Endpoint thường mở công khai:

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `GET /api/v1/auth/refresh`
- URL ảnh Cloudinary (`secure_url`)

Lưu ý: quyền truy cập thật sự phụ thuộc vào bảng `Permission (apiPath, method)` và role gán cho user.

---

## 3. Danh sách endpoint backend hiện tại

### AuthController (`/api/v1`)

- `POST /api/v1/auth/login`
- `GET /api/v1/auth/account`
- `GET /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`
- `PUT /api/v1/auth/change-password`
- `PUT /api/v1/auth/profile` (multipart/form-data)
- `POST /api/v1/auth/register`

### SanPhamController (`/api/v1/san-pham`)

- `GET /api/v1/san-pham`
- `GET /api/v1/san-pham/{id}`
- `POST /api/v1/san-pham` (multipart/form-data)
- `PUT /api/v1/san-pham` (multipart/form-data)
- `DELETE /api/v1/san-pham/{id}`

### ChiTietSanPhamController (`/api/v1/chi-tiet-san-pham`)

- `GET /api/v1/chi-tiet-san-pham`
- `GET /api/v1/chi-tiet-san-pham/{id}`
- `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`
- `GET /api/v1/chi-tiet-san-pham/san-pham-tai-cua-hang`
- `POST /api/v1/chi-tiet-san-pham` (multipart/form-data)
- `PUT /api/v1/chi-tiet-san-pham` (multipart/form-data)
- `DELETE /api/v1/chi-tiet-san-pham/{id}`

### DonHangController (`/api/v1/don-hang`)

- `GET /api/v1/don-hang`
- `GET /api/v1/don-hang/{id}`
- `POST /api/v1/don-hang/online`
- `POST /api/v1/don-hang/tai-quay`
- `PUT /api/v1/don-hang`
- `DELETE /api/v1/don-hang/{id}`

### ChiTietDonHangController (`/api/v1/chi-tiet-don-hang`)

- `GET /api/v1/chi-tiet-don-hang`
- `GET /api/v1/chi-tiet-don-hang/{id}`
- `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}`
- `POST /api/v1/chi-tiet-don-hang`
- `PUT /api/v1/chi-tiet-don-hang`
- `DELETE /api/v1/chi-tiet-don-hang/{id}`

### GioHangController (`/api/v1/gio-hang`)

- `POST /api/v1/gio-hang/them-san-pham`
- `GET /api/v1/gio-hang/cua-toi`
- `DELETE /api/v1/gio-hang/chi-tiet/{maChiTietGioHang}`
- `GET /api/v1/gio-hang/khuyen-mai-hop-le`
- `POST /api/v1/gio-hang/ap-dung-khuyen-mai`

### GioHangNhanVienController (`/api/v1/gio-hang-nhan-vien`)

- `GET /api/v1/gio-hang-nhan-vien/danh-sach`
- `GET /api/v1/gio-hang-nhan-vien/{id}`
- `DELETE /api/v1/gio-hang-nhan-vien/{id}`
- `POST /api/v1/gio-hang-nhan-vien/moi`
- `GET /api/v1/gio-hang-nhan-vien/hien-tai`
- `PUT /api/v1/gio-hang-nhan-vien/thong-tin-khach`
- `POST /api/v1/gio-hang-nhan-vien/them-san-pham`
- `PUT /api/v1/gio-hang-nhan-vien/chi-tiet/{id}`
- `DELETE /api/v1/gio-hang-nhan-vien/chi-tiet/{id}`
- `PUT /api/v1/gio-hang-nhan-vien/khuyen-mai`
- `POST /api/v1/gio-hang-nhan-vien/thanh-toan`
- `POST /api/v1/gio-hang-nhan-vien/thanh-toan/vnpay-url`

### KhachHangLookupController (`/api/v1/khach-hang`)

- `GET /api/v1/khach-hang/lookup`

### PhieuNhapController (`/api/v1/phieu-nhap`)

- `GET /api/v1/phieu-nhap`
- `GET /api/v1/phieu-nhap/{id}`
- `POST /api/v1/phieu-nhap`
- `PUT /api/v1/phieu-nhap`
- `PUT /api/v1/phieu-nhap/kiem-ke/{id}`
- `DELETE /api/v1/phieu-nhap/{id}`

### ChiTietPhieuNhapController (`/api/v1/chi-tiet-phieu-nhap`)

- `GET /api/v1/chi-tiet-phieu-nhap`
- `GET /api/v1/chi-tiet-phieu-nhap/{id}`
- `GET /api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}`
- `POST /api/v1/chi-tiet-phieu-nhap`
- `PUT /api/v1/chi-tiet-phieu-nhap`
- `DELETE /api/v1/chi-tiet-phieu-nhap/{id}`

### HinhAnhController (`/api/v1/hinh-anh`)

- `GET /api/v1/hinh-anh`
- `GET /api/v1/hinh-anh/{id}`
- `GET /api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}`
- `POST /api/v1/hinh-anh`
- `POST /api/v1/hinh-anh/upload/{chiTietSanPhamId}` (multipart/form-data)
- `PUT /api/v1/hinh-anh`
- `DELETE /api/v1/hinh-anh/{id}`

### BoSuuTapController (`/api/v1/bo-suu-tap`)

- `GET /api/v1/bo-suu-tap`
- `GET /api/v1/bo-suu-tap/{id}`
- `POST /api/v1/bo-suu-tap`
- `PUT /api/v1/bo-suu-tap`
- `DELETE /api/v1/bo-suu-tap/{id}`

### KieuSanPhamController (`/api/v1/kieu-san-pham`)

- `GET /api/v1/kieu-san-pham`
- `GET /api/v1/kieu-san-pham/{id}`
- `POST /api/v1/kieu-san-pham`
- `PUT /api/v1/kieu-san-pham`
- `DELETE /api/v1/kieu-san-pham/{id}`

### ThuongHieuController (`/api/v1/thuong-hieu`)

- `GET /api/v1/thuong-hieu`
- `GET /api/v1/thuong-hieu/{id}`
- `POST /api/v1/thuong-hieu`
- `PUT /api/v1/thuong-hieu`
- `DELETE /api/v1/thuong-hieu/{id}`

### MauSacController (`/api/v1/mau-sac`)

- `GET /api/v1/mau-sac`
- `GET /api/v1/mau-sac/{id}`
- `POST /api/v1/mau-sac`
- `PUT /api/v1/mau-sac`
- `DELETE /api/v1/mau-sac/{id}`

### KichThuocController (`/api/v1/kich-thuoc`)

- `GET /api/v1/kich-thuoc`
- `GET /api/v1/kich-thuoc/{id}`
- `POST /api/v1/kich-thuoc`
- `PUT /api/v1/kich-thuoc`
- `DELETE /api/v1/kich-thuoc/{id}`

### CuaHangController (`/api/v1/cua-hang`)

- `GET /api/v1/cua-hang`
- `GET /api/v1/cua-hang/{id}`
- `POST /api/v1/cua-hang`
- `PUT /api/v1/cua-hang`
- `DELETE /api/v1/cua-hang/{id}`

### NhaCungCapController (`/api/v1/nha-cung-cap`)

- `GET /api/v1/nha-cung-cap`
- `GET /api/v1/nha-cung-cap/{id}`
- `POST /api/v1/nha-cung-cap`
- `PUT /api/v1/nha-cung-cap`
- `DELETE /api/v1/nha-cung-cap/{id}`

### RoleController (`/api/v1/roles`)

- `GET /api/v1/roles`
- `GET /api/v1/roles/{id}`
- `POST /api/v1/roles`
- `PUT /api/v1/roles`
- `DELETE /api/v1/roles/{id}`

### PermissionController (`/api/v1/permissions`)

- `GET /api/v1/permissions`
- `GET /api/v1/permissions/{id}`
- `POST /api/v1/permissions`
- `PUT /api/v1/permissions`
- `DELETE /api/v1/permissions/{id}`

### KhuyenMaiTheoHoaDonController (`/api/v1/khuyen-mai-theo-hoa-don`)

- `GET /api/v1/khuyen-mai-theo-hoa-don`
- `GET /api/v1/khuyen-mai-theo-hoa-don/{id}`
- `POST /api/v1/khuyen-mai-theo-hoa-don`
- `PUT /api/v1/khuyen-mai-theo-hoa-don`
- `DELETE /api/v1/khuyen-mai-theo-hoa-don/{id}`

### KhuyenMaiTheoDiemController (`/api/v1/khuyen-mai-theo-diem`)

- `GET /api/v1/khuyen-mai-theo-diem`
- `GET /api/v1/khuyen-mai-theo-diem/{id}`
- `POST /api/v1/khuyen-mai-theo-diem`
- `PUT /api/v1/khuyen-mai-theo-diem`
- `DELETE /api/v1/khuyen-mai-theo-diem/{id}`

### DanhGiaSanPhamController (`/api/v1/danh-gia-san-pham`)

- `GET /api/v1/danh-gia-san-pham`
- `GET /api/v1/danh-gia-san-pham/{id}`
- `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`
- `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}`
- `GET /api/v1/danh-gia-san-pham/cua-toi`
- `POST /api/v1/danh-gia-san-pham` (multipart/form-data)
- `PUT /api/v1/danh-gia-san-pham/{id}` (multipart/form-data)
- `DELETE /api/v1/danh-gia-san-pham/{id}`

### NhanVienController (`/api/v1/nhan-vien`)

- `GET /api/v1/nhan-vien`
- `POST /api/v1/nhan-vien`
- `PUT /api/v1/nhan-vien`
- `DELETE /api/v1/nhan-vien/{id}`

### TraHangController (`/api/v1/tra-hang`)

- `GET /api/v1/tra-hang`
- `GET /api/v1/tra-hang/{id}`
- `GET /api/v1/tra-hang/don-hang/{donHangId}`
- `POST /api/v1/tra-hang` (application/json)
- `POST /api/v1/tra-hang` (multipart/form-data, part `data` + `file`)
- `PUT /api/v1/tra-hang/{id}/trang-thai`

### LoaiKiemKeController (`/api/v1/loai-kiem-ke`)

- `GET /api/v1/loai-kiem-ke`
- `GET /api/v1/loai-kiem-ke/{id}`
- `POST /api/v1/loai-kiem-ke`
- `PUT /api/v1/loai-kiem-ke`
- `DELETE /api/v1/loai-kiem-ke/{id}`

### KiemKeHangHoaController (`/api/v1/kiem-ke-hang-hoa`)

- `GET /api/v1/kiem-ke-hang-hoa`
- `GET /api/v1/kiem-ke-hang-hoa/{id}`
- `POST /api/v1/kiem-ke-hang-hoa`
- `PUT /api/v1/kiem-ke-hang-hoa`
- `PUT /api/v1/kiem-ke-hang-hoa/{id}/gui-duyet`
- `PUT /api/v1/kiem-ke-hang-hoa/{id}/duyet`

### DonLuanChuyenController (`/api/v1/don-luan-chuyen`)

- `GET /api/v1/don-luan-chuyen`
- `GET /api/v1/don-luan-chuyen/{id}`
- `GET /api/v1/don-luan-chuyen/cua-hang-dat/{cuaHangId}`
- `GET /api/v1/don-luan-chuyen/cua-hang-gui/{cuaHangId}`
- `POST /api/v1/don-luan-chuyen`
- `PUT /api/v1/don-luan-chuyen/{id}/trang-thai`

### LoaiDonLuanChuyenController (`/api/v1/loai-don-luan-chuyen`)

- `GET /api/v1/loai-don-luan-chuyen`
- `GET /api/v1/loai-don-luan-chuyen/{id}`
- `POST /api/v1/loai-don-luan-chuyen`
- `PUT /api/v1/loai-don-luan-chuyen`
- `DELETE /api/v1/loai-don-luan-chuyen/{id}`

### DoiHangController (`/api/v1/doi-hang`)

- `GET /api/v1/doi-hang`
- `GET /api/v1/doi-hang/{id}`
- `GET /api/v1/doi-hang/don-hang/{donHangId}`
- `POST /api/v1/doi-hang`
- `PUT /api/v1/doi-hang/{id}/trang-thai`

### CaLamViecController (`/api/v1/ca-lam-viec`)

- `GET /api/v1/ca-lam-viec`
- `GET /api/v1/ca-lam-viec/{id}`
- `POST /api/v1/ca-lam-viec`
- `PUT /api/v1/ca-lam-viec`
- `DELETE /api/v1/ca-lam-viec/{id}`

### LichLamViecController (`/api/v1/lich-lam-viec`)

- `GET /api/v1/lich-lam-viec`
- `GET /api/v1/lich-lam-viec/{id}`
- `GET /api/v1/lich-lam-viec/nhan-vien/{nhanVienId}`
- `GET /api/v1/lich-lam-viec/cua-hang/{cuaHangId}`
- `GET /api/v1/lich-lam-viec/cua-hang/{cuaHangId}/thang`
- `PUT /api/v1/lich-lam-viec/cua-hang/{cuaHangId}/ngay/trang-thai`
- `POST /api/v1/lich-lam-viec/cua-hang/{cuaHangId}/ngay/ca-lam-viec`
- `DELETE /api/v1/lich-lam-viec/cua-hang/{cuaHangId}/ngay/ca-lam-viec`
- `POST /api/v1/lich-lam-viec`
- `PUT /api/v1/lich-lam-viec`
- `DELETE /api/v1/lich-lam-viec/{id}`
- `POST /api/v1/lich-lam-viec/cua-hang/{cuaHangId}/import` (multipart/form-data)
- `GET /api/v1/lich-lam-viec/cua-hang/{cuaHangId}/download-template`

### ChiTietLichLamController (`/api/v1/chi-tiet-lich-lam`)

- `GET /api/v1/chi-tiet-lich-lam`
- `GET /api/v1/chi-tiet-lich-lam/{id}`
- `GET /api/v1/chi-tiet-lich-lam/lich-lam-viec/{lichLamViecId}`
- `POST /api/v1/chi-tiet-lich-lam`
- `PUT /api/v1/chi-tiet-lich-lam`
- `DELETE /api/v1/chi-tiet-lich-lam/{id}`

### LuongCobanController (`/api/v1/luong-co-ban`)

- `GET /api/v1/luong-co-ban`
- `GET /api/v1/luong-co-ban/{id}`
- `GET /api/v1/luong-co-ban/nhan-vien/{nhanVienId}`
- `POST /api/v1/luong-co-ban`
- `PUT /api/v1/luong-co-ban`

### LuongThuongController (`/api/v1/luong-thuong`)

- `GET /api/v1/luong-thuong`
- `GET /api/v1/luong-thuong/{id}`
- `GET /api/v1/luong-thuong/nhan-vien/{nhanVienId}`
- `POST /api/v1/luong-thuong`
- `PUT /api/v1/luong-thuong`
- `DELETE /api/v1/luong-thuong/{id}`

### DoiCaController (`/api/v1/doi-ca`)

- `GET /api/v1/doi-ca`
- `GET /api/v1/doi-ca/{id}`
- `GET /api/v1/doi-ca/lich-lam-viec/{lichLamViecId}`
- `POST /api/v1/doi-ca`
- `PUT /api/v1/doi-ca`
- `DELETE /api/v1/doi-ca/{id}`

### LoiPhatSinhController (`/api/v1/loi-phat-sinh`)

- `GET /api/v1/loi-phat-sinh`
- `GET /api/v1/loi-phat-sinh/{id}`
- `GET /api/v1/loi-phat-sinh/lich-lam-viec/{lichLamViecId}`
- `POST /api/v1/loi-phat-sinh`
- `PUT /api/v1/loi-phat-sinh`
- `DELETE /api/v1/loi-phat-sinh/{id}`

### VNPayController (`/api/v1/auth/vnpay`)

- `POST /api/v1/auth/vnpay/create-payment-url`
- `GET /api/v1/auth/vnpay/return`

---

## 4. Mã trạng thái HTTP thường gặp

| Status             | Ý nghĩa                                               |
| ------------------ | ----------------------------------------------------- |
| `200 OK`           | Thành công                                            |
| `201 Created`      | Tạo mới thành công                                    |
| `204 No Content`   | Xóa thành công                                        |
| `400 Bad Request`  | Dữ liệu đầu vào hoặc ràng buộc nghiệp vụ không hợp lệ |
| `401 Unauthorized` | Chưa xác thực hoặc token không hợp lệ                 |
| `403 Forbidden`    | Không có quyền truy cập endpoint                      |
| `404 Not Found`    | Không tìm thấy dữ liệu                                |
