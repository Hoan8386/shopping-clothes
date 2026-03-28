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
        ('NHAN_VIEN',  'Nhân viên bán hàng',        TRUE, NOW()),
        ('KHACH_HANG', 'Khách hàng mua sắm',        TRUE, NOW());

    -- ---------------------------------------------------------
    -- 2. PERMISSIONS (141 quyền cho tất cả endpoint)
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

        -- === CHI_TIET_SP (16-21,119) ===
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
        ('Xóa CTPN',                   '/api/v1/chi-tiet-phieu-nhap/{id}',                         'DELETE', 'CHI_TIET_PHIEU_NHAP', NOW()),

        -- === DON_HANG (77-82) ===
        ('Xem tất cả đơn hàng',        '/api/v1/don-hang',                                         'GET',    'DON_HANG',            NOW()),
        ('Xem chi tiết đơn hàng',      '/api/v1/don-hang/{id}',                                    'GET',    'DON_HANG',            NOW()),
        ('Tạo đơn hàng online',        '/api/v1/don-hang/online',                                  'POST',   'DON_HANG',            NOW()),
        ('Tạo đơn hàng tại quầy',      '/api/v1/don-hang/tai-quay',                                'POST',   'DON_HANG',            NOW()),
        ('Cập nhật đơn hàng',          '/api/v1/don-hang',                                         'PUT',    'DON_HANG',            NOW()),
        ('Xóa đơn hàng',               '/api/v1/don-hang/{id}',                                    'DELETE', 'DON_HANG',            NOW()),

        -- === CHI_TIET_DON_HANG (83-88) ===
        ('Xem tất cả CTDH',            '/api/v1/chi-tiet-don-hang',                                'GET',    'CHI_TIET_DON_HANG',   NOW()),
        ('Xem CTDH theo mã đơn',       '/api/v1/chi-tiet-don-hang/don-hang/{donHangId}',           'GET',    'CHI_TIET_DON_HANG',   NOW()),
        ('Xem CTDH theo id',           '/api/v1/chi-tiet-don-hang/{id}',                           'GET',    'CHI_TIET_DON_HANG',   NOW()),
        ('Tạo CTDH',                   '/api/v1/chi-tiet-don-hang',                                'POST',   'CHI_TIET_DON_HANG',   NOW()),
        ('Cập nhật CTDH',              '/api/v1/chi-tiet-don-hang',                                'PUT',    'CHI_TIET_DON_HANG',   NOW()),
        ('Xóa CTDH',                   '/api/v1/chi-tiet-don-hang/{id}',                           'DELETE', 'CHI_TIET_DON_HANG',   NOW()),

        -- === KHUYEN_MAI_THEO_HOA_DON (89-93) ===
        ('Xem tất cả KMHD',            '/api/v1/khuyen-mai-theo-hoa-don',                          'GET',    'KHUYEN_MAI_HOA_DON',  NOW()),
        ('Xem KMHD theo id',           '/api/v1/khuyen-mai-theo-hoa-don/{id}',                     'GET',    'KHUYEN_MAI_HOA_DON',  NOW()),
        ('Tạo KMHD',                   '/api/v1/khuyen-mai-theo-hoa-don',                          'POST',   'KHUYEN_MAI_HOA_DON',  NOW()),
        ('Cập nhật KMHD',              '/api/v1/khuyen-mai-theo-hoa-don',                          'PUT',    'KHUYEN_MAI_HOA_DON',  NOW()),
        ('Xóa KMHD',                   '/api/v1/khuyen-mai-theo-hoa-don/{id}',                     'DELETE', 'KHUYEN_MAI_HOA_DON',  NOW()),

        -- === KHUYEN_MAI_THEO_DIEM (94-98) ===
        ('Xem tất cả KMD',             '/api/v1/khuyen-mai-theo-diem',                             'GET',    'KHUYEN_MAI_DIEM',     NOW()),
        ('Xem KMD theo id',            '/api/v1/khuyen-mai-theo-diem/{id}',                        'GET',    'KHUYEN_MAI_DIEM',     NOW()),
        ('Tạo KMD',                    '/api/v1/khuyen-mai-theo-diem',                             'POST',   'KHUYEN_MAI_DIEM',     NOW()),
        ('Cập nhật KMD',               '/api/v1/khuyen-mai-theo-diem',                             'PUT',    'KHUYEN_MAI_DIEM',     NOW()),
        ('Xóa KMD',                    '/api/v1/khuyen-mai-theo-diem/{id}',                        'DELETE', 'KHUYEN_MAI_DIEM',     NOW()),

        -- === DANH_GIA_SAN_PHAM (99-104) ===
        ('Xem tất cả đánh giá SP',     '/api/v1/danh-gia-san-pham',                                'GET',    'DANH_GIA_SP',         NOW()),
        ('Xem đánh giá SP theo id',     '/api/v1/danh-gia-san-pham/{id}',                           'GET',    'DANH_GIA_SP',         NOW()),
        ('Xem đánh giá theo sản phẩm',  '/api/v1/danh-gia-san-pham/san-pham/{sanPhamId}',           'GET',    'DANH_GIA_SP',         NOW()),
        ('Xem đánh giá của tôi',        '/api/v1/danh-gia-san-pham/cua-toi',                        'GET',    'DANH_GIA_SP',         NOW()),
        ('Tạo đánh giá SP',             '/api/v1/danh-gia-san-pham',                                'POST',   'DANH_GIA_SP',         NOW()),
        ('Xóa đánh giá SP',             '/api/v1/danh-gia-san-pham/{id}',                           'DELETE', 'DANH_GIA_SP',         NOW()),

        -- === PHIEU_NHAP_KIEM_KE (105) ===
        ('Kiểm kê phiếu nhập',           '/api/v1/phieu-nhap/kiem-ke/{id}',                          'PUT',    'PHIEU_NHAP',          NOW()),

        -- === DANH_GIA_SP_UPDATE (106-107) ===
        ('Cập nhật đánh giá SP',        '/api/v1/danh-gia-san-pham/{id}',                           'PUT',    'DANH_GIA_SP',         NOW()),
        ('Xem đánh giá theo CTDH',      '/api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}', 'GET', 'DANH_GIA_SP',    NOW()),

        -- === GIO_HANG_KHUYEN_MAI (108-109) ===
        ('Xem khuyến mãi hợp lệ giỏ hàng', '/api/v1/gio-hang/khuyen-mai-hop-le',                    'GET',    'GIO_HANG',            NOW()),
        ('Xem trước áp dụng khuyến mãi',   '/api/v1/gio-hang/ap-dung-khuyen-mai',                   'POST',   'GIO_HANG',            NOW()),

        -- === NHAN_VIEN (110-113) ===
        ('Xem tất cả nhân viên',           '/api/v1/nhan-vien',                                     'GET',    'NHAN_VIEN',           NOW()),
        ('Tạo nhân viên',                  '/api/v1/nhan-vien',                                     'POST',   'NHAN_VIEN',           NOW()),
        ('Cập nhật nhân viên',             '/api/v1/nhan-vien',                                     'PUT',    'NHAN_VIEN',           NOW()),
        ('Xóa nhân viên',                  '/api/v1/nhan-vien/{id}',                                'DELETE', 'NHAN_VIEN',           NOW()),

        -- === TRA_HANG (114-118) ===
        ('Tạo phiếu trả hàng',             '/api/v1/tra-hang',                                      'POST',   'TRA_HANG',            NOW()),
        ('Xem tất cả phiếu trả hàng',      '/api/v1/tra-hang',                                      'GET',    'TRA_HANG',            NOW()),
        ('Xem phiếu trả hàng theo mã',     '/api/v1/tra-hang/{id}',                                 'GET',    'TRA_HANG',            NOW()),
        ('Xem phiếu trả theo đơn hàng',    '/api/v1/tra-hang/don-hang/{donHangId}',                 'GET',    'TRA_HANG',            NOW()),
        ('Cập nhật trạng thái trả hàng',   '/api/v1/tra-hang/{id}/trang-thai',                      'PUT',    'TRA_HANG',            NOW()),

        -- === CHI_TIET_SP_THEO_CUA_HANG (119) ===
        ('Xem CTSP tại cửa hàng nhân viên', '/api/v1/chi-tiet-san-pham/san-pham-tai-cua-hang',      'GET',    'CHI_TIET_SP',         NOW()),

        -- === LOAI_DON_LUAN_CHUYEN (120-124) ===
        ('Xem tất cả loại đơn luân chuyển',   '/api/v1/loai-don-luan-chuyen',        'GET',    'LOAI_DON_LUAN_CHUYEN', NOW()),
        ('Xem loại đơn luân chuyển theo id',   '/api/v1/loai-don-luan-chuyen/{id}',   'GET',    'LOAI_DON_LUAN_CHUYEN', NOW()),
        ('Tạo loại đơn luân chuyển',           '/api/v1/loai-don-luan-chuyen',        'POST',   'LOAI_DON_LUAN_CHUYEN', NOW()),
        ('Cập nhật loại đơn luân chuyển',      '/api/v1/loai-don-luan-chuyen',        'PUT',    'LOAI_DON_LUAN_CHUYEN', NOW()),
        ('Xóa loại đơn luân chuyển',           '/api/v1/loai-don-luan-chuyen/{id}',   'DELETE', 'LOAI_DON_LUAN_CHUYEN', NOW()),

        -- === DON_LUAN_CHUYEN (125-130) ===
        ('Tạo đơn luân chuyển',                       '/api/v1/don-luan-chuyen',                          'POST',   'DON_LUAN_CHUYEN', NOW()),
        ('Xem tất cả đơn luân chuyển',                '/api/v1/don-luan-chuyen',                          'GET',    'DON_LUAN_CHUYEN', NOW()),
        ('Xem đơn luân chuyển theo mã',               '/api/v1/don-luan-chuyen/{id}',                     'GET',    'DON_LUAN_CHUYEN', NOW()),
        ('Xem đơn luân chuyển theo cửa hàng đặt',     '/api/v1/don-luan-chuyen/cua-hang-dat/{cuaHangId}', 'GET',    'DON_LUAN_CHUYEN', NOW()),
        ('Xem đơn luân chuyển theo cửa hàng gửi',     '/api/v1/don-luan-chuyen/cua-hang-gui/{cuaHangId}', 'GET',    'DON_LUAN_CHUYEN', NOW()),
        ('Cập nhật trạng thái đơn luân chuyển',       '/api/v1/don-luan-chuyen/{id}/trang-thai',          'PUT',    'DON_LUAN_CHUYEN', NOW()),

        -- === LOAI_KIEM_KE (131-135) ===
        ('Xem tất cả loại kiểm kê',                   '/api/v1/loai-kiem-ke',                              'GET',    'LOAI_KIEM_KE',     NOW()),
        ('Xem loại kiểm kê theo id',                  '/api/v1/loai-kiem-ke/{id}',                         'GET',    'LOAI_KIEM_KE',     NOW()),
        ('Tạo loại kiểm kê',                          '/api/v1/loai-kiem-ke',                              'POST',   'LOAI_KIEM_KE',     NOW()),
        ('Cập nhật loại kiểm kê',                     '/api/v1/loai-kiem-ke',                              'PUT',    'LOAI_KIEM_KE',     NOW()),
        ('Xóa loại kiểm kê',                          '/api/v1/loai-kiem-ke/{id}',                         'DELETE', 'LOAI_KIEM_KE',     NOW()),

        -- === KIEM_KE_HANG_HOA (136-141) ===
        ('Xem danh sách phiếu kiểm kê',               '/api/v1/kiem-ke-hang-hoa',                          'GET',    'KIEM_KE_HANG_HOA', NOW()),
        ('Xem phiếu kiểm kê theo id',                 '/api/v1/kiem-ke-hang-hoa/{id}',                     'GET',    'KIEM_KE_HANG_HOA', NOW()),
        ('Tạo phiếu kiểm kê',                         '/api/v1/kiem-ke-hang-hoa',                          'POST',   'KIEM_KE_HANG_HOA', NOW()),
        ('Cập nhật phiếu kiểm kê',                    '/api/v1/kiem-ke-hang-hoa',                          'PUT',    'KIEM_KE_HANG_HOA', NOW()),
        ('Gửi duyệt phiếu kiểm kê',                   '/api/v1/kiem-ke-hang-hoa/{id}/gui-duyet',           'PUT',    'KIEM_KE_HANG_HOA', NOW()),
        ('Duyệt phiếu kiểm kê',                       '/api/v1/kiem-ke-hang-hoa/{id}/duyet',               'PUT',    'KIEM_KE_HANG_HOA', NOW()),

        -- === CA_LAM_VIEC (142-146) ===
        ('Xem tất cả ca làm việc',     '/api/v1/ca-lam-viec',          'GET',    'CA_LAM_VIEC',    NOW()),
        ('Xem ca làm việc theo id',    '/api/v1/ca-lam-viec/{id}',     'GET',    'CA_LAM_VIEC',    NOW()),
        ('Tạo ca làm việc',            '/api/v1/ca-lam-viec',          'POST',   'CA_LAM_VIEC',    NOW()),
        ('Cập nhật ca làm việc',       '/api/v1/ca-lam-viec',          'PUT',    'CA_LAM_VIEC',    NOW()),
        ('Xóa ca làm việc',            '/api/v1/ca-lam-viec/{id}',     'DELETE', 'CA_LAM_VIEC',    NOW()),

        -- === LICH_LAM_VIEC (147-154) ===
        ('Xem tất cả lịch làm việc',          '/api/v1/lich-lam-viec',                       'GET',    'LICH_LAM_VIEC', NOW()),
        ('Xem lịch làm việc theo id',         '/api/v1/lich-lam-viec/{id}',                  'GET',    'LICH_LAM_VIEC', NOW()),
        ('Xem lịch làm việc theo nhân viên',  '/api/v1/lich-lam-viec/nhan-vien/{nhanVienId}','GET',    'LICH_LAM_VIEC', NOW()),
        ('Tạo lịch làm việc',                 '/api/v1/lich-lam-viec',                       'POST',   'LICH_LAM_VIEC', NOW()),
        ('Cập nhật lịch làm việc',            '/api/v1/lich-lam-viec',                       'PUT',    'LICH_LAM_VIEC', NOW()),
        ('Xóa lịch làm việc',                 '/api/v1/lich-lam-viec/{id}',                  'DELETE', 'LICH_LAM_VIEC', NOW()),
        ('Import lịch làm việc từ Excel',     '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}/import',                'POST',   'LICH_LAM_VIEC', NOW()),
        ('Tải file Excel mẫu lịch làm việc',  '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}/download-template',     'GET',    'LICH_LAM_VIEC', NOW()),

        -- === CHI_TIET_LICH_LAM (155-160) ===
        ('Xem tất cả chi tiết lịch làm',         '/api/v1/chi-tiet-lich-lam',                                  'GET',    'CHI_TIET_LICH_LAM', NOW()),
        ('Xem chi tiết lịch làm theo id',         '/api/v1/chi-tiet-lich-lam/{id}',                             'GET',    'CHI_TIET_LICH_LAM', NOW()),
        ('Xem chi tiết lịch làm theo lịch',       '/api/v1/chi-tiet-lich-lam/lich-lam-viec/{lichLamViecId}',   'GET',    'CHI_TIET_LICH_LAM', NOW()),
        ('Tạo chi tiết lịch làm',                 '/api/v1/chi-tiet-lich-lam',                                  'POST',   'CHI_TIET_LICH_LAM', NOW()),
        ('Cập nhật chi tiết lịch làm',            '/api/v1/chi-tiet-lich-lam',                                  'PUT',    'CHI_TIET_LICH_LAM', NOW()),
        ('Xóa chi tiết lịch làm',                 '/api/v1/chi-tiet-lich-lam/{id}',                             'DELETE', 'CHI_TIET_LICH_LAM', NOW()),

        -- === LUONG_CO_BAN (161-165) ===
        ('Xem tất cả lương cơ bản',       '/api/v1/luong-co-ban',                         'GET',    'LUONG_CO_BAN', NOW()),
        ('Xem lương cơ bản theo id',      '/api/v1/luong-co-ban/{id}',                    'GET',    'LUONG_CO_BAN', NOW()),
        ('Xem lương cơ bản theo NV',      '/api/v1/luong-co-ban/nhan-vien/{nhanVienId}',  'GET',    'LUONG_CO_BAN', NOW()),
        ('Tạo lương cơ bản',              '/api/v1/luong-co-ban',                         'POST',   'LUONG_CO_BAN', NOW()),
        ('Cập nhật lương cơ bản',         '/api/v1/luong-co-ban',                         'PUT',    'LUONG_CO_BAN', NOW()),

        -- === LUONG_THUONG (166-170) ===
        ('Xem tất cả lương thưởng',       '/api/v1/luong-thuong',                         'GET',    'LUONG_THUONG', NOW()),
        ('Xem lương thưởng theo id',      '/api/v1/luong-thuong/{id}',                    'GET',    'LUONG_THUONG', NOW()),
        ('Xem lương thưởng theo NV',      '/api/v1/luong-thuong/nhan-vien/{nhanVienId}',  'GET',    'LUONG_THUONG', NOW()),
        ('Tạo lương thưởng',              '/api/v1/luong-thuong',                         'POST',   'LUONG_THUONG', NOW()),
        ('Cập nhật lương thưởng',         '/api/v1/luong-thuong',                         'PUT',    'LUONG_THUONG', NOW()),

        -- === DOI_CA (171-175) ===
        ('Xem tất cả đổi ca',             '/api/v1/doi-ca',                                 'GET',    'DOI_CA', NOW()),
        ('Xem đổi ca theo id',            '/api/v1/doi-ca/{id}',                            'GET',    'DOI_CA', NOW()),
        ('Xem đổi ca theo lịch',          '/api/v1/doi-ca/lich-lam-viec/{lichLamViecId}',  'GET',    'DOI_CA', NOW()),
        ('Tạo đổi ca',                    '/api/v1/doi-ca',                                 'POST',   'DOI_CA', NOW()),
        ('Cập nhật đổi ca',               '/api/v1/doi-ca',                                 'PUT',    'DOI_CA', NOW()),

        -- === LOI_PHAT_SINH (176-181) ===
        ('Xem tất cả lỗi phát sinh',       '/api/v1/loi-phat-sinh',                                  'GET',    'LOI_PHAT_SINH', NOW()),
        ('Xem lỗi phát sinh theo id',      '/api/v1/loi-phat-sinh/{id}',                             'GET',    'LOI_PHAT_SINH', NOW()),
        ('Xem lỗi phát sinh theo lịch',    '/api/v1/loi-phat-sinh/lich-lam-viec/{lichLamViecId}',    'GET',    'LOI_PHAT_SINH', NOW()),
        ('Tạo lỗi phát sinh',              '/api/v1/loi-phat-sinh',                                  'POST',   'LOI_PHAT_SINH', NOW()),
        ('Cập nhật lỗi phát sinh',         '/api/v1/loi-phat-sinh',                                  'PUT',    'LOI_PHAT_SINH', NOW()),
        ('Xóa lỗi phát sinh',              '/api/v1/loi-phat-sinh/{id}',                             'DELETE', 'LOI_PHAT_SINH', NOW()),

        -- === LICH_LAM_VIEC_EXTRA (182-186) ===
        ('Xem lịch làm việc theo cửa hàng', '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}',        'GET',    'LICH_LAM_VIEC', NOW()),
        ('Xem lịch làm việc theo tháng',    '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}/thang', 'GET',    'LICH_LAM_VIEC', NOW()),
        ('Cập nhật trạng thái ngày làm việc', '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}/ngay/trang-thai', 'PUT', 'LICH_LAM_VIEC', NOW()),
        ('Thêm nhân viên vào ca', '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}/ngay/ca-lam-viec', 'POST', 'LICH_LAM_VIEC', NOW()),
        ('Xóa nhân viên khỏi ca', '/api/v1/lich-lam-viec/cua-hang/{cuaHangId}/ngay/ca-lam-viec', 'DELETE', 'LICH_LAM_VIEC', NOW()),
        ('Tạo phiếu đổi hàng',             '/api/v1/doi-hang',                                      'POST',   'DOI_HANG',            NOW()),
        ('Xem tất cả phiếu đổi hàng',      '/api/v1/doi-hang',                                      'GET',    'DOI_HANG',            NOW()),
        ('Xem phiếu đổi hàng theo mã',     '/api/v1/doi-hang/{id}',                                 'GET',    'DOI_HANG',            NOW()),
        ('Xem phiếu đổi theo đơn hàng',    '/api/v1/doi-hang/don-hang/{donHangId}',                 'GET',    'DOI_HANG',            NOW()),
        ('Cập nhật trạng thái đổi hàng',   '/api/v1/doi-hang/{id}/trang-thai',                      'PUT',    'DOI_HANG',            NOW()),

        -- === LOI_PHAT_SINH_EXTRA (192-193) ===
        ('Upload ảnh lỗi phát sinh',       '/api/v1/loi-phat-sinh/upload-image',                    'POST',   'LOI_PHAT_SINH',       NOW()),
        ('Xem lỗi phát sinh theo cửa hàng', '/api/v1/loi-phat-sinh/cua-hang/{cuaHangId}',           'GET',    'LOI_PHAT_SINH',       NOW());

    -- ---------------------------------------------------------
    -- 3. PERMISSION_ROLE
    -- ---------------------------------------------------------

    -- ADMIN (role_id=1): TẤT CẢ QUYỀN (1-181)
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
        (1,71),(1,72),(1,73),(1,74),(1,75),(1,76),
        (1,77),(1,78),(1,79),(1,80),(1,81),(1,82),
        (1,83),(1,84),(1,85),(1,86),(1,87),(1,88),
        (1,89),(1,90),(1,91),(1,92),(1,93),
        (1,94),(1,95),(1,96),(1,97),(1,98),
        (1,99),(1,100),(1,101),(1,102),(1,103),(1,104),
        (1,105),(1,106),(1,107),(1,108),(1,109),
        (1,110),(1,111),(1,112),(1,113),
        (1,114),(1,115),(1,116),(1,117),(1,118),(1,119),
        (1,120),(1,121),(1,122),(1,123),(1,124),
        (1,125),(1,126),(1,127),(1,128),(1,129),(1,130),(1,131),
        (1,132),(1,133),(1,134),(1,135),(1,136),
        (1,137),(1,138),(1,139),(1,140),(1,141),
        -- CA_LAM_VIEC (142-146)
        (1,142),(1,143),(1,144),(1,145),(1,146),
        -- LICH_LAM_VIEC (147-154)
        (1,147),(1,148),(1,149),(1,150),(1,151),(1,152),(1,153),(1,154),
        -- CHI_TIET_LICH_LAM (155-160)
        (1,155),(1,156),(1,157),(1,158),(1,159),(1,160),
        -- LUONG_CO_BAN (161-165)
        (1,161),(1,162),(1,163),(1,164),(1,165),
        -- LUONG_THUONG (166-170)
        (1,166),(1,167),(1,168),(1,169),(1,170),
        -- DOI_CA (171-175)
        (1,171),(1,172),(1,173),(1,174),(1,175),
        -- LOI_PHAT_SINH (176-181)
        (1,176),(1,177),(1,178),(1,179),(1,180),(1,181),
        -- LICH_LAM_VIEC_EXTRA (182-186)
        (1,182),(1,183),(1,184),(1,185),(1,186),
        -- DOI_HANG (187-191)
        (1,187),(1,188),(1,189),(1,190),(1,191),
        -- LOI_PHAT_SINH_EXTRA (192-193)
        (1,192),(1,193);

    -- NHAN_VIEN (role_id=2): Xem tất cả danh mục, SP, CTSP, hình ảnh, cửa hàng (chỉ GET) + Phiếu nhập + Đơn hàng + Ca & Lịch làm
    INSERT INTO permission_role (role_id, permission_id) VALUES
        (2,1),(2,2),           -- SAN_PHAM: xem all, xem id
        (2,6),(2,7),           -- MAU_SAC: xem all, xem id
        (2,11),(2,12),         -- KICH_THUOC: xem all, xem id
        (2,16),(2,17),(2,18),(2,119),  -- CHI_TIET_SP: xem all, xem id, xem theo SP, xem theo cửa hàng đang đăng nhập
        (2,25),(2,26),         -- KIEU_SAN_PHAM: xem all, xem id
        (2,30),(2,31),         -- BO_SUU_TAP: xem all, xem id
        (2,35),(2,36),         -- THUONG_HIEU: xem all, xem id
        (2,40),(2,41),(2,42),  -- HINH_ANH: xem all, xem id, xem theo CTSP
        (2,46),(2,47),         -- CUA_HANG: xem all, xem id
        (2,61),(2,62),         -- NHA_CUNG_CAP: xem all, xem id
        (2,66),(2,67),(2,68),(2,69),(2,105), -- PHIEU_NHAP: xem all, xem id, tạo, cập nhật, kiểm kê
        (2,71),(2,72),(2,73),(2,74),(2,75),  -- CHI_TIET_PHIEU_NHAP: xem all, xem id, xem theo PN, tạo, cập nhật
        (2,77),(2,78),(2,80),(2,81),  -- DON_HANG: xem all, xem id, tạo tại quầy, cập nhật
        (2,83),(2,84),(2,85),  -- CHI_TIET_DON_HANG: xem all, xem theo đơn, xem id
        (2,89),(2,90),         -- KHUYEN_MAI_HOA_DON: xem all, xem id
        (2,94),(2,95),         -- KHUYEN_MAI_DIEM: xem all, xem id
        (2,99),(2,100),(2,101),(2,107), -- DANH_GIA_SP: xem all, xem id, xem theo SP, xem theo CTDH
        (2,110),          -- NHAN_VIEN: xem danh sách nhân viên (cùng cửa hàng)
        (2,115),(2,116),(2,117),(2,118), -- TRA_HANG: xem all, xem mã, xem theo đơn, cập nhật trạng thái
        (2,120),(2,121),                -- LOAI_DON_LUAN_CHUYEN: xem all, xem id
        (2,125),(2,126),(2,127),(2,128),(2,129),(2,130), -- DON_LUAN_CHUYEN: tạo, xem all, xem mã, xem theo ch đặt, xem theo ch gửi, cập nhật TT
        (2,131),(2,132),                -- LOAI_KIEM_KE: xem all, xem id
        (2,136),(2,137),(2,138),(2,139),(2,140), -- KIEM_KE_HANG_HOA: xem all, xem id, tạo, cập nhật, gửi duyệt
        -- CA_LAM_VIEC: chỉ xem
        (2,142),(2,143),
        -- LICH_LAM_VIEC: xem all, xem id, xem theo NV, tạo, xóa, import, download-template
        (2,147),(2,148),(2,149),(2,150),(2,152),(2,153),(2,154),
        -- CHI_TIET_LICH_LAM: xem all, xem id, xem theo lịch, tạo
        (2,155),(2,156),(2,157),(2,158),
        -- DOI_CA: xem all, xem id, xem theo lịch, tạo, cập nhật
        (2,171),(2,172),(2,173),(2,174),(2,175),
        -- LOI_PHAT_SINH: xem all, xem id, xem theo lịch, tạo
        (2,176),(2,177),(2,178),(2,179),
        -- LICH_LAM_VIEC_EXTRA
        (2,182),(2,183),(2,184),(2,185),(2,186),
        -- DOI_HANG: xem all, xem theo mã, xem theo đơn hàng, cập nhật trạng thái
        (2,188),(2,189),(2,190),(2,191),
        -- LOI_PHAT_SINH_EXTRA
        (2,192),(2,193);

    -- KHACH_HANG (role_id=3): Xem SP/danh mục + giỏ hàng (thêm/xem/xóa/khuyến mãi)
    INSERT INTO permission_role (role_id, permission_id) VALUES
        (3,1),(3,2),           -- SAN_PHAM: xem all, xem id
        (3,6),(3,7),           -- MAU_SAC: xem all, xem id
        (3,11),(3,12),         -- KICH_THUOC: xem all, xem id
        (3,16),(3,17),(3,18),  -- CHI_TIET_SP: xem all, xem id, xem theo SP
        (3,22),(3,23),(3,24),(3,108),(3,109),  -- GIO_HANG: thêm SP, xem theo KH, xóa, khuyến mãi
        (3,25),(3,26),         -- KIEU_SAN_PHAM: xem all, xem id
        (3,30),(3,31),         -- BO_SUU_TAP: xem all, xem id
        (3,35),(3,36),         -- THUONG_HIEU: xem all, xem id
        (3,40),(3,41),(3,42),  -- HINH_ANH: xem all, xem id, xem theo CTSP
        (3,46),(3,47),         -- CUA_HANG: xem all, xem id
        (3,77),(3,78),(3,79),(3,81),  -- DON_HANG: xem all, xem id, tạo online, cập nhật
        (3,83),(3,84),(3,85),  -- CHI_TIET_DON_HANG: xem all, xem theo đơn, xem id
        (3,89),(3,90),         -- KHUYEN_MAI_HOA_DON: xem all, xem id
        (3,94),(3,95),         -- KHUYEN_MAI_DIEM: xem all, xem id
        (3,99),(3,100),(3,101),(3,102),(3,103),(3,104),(3,106),(3,107), -- DANH_GIA_SP: xem all, xem id, xem theo SP, xem của tôi, tạo, xóa, cập nhật, xem theo CTDH
        (3,114),(3,116),(3,117), -- TRA_HANG: tạo phiếu, xem theo mã, xem theo đơn hàng
        (3,187),(3,189),(3,190); -- DOI_HANG: tạo phiếu, xem theo mã, xem theo đơn hàng

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
        (1, 3, 1, 'Áo Oxford',     100000, 200000, 0,  1, NOW()),   -- id=1
        (2, 3, 1, 'Quần Jean',     200000, 400000, 10, 1, NOW()),   -- id=2
        (3, 1, 2, 'Váy Hoa',       150000, 300000, 5,  1, NOW()),   -- id=3
        (4, 3, 1, 'Nịt Da',         50000, 100000, 0,  1, NOW()),   -- id=4
        (1, 2, 2, 'Áo Phao',       300000, 600000, 15, 0, NOW());   -- id=5

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
        (1, 2, 'An',   'an@s.com', '0901000001', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC', 1),
        (1, 2, 'Bình', 'b@s.com',  '0901000002', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC', 1),
        (2, 2, 'Chi',  'c@s.com',  '0901000003', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC', 1),
        (2, 2, 'Danh', 'd@s.com',  '0901000004', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC', 1),
        (1, 1, 'Hùng', 'h@s.com',  '0901000005', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC', 1);

    -- ---------------------------------------------------------
    -- 14. KHÁCH HÀNG (Password = BCrypt '123456')
    -- ---------------------------------------------------------
    INSERT INTO KhachHang (role_id, TenKhachHang, Email, Sdt, Password, DiemTichLuy) VALUES
        (3, 'Lan',  'lan@g.com', '0911000001', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC',  10),
        (3, 'Minh', 'm@g.com',   '0922000002', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC',   0),
        (3, 'Hoa',  'h@g.com',   '0933000003', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC', 100),
        (3, 'Tuấn', 't@g.com',   '0944000004', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC',   5),
        (3, 'Yến',  'y@g.com',   '0955000005', '$2a$10$iIsMPCG8iMiSnXa9UQ5jF.1uGOkLshrEI0Ymp.S.OA6iY4fGRvEmC',  50);

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
    INSERT INTO PhieuNhap (MaCuaHang, MaNhaCungCap, TenPhieuNhap, TrangThai, NgayDatHang, NgayTao) VALUES
        (1, 1, 'Nhập hàng đợt 1 - CN Q.1',  1, '2026-02-20 10:00:00', NOW()),   -- id=1
        (2, 2, 'Nhập hàng đợt 1 - CN Q.3',  1, '2026-02-22 10:00:00', NOW()),   -- id=2
        (1, 3, 'Nhập bổ sung áo phao',       0, '2026-03-01 10:00:00', NOW());   -- id=3

    -- ---------------------------------------------------------
    -- 17. CHI TIẾT PHIẾU NHẬP
    -- ---------------------------------------------------------
    INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaChiTietSanPham, SoLuong, TrangThai, NgayTao) VALUES
        (1, 1, 50, 1, NOW()),   -- Phiếu 1: Áo Oxford M Trắng x50
        (1, 2, 30, 1, NOW()),   -- Phiếu 1: Áo Oxford L Trắng x30
        (1, 3, 40, 1, NOW()),   -- Phiếu 1: Quần Jean M Đen x40
        (2, 4, 25, 1, NOW()),   -- Phiếu 2: Váy Hoa S Đỏ x25
        (2, 5, 20, 1, NOW());   -- Phiếu 2: Nịt Da L Đen x20

    -- ---------------------------------------------------------
    -- 18. KHUYẾN MÃI THEO HÓA ĐƠN
    -- ---------------------------------------------------------
    INSERT INTO KhuyenMaiTheoHoaDon (TenKhuyenMai, GiamToiDa, HoaDonToiThieu, PhanTramGiam, HinhThuc, ThoiGianBatDau, ThoiGianKetThuc, SoLuong, TrangThai, NgayTao) VALUES
        ('Giảm 10% đơn từ 500K',   100000,  500000,  10.0, 1, '2026-01-01 00:00:00', '2026-06-30 23:59:59', 100, 1, NOW()),   -- id=1
        ('Giảm 20% đơn từ 1 triệu', 300000, 1000000, 20.0, 1, '2026-02-01 00:00:00', '2026-04-30 23:59:59', 50,  1, NOW()),   -- id=2
        ('Giảm 5% tất cả đơn',      50000,  200000,   5.0, 0, '2026-03-01 00:00:00', '2026-12-31 23:59:59', 200, 1, NOW());   -- id=3

    -- ---------------------------------------------------------
    -- 19. KHUYẾN MÃI THEO ĐIỂM
    -- ---------------------------------------------------------
    INSERT INTO KhuyenMaiTheoDiem (TenKhuyenMai, DiemToiThieu, GiamToiDa, HoaDonToiThieu, PhanTramGiam, HinhThuc, ThoiGianBatDau, ThoiGianKetThuc, SoLuong, TrangThai, NgayTao) VALUES
        ('Đổi 50 điểm giảm 15%',   50,  200000,  800000,  15.0, 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 100, 1, NOW()),   -- id=1
        ('Đổi 100 điểm giảm 25%',  100, 500000,  1500000, 25.0, 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 50,  1, NOW()),   -- id=2
        ('Đổi 20 điểm giảm 5%',     20,  80000,  300000,   5.0, 0, '2026-03-01 00:00:00', '2026-06-30 23:59:59', 200, 1, NOW());   -- id=3

    -- ---------------------------------------------------------
    -- 20. ĐƠN HÀNG
    -- ---------------------------------------------------------
    INSERT INTO DonHang (MaCuaHang, MaKhachHang, MaNhanVien, MaKhuyenMaiHoaDon, MaKhuyenMaiDiem, DiaChi, Sdt, TongTien, TienGiam, TongTienGiam, TongTienTra, TrangThai, TrangThaiThanhToan, HinhThucDonHang, NgayTao) VALUES
        (1, 1, 5, NULL, NULL, '123 Nguyễn Trãi, Q.1, TP.HCM',     '0911000001', 600, 0,   0,   600, 1, 1, 1, NOW()),   -- id=1: KH Lan, NV Hùng, CN Q.1, online
        (1, 2, 2, NULL, NULL, '456 Lê Lợi, Q.3, TP.HCM',           '0922000002', 400, 40,  40,  360, 2, 1, 1, NOW()),   -- id=2: KH Minh, NV Bình, CN Q.1, online
        (2, 3, 3, NULL, NULL, 'Mua tại cửa hàng',                   '0933000003', 300, 15,  15,  285, 5, 1, 0, NOW()),   -- id=3: KH Hoa, NV Chi, CN Q.3, đã nhận hàng
        (1, 4, 5, NULL, NULL, '789 Trần Hưng Đạo, Q.5, TP.HCM',    '0944000004', 200, 0,   0,   200, 0, 0, 1, NOW()),   -- id=4: KH Tuấn, NV Hùng, chờ xác nhận
        (2, 5, 4, NULL, NULL, 'Mua tại cửa hàng',                  '0955000005', 1000, 100, 100, 900, 5, 1, 0, NOW());   -- id=5: KH Yến, NV Danh, CN Q.3, đã nhận hàng

    -- ---------------------------------------------------------
    -- 21. CHI TIẾT ĐƠN HÀNG
    -- ---------------------------------------------------------
    INSERT INTO ChiTietDonHang (MaDon, MaChiTietSanPham, GiaSanPham, GiamGia, GiaGiam, SoLuong, ThanhTien, NgayTao) VALUES
        (1, 1, 200, 0,  0,  2, 400, NOW()),   -- Đơn 1: Áo Oxford M Trắng x2 = 400
        (1, 3, 400, 10, 40, 1, 360, NOW()),   -- Đơn 1: Quần Jean M Đen x1 (giảm 10%) = 360 → Tổng đơn ≈ 600 (đã làm tròn)
        (2, 2, 200, 0,  0,  2, 400, NOW()),   -- Đơn 2: Áo Oxford L Trắng x2 = 400
        (3, 4, 300, 5,  15, 1, 285, NOW()),   -- Đơn 3: Váy Hoa S Đỏ x1 (giảm 5%) = 285
        (4, 5, 100, 0,  0,  2, 200, NOW()),   -- Đơn 4: Nịt Da L Đen x2 = 200
        (5, 1, 200, 0,  0,  3, 600, NOW()),   -- Đơn 5: Áo Oxford M Trắng x3 = 600
        (5, 3, 400, 0,  0,  1, 400, NOW());   -- Đơn 5: Quần Jean M Đen x1 = 400 → Tổng đơn = 1000

    -- ---------------------------------------------------------
    -- 22. ĐÁNH GIÁ SẢN PHẨM
    -- ---------------------------------------------------------
    INSERT INTO DanhGiaSanPham (MaKhachHang, MaChiTietDonHang, SoSao, GhiTru, HinhAnh, NgayTao) VALUES
        (3, 4, 5, 'Váy rất đẹp, đúng size, vải mát', NULL, NOW()),          -- id=1: KH Hoa, CTDH 4 (đơn 3, Váy Hoa S Đỏ)
        (5, 6, 4, 'Áo Oxford chất lượng tốt', NULL, NOW()),                  -- id=2: KH Yến, CTDH 6 (đơn 5, Áo Oxford M Trắng)
        (5, 7, 3, 'Quần Jean hơi chật một chút', NULL, NOW());               -- id=3: KH Yến, CTDH 7 (đơn 5, Quần Jean M Đen)

    -- ---------------------------------------------------------
    -- 23. GIỎ HÀNG
    -- ---------------------------------------------------------
    INSERT INTO GioHang (MaKhachHang, NgayTao) VALUES
        (1, NOW()),   -- id=1: KH Lan
        (2, NOW()),   -- id=2: KH Minh
        (3, NOW()),   -- id=3: KH Hoa
        (4, NOW()),   -- id=4: KH Tuấn
        (5, NOW());   -- id=5: KH Yến

    -- ---------------------------------------------------------
    -- 24. CHI TIẾT GIỎ HÀNG
    -- ---------------------------------------------------------
    INSERT INTO ChiTietGioHang (MaGioHang, MaChiTietSanPham, SoLuong) VALUES
        (1, 3, 2),   -- KH Lan: Quần Jean M Đen x2
        (1, 4, 1),   -- KH Lan: Váy Hoa S Đỏ x1
        (2, 1, 1),   -- KH Minh: Áo Oxford M Trắng x1
        (4, 5, 3),   -- KH Tuấn: Nịt Da L Đen x3
        (5, 2, 2);   -- KH Yến: Áo Oxford L Trắng x2

    -- ---------------------------------------------------------
    -- 25. TRẢ HÀNG
    -- ---------------------------------------------------------
    INSERT INTO TraHang (MaDonHang, LyDoTraHang, TrangThai, TongTien, NgayTao) VALUES
        (5, 'Quần Jean không đúng màu đã đặt', 0, 500, NOW()),   -- id=1: KH Yến trả CTDH 7 (Quần Jean x1), tongTien = 900 - 400 = 500
        (3, 'Váy bị lỗi đường may',            1, 0,   NOW());   -- id=2: KH Hoa trả CTDH 4 (Váy Hoa x1), tongTien = 285 - 300 = -15 → 0 (đã duyệt)

    -- ---------------------------------------------------------
    -- 26. CHI TIẾT TRẢ HÀNG
    -- ---------------------------------------------------------
    INSERT INTO ChiTietTraHang (MaTraHang, MaSanPhamTra, GhiTru, TrangThai, NgayTao) VALUES
        (1, 7, 'Màu đen nhưng nhận được màu xanh',   0, NOW()),   -- Trả Quần Jean M Đen x1 (CTDH 7 - đơn 5)
        (2, 4, 'Đường may bị lỗi ở phần eo',         1, NOW());   -- Trả Váy Hoa S Đỏ x1 (CTDH 4 - đơn 3), đã duyệt

    -- ---------------------------------------------------------
    -- 27. LOẠI ĐƠN LUÂN CHUYỂN
    -- ---------------------------------------------------------
    INSERT INTO LoaiDonLuanChuyen (TenLoai, MoTa, NgayTao) VALUES
        ('Luân chuyển nội bộ',   'Chuyển hàng giữa các chi nhánh cùng hệ thống',   NOW()),   -- id=1
        ('Điều phối bổ sung',    'Bổ sung hàng cho chi nhánh thiếu hàng',           NOW()),   -- id=2
        ('Thu hồi hàng tồn',     'Thu hồi hàng tồn kho từ chi nhánh khác',          NOW());   -- id=3

    -- ---------------------------------------------------------
    -- 28. ĐƠN LUÂN CHUYỂN
    -- ---------------------------------------------------------
    INSERT INTO DonLuanChuyen (CuaHangDat, CuaHangGui, MaLoaiDonLuanChuyen, TenDon, TrangThai, NgayTao) VALUES
        (1, 2, 1, 'Luân chuyển Áo Oxford từ Q.3 về Q.1',  0, NOW()),   -- id=1: CN Q.1 đặt, CN Q.3 gửi, chờ xử lý
        (2, 1, 2, 'Bổ sung Quần Jean cho CN Q.3',          1, NOW()),   -- id=2: CN Q.3 đặt, CN Q.1 gửi, đang giao
        (3, 1, 1, 'Luân chuyển Nịt Da về Gò Vấp',          2, NOW());   -- id=3: CN Gò Vấp đặt, CN Q.1 gửi, đã nhận

    -- ---------------------------------------------------------
    -- 29. CHI TIẾT ĐƠN LUÂN CHUYỂN
    -- ---------------------------------------------------------
    INSERT INTO ChiTietDonLuanChuyen (MaDonLuanChuyen, MaChiTietSanPham, SoLuong, TrangThai, NgayTao) VALUES
        (1, 4, 5,  0, NOW()),   -- Đơn 1: Váy Hoa S Đỏ x5, chờ xử lý
        (1, 5, 3,  0, NOW()),   -- Đơn 1: Nịt Da L Đen x3, chờ xử lý
        (2, 3, 10, 1, NOW()),   -- Đơn 2: Quần Jean M Đen x10, đang giao
        (3, 1, 8,  2, NOW()),   -- Đơn 3: Áo Oxford M Trắng x8, đã nhận
        (3, 2, 5,  2, NOW());   -- Đơn 3: Áo Oxford L Trắng x5, đã nhận

    -- ---------------------------------------------------------
    -- 30. LOẠI KIỂM KÊ
    -- ---------------------------------------------------------
    INSERT INTO LoaiKiemKe (TenLoaiKiemKe, MoTa, TrangThai, NgayTao) VALUES
        ('Kiểm kê định kỳ',     'Kiểm kê theo chu kỳ tháng/quý', 1, NOW()),
        ('Kiểm kê đột xuất',    'Kiểm kê khi có chênh lệch bất thường', 1, NOW()),
        ('Kiểm kê trước khuyến mãi', 'Đồng bộ tồn kho trước campaign', 1, NOW());

    -- ---------------------------------------------------------
    -- 31. KIỂM KÊ HÀNG HÓA
    -- ---------------------------------------------------------
    INSERT INTO KiemKeHangHoa
        (MaLoaiKiemKe, MaCuaHang, MaNhanVienTao, MaNhanVienDuyet, TenPhieuKiemKe, TrangThai, GhiChu, NgayKiemKe, NgayXacNhan, NgayTao)
    VALUES
        (1, 1, 1, 5, 'Kiểm kê cuối tháng CN Q.1', 3, 'Đã xác nhận và cập nhật tồn', '2026-03-10 08:00:00', '2026-03-10 16:00:00', NOW()),
        (2, 2, 3, NULL, 'Kiểm kê đột xuất CN Q.3', 1, 'Chờ admin duyệt', '2026-03-12 09:00:00', NULL, NOW()),
        (1, 1, 2, 5, 'Kiểm kê tuần 2 CN Q.1', 2, 'Cần kiểm lại khu A', '2026-03-13 09:00:00', NULL, NOW());

    -- ---------------------------------------------------------
    -- 32. CHI TIẾT KIỂM KÊ
    -- ---------------------------------------------------------
    INSERT INTO ChiTietKiemKe
        (MaKiemKe, MaChiTietSanPham, SoLuongHeThong, SoLuongThucTe, ChenhLech, GhiChu, NgayTao)
    VALUES
        (1, 1, 50, 48, -2, 'Thiếu do hư hỏng', NOW()),
        (1, 2, 30, 30, 0,  'Đủ', NOW()),
        (2, 4, 25, 24, -1, 'Thiếu 1 sản phẩm', NOW()),
        (2, 5, 20, 22, 2,  'Dư 2 sản phẩm', NOW()),
        (3, 1, 48, 47, -1, 'Đếm lại lần 1', NOW());

    -- ---------------------------------------------------------
    -- 33. CA LÀM VIỆC
    -- ---------------------------------------------------------
    INSERT INTO CaLamViec (TenCaLam, GioBatDau, GioKetThuc, TrangThai) VALUES
        ('Ca Sáng',   '07:00:00', '11:30:00', 1),   -- id=1
        ('Ca Chiều',  '12:00:00', '17:00:00', 1),   -- id=2
        ('Ca Tối',    '17:00:00', '22:00:00', 1),   -- id=3
        ('Ca Toàn Ngày', '08:00:00', '17:00:00', 1); -- id=4

    -- ---------------------------------------------------------
    -- 34. LỊCH LÀM VIỆC
    -- ---------------------------------------------------------
    INSERT INTO LichLamViec (MaNhanVien, NgayLamViec, TrangThai, Json, NgayTao) VALUES
        (1, '2026-03-24', 1, NULL, NOW()),   -- id=1: NV An - 24/03
        (2, '2026-03-24', 1, NULL, NOW()),   -- id=2: NV Bình - 24/03
        (3, '2026-03-24', 1, NULL, NOW()),   -- id=3: NV Chi - 24/03
        (1, '2026-03-25', 1, NULL, NOW()),   -- id=4: NV An - 25/03
        (4, '2026-03-25', 0, '{"isHoliday": true, "isFestival": false}', NOW()),   -- id=5: NV Danh - 25/03 (nghỉ)
        (5, '2026-04-30', 2, '{"isHoliday": false, "isFestival": true}', NOW());   -- id=6: NV Hùng - 30/04 (lễ)

    -- ---------------------------------------------------------
    -- 35. CHI TIẾT LỊCH LÀM
    -- ---------------------------------------------------------
    INSERT INTO ChiTietLichLam (MaLichLam, MaCaLam, TrangThai, NgayTao) VALUES
        (1, 1, 1, NOW()),   -- Lịch 1 (NV An 24/03) - Ca Sáng
        (1, 2, 1, NOW()),   -- Lịch 1 (NV An 24/03) - Ca Chiều
        (2, 2, 1, NOW()),   -- Lịch 2 (NV Bình 24/03) - Ca Chiều
        (3, 3, 1, NOW()),   -- Lịch 3 (NV Chi 24/03) - Ca Tối
        (4, 4, 1, NOW());   -- Lịch 4 (NV An 25/03) - Ca Toàn Ngày

    -- ---------------------------------------------------------
    -- 36. LƯƠNG CƠ BẢN
    -- ---------------------------------------------------------
    INSERT INTO LuongCoBan (MaNhanVien, LuongCoBan, NgayApDung, TrangThai) VALUES
        (1, 8000000,  '2026-01-01 00:00:00', 1),   -- NV An: 8tr
        (2, 7500000,  '2026-01-01 00:00:00', 1),   -- NV Bình: 7.5tr
        (3, 7500000,  '2026-01-01 00:00:00', 1),   -- NV Chi: 7.5tr
        (4, 7000000,  '2026-01-01 00:00:00', 1),   -- NV Danh: 7tr
        (5, 12000000, '2026-01-01 00:00:00', 1);   -- NV Hùng (Admin): 12tr

    -- ---------------------------------------------------------
    -- 37. LƯƠNG THƯỞNG
    -- ---------------------------------------------------------
    INSERT INTO LuongThuong (MaNhanVien, TienThuong, NgayBatDau, NgayKetThuc, TrangThai) VALUES
        (1, 500000,  '2026-02-01 00:00:00', '2026-02-28 23:59:59', 1),   -- NV An T2: thưởng 500k (đã chi)
        (3, 300000,  '2026-02-01 00:00:00', '2026-02-28 23:59:59', 1),   -- NV Chi T2: thưởng 300k (đã chi)
        (2, 400000,  '2026-03-01 00:00:00', '2026-03-31 23:59:59', 0),   -- NV Bình T3: thưởng 400k (chờ chi)
        (4, 200000,  '2026-03-01 00:00:00', '2026-03-31 23:59:59', 0);   -- NV Danh T3: thưởng 200k (chờ chi)

    -- ---------------------------------------------------------
    -- 38. ĐỔI CA
    -- ---------------------------------------------------------
    INSERT INTO DoiCa (MaLichLam, MaChiTietLich, MaNhanVienNhanCa, TrangThai, NgayTao) VALUES
        (1, 2, 2, 1, NOW()),   -- AnĐổi ca Chiều (lịch 1, ct 2) cho Bình - đồng ý
        (3, 4, 1, 0, NOW()),   -- Chi đổi ca Tối (lịch 3, ct 4) cho An - chờ duyệt
        (2, 3, 3, 2, NOW());   -- Bình đổi ca Chiều (lịch 2, ct 3) cho Chi - từ chối

    -- ---------------------------------------------------------
    -- 39. LỖI PHÁT SINH
    -- ---------------------------------------------------------
    INSERT INTO LoiPhatSinh (MaLichLam, MaChiTietLich, TenLoiPhatSinh, SoTienTru, TrangThai, NgayTao) VALUES
        (2, 3, 'Đi trễ 20 phút không có lý do',     50000,  1, NOW()),   -- Bình đi trễ, đã xử lý
        (3, 4, 'Rời ca sớm 30 phút không báo cáo',   80000,  0, NOW()),   -- Chi rời ca sớm, chờ xử lý
        (4, 5, 'Không mặc đồng phục theo quy định',   30000,  0, NOW());   -- An không mặc đồng phục, chờ xử lý

    -- ---------------------------------------------------------
    -- 40. ĐỔI HÀNG
    -- ---------------------------------------------------------
    INSERT INTO DoiHang (MaDonHang, GhiTru, TrangThai, TongTien, NgayTao) VALUES
        (5, 'Đổi áo Oxford M Trắng lấy L Trắng do mặc chật', 0, 0, NOW()),   -- id=1: KH Yến đổi CTDH 6 (Áo Oxford M Trắng) sang L Trắng.
        (3, 'Đổi Váy Hoa S Đỏ sang áo Oxford M', 1, 0, NOW());   -- id=2: KH Hoa đổi CTDH 4 (Váy Hoa 300k) sang áo Oxford (200k)

    -- ---------------------------------------------------------
    -- 41. CHI TIẾT ĐỔI HÀNG
    -- ---------------------------------------------------------
    INSERT INTO ChiTietDoiHang (MaDoiHang, MaSanPhamTra, MaSanPhamDoi, GhiTru, TrangThai, NgayTao) VALUES
        (1, 6, 2, 'Size M hơi chật, đổi sang L', 0, NOW()),   -- Đổi CTDH 6 (Áo Trắng M) lấy CTSP 2 (Áo Trắng L), chờ xử lý
        (2, 4, 1, 'Đổi kiểu khác', 1, NOW());   -- Đổi CTDH 4 (Váy Hoa) lấy CTSP 1 (Áo Trắng M), đã duyệt
