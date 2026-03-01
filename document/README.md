# Shopping API Documentation

> Tài liệu mô tả tất cả các API endpoints của hệ thống Shopping.  
> Base URL: `/api/v1`

---

## Mục lục

| #   | Controller                    | File tài liệu                                        | Mô tả                                                  |
| --- | ----------------------------- | ---------------------------------------------------- | ------------------------------------------------------ |
| 1   | AuthController                | [01_Auth.md](01_Auth.md)                             | Xác thực: Đăng nhập, Đăng ký, Refresh Token, Đăng xuất |
| 2   | SanPhamController             | [02_SanPham.md](02_SanPham.md)                       | Quản lý sản phẩm                                       |
| 3   | ChiTietSanPhamController      | [03_ChiTietSanPham.md](03_ChiTietSanPham.md)         | Quản lý chi tiết sản phẩm (biến thể)                   |
| 4   | DonHangController             | [04_DonHang.md](04_DonHang.md)                       | Quản lý đơn hàng                                       |
| 5   | ChiTietDonHangController      | [05_ChiTietDonHang.md](05_ChiTietDonHang.md)         | Quản lý chi tiết đơn hàng                              |
| 6   | GioHangController             | [06_GioHang.md](06_GioHang.md)                       | Quản lý giỏ hàng                                       |
| 7   | PhieuNhapController           | [07_PhieuNhap.md](07_PhieuNhap.md)                   | Quản lý phiếu nhập hàng                                |
| 8   | ChiTietPhieuNhapController    | [08_ChiTietPhieuNhap.md](08_ChiTietPhieuNhap.md)     | Quản lý chi tiết phiếu nhập                            |
| 9   | HinhAnhController             | [09_HinhAnh.md](09_HinhAnh.md)                       | Quản lý hình ảnh sản phẩm                              |
| 10  | BoSuuTapController            | [10_BoSuuTap.md](10_BoSuuTap.md)                     | Quản lý bộ sưu tập                                     |
| 11  | KieuSanPhamController         | [11_KieuSanPham.md](11_KieuSanPham.md)               | Quản lý kiểu sản phẩm                                  |
| 12  | ThuongHieuController          | [12_ThuongHieu.md](12_ThuongHieu.md)                 | Quản lý thương hiệu                                    |
| 13  | MauSacController              | [13_MauSac.md](13_MauSac.md)                         | Quản lý màu sắc                                        |
| 14  | KichThuocController           | [14_KichThuoc.md](14_KichThuoc.md)                   | Quản lý kích thước                                     |
| 15  | CuaHangController             | [15_CuaHang.md](15_CuaHang.md)                       | Quản lý cửa hàng                                       |
| 16  | NhaCungCapController          | [16_NhaCungCap.md](16_NhaCungCap.md)                 | Quản lý nhà cung cấp                                   |
| 17  | RoleController                | [17_Role.md](17_Role.md)                             | Quản lý vai trò                                        |
| 18  | PermissionController          | [18_Permission.md](18_Permission.md)                 | Quản lý quyền hạn                                      |
| 19  | StorageController             | [19_Storage.md](19_Storage.md)                       | Phục vụ file từ MinIO                                  |
| 20  | KhuyenMaiTheoHoaDonController | [Khuyến Mãi Theo Hóa Đơn](20_KhuyenMaiTheoHoaDon.md) | Quản lý khuyến mãi theo hóa đơn                        |
| 21  | KhuyenMaiTheoDiemController   | [Khuyến Mãi Theo Điểm](21_KhuyenMaiTheoDiem.md)      | Quản lý khuyến mãi theo điểm tích lũy                  |

---

## Xác thực

Hệ thống sử dụng **JWT (JSON Web Token)** để xác thực:

1. Gọi `POST /api/v1/auth/login` để lấy `accessToken`
2. Đính kèm token vào header: `Authorization: Bearer <accessToken>`
3. Khi token hết hạn, gọi `GET /api/v1/auth/refresh` (sử dụng cookie `refresh_token`)
4. Đăng xuất bằng `POST /api/v1/auth/logout`

**Endpoints không yêu cầu xác thực:**

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `GET /api/v1/auth/refresh`
- `GET /storage/{fileName}`

---

## Mã lỗi chung

| HTTP Status        | Mô tả                         |
| ------------------ | ----------------------------- |
| `200 OK`           | Thành công                    |
| `201 Created`      | Tạo mới thành công            |
| `204 No Content`   | Xóa thành công                |
| `400 Bad Request`  | Dữ liệu đầu vào không hợp lệ  |
| `401 Unauthorized` | Chưa xác thực / Token hết hạn |
| `403 Forbidden`    | Không có quyền truy cập       |
| `404 Not Found`    | Không tìm thấy tài nguyên     |
