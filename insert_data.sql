-- =============================================================
-- INSERT DỮ LIỆU MẪU - SHOPPING DATABASE
-- Chạy sau khi Hibernate (ddl-auto=update) đã tạo bảng
-- CHỈ ADMIN có toàn quyền truy cập API
-- =============================================================

CREATE DATABASE IF NOT EXISTS shopping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopping;

-- ---------------------------------------------------------
-- 1. ROLES
-- ---------------------------------------------------------
INSERT INTO roles (name, description, active, createdAt) VALUES
    ('ADMIN',      'Quản trị viên toàn quyền', TRUE, NOW()),
    ('QUAN_LY',    'Quản lý cửa hàng',         TRUE, NOW()),
    ('NHAN_VIEN',  'Nhân viên bán hàng',        TRUE, NOW()),
    ('KHACH_HANG', 'Khách hàng mua sắm',        TRUE, NOW());

-- ---------------------------------------------------------
-- 2. PERMISSIONS (60 quyền cho tất cả endpoint)
-- ---------------------------------------------------------
INSERT INTO permissions (name, apiPath, method, module, createdAt) VALUES
    -- === SAN_PHAM (1-5) ===
    ('Xem tất cả sản phẩm',        '/api/v1/san-pham',          'GET',    'SAN_PHAM',    NOW()),
    ('Xem chi tiết sản phẩm',      '/api/v1/san-pham/{id}',     'GET',    'SAN_PHAM',    NOW()),
    ('Tạo sản phẩm',               '/api/v1/san-pham',          'POST',   'SAN_PHAM',    NOW()),
    ('Cập nhật sản phẩm',          '/api/v1/san-pham',          'PUT',    'SAN_PHAM',    NOW()),
    ('Xóa sản phẩm',               '/api/v1/san-pham/{id}',     'DELETE', 'SAN_PHAM',    NOW()),

    -- === MAU_SAC (6-10) ===
    ('Xem tất cả màu sắc',         '/api/v1/mau-sac',           'GET',    'MAU_SAC',     NOW()),
    ('Xem chi tiết màu sắc',       '/api/v1/mau-sac/{id}',      'GET',    'MAU_SAC',     NOW()),
    ('Tạo màu sắc',                '/api/v1/mau-sac',           'POST',   'MAU_SAC',     NOW()),
    ('Cập nhật màu sắc',           '/api/v1/mau-sac',           'PUT',    'MAU_SAC',     NOW()),
    ('Xóa màu sắc',                '/api/v1/mau-sac/{id}',      'DELETE', 'MAU_SAC',     NOW()),

    -- === KICH_THUOC (11-15) ===
    ('Xem tất cả kích thước',      '/api/v1/kich-thuoc',        'GET',    'KICH_THUOC',  NOW()),
    ('Xem chi tiết kích thước',    '/api/v1/kich-thuoc/{id}',   'GET',    'KICH_THUOC',  NOW()),
    ('Tạo kích thước',             '/api/v1/kich-thuoc',        'POST',   'KICH_THUOC',  NOW()),
    ('Cập nhật kích thước',        '/api/v1/kich-thuoc',        'PUT',    'KICH_THUOC',  NOW()),
    ('Xóa kích thước',             '/api/v1/kich-thuoc/{id}',   'DELETE', 'KICH_THUOC',  NOW()),

    -- === CHI_TIET_SP (16-21) ===
    ('Xem tất cả CTSP',            '/api/v1/chi-tiet-san-pham',                          'GET',    'CHI_TIET_SP', NOW()),
    ('Xem CTSP theo id',           '/api/v1/chi-tiet-san-pham/{id}',                     'GET',    'CHI_TIET_SP', NOW()),
    ('Xem CTSP theo sản phẩm',     '/api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}',     'GET',    'CHI_TIET_SP', NOW()),
    ('Tạo CTSP',                   '/api/v1/chi-tiet-san-pham',                          'POST',   'CHI_TIET_SP', NOW()),
    ('Cập nhật CTSP',              '/api/v1/chi-tiet-san-pham',                          'PUT',    'CHI_TIET_SP', NOW()),
    ('Xóa CTSP',                   '/api/v1/chi-tiet-san-pham/{id}',                     'DELETE', 'CHI_TIET_SP', NOW()),

    -- === GIO_HANG (22-24) ===
    ('Thêm SP vào giỏ hàng',       '/api/v1/gio-hang/them-san-pham',                     'POST',   'GIO_HANG',    NOW()),
    ('Xem giỏ hàng của tôi',       '/api/v1/gio-hang/cua-toi',                           'GET',    'GIO_HANG',    NOW()),
    ('Xóa SP khỏi giỏ hàng',      '/api/v1/gio-hang/chi-tiet/{maChiTietGioHang}',       'DELETE', 'GIO_HANG',    NOW()),

    -- === KIEU_SAN_PHAM (25-29) ===
    ('Xem tất cả kiểu sản phẩm',   '/api/v1/kieu-san-pham',        'GET',    'KIEU_SAN_PHAM', NOW()),
    ('Xem chi tiết kiểu sản phẩm', '/api/v1/kieu-san-pham/{id}',   'GET',    'KIEU_SAN_PHAM', NOW()),
    ('Tạo kiểu sản phẩm',          '/api/v1/kieu-san-pham',        'POST',   'KIEU_SAN_PHAM', NOW()),
    ('Cập nhật kiểu sản phẩm',     '/api/v1/kieu-san-pham',        'PUT',    'KIEU_SAN_PHAM', NOW()),
    ('Xóa kiểu sản phẩm',          '/api/v1/kieu-san-pham/{id}',   'DELETE', 'KIEU_SAN_PHAM', NOW()),

    -- === BO_SUU_TAP (30-34) ===
    ('Xem tất cả bộ sưu tập',      '/api/v1/bo-suu-tap',           'GET',    'BO_SUU_TAP',    NOW()),
    ('Xem chi tiết bộ sưu tập',    '/api/v1/bo-suu-tap/{id}',      'GET',    'BO_SUU_TAP',    NOW()),
    ('Tạo bộ sưu tập',             '/api/v1/bo-suu-tap',           'POST',   'BO_SUU_TAP',    NOW()),
    ('Cập nhật bộ sưu tập',        '/api/v1/bo-suu-tap',           'PUT',    'BO_SUU_TAP',    NOW()),
    ('Xóa bộ sưu tập',             '/api/v1/bo-suu-tap/{id}',      'DELETE', 'BO_SUU_TAP',    NOW()),

    -- === THUONG_HIEU (35-39) ===
    ('Xem tất cả thương hiệu',     '/api/v1/thuong-hieu',          'GET',    'THUONG_HIEU',   NOW()),
    ('Xem chi tiết thương hiệu',   '/api/v1/thuong-hieu/{id}',     'GET',    'THUONG_HIEU',   NOW()),
    ('Tạo thương hiệu',            '/api/v1/thuong-hieu',          'POST',   'THUONG_HIEU',   NOW()),
    ('Cập nhật thương hiệu',       '/api/v1/thuong-hieu',          'PUT',    'THUONG_HIEU',   NOW()),
    ('Xóa thương hiệu',            '/api/v1/thuong-hieu/{id}',     'DELETE', 'THUONG_HIEU',   NOW()),

    -- === HINH_ANH (40-45) ===
    ('Xem tất cả hình ảnh',        '/api/v1/hinh-anh',                                   'GET',    'HINH_ANH',    NOW()),
    ('Xem hình ảnh theo id',       '/api/v1/hinh-anh/{id}',                              'GET',    'HINH_ANH',    NOW()),
    ('Xem hình ảnh theo CTSP',     '/api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}', 'GET', 'HINH_ANH',    NOW()),
    ('Tạo hình ảnh',               '/api/v1/hinh-anh',                                   'POST',   'HINH_ANH',    NOW()),
    ('Cập nhật hình ảnh',          '/api/v1/hinh-anh',                                   'PUT',    'HINH_ANH',    NOW()),
    ('Xóa hình ảnh',               '/api/v1/hinh-anh/{id}',                              'DELETE', 'HINH_ANH',    NOW()),

    -- === CUA_HANG (46-50) ===
    ('Xem tất cả cửa hàng',        '/api/v1/cua-hang',             'GET',    'CUA_HANG',      NOW()),
    ('Xem chi tiết cửa hàng',      '/api/v1/cua-hang/{id}',        'GET',    'CUA_HANG',      NOW()),
    ('Tạo cửa hàng',               '/api/v1/cua-hang',             'POST',   'CUA_HANG',      NOW()),
    ('Cập nhật cửa hàng',          '/api/v1/cua-hang',             'PUT',    'CUA_HANG',      NOW()),
    ('Xóa cửa hàng',               '/api/v1/cua-hang/{id}',        'DELETE', 'CUA_HANG',      NOW()),

    -- === ROLES (51-55) ===
    ('Xem tất cả vai trò',         '/api/v1/roles',                'GET',    'ROLES',         NOW()),
    ('Xem chi tiết vai trò',       '/api/v1/roles/{id}',           'GET',    'ROLES',         NOW()),
    ('Tạo vai trò',                '/api/v1/roles',                'POST',   'ROLES',         NOW()),
    ('Cập nhật vai trò',           '/api/v1/roles',                'PUT',    'ROLES',         NOW()),
    ('Xóa vai trò',                '/api/v1/roles/{id}',           'DELETE', 'ROLES',         NOW()),

    -- === PERMISSIONS (56-60) ===
    ('Xem tất cả quyền',           '/api/v1/permissions',          'GET',    'PERMISSIONS',   NOW()),
    ('Xem chi tiết quyền',         '/api/v1/permissions/{id}',     'GET',    'PERMISSIONS',   NOW()),
    ('Tạo quyền',                  '/api/v1/permissions',          'POST',   'PERMISSIONS',   NOW()),
    ('Cập nhật quyền',             '/api/v1/permissions',          'PUT',    'PERMISSIONS',   NOW()),
    ('Xóa quyền',                  '/api/v1/permissions/{id}',     'DELETE', 'PERMISSIONS',   NOW()),

    -- === NHA_CUNG_CAP (61-65) ===
    ('Xem tất cả nhà cung cấp',    '/api/v1/nha-cung-cap',         'GET',    'NHA_CUNG_CAP',  NOW()),
    ('Xem chi tiết nhà cung cấp',  '/api/v1/nha-cung-cap/{id}',    'GET',    'NHA_CUNG_CAP',  NOW()),
    ('Tạo nhà cung cấp',           '/api/v1/nha-cung-cap',         'POST',   'NHA_CUNG_CAP',  NOW()),
    ('Cập nhật nhà cung cấp',      '/api/v1/nha-cung-cap',         'PUT',    'NHA_CUNG_CAP',  NOW()),
    ('Xóa nhà cung cấp',           '/api/v1/nha-cung-cap/{id}',    'DELETE', 'NHA_CUNG_CAP',  NOW()),

    -- === PHIEU_NHAP (66-70) ===
    ('Xem tất cả phiếu nhập',      '/api/v1/phieu-nhap',           'GET',    'PHIEU_NHAP',    NOW()),
    ('Xem chi tiết phiếu nhập',    '/api/v1/phieu-nhap/{id}',      'GET',    'PHIEU_NHAP',    NOW()),
    ('Tạo phiếu nhập',             '/api/v1/phieu-nhap',           'POST',   'PHIEU_NHAP',    NOW()),
    ('Cập nhật phiếu nhập',        '/api/v1/phieu-nhap',           'PUT',    'PHIEU_NHAP',    NOW()),
    ('Xóa phiếu nhập',             '/api/v1/phieu-nhap/{id}',      'DELETE', 'PHIEU_NHAP',    NOW()),

    -- === CHI_TIET_PHIEU_NHAP (71-76) ===
    ('Xem tất cả CTPN',            '/api/v1/chi-tiet-phieu-nhap',                              'GET',    'CHI_TIET_PHIEU_NHAP', NOW()),
    ('Xem CTPN theo id',           '/api/v1/chi-tiet-phieu-nhap/{id}',                         'GET',    'CHI_TIET_PHIEU_NHAP', NOW()),
    ('Xem CTPN theo phiếu nhập',   '/api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}',     'GET',    'CHI_TIET_PHIEU_NHAP', NOW()),
    ('Tạo CTPN',                   '/api/v1/chi-tiet-phieu-nhap',                              'POST',   'CHI_TIET_PHIEU_NHAP', NOW()),
    ('Cập nhật CTPN',              '/api/v1/chi-tiet-phieu-nhap',                              'PUT',    'CHI_TIET_PHIEU_NHAP', NOW()),
    ('Xóa CTPN',                   '/api/v1/chi-tiet-phieu-nhap/{id}',                         'DELETE', 'CHI_TIET_PHIEU_NHAP', NOW());

