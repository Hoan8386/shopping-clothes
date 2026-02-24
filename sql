-- =============================================================
-- DATABASE: QUẢN LÝ SHOP QUẦN ÁO ĐA CHI NHÁNH
-- CHỈ INSERT DỮ LIỆU MẪU
-- Hibernate (ddl-auto=update) sẽ tự tạo bảng từ domain entities
-- =============================================================

-- Bước 1: Tạo database (nếu chưa có) rồi chạy app để Hibernate tạo bảng
-- Bước 2: Chạy file này để INSERT dữ liệu mẫu

CREATE DATABASE IF NOT EXISTS shopping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopping;

-- ---------------------------------------------------------
-- INSERT DỮ LIỆU MẪU
-- ---------------------------------------------------------

-- Roles & Permissions
-- (Role: cột không có @Column → Hibernate dùng tên field camelCase)
INSERT INTO roles (name, description, active, createdAt) VALUES
    ('ADMIN', 'Quản trị viên toàn quyền', TRUE, NOW()),
    ('QUAN_LY', 'Quản lý cửa hàng', TRUE, NOW()),
    ('NHAN_VIEN', 'Nhân viên bán hàng', TRUE, NOW()),
    ('KHACH_HANG', 'Khách hàng mua sắm', TRUE, NOW());

-- (Permission: cột không có @Column → Hibernate dùng tên field camelCase)
INSERT INTO permissions (name, apiPath, method, module, createdAt) VALUES
    ('Tạo sản phẩm',    '/api/v1/san-pham',    'POST',   'SAN_PHAM', NOW()),
    ('Sửa sản phẩm',    '/api/v1/san-pham',    'PUT',    'SAN_PHAM', NOW()),
    ('Xóa sản phẩm',    '/api/v1/san-pham/{id}','DELETE', 'SAN_PHAM', NOW()),
    ('Xem sản phẩm',    '/api/v1/san-pham',    'GET',    'SAN_PHAM', NOW()),
    ('Thêm giỏ hàng',   '/api/v1/gio-hang',    'POST',   'GIO_HANG', NOW()),
    ('Xem giỏ hàng',    '/api/v1/gio-hang',    'GET',    'GIO_HANG', NOW()),
    ('Xóa giỏ hàng',    '/api/v1/gio-hang/{id}','DELETE', 'GIO_HANG', NOW());

-- ADMIN: tất cả permissions
INSERT INTO permission_role (role_id, permission_id) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7);
-- QUAN_LY: CRUD sản phẩm + xem
INSERT INTO permission_role (role_id, permission_id) VALUES (2,1),(2,2),(2,3),(2,4);
-- NHAN_VIEN: xem sản phẩm
INSERT INTO permission_role (role_id, permission_id) VALUES (3,4);
-- KHACH_HANG: xem sản phẩm + giỏ hàng
INSERT INTO permission_role (role_id, permission_id) VALUES (4,4),(4,5),(4,6),(4,7);

-- Danh mục lẻ
INSERT INTO CuaHang (MaCuaHang, TenCuaHang, DiaChi, Email, Sdt, TrangThai) VALUES
    ('CH01','Chi nhánh Q1','1 Lợi Lê','q1@shop.com','0281','Mở'),
    ('CH02','Chi nhánh Q7','2 Linh Nguyễn','q7@shop.com','0282','Mở'),
    ('CH03','Chi nhánh HN','3 Phố Cổ','hn@shop.com','0241','Mở'),
    ('CH04','Kho Tổng','Bình Dương','kho@shop.com','0271','Mở'),
    ('CH05','Chi nhánh ĐN','Đà Nẵng','dn@shop.com','0231','Đóng');

INSERT INTO KieuSanPham (MaKieuSanPham, TenKieuSanPham, NgayTao) VALUES
    ('KSP1','Áo Nam','2026-01-01'),('KSP2','Quần Nam','2026-01-01'),('KSP3','Váy Nữ','2026-01-01'),('KSP4','Phụ Kiện','2026-01-01'),('KSP5','Đồ Đông','2026-01-01');

INSERT INTO NhaCungCap (MaNhaCungCap, TenNhaCungCap, SoDienThoai, DiaChi) VALUES
    ('NCC1','Dệt May 1','0901','HCM'),('NCC2','Vải Thái','0902','Long An'),('NCC3','Jean Gia Định','0903','HCM'),('NCC4','Xưởng HN','0904','Hà Nội'),('NCC5','Chợ Ninh Hiệp','0905','Hà Nội');

