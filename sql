-- =============================================================
-- DATABASE: QUẢN LÝ SHOP QUẦN ÁO ĐA CHI NHÁNH
-- HỆ QUẢN TRỊ: MySQL (Engine InnoDB)
-- =============================================================

CREATE DATABASE IF NOT EXISTS ShopQuanAoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ShopQuanAoDB;

-- ---------------------------------------------------------
-- 1. Cấu trúc các bảng (Schema)
-- ---------------------------------------------------------

-- Nhóm Phân quyền
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    api_path VARCHAR(255) NOT NULL,
    method VARCHAR(50) NOT NULL,
    module VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE permission_role (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- Nhóm Danh mục & Thuộc tính sản phẩm
CREATE TABLE KieuSanPham (MaKieuSanPham VARCHAR(50) PRIMARY KEY, TenKieuSanPham VARCHAR(255), NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP);
CREATE TABLE NhaCungCap (MaNhaCungCap VARCHAR(50) PRIMARY KEY, TenNhaCungCap VARCHAR(255), SoDienThoai VARCHAR(20), DiaChi TEXT);
CREATE TABLE BoSuuTap (MaSuuTap VARCHAR(50) PRIMARY KEY, TenSuuTap VARCHAR(255), MoTa TEXT);
CREATE TABLE ThuongHieu (MaThuongHieu VARCHAR(50) PRIMARY KEY, TenThuongHieu VARCHAR(255));
CREATE TABLE KichThuoc (MaKichThuc VARCHAR(50) PRIMARY KEY, TenKichThuoc VARCHAR(50));
CREATE TABLE MauSac (MaMauSac VARCHAR(50) PRIMARY KEY, TenMauSac VARCHAR(50));
CREATE TABLE LoaiKiemKe (MaLoaiKiemKe VARCHAR(50) PRIMARY KEY, TenLoai VARCHAR(255));
CREATE TABLE HinhAnh (MaHinhAnh VARCHAR(50) PRIMARY KEY, DuongDan TEXT, NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP);

-- Nhóm Sản phẩm & Biến thể
CREATE TABLE SanPham (
    MaSanPham VARCHAR(50) PRIMARY KEY, TenSanPham VARCHAR(255) NOT NULL, GiaVon DECIMAL(18,2), GiaBan DECIMAL(18,2),
    MaKieuSanPham VARCHAR(50), MaNhaCungCap VARCHAR(50), MaSuuTap VARCHAR(50), MaThuongHieu VARCHAR(50), TrangThai VARCHAR(50),
    FOREIGN KEY (MaKieuSanPham) REFERENCES KieuSanPham(MaKieuSanPham),
    FOREIGN KEY (MaNhaCungCap) REFERENCES NhaCungCap(MaNhaCungCap),
    FOREIGN KEY (MaSuuTap) REFERENCES BoSuuTap(MaSuuTap),
    FOREIGN KEY (MaThuongHieu) REFERENCES ThuongHieu(MaThuongHieu)
);

CREATE TABLE ChiTietSanPham (
    MaChiTietSanPham VARCHAR(50) PRIMARY KEY, MaSanPham VARCHAR(50), MaKichThuoc VARCHAR(50), MaMauSac VARCHAR(50), SKU VARCHAR(100) UNIQUE,
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham),
    FOREIGN KEY (MaKichThuoc) REFERENCES KichThuoc(MaKichThuc),
    FOREIGN KEY (MaMauSac) REFERENCES MauSac(MaMauSac)
);