-- ---------------------------------------------------------
-- 3. PERMISSION_ROLE
-- ---------------------------------------------------------

-- ADMIN (role_id=1): TẤT CẢ QUYỀN (1-76)
INSERT INTO permission_role (role_id, permission_id) VALUES
    (1,1),(1,2),(1,3),(1,4),(1,5),
    (1,6),(1,7),(1,8),(1,9),(1,10),
    (1,11),(1,12),(1,13),(1,14),(1,15),
    (1,16),(1,17),(1,18),(1,19),(1,20),
    (1,21),(1,22),(1,23),(1,24),(1,25),
    (1,26),(1,27),(1,28),(1,29),(1,30),
    (1,31),(1,32),(1,33),(1,34),(1,35),
    (1,36),(1,37),(1,38),(1,39),(1,40),
    (1,41),(1,42),(1,43),(1,44),(1,45),
    (1,46),(1,47),(1,48),(1,49),(1,50),
    (1,51),(1,52),(1,53),(1,54),(1,55),
    (1,56),(1,57),(1,58),(1,59),(1,60),
    (1,61),(1,62),(1,63),(1,64),(1,65),
    (1,66),(1,67),(1,68),(1,69),(1,70),
    (1,71),(1,72),(1,73),(1,74),(1,75),(1,76);