INSERT INTO BoSuuTap (MaSuuTap, TenSuuTap, MoTa) VALUES
    ('BST1','Hè 26','Mát mẻ'),('BST2','Thu 26','Sang trọng'),('BST3','Basic','Cơ bản'),('BST4','Luxury','Cao cấp'),('BST5','Sale Off','Giảm giá');

INSERT INTO ThuongHieu (MaThuongHieu, TenThuongHieu) VALUES
    ('TH1','ABC'),('TH2','XYZ'),('TH3','Kids'),('TH4','Sport'),('TH5','Vintage');

INSERT INTO KichThuoc (MaKichThuc, TenKichThuoc) VALUES
    ('S','Nhỏ'),('M','Vừa'),('L','Lớn'),('XL','Rất lớn'),('XXL','Đặc biệt');

INSERT INTO MauSac (MaMauSac, TenMauSac) VALUES
    ('TR','Trắng'),('DE','Đen'),('DO','Đỏ'),('XA','Xanh'),('VA','Vàng');

INSERT INTO HinhAnh (MaHinhAnh, DuongDan, NgayTao) VALUES
    ('IMG1','/img1.png',NOW()),('IMG2','/img2.png',NOW()),('IMG3','/img3.png',NOW()),('IMG4','/img4.png',NOW()),('IMG5','/img5.png',NOW());

INSERT INTO LoaiKiemKe (MaLoaiKiemKe, TenLoai) VALUES
    ('LKK1','Định kỳ'),('LKK2','Đột xuất'),('LKK3','Cuối năm'),('LKK4','Đóng cửa'),('LKK5','Bàn giao');

-- Nhân sự (password = BCrypt hash của '123456')
-- ChucVu phải khớp với enum ChucVu: NHAN_VIEN, QUAN_LY, CHU_CUA_HANG, ADMIN
INSERT INTO NhanVien (MaNhanVien, TenNhanVien, Email, Password, Sdt, MaCuaHang, ChucVu, role_id) VALUES
    ('NV1','An','an@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','01','CH01','QUAN_LY',2),
    ('NV2','Bình','b@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','02','CH01','NHAN_VIEN',3),
    ('NV3','Chi','c@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','03','CH02','NHAN_VIEN',3),
    ('NV4','Danh','d@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','04','CH03','QUAN_LY',2),
    ('NV5','Hùng','h@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','05','CH04','ADMIN',1);

INSERT INTO LichLamViec (MaLichLamViec, MaNhanVien, Ngay, Ca, MaCuaHang) VALUES
    ('L1','NV2','2026-02-14','Sáng','CH01'),('L2','NV3','2026-02-14','Chiều','CH02'),('L3','NV2','2026-02-15','Sáng','CH01'),('L4','NV1','2026-02-14','Hành chính','CH01'),('L5','NV5','2026-02-14','Sáng','CH04');

INSERT INTO DangKyLichLam (MaDangKy, MaNhanVien, NgayDangKy, CaDangKy) VALUES
    ('DK1','NV2','2026-02-20','Sáng'),('DK2','NV3','2026-02-20','Tối'),('DK3','NV1','2026-02-21','Hành chính'),('DK4','NV5','2026-02-21','Sáng'),('DK5','NV2','2026-02-21','Chiều');

INSERT INTO DoiCa (MaDoiCa, MaLichLamViec, MaNhanVienThayThe, GhiTru) VALUES
    ('DC1','L1','NV3','Ốm'),('DC2','L2','NV2','Việc riêng'),('DC3','L3','NV1','Bận'),('DC4','L4','NV2','Đổi ngang'),('DC5','L5','NV1','Hỗ trợ');

INSERT INTO HuyCa (MaHuyCa, MaLichLamViec, LyDo, NgayHuy) VALUES
    ('HC1','L1','Lễ','2026-02-13'),('HC2','L2','Mưa','2026-02-13'),('HC3','L3','Sửa điện','2026-02-13'),('HC4','L4','Cúp điện','2026-02-13'),('HC5','L5','Đóng kho','2026-02-13');

INSERT INTO LoiPhatSinh (MaLoi, MaLichLamViec, MoTaLoi) VALUES
    ('LOI1','L1','Trễ giờ'),('LOI2','L2','Thiếu hụt'),('LOI3','L3','Mất đồ'),('LOI4','L4','Sai hóa đơn'),('LOI5','L5','Vỡ hàng');

