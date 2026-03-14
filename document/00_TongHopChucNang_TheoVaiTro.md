# TỔNG HỢP CHỨC NĂNG THEO VAI TRÒ — HỆ THỐNG SHOPPING

> **Mục đích:** Tài liệu tổng hợp toàn bộ chức năng của từng vai trò trong hệ thống, giúp lập trình viên Frontend hiểu rõ phạm vi quyền hạn để xây dựng giao diện phù hợp.
>
> **Ngày tạo:** 2026-03-09

---

## MỤC LỤC

1. [Tổng quan hệ thống](#1-tổng-quan-hệ-thống)
2. [Xác thực & Phân quyền](#2-xác-thực--phân-quyền)
3. [Các vai trò trong hệ thống](#3-các-vai-trò-trong-hệ-thống)
4. [ADMIN — Quản trị viên toàn quyền](#4-admin--quản-trị-viên-toàn-quyền)
5. [NHAN_VIEN — Nhân viên bán hàng](#5-nhan_vien--nhân-viên-bán-hàng)
6. [KHACH_HANG — Khách hàng](#6-khach_hang--khách-hàng)
7. [Endpoint công khai (không cần đăng nhập)](#7-endpoint-công-khai-không-cần-đăng-nhập)
8. [Bảng ma trận phân quyền chi tiết](#8-bảng-ma-trận-phân-quyền-chi-tiết)
9. [Gợi ý xây dựng Frontend theo vai trò](#9-gợi-ý-xây-dựng-frontend-theo-vai-trò)

---

## 1. Tổng quan hệ thống

Hệ thống **Shopping** là ứng dụng quản lý bán hàng thời trang với các module:

| STT | Module              | Base Path API                     | Mô tả                              |
| --- | ------------------- | --------------------------------- | ---------------------------------- |
| 1   | Xác thực            | `/api/v1/auth`                    | Đăng nhập, đăng ký, refresh token  |
| 2   | Sản phẩm            | `/api/v1/san-pham`                | Quản lý sản phẩm                   |
| 3   | Chi tiết sản phẩm   | `/api/v1/chi-tiet-san-pham`       | Biến thể SP (màu, size, cửa hàng)  |
| 4   | Đơn hàng            | `/api/v1/don-hang`                | Quản lý đơn hàng online & tại quầy |
| 5   | Chi tiết đơn hàng   | `/api/v1/chi-tiet-don-hang`       | Dòng sản phẩm trong đơn hàng       |
| 6   | Giỏ hàng            | `/api/v1/gio-hang`                | Giỏ hàng khách hàng                |
| 7   | Phiếu nhập          | `/api/v1/phieu-nhap`              | Nhập hàng từ nhà cung cấp          |
| 8   | Chi tiết phiếu nhập | `/api/v1/chi-tiet-phieu-nhap`     | Dòng SP trong phiếu nhập           |
| 9   | Hình ảnh            | `/api/v1/hinh-anh`                | Quản lý ảnh SP (MinIO)             |
| 10  | Bộ sưu tập          | `/api/v1/bo-suu-tap`              | BST theo mùa                       |
| 11  | Kiểu sản phẩm       | `/api/v1/kieu-san-pham`           | Loại SP: Áo, Quần, Váy, ...        |
| 12  | Thương hiệu         | `/api/v1/thuong-hieu`             | Nike, Adidas, Uniqlo, ...          |
| 13  | Màu sắc             | `/api/v1/mau-sac`                 | Đen, Trắng, Đỏ, ...                |
| 14  | Kích thước          | `/api/v1/kich-thuoc`              | S, M, L, XL, ...                   |
| 15  | Cửa hàng            | `/api/v1/cua-hang`                | Chi nhánh cửa hàng                 |
| 16  | Nhà cung cấp        | `/api/v1/nha-cung-cap`            | Nhà cung cấp hàng hóa              |
| 17  | Vai trò             | `/api/v1/roles`                   | Quản lý Role (RBAC)                |
| 18  | Quyền hạn           | `/api/v1/permissions`             | Quản lý Permission                 |
| 19  | KM theo hóa đơn     | `/api/v1/khuyen-mai-theo-hoa-don` | Giảm giá theo tổng đơn hàng        |
| 20  | KM theo điểm        | `/api/v1/khuyen-mai-theo-diem`    | Đổi điểm tích lũy lấy mã giảm      |
| 21  | Đánh giá sản phẩm   | `/api/v1/danh-gia-san-pham`       | Đánh giá & bình luận SP            |
| 22  | Storage (Public)    | `/storage/{fileName}`             | Truy cập ảnh (không cần auth)      |
| 23  | Nhân viên           | `/api/v1/nhan-vien`               | Quản lý thông tin nhân viên        |
| 24  | Trả hàng            | `/api/v1/tra-hang`                | Phiếu trả hàng & hoàn tiền         |

---

## 2. Xác thực & Phân quyền

### 2.1 Cơ chế xác thực — JWT

- **Access Token**: gửi qua header `Authorization: Bearer <token>`, thời gian sống ngắn
- **Refresh Token**: lưu trong cookie `HttpOnly`, dùng để gia hạn access token
- Hệ thống hỗ trợ 2 loại user: **NhanVien** (nhân viên) và **KhachHang** (khách hàng)

### 2.2 Cơ chế phân quyền — RBAC

- Mỗi user được gán 1 **Role** (vai trò)
- Mỗi Role chứa danh sách **Permission** (nhiều-nhiều)
- Mỗi Permission = 1 cặp `(apiPath, method)` → tương ứng 1 endpoint cụ thể
- Khi gọi API, **PermissionInterceptor** kiểm tra:
  1. Lấy email user từ JWT
  2. Tìm user trong bảng `NhanVien` hoặc `KhachHang`
  3. Lấy Role → danh sách Permission
  4. So khớp `(apiPath, method)` với request → cho phép hoặc trả về `403 Forbidden`

### 2.3 Các endpoint đăng nhập / đăng ký (Whitelist — không cần token)

| Endpoint                | Method | Mô tả                                      |
| ----------------------- | ------ | ------------------------------------------ |
| `/api/v1/auth/login`    | POST   | Đăng nhập (email + password)               |
| `/api/v1/auth/register` | POST   | Đăng ký tài khoản khách hàng mới           |
| `/api/v1/auth/refresh`  | GET    | Gia hạn access token (dùng refresh cookie) |
| `/storage/{fileName}`   | GET    | Xem ảnh sản phẩm (public)                  |
| `/v3/api-docs/**`       | GET    | Swagger API docs                           |
| `/swagger-ui/**`        | GET    | Swagger UI                                 |

---

## 3. Các vai trò trong hệ thống

| ID  | Tên vai trò  | Loại user | Mô tả                                                          |
| --- | ------------ | --------- | -------------------------------------------------------------- |
| 1   | `ADMIN`      | NhanVien  | Quản trị viên toàn quyền — truy cập mọi chức năng              |
| 2   | `NHAN_VIEN`  | NhanVien  | Nhân viên bán hàng — xem danh mục, xử lý đơn hàng & phiếu nhập |
| 3   | `KHACH_HANG` | KhachHang | Khách hàng — mua sắm, giỏ hàng, đặt hàng, đánh giá sản phẩm    |

---

## 4. ADMIN — Quản trị viên toàn quyền

> **Phạm vi:** Toàn bộ 113 permission — truy cập mọi API endpoint.

### 4.1 Quản lý sản phẩm & danh mục

| Chức năng                                | Endpoint                                                                                                           | Method |
| ---------------------------------------- | ------------------------------------------------------------------------------------------------------------------ | ------ |
| Xem danh sách sản phẩm (lọc, phân trang) | `GET /api/v1/san-pham?tenSanPham=&kieuSanPhamId=&boSuuTapId=&thuongHieuId=&trangThai=&giaMin=&giaMax=&page=&size=` | GET    |
| Xem chi tiết sản phẩm                    | `GET /api/v1/san-pham/{id}`                                                                                        | GET    |
| Tạo sản phẩm (upload ảnh)                | `POST /api/v1/san-pham` (multipart/form-data)                                                                      | POST   |
| Cập nhật sản phẩm                        | `PUT /api/v1/san-pham`                                                                                             | PUT    |
| Xóa sản phẩm                             | `DELETE /api/v1/san-pham/{id}`                                                                                     | DELETE |
| CRUD Kiểu sản phẩm                       | `/api/v1/kieu-san-pham` + `/{id}`                                                                                  | CRUD   |
| CRUD Bộ sưu tập                          | `/api/v1/bo-suu-tap` + `/{id}`                                                                                     | CRUD   |
| CRUD Thương hiệu                         | `/api/v1/thuong-hieu` + `/{id}`                                                                                    | CRUD   |
| CRUD Màu sắc                             | `/api/v1/mau-sac` + `/{id}`                                                                                        | CRUD   |
| CRUD Kích thước                          | `/api/v1/kich-thuoc` + `/{id}`                                                                                     | CRUD   |

### 4.2 Quản lý chi tiết sản phẩm & hình ảnh

| Chức năng                    | Endpoint                                                      | Method |
| ---------------------------- | ------------------------------------------------------------- | ------ |
| Xem tất cả biến thể SP       | `GET /api/v1/chi-tiet-san-pham`                               | GET    |
| Xem biến thể theo ID         | `GET /api/v1/chi-tiet-san-pham/{id}`                          | GET    |
| Xem biến thể theo sản phẩm   | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`          | GET    |
| Tạo biến thể SP (upload ảnh) | `POST /api/v1/chi-tiet-san-pham` (multipart)                  | POST   |
| Cập nhật biến thể            | `PUT /api/v1/chi-tiet-san-pham`                               | PUT    |
| Xóa biến thể                 | `DELETE /api/v1/chi-tiet-san-pham/{id}`                       | DELETE |
| Xem hình ảnh theo CTSP       | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}`   | GET    |
| Upload hình ảnh cho CTSP     | `POST /api/v1/hinh-anh/upload/{chiTietSanPhamId}` (multipart) | POST   |
| Tạo/Cập nhật/Xóa hình ảnh    | `/api/v1/hinh-anh` + `/{id}`                                  | CRUD   |

### 4.3 Quản lý đơn hàng

| Chức năng                                | Endpoint                                                                                                  | Method |
| ---------------------------------------- | --------------------------------------------------------------------------------------------------------- | ------ |
| Xem danh sách đơn hàng (lọc, phân trang) | `GET /api/v1/don-hang?cuaHangId=&nhanVienId=&trangThai=&trangThaiThanhToan=&hinhThucDonHang=&page=&size=` | GET    |
| Xem chi tiết đơn hàng                    | `GET /api/v1/don-hang/{id}`                                                                               | GET    |
| Tạo đơn hàng online                      | `POST /api/v1/don-hang/online`                                                                            | POST   |
| Tạo đơn hàng tại quầy                    | `POST /api/v1/don-hang/tai-quay`                                                                          | POST   |
| Cập nhật trạng thái đơn hàng             | `PUT /api/v1/don-hang`                                                                                    | PUT    |
| Xóa đơn hàng                             | `DELETE /api/v1/don-hang/{id}`                                                                            | DELETE |
| Xem chi tiết dòng sản phẩm trong đơn     | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}`                                                      | GET    |
| CRUD chi tiết đơn hàng                   | `/api/v1/chi-tiet-don-hang` + `/{id}`                                                                     | CRUD   |

**Trạng thái đơn hàng (trangThai):**

| Giá trị | Ý nghĩa         | Ghi chú                                      |
| ------- | --------------- | -------------------------------------------- |
| 0       | Chờ xác nhận    | Mặc định khi tạo đơn online                  |
| 1       | Đã xác nhận     | NV/Admin xác nhận                            |
| 2       | Đang đóng gói   | Đang chuẩn bị hàng                           |
| 3       | Đang giao hàng  | Đã gửi cho vận chuyển                        |
| 4       | Đã hủy          | Hủy đơn hàng                                 |
| 5       | Đã nhận hàng ✅ | Giao thành công → tự động cộng điểm tích lũy |

**Trạng thái thanh toán (trangThaiThanhToan):** `0` = Chưa thanh toán, `1` = Đã thanh toán

**Hình thức đơn hàng (hinhThucDonHang):** `0` = Tại quầy, `1` = Online

### 4.4 Quản lý nhập hàng

| Chức năng                      | Endpoint                                                                                        | Method |
| ------------------------------ | ----------------------------------------------------------------------------------------------- | ------ |
| Xem danh sách phiếu nhập (lọc) | `GET /api/v1/phieu-nhap?tenPhieuNhap=&trangThai=&cuaHang=&nhaCungCap=&ngayBatDau=&ngayKetThuc=` | GET    |
| Xem chi tiết phiếu nhập        | `GET /api/v1/phieu-nhap/{id}`                                                                   | GET    |
| Tạo phiếu nhập                 | `POST /api/v1/phieu-nhap`                                                                       | POST   |
| Cập nhật phiếu nhập            | `PUT /api/v1/phieu-nhap`                                                                        | PUT    |
| **Kiểm kê phiếu nhập**         | `PUT /api/v1/phieu-nhap/kiem-ke/{id}`                                                           | PUT    |
| Xóa phiếu nhập                 | `DELETE /api/v1/phieu-nhap/{id}`                                                                | DELETE |
| CRUD chi tiết phiếu nhập       | `/api/v1/chi-tiet-phieu-nhap` + `/{id}` + `/phieu-nhap/{phieuNhapId}`                           | CRUD   |

**Trạng thái phiếu nhập:**

| Giá trị | Ý nghĩa       |
| ------- | ------------- |
| 0       | Đã đặt hàng   |
| 1       | Đã nhận hàng  |
| 2       | Chậm giao     |
| 3       | Đã hủy        |
| 4       | Thiếu hàng    |
| 5       | Hoàn thành ✅ |

### 4.5 Quản lý khuyến mãi

| Chức năng                    | Endpoint                                    | Method |
| ---------------------------- | ------------------------------------------- | ------ |
| CRUD Khuyến mãi theo hóa đơn | `/api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | CRUD   |
| CRUD Khuyến mãi theo điểm    | `/api/v1/khuyen-mai-theo-diem` + `/{id}`    | CRUD   |

### 4.6 Quản lý cửa hàng & nhà cung cấp

| Chức năng         | Endpoint                         | Method |
| ----------------- | -------------------------------- | ------ |
| CRUD Cửa hàng     | `/api/v1/cua-hang` + `/{id}`     | CRUD   |
| CRUD Nhà cung cấp | `/api/v1/nha-cung-cap` + `/{id}` | CRUD   |
| CRUD Nhân viên    | `/api/v1/nhan-vien` + `/{id}`    | CRUD   |

### 4.7 Quản lý phân quyền (RBAC)

| Chức năng      | Endpoint                        | Method |
| -------------- | ------------------------------- | ------ |
| CRUD Vai trò   | `/api/v1/roles` + `/{id}`       | CRUD   |
| CRUD Quyền hạn | `/api/v1/permissions` + `/{id}` | CRUD   |

### 4.8 Quản lý đánh giá sản phẩm

| Chức năng                           | Endpoint                                                             | Method |
| ----------------------------------- | -------------------------------------------------------------------- | ------ |
| Xem tất cả đánh giá                 | `GET /api/v1/danh-gia-san-pham`                                      | GET    |
| Xem đánh giá theo ID                | `GET /api/v1/danh-gia-san-pham/{id}`                                 | GET    |
| Xem đánh giá theo sản phẩm          | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`                 | GET    |
| Xem đánh giá theo chi tiết đơn hàng | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` | GET    |
| **Xóa bất kỳ đánh giá nào**         | `DELETE /api/v1/danh-gia-san-pham/{id}`                              | DELETE |

> **Lưu ý:** Admin KHÔNG tạo/sửa đánh giá, chỉ có thể xóa bất kỳ đánh giá nào (kiểm duyệt).

### 4.9 Xác thực

| Chức năng               | Endpoint                   | Method |
| ----------------------- | -------------------------- | ------ |
| Đăng nhập               | `POST /api/v1/auth/login`  | POST   |
| Xem thông tin tài khoản | `GET /api/v1/auth/account` | GET    |
| Gia hạn token           | `GET /api/v1/auth/refresh` | GET    |
| Đăng xuất               | `POST /api/v1/auth/logout` | POST   |

---

## 5. NHAN_VIEN — Nhân viên bán hàng

> **Phạm vi:** Xem danh mục & sản phẩm (chỉ GET), xử lý đơn hàng tại quầy, quản lý phiếu nhập.
>
> **Tổng:** 47 permission

### 5.1 Xem sản phẩm & danh mục (CHỈ XEM — không tạo/sửa/xóa)

| Chức năng                   | Endpoint                                                         | Method |
| --------------------------- | ---------------------------------------------------------------- | ------ |
| Xem danh sách sản phẩm      | `GET /api/v1/san-pham`                                           | GET    |
| Xem chi tiết sản phẩm       | `GET /api/v1/san-pham/{id}`                                      | GET    |
| Xem tất cả biến thể SP      | `GET /api/v1/chi-tiet-san-pham`                                  | GET    |
| Xem biến thể theo ID        | `GET /api/v1/chi-tiet-san-pham/{id}`                             | GET    |
| Xem biến thể theo sản phẩm  | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`             | GET    |
| Xem danh sách màu sắc       | `GET /api/v1/mau-sac` + `/{id}`                                  | GET    |
| Xem danh sách kích thước    | `GET /api/v1/kich-thuoc` + `/{id}`                               | GET    |
| Xem danh sách kiểu sản phẩm | `GET /api/v1/kieu-san-pham` + `/{id}`                            | GET    |
| Xem danh sách bộ sưu tập    | `GET /api/v1/bo-suu-tap` + `/{id}`                               | GET    |
| Xem danh sách thương hiệu   | `GET /api/v1/thuong-hieu` + `/{id}`                              | GET    |
| Xem hình ảnh SP             | `GET /api/v1/hinh-anh` + `/{id}` + `/chi-tiet-san-pham/{ctspId}` | GET    |
| Xem danh sách cửa hàng      | `GET /api/v1/cua-hang` + `/{id}`                                 | GET    |
| Xem danh sách nhân viên     | `GET /api/v1/nhan-vien`                                          | GET    |

> Với endpoint `GET /api/v1/nhan-vien`: nhân viên chỉ xem danh sách nhân viên cùng cửa hàng với tài khoản đang đăng nhập.

### 5.2 Quản lý đơn hàng (XEM + TẠO TẠI QUẦY + CẬP NHẬT)

| Chức năng                        | Endpoint                                           | Method |
| -------------------------------- | -------------------------------------------------- | ------ |
| Xem danh sách đơn hàng           | `GET /api/v1/don-hang`                             | GET    |
| Xem chi tiết đơn hàng            | `GET /api/v1/don-hang/{id}`                        | GET    |
| **Tạo đơn hàng tại quầy**        | `POST /api/v1/don-hang/tai-quay`                   | POST   |
| **Cập nhật trạng thái đơn hàng** | `PUT /api/v1/don-hang`                             | PUT    |
| Xem chi tiết sản phẩm trong đơn  | `GET /api/v1/chi-tiet-don-hang` + `/don-hang/{id}` | GET    |
| Xem chi tiết đơn hàng theo ID    | `GET /api/v1/chi-tiet-don-hang/{id}`               | GET    |

> **Nhân viên KHÔNG thể:** Tạo đơn hàng online, xóa đơn hàng, tạo/sửa/xóa chi tiết đơn hàng.

### 5.3 Quản lý nhập hàng (PHIẾU NHẬP — gần như toàn quyền)

| Chức năng                        | Endpoint                                                                  | Method |
| -------------------------------- | ------------------------------------------------------------------------- | ------ |
| Xem danh sách phiếu nhập         | `GET /api/v1/phieu-nhap`                                                  | GET    |
| Xem chi tiết phiếu nhập          | `GET /api/v1/phieu-nhap/{id}`                                             | GET    |
| **Tạo phiếu nhập**               | `POST /api/v1/phieu-nhap`                                                 | POST   |
| **Cập nhật phiếu nhập**          | `PUT /api/v1/phieu-nhap`                                                  | PUT    |
| **Kiểm kê phiếu nhập**           | `PUT /api/v1/phieu-nhap/kiem-ke/{id}`                                     | PUT    |
| Xem chi tiết phiếu nhập (dòng)   | `GET /api/v1/chi-tiet-phieu-nhap` + `/{id}` + `/phieu-nhap/{phieuNhapId}` | GET    |
| **Tạo chi tiết phiếu nhập**      | `POST /api/v1/chi-tiet-phieu-nhap`                                        | POST   |
| **Cập nhật chi tiết phiếu nhập** | `PUT /api/v1/chi-tiet-phieu-nhap`                                         | PUT    |

> **Nhân viên KHÔNG thể:** Xóa phiếu nhập, xóa chi tiết phiếu nhập.

### 5.4 Xem khuyến mãi (CHỈ XEM)

| Chức năng                   | Endpoint                                        | Method |
| --------------------------- | ----------------------------------------------- | ------ |
| Xem khuyến mãi theo hóa đơn | `GET /api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | GET    |
| Xem khuyến mãi theo điểm    | `GET /api/v1/khuyen-mai-theo-diem` + `/{id}`    | GET    |

### 5.5 Xem đánh giá sản phẩm (CHỈ XEM)

| Chức năng                           | Endpoint                                                             | Method |
| ----------------------------------- | -------------------------------------------------------------------- | ------ |
| Xem tất cả đánh giá                 | `GET /api/v1/danh-gia-san-pham`                                      | GET    |
| Xem đánh giá theo ID                | `GET /api/v1/danh-gia-san-pham/{id}`                                 | GET    |
| Xem đánh giá theo sản phẩm          | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`                 | GET    |
| Xem đánh giá theo chi tiết đơn hàng | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` | GET    |

### 5.6 Nhân viên KHÔNG CÓ quyền

- ❌ Tạo/Sửa/Xóa sản phẩm, danh mục (kiểu SP, BST, thương hiệu, màu sắc, kích thước)
- ❌ Tạo/Sửa/Xóa hình ảnh, cửa hàng, nhà cung cấp
- ❌ Tạo/Sửa/Xóa nhân viên
- ❌ Quản lý vai trò, quyền hạn
- ❌ Tạo/Sửa/Xóa khuyến mãi
- ❌ Tạo đơn hàng online
- ❌ Xóa đơn hàng, phiếu nhập
- ❌ Giỏ hàng (chỉ dành cho khách hàng)
- ❌ Tạo/Sửa/Xóa đánh giá sản phẩm

---

## 6. KHACH_HANG — Khách hàng

> **Phạm vi:** Xem sản phẩm, giỏ hàng, đặt hàng online, đánh giá sản phẩm, sử dụng khuyến mãi.
>
> **Tổng:** 40+ permission

### 6.1 Đăng ký & Đăng nhập

| Chức năng               | Endpoint                     | Method | Ghi chú                 |
| ----------------------- | ---------------------------- | ------ | ----------------------- |
| Đăng ký tài khoản       | `POST /api/v1/auth/register` | POST   | Public, không cần token |
| Đăng nhập               | `POST /api/v1/auth/login`    | POST   | Email + Password        |
| Xem thông tin tài khoản | `GET /api/v1/auth/account`   | GET    | Bao gồm `diemTichLuy`   |
| Gia hạn token           | `GET /api/v1/auth/refresh`   | GET    | Dùng refresh cookie     |
| Đăng xuất               | `POST /api/v1/auth/logout`   | POST   | Xóa refresh token       |

**Response đăng nhập chứa:**

```json
{
  "access_token": "eyJ...",
  "user": {
    "id": 1,
    "email": "customer@example.com",
    "tenKhachHang": "Nguyễn Văn A",
    "diemTichLuy": 150,
    "role": {
      "id": 4,
      "name": "KHACH_HANG"
    }
  }
}
```

### 6.2 Duyệt sản phẩm (CHỈ XEM)

| Chức năng                                  | Endpoint                                                                                               | Method |
| ------------------------------------------ | ------------------------------------------------------------------------------------------------------ | ------ |
| Xem danh sách sản phẩm (lọc, tìm kiếm)     | `GET /api/v1/san-pham?tenSanPham=&kieuSanPhamId=&boSuuTapId=&thuongHieuId=&trangThai=&giaMin=&giaMax=` | GET    |
| Xem chi tiết sản phẩm                      | `GET /api/v1/san-pham/{id}`                                                                            | GET    |
| Xem biến thể sản phẩm (màu, size, tồn kho) | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`                                                   | GET    |
| Xem biến thể theo ID                       | `GET /api/v1/chi-tiet-san-pham/{id}`                                                                   | GET    |
| Xem tất cả biến thể                        | `GET /api/v1/chi-tiet-san-pham`                                                                        | GET    |
| Xem hình ảnh sản phẩm                      | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{ctspId}`                                                      | GET    |
| Xem danh sách danh mục                     | `GET /api/v1/kieu-san-pham` + `/{id}`                                                                  | GET    |
| Xem danh sách bộ sưu tập                   | `GET /api/v1/bo-suu-tap` + `/{id}`                                                                     | GET    |
| Xem danh sách thương hiệu                  | `GET /api/v1/thuong-hieu` + `/{id}`                                                                    | GET    |
| Xem danh sách màu sắc                      | `GET /api/v1/mau-sac` + `/{id}`                                                                        | GET    |
| Xem danh sách kích thước                   | `GET /api/v1/kich-thuoc` + `/{id}`                                                                     | GET    |
| Xem danh sách cửa hàng                     | `GET /api/v1/cua-hang` + `/{id}`                                                                       | GET    |
| Xem ảnh sản phẩm (public)                  | `GET /storage/{fileName}`                                                                              | GET    |

### 6.3 Giỏ hàng (CHỈ KHÁCH HÀNG)

| Chức năng                 | Endpoint                                              | Method |
| ------------------------- | ----------------------------------------------------- | ------ |
| **Thêm sản phẩm vào giỏ** | `POST /api/v1/gio-hang/them-san-pham`                 | POST   |
| **Xem giỏ hàng của tôi**  | `GET /api/v1/gio-hang/cua-toi`                        | GET    |
| **Xóa sản phẩm khỏi giỏ** | `DELETE /api/v1/gio-hang/chi-tiet/{maChiTietGioHang}` | DELETE |
| **Xem khuyến mãi hợp lệ** | `GET /api/v1/gio-hang/khuyen-mai-hop-le`              | GET    |
| **Xem trước giảm giá**    | `POST /api/v1/gio-hang/ap-dung-khuyen-mai`            | POST   |

**Request thêm SP vào giỏ:**

```json
{
  "maChiTietSanPham": 5,
  "soLuong": 2
}
```

**Logic đặc biệt:** Nếu sản phẩm đã tồn tại trong giỏ → tự động cộng thêm số lượng.

### 6.4 Đặt hàng online

| Chức năng                                         | Endpoint                                             | Method |
| ------------------------------------------------- | ---------------------------------------------------- | ------ |
| **Tạo đơn hàng online**                           | `POST /api/v1/don-hang/online`                       | POST   |
| Xem danh sách đơn hàng của tôi                    | `GET /api/v1/don-hang`                               | GET    |
| Xem chi tiết đơn hàng                             | `GET /api/v1/don-hang/{id}`                          | GET    |
| **Cập nhật đơn hàng** (hủy đơn, cập nhật địa chỉ) | `PUT /api/v1/don-hang`                               | PUT    |
| Xem sản phẩm trong đơn hàng                       | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}` | GET    |
| Xem chi tiết dòng sản phẩm                        | `GET /api/v1/chi-tiet-don-hang/{id}`                 | GET    |
| Xem tất cả chi tiết đơn hàng                      | `GET /api/v1/chi-tiet-don-hang`                      | GET    |

**Request tạo đơn hàng online:**

```json
{
  "diaChi": "123 Nguyễn Trãi, Q.1, TP.HCM",
  "sdt": "0901234567",
  "cuaHangId": 1,
  "maKhuyenMaiHoaDon": 1,
  "maKhuyenMaiDiem": 2
}
```

**Luồng đặt hàng online:**

1. KH thêm SP vào giỏ hàng → `POST /api/v1/gio-hang/them-san-pham`
2. KH xem giỏ hàng → `GET /api/v1/gio-hang/cua-toi`
3. KH xem khuyến mãi hợp lệ → `GET /api/v1/gio-hang/khuyen-mai-hop-le`
4. KH áp dụng khuyến mãi (xem trước giảm giá) → `POST /api/v1/gio-hang/ap-dung-khuyen-mai`
5. KH đặt hàng → `POST /api/v1/don-hang/online`
6. Hệ thống tự động: tạo đơn + chi tiết đơn → giảm tồn kho → xóa giỏ hàng
7. KH theo dõi đơn → `GET /api/v1/don-hang/{id}`
8. Khi đơn hoàn thành (trangThai=5) → hệ thống tự cộng điểm tích lũy

**Quy tắc tích điểm:** Mỗi 100.000 VND → 10 điểm. Ví dụ: đơn 350.000đ → 30 điểm.

### 6.5 Xem khuyến mãi (CHỈ XEM)

| Chức năng                   | Endpoint                                        | Method |
| --------------------------- | ----------------------------------------------- | ------ |
| Xem khuyến mãi theo hóa đơn | `GET /api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | GET    |
| Xem khuyến mãi theo điểm    | `GET /api/v1/khuyen-mai-theo-diem` + `/{id}`    | GET    |

### 6.6 Đánh giá sản phẩm

| Chức năng                              | Endpoint                                                             | Method |
| -------------------------------------- | -------------------------------------------------------------------- | ------ |
| Xem tất cả đánh giá                    | `GET /api/v1/danh-gia-san-pham`                                      | GET    |
| Xem đánh giá theo ID                   | `GET /api/v1/danh-gia-san-pham/{id}`                                 | GET    |
| Xem đánh giá theo sản phẩm             | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`                 | GET    |
| Xem đánh giá theo chi tiết đơn hàng    | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` | GET    |
| **Xem đánh giá của tôi**               | `GET /api/v1/danh-gia-san-pham/cua-toi`                              | GET    |
| **Tạo đánh giá** (multipart/form-data) | `POST /api/v1/danh-gia-san-pham`                                     | POST   |
| **Cập nhật đánh giá** (của mình)       | `PUT /api/v1/danh-gia-san-pham/{id}`                                 | PUT    |
| **Xóa đánh giá** (của mình)            | `DELETE /api/v1/danh-gia-san-pham/{id}`                              | DELETE |

**Điều kiện tạo đánh giá:**

- ✅ Đã đăng nhập bằng tài khoản khách hàng
- ✅ Chi tiết đơn hàng thuộc đơn hàng **của mình**
- ✅ Đơn hàng ở trạng thái **5 (Đã nhận hàng)**
- ✅ Chưa đánh giá dòng sản phẩm này (mỗi dòng chỉ đánh giá 1 lần)

**Request tạo đánh giá (multipart/form-data):**

```
chiTietDonHangId = 7
soSao            = 5      (1-5 sao)
ghiTru           = "Sản phẩm rất đẹp!"
file             = (ảnh đánh giá, tùy chọn)
```

### 6.7 Khách hàng KHÔNG CÓ quyền

- ❌ Tạo/Sửa/Xóa sản phẩm, danh mục, hình ảnh
- ❌ Quản lý cửa hàng, nhà cung cấp
- ❌ Quản lý vai trò, quyền hạn
- ❌ Quản lý phiếu nhập
- ❌ Tạo/Sửa/Xóa khuyến mãi
- ❌ Tạo đơn hàng tại quầy
- ❌ Xóa đơn hàng
- ❌ Tạo/Sửa/Xóa chi tiết đơn hàng
- ❌ Xem nhà cung cấp
- ❌ Đánh giá sản phẩm của người khác

---

## 7. Endpoint công khai (không cần đăng nhập)

| Endpoint                     | Method | Mô tả                                     |
| ---------------------------- | ------ | ----------------------------------------- |
| `POST /api/v1/auth/login`    | POST   | Đăng nhập                                 |
| `POST /api/v1/auth/register` | POST   | Đăng ký tài khoản khách hàng              |
| `GET /api/v1/auth/refresh`   | GET    | Gia hạn access token (cần refresh cookie) |
| `GET /storage/{fileName}`    | GET    | Xem ảnh sản phẩm (public, cache 1 ngày)   |
| `GET /v3/api-docs/**`        | GET    | Swagger API documentation                 |
| `GET /swagger-ui/**`         | GET    | Swagger UI                                |

---

## 8. Bảng ma trận phân quyền chi tiết

### 8.1 Sản phẩm & Danh mục

| Module      | Endpoint                     | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ----------- | ---------------------------- | ------ | ----- | --------- | ---------- |
| Sản phẩm    | `/api/v1/san-pham`           | GET    | ✅    | ✅        | ✅         |
| Sản phẩm    | `/api/v1/san-pham/{id}`      | GET    | ✅    | ✅        | ✅         |
| Sản phẩm    | `/api/v1/san-pham`           | POST   | ✅    | ❌        | ❌         |
| Sản phẩm    | `/api/v1/san-pham`           | PUT    | ✅    | ❌        | ❌         |
| Sản phẩm    | `/api/v1/san-pham/{id}`      | DELETE | ✅    | ❌        | ❌         |
| Kiểu SP     | `/api/v1/kieu-san-pham`      | GET    | ✅    | ✅        | ✅         |
| Kiểu SP     | `/api/v1/kieu-san-pham`      | POST   | ✅    | ❌        | ❌         |
| Kiểu SP     | `/api/v1/kieu-san-pham`      | PUT    | ✅    | ❌        | ❌         |
| Kiểu SP     | `/api/v1/kieu-san-pham/{id}` | DELETE | ✅    | ❌        | ❌         |
| BST         | `/api/v1/bo-suu-tap`         | GET    | ✅    | ✅        | ✅         |
| BST         | `/api/v1/bo-suu-tap`         | POST   | ✅    | ❌        | ❌         |
| BST         | `/api/v1/bo-suu-tap`         | PUT    | ✅    | ❌        | ❌         |
| BST         | `/api/v1/bo-suu-tap/{id}`    | DELETE | ✅    | ❌        | ❌         |
| Thương hiệu | `/api/v1/thuong-hieu`        | GET    | ✅    | ✅        | ✅         |
| Thương hiệu | `/api/v1/thuong-hieu`        | CRUD   | ✅    | ❌        | ❌         |
| Màu sắc     | `/api/v1/mau-sac`            | GET    | ✅    | ✅        | ✅         |
| Màu sắc     | `/api/v1/mau-sac`            | CRUD   | ✅    | ❌        | ❌         |
| Kích thước  | `/api/v1/kich-thuoc`         | GET    | ✅    | ✅        | ✅         |
| Kích thước  | `/api/v1/kich-thuoc`         | CRUD   | ✅    | ❌        | ❌         |

### 8.2 Chi tiết SP & Hình ảnh

| Module   | Endpoint                                         | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| -------- | ------------------------------------------------ | ------ | ----- | --------- | ---------- |
| CTSP     | `/api/v1/chi-tiet-san-pham`                      | GET    | ✅    | ✅        | ✅         |
| CTSP     | `/api/v1/chi-tiet-san-pham/{id}`                 | GET    | ✅    | ✅        | ✅         |
| CTSP     | `/api/v1/chi-tiet-san-pham/san-pham/{spId}`      | GET    | ✅    | ✅        | ✅         |
| CTSP     | `/api/v1/chi-tiet-san-pham`                      | POST   | ✅    | ❌        | ❌         |
| CTSP     | `/api/v1/chi-tiet-san-pham`                      | PUT    | ✅    | ❌        | ❌         |
| CTSP     | `/api/v1/chi-tiet-san-pham/{id}`                 | DELETE | ✅    | ❌        | ❌         |
| Hình ảnh | `/api/v1/hinh-anh`, `/{id}`, `/chi-tiet-sp/{id}` | GET    | ✅    | ✅        | ✅         |
| Hình ảnh | `/api/v1/hinh-anh` + upload                      | POST   | ✅    | ❌        | ❌         |
| Hình ảnh | `/api/v1/hinh-anh`                               | PUT    | ✅    | ❌        | ❌         |
| Hình ảnh | `/api/v1/hinh-anh/{id}`                          | DELETE | ✅    | ❌        | ❌         |

### 8.3 Giỏ hàng

| Endpoint                                   | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ------------------------------------------ | ------ | ----- | --------- | ---------- |
| `POST /api/v1/gio-hang/them-san-pham`      | POST   | ✅    | ❌        | ✅         |
| `GET /api/v1/gio-hang/cua-toi`             | GET    | ✅    | ❌        | ✅         |
| `DELETE /api/v1/gio-hang/chi-tiet/{id}`    | DELETE | ✅    | ❌        | ✅         |
| `GET /api/v1/gio-hang/khuyen-mai-hop-le`   | GET    | ✅    | ❌        | ✅         |
| `POST /api/v1/gio-hang/ap-dung-khuyen-mai` | POST   | ✅    | ❌        | ✅         |

### 8.4 Đơn hàng

| Endpoint                                      | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| --------------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/don-hang`                        | GET    | ✅    | ✅        | ✅         |
| `GET /api/v1/don-hang/{id}`                   | GET    | ✅    | ✅        | ✅         |
| `POST /api/v1/don-hang/online`                | POST   | ✅    | ❌        | ✅         |
| `POST /api/v1/don-hang/tai-quay`              | POST   | ✅    | ✅        | ❌         |
| `PUT /api/v1/don-hang`                        | PUT    | ✅    | ✅        | ✅         |
| `DELETE /api/v1/don-hang/{id}`                | DELETE | ✅    | ❌        | ❌         |
| `GET /api/v1/chi-tiet-don-hang`               | GET    | ✅    | ✅        | ✅         |
| `GET /api/v1/chi-tiet-don-hang/don-hang/{id}` | GET    | ✅    | ✅        | ✅         |
| `GET /api/v1/chi-tiet-don-hang/{id}`          | GET    | ✅    | ✅        | ✅         |
| `POST /api/v1/chi-tiet-don-hang`              | POST   | ✅    | ❌        | ❌         |
| `PUT /api/v1/chi-tiet-don-hang`               | PUT    | ✅    | ❌        | ❌         |
| `DELETE /api/v1/chi-tiet-don-hang/{id}`       | DELETE | ✅    | ❌        | ❌         |

### 8.5 Phiếu nhập

| Endpoint                                                         | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ---------------------------------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/phieu-nhap`                                         | GET    | ✅    | ✅        | ❌         |
| `GET /api/v1/phieu-nhap/{id}`                                    | GET    | ✅    | ✅        | ❌         |
| `POST /api/v1/phieu-nhap`                                        | POST   | ✅    | ✅        | ❌         |
| `PUT /api/v1/phieu-nhap`                                         | PUT    | ✅    | ✅        | ❌         |
| `PUT /api/v1/phieu-nhap/kiem-ke/{id}`                            | PUT    | ✅    | ✅        | ❌         |
| `DELETE /api/v1/phieu-nhap/{id}`                                 | DELETE | ✅    | ❌        | ❌         |
| `GET /api/v1/chi-tiet-phieu-nhap` + `/{id}` + `/phieu-nhap/{id}` | GET    | ✅    | ✅        | ❌         |
| `POST /api/v1/chi-tiet-phieu-nhap`                               | POST   | ✅    | ✅        | ❌         |
| `PUT /api/v1/chi-tiet-phieu-nhap`                                | PUT    | ✅    | ✅        | ❌         |
| `DELETE /api/v1/chi-tiet-phieu-nhap/{id}`                        | DELETE | ✅    | ❌        | ❌         |

### 8.6 Cửa hàng & Nhà cung cấp

| Endpoint                               | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| -------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/cua-hang` + `/{id}`       | GET    | ✅    | ✅        | ✅         |
| `POST/PUT/DELETE /api/v1/cua-hang`     | CRUD   | ✅    | ❌        | ❌         |
| `GET /api/v1/nha-cung-cap` + `/{id}`   | GET    | ✅    | ❌        | ❌         |
| `POST/PUT/DELETE /api/v1/nha-cung-cap` | CRUD   | ✅    | ❌        | ❌         |

### 8.7 Khuyến mãi

| Endpoint                                        | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ----------------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | GET    | ✅    | ✅        | ✅         |
| `POST/PUT/DELETE khuyen-mai-theo-hoa-don`       | CRUD   | ✅    | ❌        | ❌         |
| `GET /api/v1/khuyen-mai-theo-diem` + `/{id}`    | GET    | ✅    | ✅        | ✅         |
| `POST/PUT/DELETE khuyen-mai-theo-diem`          | CRUD   | ✅    | ❌        | ❌         |

### 8.8 Vai trò & Quyền hạn

| Endpoint                                  | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ----------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET/POST/PUT/DELETE /api/v1/roles`       | CRUD   | ✅    | ❌        | ❌         |
| `GET/POST/PUT/DELETE /api/v1/permissions` | CRUD   | ✅    | ❌        | ❌         |

### 8.9 Đánh giá sản phẩm

| Endpoint                                               | Method | ADMIN       | NHAN_VIEN | KHACH_HANG    |
| ------------------------------------------------------ | ------ | ----------- | --------- | ------------- |
| `GET /api/v1/danh-gia-san-pham`                        | GET    | ✅          | ✅        | ✅            |
| `GET /api/v1/danh-gia-san-pham/{id}`                   | GET    | ✅          | ✅        | ✅            |
| `GET /api/v1/danh-gia-san-pham/san-pham/{spId}`        | GET    | ✅          | ✅        | ✅            |
| `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{id}` | GET    | ✅          | ✅        | ✅            |
| `GET /api/v1/danh-gia-san-pham/cua-toi`                | GET    | ❌          | ❌        | ✅            |
| `POST /api/v1/danh-gia-san-pham`                       | POST   | ❌          | ❌        | ✅            |
| `PUT /api/v1/danh-gia-san-pham/{id}`                   | PUT    | ❌          | ❌        | ✅ (của mình) |
| `DELETE /api/v1/danh-gia-san-pham/{id}`                | DELETE | ✅ (tất cả) | ❌        | ✅ (của mình) |

---

## 9. Gợi ý xây dựng Frontend theo vai trò

### 9.1 Trang ADMIN (Dashboard quản trị)

**Sidebar Menu gợi ý:**

```
📊 Dashboard
📦 Quản lý sản phẩm
   ├── Sản phẩm
   ├── Chi tiết sản phẩm (biến thể)
   ├── Hình ảnh sản phẩm
   ├── Kiểu sản phẩm
   ├── Bộ sưu tập
   ├── Thương hiệu
   ├── Màu sắc
   └── Kích thước
🛒 Quản lý đơn hàng
   ├── Danh sách đơn hàng
   ├── Tạo đơn tại quầy
   └── Chi tiết đơn hàng
📥 Quản lý nhập hàng
   ├── Phiếu nhập
   ├── Chi tiết phiếu nhập
   └── Kiểm kê
🏪 Hệ thống
   ├── Cửa hàng
   ├── Nhà cung cấp
   └── Nhân viên (*)
🎁 Khuyến mãi
   ├── KM theo hóa đơn
   └── KM theo điểm
⭐ Đánh giá sản phẩm
🔐 Phân quyền
   ├── Vai trò
   └── Quyền hạn
👤 Tài khoản
```

### 9.2 Trang NHAN_VIEN (Nhân viên bán hàng)

**Sidebar Menu gợi ý:**

```
📊 Dashboard
📦 Sản phẩm (chỉ xem)
   ├── Danh sách sản phẩm
   └── Chi tiết sản phẩm
🛒 Đơn hàng
   ├── Danh sách đơn hàng
   ├── Tạo đơn tại quầy
   └── Cập nhật trạng thái
📥 Nhập hàng
   ├── Phiếu nhập (tạo/sửa)
   ├── Chi tiết phiếu nhập (tạo/sửa)
   └── Kiểm kê
🎁 Khuyến mãi (chỉ xem)
⭐ Đánh giá sản phẩm (chỉ xem)
👤 Tài khoản
```

### 9.3 Trang KHACH_HANG (Giao diện mua sắm)

**Navigation gợi ý:**

```
🏠 Trang chủ
🔍 Tìm kiếm / Lọc sản phẩm
   ├── Theo danh mục (kiểu SP)
   ├── Theo thương hiệu
   ├── Theo bộ sưu tập
   ├── Theo khoảng giá
   └── Theo màu sắc / kích thước
📄 Chi tiết sản phẩm
   ├── Chọn màu sắc / kích thước
   ├── Xem đánh giá
   └── Thêm vào giỏ
🛒 Giỏ hàng
   ├── Xem giỏ hàng
   ├── Cập nhật số lượng
   └── Xóa sản phẩm
💳 Đặt hàng (checkout)
   ├── Chọn khuyến mãi hóa đơn
   ├── Đổi điểm lấy KM
   └── Xác nhận đơn hàng
📋 Đơn hàng của tôi
   ├── Theo dõi trạng thái
   ├── Hủy đơn (khi chờ xác nhận)
   └── Đánh giá sản phẩm (khi đã nhận)
⭐ Đánh giá của tôi
   ├── Xem danh sách đánh giá
   ├── Sửa đánh giá
   └── Xóa đánh giá
👤 Tài khoản
   ├── Thông tin cá nhân
   ├── Điểm tích lũy
   └── Đăng xuất
```

### 9.4 Cách xử lý phân quyền động trên FE

Khi đăng nhập thành công, FE nhận được thông tin user bao gồm `role`. Để render menu/chức năng:

**Cách 1: Hardcode theo tên role**

```javascript
const role = user.role.name; // "ADMIN" | "NHAN_VIEN" | "KHACH_HANG"

if (role === "ADMIN") {
  // Hiện tất cả menu
} else if (role === "NHAN_VIEN") {
  // Hiện menu nhân viên
} else if (role === "KHACH_HANG") {
  // Hiện menu khách hàng
}
```

**Cách 2: Dựa trên danh sách permissions (linh hoạt hơn)**

```javascript
// Lấy thông tin tài khoản + permissions
const account = await fetch("/api/v1/auth/account");
const permissions = account.role.permissions;

// Kiểm tra quyền trước khi hiện nút/menu
function hasPermission(apiPath, method) {
  return permissions.some((p) => p.apiPath === apiPath && p.method === method);
}

// Ví dụ: chỉ hiện nút "Tạo sản phẩm" nếu có quyền
if (hasPermission("/api/v1/san-pham", "POST")) {
  showButton("Tạo sản phẩm");
}
```

### 9.5 Xử lý lỗi phân quyền trên FE

| HTTP Status | Ý nghĩa                        | Hành động FE                                     |
| ----------- | ------------------------------ | ------------------------------------------------ |
| `401`       | Chưa đăng nhập / token hết hạn | Redirect → trang đăng nhập hoặc tự refresh token |
| `403`       | Không có quyền                 | Hiển thị thông báo "Bạn không có quyền truy cập" |
| `400`       | Lỗi nghiệp vụ                  | Hiển thị message lỗi từ response                 |
| `500`       | Lỗi server                     | Hiển thị thông báo lỗi chung                     |

**Cấu trúc response lỗi:**

```json
{
  "statusCode": 400,
  "error": "Bad Request",
  "message": "Không tìm thấy sản phẩm"
}
```

---

> **Tài liệu chi tiết từng module:** Xem các file `01_Auth.md` → `22_DanhGiaSanPham.md` trong thư mục `document/`.
