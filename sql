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
    -- SanPham (1-4, 8)
    ('Tạo sản phẩm',            '/api/v1/san-pham',                     'POST',   'SAN_PHAM',       NOW()),
    ('Sửa sản phẩm',            '/api/v1/san-pham',                     'PUT',    'SAN_PHAM',       NOW()),
    ('Xóa sản phẩm',            '/api/v1/san-pham/{id}',                'DELETE', 'SAN_PHAM',       NOW()),
    ('Xem sản phẩm',            '/api/v1/san-pham',                     'GET',    'SAN_PHAM',       NOW()),
    -- GioHang (5-7)
    ('Thêm SP vào giỏ hàng',    '/api/v1/gio-hang/them-san-pham',                                        'POST',   'GIO_HANG',       NOW()),
    ('Xem giỏ hàng theo KH',    '/api/v1/gio-hang/khach-hang/{khachHangId}',                              'GET',    'GIO_HANG',       NOW()),
    ('Xóa SP khỏi giỏ hàng',   '/api/v1/gio-hang/chi-tiet/{maChiTietGioHang}', 'DELETE', 'GIO_HANG',       NOW()),
    -- SanPham chi tiết (8)
    ('Xem chi tiết sản phẩm',   '/api/v1/san-pham/{id}',                'GET',    'SAN_PHAM',       NOW()),
    -- MauSac (9-13)
    ('Xem màu sắc',             '/api/v1/mau-sac',                      'GET',    'MAU_SAC',        NOW()),
    ('Tạo màu sắc',             '/api/v1/mau-sac',                      'POST',   'MAU_SAC',        NOW()),
    ('Sửa màu sắc',             '/api/v1/mau-sac',                      'PUT',    'MAU_SAC',        NOW()),
    ('Xóa màu sắc',             '/api/v1/mau-sac/{id}',                 'DELETE', 'MAU_SAC',        NOW()),
    ('Xem chi tiết màu sắc',    '/api/v1/mau-sac/{id}',                 'GET',    'MAU_SAC',        NOW()),
    -- KichThuoc (14-18)
    ('Xem kích thước',          '/api/v1/kich-thuoc',                   'GET',    'KICH_THUOC',     NOW()),
    ('Tạo kích thước',          '/api/v1/kich-thuoc',                   'POST',   'KICH_THUOC',     NOW()),
    ('Sửa kích thước',          '/api/v1/kich-thuoc',                   'PUT',    'KICH_THUOC',     NOW()),
    ('Xóa kích thước',          '/api/v1/kich-thuoc/{id}',              'DELETE', 'KICH_THUOC',     NOW()),
    ('Xem chi tiết kích thước', '/api/v1/kich-thuoc/{id}',              'GET',    'KICH_THUOC',     NOW()),
    -- ChiTietSanPham (19-24)
    ('Xem chi tiết SP (all)',    '/api/v1/chi-tiet-san-pham',           'GET',    'CHI_TIET_SP',    NOW()),
    ('Xem chi tiết SP (id)',     '/api/v1/chi-tiet-san-pham/{id}',      'GET',    'CHI_TIET_SP',    NOW()),
    ('Xem CTSP theo sản phẩm',  '/api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}','GET', 'CHI_TIET_SP',    NOW()),
    ('Tạo chi tiết SP',         '/api/v1/chi-tiet-san-pham',           'POST',   'CHI_TIET_SP',    NOW()),
    ('Sửa chi tiết SP',         '/api/v1/chi-tiet-san-pham',           'PUT',    'CHI_TIET_SP',    NOW()),
    ('Xóa chi tiết SP',         '/api/v1/chi-tiet-san-pham/{id}',      'DELETE', 'CHI_TIET_SP',    NOW());

-- 3. permission_role (join table Role ↔ Permission)
-- ADMIN: tất cả permissions (1-24)
INSERT INTO permission_role (role_id, permission_id) VALUES
    (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),
    (1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),
    (1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24);
-- QUAN_LY: CRUD sp + mau sac + kich thuoc + chi tiet sp
INSERT INTO permission_role (role_id, permission_id) VALUES
    (2,1),(2,2),(2,3),(2,4),(2,8),
    (2,9),(2,10),(2,11),(2,12),(2,13),
    (2,14),(2,15),(2,16),(2,17),(2,18),
    (2,19),(2,20),(2,21),(2,22),(2,23),(2,24);
-- NHAN_VIEN: chỉ xem
INSERT INTO permission_role (role_id, permission_id) VALUES
    (3,4),(3,8),(3,9),(3,13),(3,14),(3,18),(3,19),(3,20),(3,21);
-- KHACH_HANG: xem sp + giỏ hàng (thêm/xem/xóa) + mau sac + kich thuoc + chi tiet sp
INSERT INTO permission_role (role_id, permission_id) VALUES
    (4,4),(4,5),(4,6),(4,7),(4,8),(4,9),(4,13),(4,14),(4,18),(4,19),(4,20),(4,21);

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
    ('Áo Oxford', 100, 200, 'Kinh doanh'),  -- id = 1
    ('Quần Jean', 200, 400, 'Kinh doanh'),  -- id = 2
    ('Váy Hoa',   150, 300, 'Kinh doanh'),  -- id = 3
    ('Nịt Da',     50, 100, 'Kinh doanh'),  -- id = 4
    ('Áo Phao',   300, 600, 'Ngừng');       -- id = 5

-- 7. KichThuoc (KichThuoc.java)
INSERT INTO KichThuoc (TenKichThuoc) VALUES
    ('Nhỏ'),        -- id = 1
    ('Vừa'),        -- id = 2
    ('Lớn'),        -- id = 3
    ('Rất lớn'),    -- id = 4
    ('Đặc biệt');   -- id = 5

-- 8. MauSac (MauSac.java)
INSERT INTO MauSac (TenMauSac) VALUES
    ('Trắng'),  -- id = 1
    ('Đen'),    -- id = 2
    ('Đỏ'),     -- id = 3
    ('Xanh'),   -- id = 4
    ('Vàng');   -- id = 5

-- 9. ChiTietSanPham (ChiTietSanPham.java)
-- MaSanPham(FK long), MaKichThuoc(FK long), MaMauSac(FK long)
INSERT INTO ChiTietSanPham (MaSanPham, MaKichThuoc, MaMauSac, SKU) VALUES
    (1, 2, 1, 'SKU-1'),  -- id=1: Áo Oxford / Vừa / Trắng
    (1, 3, 1, 'SKU-2'),  -- id=2: Áo Oxford / Lớn / Trắng
    (2, 2, 2, 'SKU-3'),  -- id=3: Quần Jean / Vừa / Đen
    (3, 1, 3, 'SKU-4'),  -- id=4: Váy Hoa   / Nhỏ / Đỏ
    (4, 3, 2, 'SKU-5');  -- id=5: Nịt Da    / Lớn / Đen