-- Sản phẩm & Kho
INSERT INTO SanPham (MaSanPham, TenSanPham, GiaVon, GiaBan, MaKieuSanPham, MaNhaCungCap, MaSuuTap, MaThuongHieu, TrangThai) VALUES
    ('SP1','Áo Oxford',100,200,'KSP1','NCC1','BST3','TH1','Kinh doanh'),
    ('SP2','Quần Jean',200,400,'KSP2','NCC3','BST3','TH2','Kinh doanh'),
    ('SP3','Váy Hoa',150,300,'KSP3','NCC2','BST1','TH1','Kinh doanh'),
    ('SP4','Nịt Da',50,100,'KSP4','NCC5','BST3','TH5','Kinh doanh'),
    ('SP5','Áo Phao',300,600,'KSP5','NCC4','BST2','TH2','Ngừng');

INSERT INTO ChiTietSanPham (MaChiTietSanPham, MaSanPham, MaKichThuoc, MaMauSac, SKU) VALUES
    ('CT1','SP1','M','TR','SKU-1'),('CT2','SP1','L','TR','SKU-2'),('CT3','SP2','M','DE','SKU-3'),('CT4','SP3','S','DO','SKU-4'),('CT5','SP4','L','DE','SKU-5');

INSERT INTO ChiTietHinhAnh (MaChiTietHinhAnh, MaHinhAnh, MaChiTietSanPham) VALUES
    ('CTHA1','IMG1','CT1'),('CTHA2','IMG2','CT2'),('CTHA3','IMG3','CT3'),('CTHA4','IMG4','CT4'),('CTHA5','IMG5','CT5');

INSERT INTO TonKhoCuaHang (MaCuaHang, MaChiTietSanPham, SoLuongTon) VALUES
    ('CH01','CT1',10),('CH01','CT2',20),('CH02','CT3',5),('CH03','CT4',15),('CH04','CT1',100);

INSERT INTO KiemKeHangHoa (MaKiemKe, MaCuaHang, MaNhanVien, NgayBatDau, TrangThai, MaLoaiKiemKe) VALUES
    ('KK1','CH01','NV1','2026-02-01','Xong','LKK1'),('KK2','CH02','NV3','2026-02-01','Xong','LKK2'),('KK3','CH03','NV4','2026-02-01','Xong','LKK3'),('KK4','CH01','NV2','2026-02-10','Xong','LKK1'),('KK5','CH04','NV5','2026-02-10','Xong','LKK3');

INSERT INTO ChiTietKiemKe (MaKiemKe, MaChiTietSanPham, SoLuongHeThong, SoLuongThucTe, GhiTru) VALUES
    ('KK1','CT1',10,10,'Ok'),('KK1','CT2',20,19,'Mất 1'),('KK2','CT3',5,5,'Đủ'),('KK3','CT4',15,15,'Ok'),('KK5','CT1',100,100,'Ok');

INSERT INTO DonLuanChuyen (MaDonLuanChuyen, MaCuaHangDi, MaCuaHangDen, MaNhanVienTao, MaNhanVienNhan, NgayTao, TrangThai) VALUES
    ('LC1','CH04','CH01','NV5','NV1',NOW(),'Xong'),('LC2','CH04','CH02','NV5','NV3',NOW(),'Gửi'),('LC3','CH04','CH03','NV5','NV4',NOW(),'Xong'),('LC4','CH01','CH02','NV1','NV3',NOW(),'Xong'),('LC5','CH04','CH01','NV5',NULL,NOW(),'Hủy');

INSERT INTO ChiTietDonLuanChuyen (MaDonLuanChuyen, MaChiTietSanPham, SoLuong) VALUES
    ('LC1','CT1',20),('LC2','CT3',10),('LC3','CT4',5),('LC4','CT1',5),('LC1','CT2',10);

INSERT INTO PhieuNhap (MaPhieuNhap, MaNhaCungCap, MaCuaHangNhan, NgayNhap, MaNhanVien) VALUES
    ('PN1','NCC1','CH04','2026-02-01','NV5'),('PN2','NCC3','CH04','2026-02-01','NV5'),('PN3','NCC2','CH04','2026-02-01','NV5'),('PN4','NCC4','CH03','2026-02-01','NV4'),('PN5','NCC5','CH01','2026-02-01','NV1');

INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaChiTietSanPham, SoLuong, DonGiaNhap) VALUES
    ('PN1','CT1',100,100),('PN1','CT2',50,110),('PN2','CT3',30,200),('PN3','CT4',20,150),('PN4','CT1',10,100);