CREATE TABLE ChiTietHinhAnh (
    MaChiTietHinhAnh VARCHAR(50) PRIMARY KEY, MaHinhAnh VARCHAR(50), MaChiTietSanPham VARCHAR(50),
    FOREIGN KEY (MaHinhAnh) REFERENCES HinhAnh(MaHinhAnh),
    FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

-- Nhóm Cửa hàng & Nhân sự
CREATE TABLE CuaHang (MaCuaHang VARCHAR(50) PRIMARY KEY, TenCuaHang VARCHAR(255) NOT NULL, DiaChi TEXT, Email VARCHAR(100), Sdt VARCHAR(20), TrangThai VARCHAR(50));
CREATE TABLE NhanVien (
    MaNhanVien VARCHAR(50) PRIMARY KEY, TenNhanVien VARCHAR(255), Email VARCHAR(100), Password VARCHAR(255), Sdt VARCHAR(20),
    MaCuaHang VARCHAR(50), ChucVu VARCHAR(50), role_id BIGINT,
    FOREIGN KEY (MaCuaHang) REFERENCES CuaHang(MaCuaHang),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE LichLamViec (
    MaLichLamViec VARCHAR(50) PRIMARY KEY, MaNhanVien VARCHAR(50), Ngay DATE, Ca VARCHAR(50), MaCuaHang VARCHAR(50),
    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien), FOREIGN KEY (MaCuaHang) REFERENCES CuaHang(MaCuaHang)
);

CREATE TABLE DangKyLichLam (MaDangKy VARCHAR(50) PRIMARY KEY, MaNhanVien VARCHAR(50), NgayDangKy DATE, CaDangKy VARCHAR(50), FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien));
CREATE TABLE DoiCa (MaDoiCa VARCHAR(50) PRIMARY KEY, MaLichLamViec VARCHAR(50), MaNhanVienThayThe VARCHAR(50), GhiTru TEXT, FOREIGN KEY (MaLichLamViec) REFERENCES LichLamViec(MaLichLamViec), FOREIGN KEY (MaNhanVienThayThe) REFERENCES NhanVien(MaNhanVien));
CREATE TABLE HuyCa (MaHuyCa VARCHAR(50) PRIMARY KEY, MaLichLamViec VARCHAR(50), LyDo TEXT, NgayHuy DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (MaLichLamViec) REFERENCES LichLamViec(MaLichLamViec));
CREATE TABLE LoiPhatSinh (MaLoi VARCHAR(50) PRIMARY KEY, MaLichLamViec VARCHAR(50), MoTaLoi TEXT, FOREIGN KEY (MaLichLamViec) REFERENCES LichLamViec(MaLichLamViec));

-- Nhóm Vận hành Kho & Tồn kho
CREATE TABLE TonKhoCuaHang (
    MaCuaHang VARCHAR(50), MaChiTietSanPham VARCHAR(50), SoLuongTon INT DEFAULT 0,
    PRIMARY KEY (MaCuaHang, MaChiTietSanPham), FOREIGN KEY (MaCuaHang) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

CREATE TABLE KiemKeHangHoa (
    MaKiemKe VARCHAR(50) PRIMARY KEY, MaCuaHang VARCHAR(50), MaNhanVien VARCHAR(50), NgayBatDau DATETIME, TrangThai VARCHAR(50), MaLoaiKiemKe VARCHAR(50),
    FOREIGN KEY (MaCuaHang) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien), FOREIGN KEY (MaLoaiKiemKe) REFERENCES LoaiKiemKe(MaLoaiKiemKe)
);

CREATE TABLE ChiTietKiemKe (
    MaKiemKe VARCHAR(50), MaChiTietSanPham VARCHAR(50), SoLuongHeThong INT, SoLuongThucTe INT, GhiTru TEXT,
    PRIMARY KEY (MaKiemKe, MaChiTietSanPham), FOREIGN KEY (MaKiemKe) REFERENCES KiemKeHangHoa(MaKiemKe), FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

CREATE TABLE DonLuanChuyen (
    MaDonLuanChuyen VARCHAR(50) PRIMARY KEY, MaCuaHangDi VARCHAR(50), MaCuaHangDen VARCHAR(50), MaNhanVienTao VARCHAR(50), MaNhanVienNhan VARCHAR(50), NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP, TrangThai VARCHAR(50),
    FOREIGN KEY (MaCuaHangDi) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaCuaHangDen) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaNhanVienTao) REFERENCES NhanVien(MaNhanVien), FOREIGN KEY (MaNhanVienNhan) REFERENCES NhanVien(MaNhanVien)
);

CREATE TABLE ChiTietDonLuanChuyen (
    MaDonLuanChuyen VARCHAR(50), MaChiTietSanPham VARCHAR(50), SoLuong INT,
    PRIMARY KEY (MaDonLuanChuyen, MaChiTietSanPham), FOREIGN KEY (MaDonLuanChuyen) REFERENCES DonLuanChuyen(MaDonLuanChuyen), FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

CREATE TABLE PhieuNhap (
    MaPhieuNhap VARCHAR(50) PRIMARY KEY, MaNhaCungCap VARCHAR(50), MaCuaHangNhan VARCHAR(50), NgayNhap DATETIME, MaNhanVien VARCHAR(50),
    FOREIGN KEY (MaNhaCungCap) REFERENCES NhaCungCap(MaNhaCungCap), FOREIGN KEY (MaCuaHangNhan) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien)
);