-- NHAN_VIEN (role_id=3): Xem tất cả danh mục, SP, CTSP, hình ảnh, cửa hàng (chỉ GET)
INSERT INTO permission_role (role_id, permission_id) VALUES
    (3,1),(3,2),           -- SAN_PHAM: xem all, xem id
    (3,6),(3,7),           -- MAU_SAC: xem all, xem id
    (3,11),(3,12),         -- KICH_THUOC: xem all, xem id
    (3,16),(3,17),(3,18),  -- CHI_TIET_SP: xem all, xem id, xem theo SP
    (3,25),(3,26),         -- KIEU_SAN_PHAM: xem all, xem id
    (3,30),(3,31),         -- BO_SUU_TAP: xem all, xem id
    (3,35),(3,36),         -- THUONG_HIEU: xem all, xem id
    (3,40),(3,41),(3,42),  -- HINH_ANH: xem all, xem id, xem theo CTSP
    (3,46),(3,47);         -- CUA_HANG: xem all, xem id

-- KHACH_HANG (role_id=4): Xem SP/danh mục + giỏ hàng (thêm/xem/xóa)
INSERT INTO permission_role (role_id, permission_id) VALUES
    (4,1),(4,2),           -- SAN_PHAM: xem all, xem id
    (4,6),(4,7),           -- MAU_SAC: xem all, xem id
    (4,11),(4,12),         -- KICH_THUOC: xem all, xem id
    (4,16),(4,17),(4,18),  -- CHI_TIET_SP: xem all, xem id, xem theo SP
    (4,22),(4,23),(4,24),  -- GIO_HANG: thêm SP, xem theo KH, xóa
    (4,25),(4,26),         -- KIEU_SAN_PHAM: xem all, xem id
    (4,30),(4,31),         -- BO_SUU_TAP: xem all, xem id
    (4,35),(4,36),         -- THUONG_HIEU: xem all, xem id
    (4,40),(4,41),(4,42),  -- HINH_ANH: xem all, xem id, xem theo CTSP
    (4,46),(4,47);         -- CUA_HANG: xem all, xem id