-- Bán hàng
INSERT INTO KhachHang (MaKhachHang, TenKhachHang, Sdt, Email, Password, DiemTichLuy, role_id) VALUES
    ('KH1','Lan','0911','lan@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',10,4),
    ('KH2','Minh','0922','m@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',0,4),
    ('KH3','Hoa','0933','h@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',100,4),
    ('KH4','Tuấn','0944','t@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',5,4),
    ('KH5','Yến','0955','y@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',50,4);

-- Giỏ hàng mẫu
INSERT INTO GioHang (MaKhachHang, NgayTao) VALUES ('KH1', NOW()), ('KH3', NOW());
INSERT INTO ChiTietGioHang (MaGioHang, MaChiTietSanPham, SoLuong) VALUES (1,'CT1',2),(1,'CT3',1),(2,'CT4',3);

INSERT INTO DonHangTrucTiep (MaDonHang, MaCuaHang, MaNhanVien, MaKhachHang, NgayTao, TongTien) VALUES
    ('DH1','CH01','NV2','KH1',NOW(),200),('DH2','CH01','NV2','KH2',NOW(),400),('DH3','CH02','NV3','KH3',NOW(),100),('DH4','CH03','NV4','KH4',NOW(),300),('DH5','CH01','NV1','KH5',NOW(),600);

INSERT INTO ChiTietDonHangTrucTiep (MaDonHang, MaChiTietSanPham, SoLuong, GiaBan) VALUES
    ('DH1','CT1',1,200),('DH2','CT3',1,400),('DH3','CT5',1,100),('DH4','CT4',1,300),('DH5','CT1',3,200);

INSERT INTO DonHangOnline (MaDonHangOnline, MaKhachHang, NgayDat, DiaChiGiao, TrangThai, MaCuaHangXuLy, MaNhanVienXuLy) VALUES
    ('ON1','KH1',NOW(),'Địa chỉ 1','Xong','CH01','NV2'),('ON2','KH3',NOW(),'Địa chỉ 2','Giao','CH02','NV3'),('ON3','KH5',NOW(),'Địa chỉ 3','Chờ','CH03',NULL),('ON4','KH2',NOW(),'Địa chỉ 4','Hủy','CH01','NV1'),('ON5','KH1',NOW(),'Địa chỉ 5','Xong','CH02','NV3');

INSERT INTO ChiTietDonHangOnline (MaDonHangOnline, MaChiTietSanPham, SoLuong) VALUES
    ('ON1','CT1',1),('ON2','CT3',2),('ON3','CT4',1),('ON4','CT1',1),('ON5','CT2',1);

-- Khuyến mãi
INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, NgayBatDau, NgayKetThuc, TrangThai) VALUES
    ('KM1','Tết 26','2026-01-01','2026-02-28',1),('KM2','VIP','2026-01-01','2026-12-31',1),('KM3','Xả kho','2026-02-01','2026-02-15',1),('KM4','Khai trương','2026-03-01','2026-03-07',0),('KM5','Cuối tuần','2026-01-01','2026-12-31',1);

INSERT INTO KhuyenMaiTheoDiem (MaKM, DiemYeuCau, TiLeGiam) VALUES ('KM2',100,0.1);
INSERT INTO KhuyenMaiTheoHoaDon (MaKM, GiaTriToiThieu, SoTienGiam) VALUES ('KM1',1000,50);

INSERT INTO KhuyenMaiTheoSanPham (MaKM, MaSanPham, PhanTramGiam) VALUES
    ('KM3','SP1',0.2),('KM3','SP2',0.15),('KM5','SP3',0.05),('KM3','SP4',0.5),('KM1','SP5',0.3);

-- Lịch sử tồn kho
INSERT INTO LichSuTonKho (MaLichSu, MaCuaHang, MaChiTietSanPham, SoLuongTruoc, SoLuongSau, SoLuongThayDoi, LyDo, MaNguon, MaNhanVien, NgayTao) VALUES
    ('LS1','CH01','CT2',20,19,-1,'Kiểm kê phát hiện mất 1','KK1','NV1',NOW()),
    ('LS2','CH04','CT1',100,100,0,'Kiểm kê đúng','KK5','NV5',NOW()),
    ('LS3','CH01','CT1',10,30,20,'Nhận hàng luân chuyển','LC1','NV1',NOW()),
    ('LS4','CH04','CT1',100,100,0,'Nhập kho từ NCC','PN1','NV5',NOW()),
    ('LS5','CH01','CT1',10,10,0,'Bán hàng trực tiếp','DH1','NV2',NOW());