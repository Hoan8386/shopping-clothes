-- =============================================================
-- DATABASE: QUẢN LÝ SHOP QUẦN ÁO ĐA CHI NHÁNH
-- FILE NÀY CHỈ CHỨA CẤU TRÚC BẢNG (CREATE TABLE)
-- Dữ liệu mẫu nằm trong file insert_data.sql
-- =============================================================

CREATE DATABASE IF NOT EXISTS shopping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopping;

-- ==============================================
-- 1. HỆ THỐNG PHÂN QUYỀN (SYSTEM SECURITY)
-- ==============================================

CREATE TABLE permissions (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    api_path VARCHAR(255),
    method VARCHAR(255),
    module VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    active INT, 
    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE permission_role (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id)
);

-- ==============================================
-- 2. QUẢN LÝ SẢN PHẨM & DANH MỤC
-- ==============================================

CREATE TABLE KieuSanPham (
    MaKieuSanPham BIGINT PRIMARY KEY,
    TenKieuSanPham VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE BoSuuTap (
    MaSuuTap BIGINT PRIMARY KEY,
    TenSuuTap VARCHAR(255),
    MoTa VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ThuongHieu (
    MaThuongHieu BIGINT PRIMARY KEY,
    TenThuongHieu VARCHAR(255),
    TrangThaiHoatDong INT,
    TrangThaiHienThi INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE MauSac (
    MaMauSac BIGINT PRIMARY KEY,
    TenMau VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE KichThuoc (
    MaKichThuoc BIGINT PRIMARY KEY,
    TenKichThuoc VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE SanPham (
    MaSanPham BIGINT PRIMARY KEY,
    MaKieuSanPham BIGINT,
    MaSuuTap BIGINT,
    MaThuongHieu BIGINT,
    TenSanPham VARCHAR(255),
    GiaVon DOUBLE,
    GiaBan DOUBLE,
    GiaGiam INT,
    HinhAnhChinh VARCHAR(255),
    MoTa VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietSanPham (
    MaChiTietSanPham BIGINT PRIMARY KEY,
    MaSanPham BIGINT,
    MaPhieuNhap BIGINT,
    MaMauSac BIGINT,
    MaKichThuoc BIGINT,
    MaCuaHang BIGINT,
    TrangThai INT,
    MoTa VARCHAR(255),
    GhiTru VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE HinhAnh (
    MaHinhAnh BIGINT PRIMARY KEY,
    MaChiTietSanPham BIGINT,
    TenHinhAnh VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

-- ==============================================
-- 3. QUẢN LÝ NHÂN SỰ & CỬA HÀNG
-- ==============================================

CREATE TABLE CuaHang (
    MaCuaHang BIGINT PRIMARY KEY,
    TenCuaHang VARCHAR(255),
    DiaChi VARCHAR(255),
    ViTri VARCHAR(255),
    SoDienThoai VARCHAR(255),
    Email VARCHAR(255),
    TrangThai INT
);

-- ĐÃ BỎ BẢNG ChucVu Ở ĐÂY

CREATE TABLE NhanVien (
    MaNhanVien BIGINT PRIMARY KEY,
    MaCuaHang BIGINT,
    role_id BIGINT,  -- Sử dụng role_id thay cho MaChucVu
    TenNhanVien VARCHAR(255),
    Email VARCHAR(255),
    SoDienThoai VARCHAR(255),
    MatKhau VARCHAR(255),
    NgayBatDauLam DATETIME,
    NgayKetThucLam DATETIME,
    TrangThai INT
);

-- ==============================================
-- 4. QUẢN LÝ KHÁCH HÀNG, ĐÁNH GIÁ & GIỎ HÀNG
-- ==============================================

CREATE TABLE KhachHang (
    MaKhachHang BIGINT PRIMARY KEY,
    role_id BIGINT,  -- Cột phân quyền
    TenKhachHang VARCHAR(255),
    Email VARCHAR(255),
    SoDienThoai VARCHAR(255),
    DiemTichLuy INT,
    MatKhau VARCHAR(255)
);

CREATE TABLE DanhGiaSanPham (
    MaDanhGia BIGINT PRIMARY KEY,
    MaKhachHang BIGINT,
    MaSanPham BIGINT,
    MaDon BIGINT,
    SoSao INT,
    GhiTru VARCHAR(255),
    HinhAnh VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE GioHang (
    MaGioHang BIGINT PRIMARY KEY,
    MaKhachHang BIGINT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietGioHang (
    MaGioHang BIGINT,
    MaChiTietSanPham BIGINT,
    SoLuong INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    PRIMARY KEY (MaGioHang, MaChiTietSanPham)
);

-- ==============================================
-- 5. QUẢN LÝ ĐƠN HÀNG & KHUYẾN MÃI
-- ==============================================

CREATE TABLE KhuyenMaiTheoHoaDon (
    MaKhuyenMaiHoaDon BIGINT PRIMARY KEY,
    TenKhuyenMai VARCHAR(255),
    GiamToiDa INT,
    HoaDonToiDa INT,
    PhanTramGiam DOUBLE,
    HinhThuc INT,
    ThoiGianBatDau DATETIME,
    ThoiGianKetThuc DATETIME,
    SoLuong INT,
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE KhuyenMaiTheoDiem (
    MaKhuyenMaiDiem BIGINT PRIMARY KEY,
    TenKhuyenMai VARCHAR(255),
    GiamToiDa INT,
    HoaDonToiDa INT,
    PhanTramGiam DOUBLE,
    HinhThuc INT,
    ThoiGianBatDau DATETIME,
    ThoiGianKetThuc DATETIME,
    SoLuong INT,
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE DonHang (
    MaDon BIGINT PRIMARY KEY,
    MaCuaHang BIGINT,
    MaKhachHang BIGINT,
    MaNhanVien BIGINT,
    MaKhuyenMaiHoaDon BIGINT,
    MaKhuyenMaiDiem BIGINT,
    DiaChi VARCHAR(255),
    TongTien INT,
    TienGiam INT,
    TongTienGiam INT,
    TongTienTra INT,
    TrangThai INT,
    TrangThaiThanhToan INT,
    HinhThucDonHang INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietDonHang (
    MaDon BIGINT,
    MaChiTietSanPham BIGINT,
    GiaSanPham DOUBLE,
    GiamGia DOUBLE,
    GiaGiam DOUBLE,
    SoLuong INT,
    ThanhTien DOUBLE,
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    PRIMARY KEY (MaDon, MaChiTietSanPham)
);

CREATE TABLE DoiHang (
    MaDoiHang BIGINT PRIMARY KEY,
    MaDonHang BIGINT,
    GhiTru VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietDoiHang (
    MaDoiHang BIGINT,
    MaSanPhamTra BIGINT,
    MaSanPhamDoi BIGINT,
    GhiTru VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    PRIMARY KEY (MaDoiHang, MaSanPhamTra)
);

-- ==============================================
-- 6. QUẢN LÝ KHO & LUÂN CHUYỂN
-- ==============================================

CREATE TABLE NhaCungCap (
    MaNhaCungCap BIGINT PRIMARY KEY,
    TenNhaCungCap VARCHAR(255),
    SoDienThoai VARCHAR(255),
    Email VARCHAR(255),
    DiaChi VARCHAR(255),
    GhiTru VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE PhieuNhap (
    MaPhieuNhap BIGINT PRIMARY KEY,
    MaCuaHang BIGINT,
    MaNhaCungCap BIGINT,
    TenPhieuNhap VARCHAR(255),
    TrangThai INT,
    NgayGiaoHang DATETIME,
    NgayNhanHang DATETIME,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietPhieuNhap (
    MaPhieuNhap BIGINT,
    MaChiTietSanPham BIGINT,
    SoLuong INT,
    GhiTru VARCHAR(255),
    GhiTruKiemHang VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    PRIMARY KEY (MaPhieuNhap, MaChiTietSanPham)
);

CREATE TABLE LoaiKiemKe (
    MaLoaiKiemKe BIGINT PRIMARY KEY,
    TenLoai VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    TrangThai INT
);

CREATE TABLE KiemKeHangHoa (
    MaKiemKe BIGINT PRIMARY KEY,
    MaCuaHang BIGINT,
    MaLoaiKiemKe BIGINT,
    TenKiemKe VARCHAR(255),
    TrangThai INT,
    NgayBatDau DATETIME,
    NgayKetThuc DATETIME,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietKiemKe (
    MaKiemKe BIGINT,
    MaChiTietSanPham BIGINT,
    SoLuongThuc INT,
    GhiTru VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    PRIMARY KEY (MaKiemKe, MaChiTietSanPham)
);

CREATE TABLE LoaiDonLuanChuyen (
    MaLoaiDonLuanChuyen BIGINT PRIMARY KEY,
    TenLoai VARCHAR(255),
    MoTa VARCHAR(255),
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE DonLuanChuyen (
    MaDonLuanChuyen BIGINT PRIMARY KEY,
    CuaHangDat BIGINT,
    CuaHangGui BIGINT,
    MaLoaiDonLuanChuyen BIGINT,
    TenDon VARCHAR(255),
    ThoiGianGiao DATETIME,
    ThoiGianNhan DATETIME,
    GhiTru VARCHAR(255),
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietDonLuanChuyen (
    MaDonLuanChuyen BIGINT,
    MaChiTietSanPham BIGINT,
    HinhAnh VARCHAR(255),
    SoLuong INT,
    TrangThai INT,
    GhiTru VARCHAR(255),
    GhiTruKiemHang VARCHAR(255),
    PRIMARY KEY (MaDonLuanChuyen, MaChiTietSanPham)
);

-- ==============================================
-- 7. QUẢN LÝ LỊCH LÀM VIỆC & LƯƠNG
-- ==============================================

CREATE TABLE CaLamViec (
    MaCaLam BIGINT PRIMARY KEY,
    TenCaLam VARCHAR(255),
    GioBatDau TIME,
    GioKetThuc TIME,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE LichLamViec (
    MaLichLam BIGINT PRIMARY KEY,
    MaNhanVien BIGINT,
    NgayLamViec DATE,
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE ChiTietLichLam (
    MaLichLam BIGINT,
    MaCaLamViec BIGINT,
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME,
    PRIMARY KEY (MaLichLam, MaCaLamViec)
);

CREATE TABLE DoiCa (
    MaDoiCa BIGINT PRIMARY KEY,
    MaLichLam BIGINT,
    MaCaLamViec BIGINT,
    NhanVienNhanCa BIGINT,
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE LoiPhatSinh (
    MaLoiPhatSinh BIGINT PRIMARY KEY,
    MaLichLam BIGINT,
    MaCaLamViec BIGINT,
    TenLoiPhatSinh VARCHAR(255),
    SoTienTru INT,
    TrangThai INT,
    NgayTao DATETIME,
    NgayCapNhat DATETIME
);

CREATE TABLE LuongCoBan (
    MaLuongCoBan BIGINT PRIMARY KEY,
    MaNhanVien BIGINT,
    LuongCoBan INT,
    NgayApDung DATETIME,
    TrangThai INT
);

CREATE TABLE LuongThuong (
    MaLuongThuong BIGINT PRIMARY KEY,
    MaNhanVien BIGINT,
    TienThuong INT,
    NgayBatDau DATETIME,
    NgayKetThuc DATETIME,
    TrangThai INT
);