-- ---------------------------------------------------------
-- 4. CỬA HÀNG
-- ---------------------------------------------------------
INSERT INTO CuaHang (TenCuaHang, DiaChi, ViTri, SoDienThoai, Email, TrangThai) VALUES
    ('Chi nhánh Quận 1',  '123 Nguyễn Huệ, Q.1, TP.HCM',   '10.7769,106.7009', '02812345678', 'q1@shop.com', 1),
    ('Chi nhánh Quận 3',  '456 Võ Văn Tần, Q.3, TP.HCM',    '10.7756,106.6873', '02812345679', 'q3@shop.com', 1),
    ('Chi nhánh Gò Vấp',  '789 Phan Văn Trị, Gò Vấp, HCM', '10.8384,106.6498', '02812345680', 'gv@shop.com', 1);

-- ---------------------------------------------------------
-- 5. KIỂU SẢN PHẨM
-- ---------------------------------------------------------
INSERT INTO KieuSanPham (TenKieuSanPham, NgayTao) VALUES
    ('Áo',      NOW()),
    ('Quần',    NOW()),
    ('Váy',     NOW()),
    ('Phụ kiện', NOW());

-- ---------------------------------------------------------
-- 6. BỘ SƯU TẬP
-- ---------------------------------------------------------
INSERT INTO BoSuuTap (TenSuuTap, MoTa, NgayTao) VALUES
    ('Xuân Hè 2026',  'Bộ sưu tập mùa xuân hè',  NOW()),
    ('Thu Đông 2026',  'Bộ sưu tập mùa thu đông', NOW()),
    ('Basic',          'Dòng sản phẩm cơ bản',     NOW());