CREATE TABLE ChiTietPhieuNhap (
    MaPhieuNhap VARCHAR(50), MaChiTietSanPham VARCHAR(50), SoLuong INT, DonGiaNhap DECIMAL(18,2),
    PRIMARY KEY (MaPhieuNhap, MaChiTietSanPham), FOREIGN KEY (MaPhieuNhap) REFERENCES PhieuNhap(MaPhieuNhap), FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

-- Nhóm Bán hàng & Khách hàng
CREATE TABLE KhachHang (
    MaKhachHang VARCHAR(50) PRIMARY KEY, TenKhachHang VARCHAR(255), Sdt VARCHAR(20) UNIQUE,
    Email VARCHAR(100), Password VARCHAR(255), DiemTichLuy INT DEFAULT 0, role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Giỏ hàng
CREATE TABLE GioHang (
    MaGioHang BIGINT AUTO_INCREMENT PRIMARY KEY,
    MaKhachHang VARCHAR(50) NOT NULL UNIQUE,
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang)
);

CREATE TABLE ChiTietGioHang (
    MaGioHang BIGINT,
    MaChiTietSanPham VARCHAR(50),
    SoLuong INT DEFAULT 1,
    PRIMARY KEY (MaGioHang, MaChiTietSanPham),
    FOREIGN KEY (MaGioHang) REFERENCES GioHang(MaGioHang),
    FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

CREATE TABLE DonHangTrucTiep (
    MaDonHang VARCHAR(50) PRIMARY KEY, MaCuaHang VARCHAR(50), MaNhanVien VARCHAR(50), MaKhachHang VARCHAR(50), NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP, TongTien DECIMAL(18,2),
    FOREIGN KEY (MaCuaHang) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien), FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang)
);

CREATE TABLE ChiTietDonHangTrucTiep (
    MaDonHang VARCHAR(50), MaChiTietSanPham VARCHAR(50), SoLuong INT, GiaBan DECIMAL(18,2),
    PRIMARY KEY (MaDonHang, MaChiTietSanPham), FOREIGN KEY (MaDonHang) REFERENCES DonHangTrucTiep(MaDonHang), FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

CREATE TABLE DonHangOnline (
    MaDonHangOnline VARCHAR(50) PRIMARY KEY, MaKhachHang VARCHAR(50), NgayDat DATETIME, DiaChiGiao TEXT, TrangThai VARCHAR(50), MaCuaHangXuLy VARCHAR(50), MaNhanVienXuLy VARCHAR(50),
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang), FOREIGN KEY (MaCuaHangXuLy) REFERENCES CuaHang(MaCuaHang), FOREIGN KEY (MaNhanVienXuLy) REFERENCES NhanVien(MaNhanVien)
);

CREATE TABLE ChiTietDonHangOnline (
    MaDonHangOnline VARCHAR(50), MaChiTietSanPham VARCHAR(50), SoLuong INT,
    PRIMARY KEY (MaDonHangOnline, MaChiTietSanPham), FOREIGN KEY (MaDonHangOnline) REFERENCES DonHangOnline(MaDonHangOnline), FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham)
);

-- Nhóm Khuyến mãi
CREATE TABLE KhuyenMai (MaKhuyenMai VARCHAR(50) PRIMARY KEY, TenKhuyenMai VARCHAR(255), NgayBatDau DATETIME, NgayKetThuc DATETIME, TrangThai TINYINT(1) DEFAULT 1);
CREATE TABLE KhuyenMaiTheoDiem (MaKM VARCHAR(50) PRIMARY KEY, DiemYeuCau INT, TiLeGiam FLOAT, FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKhuyenMai));
CREATE TABLE KhuyenMaiTheoHoaDon (MaKM VARCHAR(50) PRIMARY KEY, GiaTriToiThieu DECIMAL(18,2), SoTienGiam DECIMAL(18,2), FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKhuyenMai));
CREATE TABLE KhuyenMaiTheoSanPham (
    MaKM VARCHAR(50), MaSanPham VARCHAR(50), PhanTramGiam FLOAT,
    PRIMARY KEY (MaKM, MaSanPham), FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKhuyenMai), FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
);

