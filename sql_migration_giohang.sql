-- =============================================================
-- MIGRATION: Sửa apiPath trong permissions cho khớp với controller
-- Chạy file này RIÊNG trên database đã có dữ liệu
-- =============================================================

USE shopping;

-- ---------------------------------------------------------
-- Sửa GioHang permissions (id=5,6,7)
-- ---------------------------------------------------------
UPDATE permissions SET
    apiPath = '/api/v1/gio-hang/khach-hang/{khachHangId}'
WHERE id = 6;

UPDATE permissions SET
    apiPath = '/api/v1/gio-hang/chi-tiet/{maChiTietGioHang}'
WHERE id = 7;

-- ---------------------------------------------------------
-- Sửa ChiTietSanPham permission (id=21)
-- ---------------------------------------------------------
UPDATE permissions SET
    apiPath = '/api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}'
WHERE id = 21;

-- ---------------------------------------------------------
-- Gán quyền giỏ hàng cho KHACH_HANG (role_id=4) nếu chưa có
-- ---------------------------------------------------------
INSERT IGNORE INTO permission_role (role_id, permission_id) VALUES (4, 5);
INSERT IGNORE INTO permission_role (role_id, permission_id) VALUES (4, 6);
INSERT IGNORE INTO permission_role (role_id, permission_id) VALUES (4, 7);

-- ---------------------------------------------------------
-- Kiểm tra kết quả
-- ---------------------------------------------------------
SELECT p.id, p.name, p.apiPath, p.method
FROM permissions p
WHERE p.module = 'GIO_HANG'
ORDER BY p.id;