-- ---------------------------------------------------------
-- 7. THƯƠNG HIỆU
-- ---------------------------------------------------------
INSERT INTO ThuongHieu (TenThuongHieu, TrangThaiHoatDong, TrangThaiHienThi, NgayTao) VALUES
    ('Shop House',   1, 1, NOW()),
    ('Urban Style',  1, 1, NOW()),
    ('Classic Wear', 1, 0, NOW());

-- ---------------------------------------------------------
-- 8. MÀU SẮC
-- ---------------------------------------------------------
INSERT INTO MauSac (TenMauSac, NgayTao) VALUES
    ('Trắng', NOW()),
    ('Đen',   NOW()),
    ('Đỏ',    NOW()),
    ('Xanh',  NOW()),
    ('Vàng',  NOW());

-- ---------------------------------------------------------
-- 9. KÍCH THƯỚC
-- ---------------------------------------------------------
INSERT INTO KichThuoc (TenKichThuoc, NgayTao) VALUES
    ('S',   NOW()),
    ('M',   NOW()),
    ('L',   NOW()),
    ('XL',  NOW()),
    ('XXL', NOW());

-- ---------------------------------------------------------
-- 10. SẢN PHẨM (có FK kiểu SP, BST, thương hiệu)
-- ---------------------------------------------------------
INSERT INTO SanPham (MaKieuSanPham, MaSuuTap, MaThuongHieu, TenSanPham, GiaVon, GiaBan, GiaGiam, TrangThai, NgayTao) VALUES
    (1, 3, 1, 'Áo Oxford',     100, 200, 0,  1, NOW()),   -- id=1
    (2, 3, 1, 'Quần Jean',     200, 400, 10, 1, NOW()),   -- id=2
    (3, 1, 2, 'Váy Hoa',       150, 300, 5,  1, NOW()),   -- id=3
    (4, 3, 1, 'Nịt Da',         50, 100, 0,  1, NOW()),   -- id=4
    (1, 2, 2, 'Áo Phao',       300, 600, 15, 0, NOW());   -- id=5

-- ---------------------------------------------------------
-- 11. CHI TIẾT SẢN PHẨM
-- ---------------------------------------------------------
INSERT INTO ChiTietSanPham (MaSanPham, MaKichThuoc, MaMauSac, MaCuaHang, TrangThai, NgayTao) VALUES
    (1, 2, 1, 1, 1, NOW()),   -- id=1: Áo Oxford / M / Trắng / CN Q.1
    (1, 3, 1, 1, 1, NOW()),   -- id=2: Áo Oxford / L / Trắng / CN Q.1
    (2, 2, 2, 1, 1, NOW()),   -- id=3: Quần Jean / M / Đen / CN Q.1
    (3, 1, 3, 2, 1, NOW()),   -- id=4: Váy Hoa   / S / Đỏ / CN Q.3
    (4, 3, 2, 2, 1, NOW());   -- id=5: Nịt Da    / L / Đen / CN Q.3