-- Nhóm Lịch sử tồn kho
CREATE TABLE LichSuTonKho (
    MaLichSu VARCHAR(50) PRIMARY KEY, MaCuaHang VARCHAR(50), MaChiTietSanPham VARCHAR(50),
    SoLuongTruoc INT, SoLuongSau INT, SoLuongThayDoi INT,
    LyDo VARCHAR(255), MaNguon VARCHAR(50), MaNhanVien VARCHAR(50), NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (MaCuaHang) REFERENCES CuaHang(MaCuaHang),
    FOREIGN KEY (MaChiTietSanPham) REFERENCES ChiTietSanPham(MaChiTietSanPham),
    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien)
);

-- ---------------------------------------------------------
-- 2. Dữ liệu mẫu (Insert Data)
-- ---------------------------------------------------------

-- Roles & Permissions
INSERT INTO roles (name, description, active, created_at) VALUES
    ('ADMIN', 'Quản trị viên toàn quyền', TRUE, NOW()),
    ('QUAN_LY', 'Quản lý cửa hàng', TRUE, NOW()),
    ('NHAN_VIEN', 'Nhân viên bán hàng', TRUE, NOW()),
    ('KHACH_HANG', 'Khách hàng mua sắm', TRUE, NOW());

INSERT INTO permissions (name, api_path, method, module, created_at) VALUES
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
INSERT INTO CuaHang VALUES ('CH01','Chi nhánh Q1','1 Lợi Lê','q1@shop.com','0281','Mở'),('CH02','Chi nhánh Q7','2 Linh Nguyễn','q7@shop.com','0282','Mở'),('CH03','Chi nhánh HN','3 Phố Cổ','hn@shop.com','0241','Mở'),('CH04','Kho Tổng','Bình Dương','kho@shop.com','0271','Mở'),('CH05','Chi nhánh ĐN','Đà Nẵng','dn@shop.com','0231','Đóng');
INSERT INTO KieuSanPham VALUES ('KSP1','Áo Nam','2026-01-01'),('KSP2','Quần Nam','2026-01-01'),('KSP3','Váy Nữ','2026-01-01'),('KSP4','Phụ Kiện','2026-01-01'),('KSP5','Đồ Đông','2026-01-01');
INSERT INTO NhaCungCap VALUES ('NCC1','Dệt May 1','0901','HCM'),('NCC2','Vải Thái','0902','Long An'),('NCC3','Jean Gia Định','0903','HCM'),('NCC4','Xưởng HN','0904','Hà Nội'),('NCC5','Chợ Ninh Hiệp','0905','Hà Nội');
INSERT INTO BoSuuTap VALUES ('BST1','Hè 26','Mát mẻ'),('BST2','Thu 26','Sang trọng'),('BST3','Basic','Cơ bản'),('BST4','Luxury','Cao cấp'),('BST5','Sale Off','Giảm giá');
INSERT INTO ThuongHieu VALUES ('TH1','ABC'),('TH2','XYZ'),('TH3','Kids'),('TH4','Sport'),('TH5','Vintage');
INSERT INTO KichThuoc VALUES ('S','Nhỏ'),('M','Vừa'),('L','Lớn'),('XL','Rất lớn'),('XXL','Đặc biệt');
INSERT INTO MauSac VALUES ('TR','Trắng'),('DE','Đen'),('DO','Đỏ'),('XA','Xanh'),('VA','Vàng');
INSERT INTO HinhAnh VALUES ('IMG1','/img1.png',now()),('IMG2','/img2.png',now()),('IMG3','/img3.png',now()),('IMG4','/img4.png',now()),('IMG5','/img5.png',now());
INSERT INTO LoaiKiemKe VALUES ('LKK1','Định kỳ'),('LKK2','Đột xuất'),('LKK3','Cuối năm'),('LKK4','Đóng cửa'),('LKK5','Bàn giao');

-- Nhân sự (password = BCrypt hash của '123456')
INSERT INTO NhanVien VALUES ('NV1','An','an@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','01','CH01','QL',2),
('NV2','Bình','b@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','02','CH01','NV',3),
('NV3','Chi','c@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','03','CH02','NV',3),
('NV4','Danh','d@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','04','CH03','QL',2),
('NV5','Hùng','h@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','05','CH04','ADMIN',1);

