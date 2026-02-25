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
-- INSERT DỮ LIỆU MẪU (chỉ các bảng có domain entity)
-- ---------------------------------------------------------

-- 1. roles (Role.java)
INSERT INTO roles (name, description, active, createdAt) VALUES
    ('ADMIN',      'Quản trị viên toàn quyền', TRUE, NOW()),
    ('QUAN_LY',    'Quản lý cửa hàng',         TRUE, NOW()),
    ('NHAN_VIEN',  'Nhân viên bán hàng',        TRUE, NOW()),
    ('KHACH_HANG', 'Khách hàng mua sắm',        TRUE, NOW());

-- 2. permissions (Permission.java)
INSERT INTO permissions (name, apiPath, method, module, createdAt) VALUES
    ('Tạo sản phẩm', '/api/v1/san-pham',     'POST',   'SAN_PHAM', NOW()),
    ('Sửa sản phẩm', '/api/v1/san-pham',     'PUT',    'SAN_PHAM', NOW()),
    ('Xóa sản phẩm', '/api/v1/san-pham/{id}','DELETE', 'SAN_PHAM', NOW()),
    ('Xem sản phẩm', '/api/v1/san-pham',     'GET',    'SAN_PHAM', NOW()),
    ('Thêm giỏ hàng','/api/v1/gio-hang',     'POST',   'GIO_HANG', NOW()),
    ('Xem giỏ hàng', '/api/v1/gio-hang',     'GET',    'GIO_HANG', NOW()),
    ('Xóa giỏ hàng',          '/api/v1/gio-hang/{id}', 'DELETE', 'GIO_HANG', NOW()),
    ('Xem chi tiết sản phẩm','/api/v1/san-pham/{id}',  'GET',    'SAN_PHAM', NOW());

-- 3. permission_role (join table Role ↔ Permission)
-- ADMIN: tất cả permissions
INSERT INTO permission_role (role_id, permission_id) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8);
-- QUAN_LY: CRUD sản phẩm
INSERT INTO permission_role (role_id, permission_id) VALUES (2,1),(2,2),(2,3),(2,4);
-- NHAN_VIEN: xem sản phẩm
INSERT INTO permission_role (role_id, permission_id) VALUES (3,4);
-- KHACH_HANG: xem sản phẩm + xem chi tiết sản phẩm
INSERT INTO permission_role (role_id, permission_id) VALUES (4,4),(4,8);

-- 4. NhanVien (NhanVien.java)
-- password = BCrypt hash của '123456'
-- ChucVu khớp enum: NHAN_VIEN, QUAN_LY, CHU_CUA_HANG, ADMIN
INSERT INTO NhanVien (TenNhanVien, Email, Password, Sdt, ChucVu, role_id) VALUES
    ('An',   'an@s.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','01','QUAN_LY', 2),
    ('Bình', 'b@s.com', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','02','NHAN_VIEN',3),
    ('Chi',  'c@s.com', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','03','NHAN_VIEN',3),
    ('Danh', 'd@s.com', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','04','QUAN_LY', 2),
    ('Hùng', 'h@s.com', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5','05','ADMIN',   1);

-- 5. KhachHang (KhachHang.java)
INSERT INTO KhachHang (TenKhachHang, Sdt, Email, Password, DiemTichLuy, role_id) VALUES
    ('Lan',  '0911','lan@g.com','$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 10,4),
    ('Minh', '0922','m@g.com',  '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',  0,4),
    ('Hoa',  '0933','h@g.com',  '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',100,4),
    ('Tuấn', '0944','t@g.com',  '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',  5,4),
    ('Yến',  '0955','y@g.com',  '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 50,4);

-- 6. SanPham (SanPham.java)
INSERT INTO SanPham (TenSanPham, GiaVon, GiaBan, TrangThai) VALUES
    ('Áo Oxford', 100, 200, 'Kinh doanh'),
    ('Quần Jean', 200, 400, 'Kinh doanh'),
    ('Váy Hoa',   150, 300, 'Kinh doanh'),
    ('Nịt Da',     50, 100, 'Kinh doanh'),
    ('Áo Phao',   300, 600, 'Ngừng');