-- ---------------------------------------------------------
-- 12. HÌNH ẢNH
-- ---------------------------------------------------------
INSERT INTO HinhAnh (MaChiTietSanPham, TenHinhAnh, NgayTao) VALUES
    (1, 'ao-oxford-trang-m-1.jpg',  NOW()),
    (1, 'ao-oxford-trang-m-2.jpg',  NOW()),
    (2, 'ao-oxford-trang-l-1.jpg',  NOW()),
    (3, 'quan-jean-den-m-1.jpg',    NOW()),
    (4, 'vay-hoa-do-s-1.jpg',       NOW());

-- ---------------------------------------------------------
-- 13. NHÂN VIÊN (MatKhau = BCrypt '123456')
-- ---------------------------------------------------------
INSERT INTO NhanVien (MaCuaHang, role_id, TenNhanVien, Email, SoDienThoai, MatKhau, TrangThai) VALUES
    (1, 2, 'An',   'an@s.com', '0901000001', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 1),
    (1, 3, 'Bình', 'b@s.com',  '0901000002', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 1),
    (2, 3, 'Chi',  'c@s.com',  '0901000003', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 1),
    (2, 2, 'Danh', 'd@s.com',  '0901000004', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 1),
    (1, 1, 'Hùng', 'h@s.com',  '0901000005', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 1);

-- ---------------------------------------------------------
-- 14. KHÁCH HÀNG (Password = BCrypt '123456')
-- ---------------------------------------------------------
INSERT INTO KhachHang (role_id, TenKhachHang, Email, Sdt, Password, DiemTichLuy) VALUES
    (4, 'Lan',  'lan@g.com', '0911000001', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',  10),
    (4, 'Minh', 'm@g.com',   '0922000002', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',   0),
    (4, 'Hoa',  'h@g.com',   '0933000003', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5', 100),
    (4, 'Tuấn', 't@g.com',   '0944000004', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',   5),
    (4, 'Yến',  'y@g.com',   '0955000005', '$2a$10$dXJ3SW6G7P50lGEheIjLOOvTnLJpv0r4a6Oq5a7n5g5z5z5z5z5z5',  50);

-- ---------------------------------------------------------
-- 15. NHÀ CUNG CẤP
-- ---------------------------------------------------------
INSERT INTO NhaCungCap (TenNhaCungCap, SoDienThoai, Email, DiaChi, TrangThai, NgayTao) VALUES
    ('Công ty TNHH Vải Việt',   '02838001001', 'vaiviet@ncc.com',   '100 Lý Thường Kiệt, Q.10, HCM', 1, NOW()),
    ('Công ty CP May Sài Gòn',  '02838002002', 'maysaigon@ncc.com', '200 Cách Mạng Tháng 8, Q.3, HCM', 1, NOW()),
    ('Xưởng may Hà Nội',        '02438003003', 'mayhanoi@ncc.com',  '50 Hàng Bông, Hoàn Kiếm, HN', 1, NOW());

-- ---------------------------------------------------------
-- 16. PHIẾU NHẬP
-- ---------------------------------------------------------
INSERT INTO PhieuNhap (MaCuaHang, MaNhaCungCap, TenPhieuNhap, TrangThai, NgayTao) VALUES
    (1, 1, 'Nhập hàng đợt 1 - CN Q.1',  1, NOW()),   -- id=1
    (2, 2, 'Nhập hàng đợt 1 - CN Q.3',  1, NOW()),   -- id=2
    (1, 3, 'Nhập bổ sung áo phao',       0, NOW());   -- id=3

-- ---------------------------------------------------------
-- 17. CHI TIẾT PHIẾU NHẬP
-- ---------------------------------------------------------
INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaChiTietSanPham, SoLuong, TrangThai, NgayTao) VALUES
    (1, 1, 50, 1, NOW()),   -- Phiếu 1: Áo Oxford M Trắng x50
    (1, 2, 30, 1, NOW()),   -- Phiếu 1: Áo Oxford L Trắng x30
    (1, 3, 40, 1, NOW()),   -- Phiếu 1: Quần Jean M Đen x40
    (2, 4, 25, 1, NOW()),   -- Phiếu 2: Váy Hoa S Đỏ x25
    (2, 5, 20, 1, NOW());   -- Phiếu 2: Nịt Da L Đen x20