INSERT INTO LichLamViec VALUES ('L1','NV2','2026-02-14','Sáng','CH01'),('L2','NV3','2026-02-14','Chiều','CH02'),('L3','NV2','2026-02-15','Sáng','CH01'),('L4','NV1','2026-02-14','Hành chính','CH01'),('L5','NV5','2026-02-14','Sáng','CH04');
INSERT INTO DangKyLichLam VALUES ('DK1','NV2','2026-02-20','Sáng'),('DK2','NV3','2026-02-20','Tối'),('DK3','NV1','2026-02-21','Hành chính'),('DK4','NV5','2026-02-21','Sáng'),('DK5','NV2','2026-02-21','Chiều');
INSERT INTO DoiCa VALUES ('DC1','L1','NV3','Ốm'),('DC2','L2','NV2','Việc riêng'),('DC3','L3','NV1','Bận'),('DC4','L4','NV2','Đổi ngang'),('DC5','L5','NV1','Hỗ trợ');
INSERT INTO HuyCa VALUES ('HC1','L1','Lễ','2026-02-13'),('HC2','L2','Mưa','2026-02-13'),('HC3','L3','Sửa điện','2026-02-13'),('HC4','L4','Cúp điện','2026-02-13'),('HC5','L5','Đóng kho','2026-02-13');
INSERT INTO LoiPhatSinh VALUES ('LOI1','L1','Trễ giờ'),('LOI2','L2','Thiếu hụt'),('LOI3','L3','Mất đồ'),('LOI4','L4','Sai hóa đơn'),('LOI5','L5','Vỡ hàng');

-- Sản phẩm & Kho
INSERT INTO SanPham VALUES ('SP1','Áo Oxford',100,200,'KSP1','NCC1','BST3','TH1','Kinh doanh'),('SP2','Quần Jean',200,400,'KSP2','NCC3','BST3','TH2','Kinh doanh'),('SP3','Váy Hoa',150,300,'KSP3','NCC2','BST1','TH1','Kinh doanh'),('SP4','Nịt Da',50,100,'KSP4','NCC5','BST3','TH5','Kinh doanh'),('SP5','Áo Phao',300,600,'KSP5','NCC4','BST2','TH2','Ngừng');
INSERT INTO ChiTietSanPham VALUES ('CT1','SP1','M','TR','SKU-1'),('CT2','SP1','L','TR','SKU-2'),('CT3','SP2','M','DE','SKU-3'),('CT4','SP3','S','DO','SKU-4'),('CT5','SP4','L','DE','SKU-5');
INSERT INTO ChiTietHinhAnh VALUES ('CTHA1','IMG1','CT1'),('CTHA2','IMG2','CT2'),('CTHA3','IMG3','CT3'),('CTHA4','IMG4','CT4'),('CTHA5','IMG5','CT5');
INSERT INTO TonKhoCuaHang VALUES ('CH01','CT1',10),('CH01','CT2',20),('CH02','CT3',5),('CH03','CT4',15),('CH04','CT1',100);
INSERT INTO KiemKeHangHoa VALUES ('KK1','CH01','NV1','2026-02-01','Xong','LKK1'),('KK2','CH02','NV3','2026-02-01','Xong','LKK2'),('KK3','CH03','NV4','2026-02-01','Xong','LKK3'),('KK4','CH01','NV2','2026-02-10','Xong','LKK1'),('KK5','CH04','NV5','2026-02-10','Xong','LKK3');
INSERT INTO ChiTietKiemKe VALUES ('KK1','CT1',10,10,'Ok'),('KK1','CT2',20,19,'Mất 1'),('KK2','CT3',5,5,'Dủ'),('KK3','CT4',15,15,'Ok'),('KK5','CT1',100,100,'Ok');
INSERT INTO DonLuanChuyen VALUES ('LC1','CH04','CH01','NV5','NV1',now(),'Xong'),('LC2','CH04','CH02','NV5','NV3',now(),'Gửi'),('LC3','CH04','CH03','NV5','NV4',now(),'Xong'),('LC4','CH01','CH02','NV1','NV3',now(),'Xong'),('LC5','CH04','CH01','NV5',NULL,now(),'Hủy');
INSERT INTO ChiTietDonLuanChuyen VALUES ('LC1','CT1',20),('LC2','CT3',10),('LC3','CT4',5),('LC4','CT1',5),('LC1','CT2',10);
INSERT INTO PhieuNhap VALUES ('PN1','NCC1','CH04','2026-02-01','NV5'),('PN2','NCC3','CH04','2026-02-01','NV5'),('PN3','NCC2','CH04','2026-02-01','NV5'),('PN4','NCC4','CH03','2026-02-01','NV4'),('PN5','NCC5','CH01','2026-02-01','NV1');
INSERT INTO ChiTietPhieuNhap VALUES ('PN1','CT1',100,100),('PN1','CT2',50,110),('PN2','CT3',30,200),('PN3','CT4',20,150),('PN4','CT1',10,100);

-- Bán hàng
INSERT INTO KhachHang VALUES ('KH1','Lan','0911','lan@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',10,4),
('KH2','Minh','0922','m@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',0,4),
('KH3','Hoa','0933','h@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',100,4),
('KH4','Tuấn','0944','t@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',5,4),
('KH5','Yến','0955','y@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',50,4);

-- Giỏ hàng mẫu
INSERT INTO GioHang (MaKhachHang, NgayTao) VALUES ('KH1', NOW()), ('KH3', NOW());
INSERT INTO ChiTietGioHang VALUES (1,'CT1',2),(1,'CT3',1),(2,'CT4',3);

INSERT INTO DonHangTrucTiep VALUES ('DH1','CH01','NV2','KH1',now(),200),('DH2','CH01','NV2','KH2',now(),400),('DH3','CH02','NV3','KH3',now(),100),('DH4','CH03','NV4','KH4',now(),300),('DH5','CH01','NV1','KH5',now(),600);
INSERT INTO ChiTietDonHangTrucTiep VALUES ('DH1','CT1',1,200),('DH2','CT3',1,400),('DH3','CT5',1,100),('DH4','CT4',1,300),('DH5','CT1',3,200);
INSERT INTO DonHangOnline VALUES ('ON1','KH1',now(),'Địa chỉ 1','Xong','CH01','NV2'),('ON2','KH3',now(),'Địa chỉ 2','Giao','CH02','NV3'),('ON3','KH5',now(),'Địa chỉ 3','Chờ','CH03',NULL),('ON4','KH2',now(),'Địa chỉ 4','Hủy','CH01','NV1'),('ON5','KH1',now(),'Địa chỉ 5','Xong','CH02','NV3');
INSERT INTO ChiTietDonHangOnline VALUES ('ON1','CT1',1),('ON2','CT3',2),('ON3','CT4',1),('ON4','CT1',1),('ON5','CT2',1);

-- Khuyến mãi
INSERT INTO KhuyenMai VALUES ('KM1','Tết 26','2026-01-01','2026-02-28',1),('KM2','VIP','2026-01-01','2026-12-31',1),('KM3','Xả kho','2026-02-01','2026-02-15',1),('KM4','Khai trương','2026-03-01','2026-03-07',0),('KM5','Cuối tuần','2026-01-01','2026-12-31',1);
INSERT INTO KhuyenMaiTheoDiem VALUES ('KM2',100,0.1);
INSERT INTO KhuyenMaiTheoHoaDon VALUES ('KM1',1000,50);
INSERT INTO KhuyenMaiTheoSanPham VALUES ('KM3','SP1',0.2),('KM3','SP2',0.15),('KM5','SP3',0.05),('KM3','SP4',0.5),('KM1','SP5',0.3);

-- Lịch sử tồn kho
INSERT INTO LichSuTonKho VALUES ('LS1','CH01','CT2',20,19,-1,'Kiểm kê phát hiện mất 1','KK1','NV1',now()),('LS2','CH04','CT1',100,100,0,'Kiểm kê đúng','KK5','NV5',now()),('LS3','CH01','CT1',10,30,20,'Nhận hàng luân chuyển','LC1','NV1',now()),('LS4','CH04','CT1',100,100,0,'Nhập kho từ NCC','PN1','NV5',now()),('LS5','CH01','CT1',10,10,0,'Bán hàng trực tiếp','DH1','NV2',now());