# Tá»”NG Há»¢P CHá»¨C NÄ‚NG THEO VAI TRÃ’ â€” Há»† THá»NG SHOPPING

> **Má»¥c Ä‘Ã­ch:** TÃ i liá»‡u tá»•ng há»£p toÃ n bá»™ chá»©c nÄƒng cá»§a tá»«ng vai trÃ² trong há»‡ thá»‘ng, giÃºp láº­p trÃ¬nh viÃªn Frontend hiá»ƒu rÃµ pháº¡m vi quyá»n háº¡n Ä‘á»ƒ xÃ¢y dá»±ng giao diá»‡n phÃ¹ há»£p.
>
> **NgÃ y táº¡o:** 2026-03-09

---

## Má»¤C Lá»¤C

1. [Tá»•ng quan há»‡ thá»‘ng](#1-tá»•ng-quan-há»‡-thá»‘ng)
2. [XÃ¡c thá»±c & PhÃ¢n quyá»n](#2-xÃ¡c-thá»±c--phÃ¢n-quyá»n)
3. [CÃ¡c vai trÃ² trong há»‡ thá»‘ng](#3-cÃ¡c-vai-trÃ²-trong-há»‡-thá»‘ng)
4. [ADMIN â€” Quáº£n trá»‹ viÃªn toÃ n quyá»n](#4-admin--quáº£n-trá»‹-viÃªn-toÃ n-quyá»n)
5. [NHAN_VIEN â€” NhÃ¢n viÃªn bÃ¡n hÃ ng](#5-nhan_vien--nhÃ¢n-viÃªn-bÃ¡n-hÃ ng)
6. [KHACH_HANG â€” KhÃ¡ch hÃ ng](#6-khach_hang--khÃ¡ch-hÃ ng)
7. [Endpoint cÃ´ng khai (khÃ´ng cáº§n Ä‘Äƒng nháº­p)](#7-endpoint-cÃ´ng-khai-khÃ´ng-cáº§n-Ä‘Äƒng-nháº­p)
8. [Báº£ng ma tráº­n phÃ¢n quyá»n chi tiáº¿t](#8-báº£ng-ma-tráº­n-phÃ¢n-quyá»n-chi-tiáº¿t)
9. [Gá»£i Ã½ xÃ¢y dá»±ng Frontend theo vai trÃ²](#9-gá»£i-Ã½-xÃ¢y-dá»±ng-frontend-theo-vai-trÃ²)

---

## 1. Tá»•ng quan há»‡ thá»‘ng

Há»‡ thá»‘ng **Shopping** lÃ  á»©ng dá»¥ng quáº£n lÃ½ bÃ¡n hÃ ng thá»i trang vá»›i cÃ¡c module:

| STT | Module                    | Base Path API                     | MÃ´ táº£                                     |
| --- | ------------------------- | --------------------------------- | -------------------------------------------- |
| 1   | XÃ¡c thá»±c               | `/api/v1/auth`                    | ÄÄƒng nháº­p, Ä‘Äƒng kÃ½, refresh token      |
| 2   | Sáº£n pháº©m              | `/api/v1/san-pham`                | Quáº£n lÃ½ sáº£n pháº©m                      |
| 3   | Chi tiáº¿t sáº£n pháº©m   | `/api/v1/chi-tiet-san-pham`       | Biáº¿n thá»ƒ SP (mÃ u, size, cá»­a hÃ ng)    |
| 4   | ÄÆ¡n hÃ ng                | `/api/v1/don-hang`                | Quáº£n lÃ½ Ä‘Æ¡n hÃ ng online & táº¡i quáº§y |
| 5   | Chi tiáº¿t Ä‘Æ¡n hÃ ng    | `/api/v1/chi-tiet-don-hang`       | DÃ²ng sáº£n pháº©m trong Ä‘Æ¡n hÃ ng         |
| 6   | Giá» hÃ ng                | `/api/v1/gio-hang`                | Giá» hÃ ng khÃ¡ch hÃ ng                      |
| 7   | Phiáº¿u nháº­p            | `/api/v1/phieu-nhap`              | Nháº­p hÃ ng tá»« nhÃ  cung cáº¥p            |
| 8   | Chi tiáº¿t phiáº¿u nháº­p | `/api/v1/chi-tiet-phieu-nhap`     | DÃ²ng SP trong phiáº¿u nháº­p                |
| 9   | HÃ¬nh áº£nh               | `/api/v1/hinh-anh`                | Quáº£n lÃ½ áº£nh SP (Cloudinary)             |
| 10  | Bá»™ sÆ°u táº­p           | `/api/v1/bo-suu-tap`              | BST theo mÃ¹a                                |
| 11  | Kiá»ƒu sáº£n pháº©m       | `/api/v1/kieu-san-pham`           | Loáº¡i SP: Ão, Quáº§n, VÃ¡y, ...             |
| 12  | ThÆ°Æ¡ng hiá»‡u           | `/api/v1/thuong-hieu`             | Nike, Adidas, Uniqlo, ...                    |
| 13  | MÃ u sáº¯c                | `/api/v1/mau-sac`                 | Äen, Tráº¯ng, Äá», ...                       |
| 14  | KÃ­ch thÆ°á»›c            | `/api/v1/kich-thuoc`              | S, M, L, XL, ...                             |
| 15  | Cá»­a hÃ ng               | `/api/v1/cua-hang`                | Chi nhÃ¡nh cá»­a hÃ ng                       |
| 16  | NhÃ  cung cáº¥p           | `/api/v1/nha-cung-cap`            | NhÃ  cung cáº¥p hÃ ng hÃ³a                   |
| 17  | Vai trÃ²                  | `/api/v1/roles`                   | Quáº£n lÃ½ Role (RBAC)                       |
| 18  | Quyá»n háº¡n              | `/api/v1/permissions`             | Quáº£n lÃ½ Permission                        |
| 19  | KM theo hÃ³a Ä‘Æ¡n        | `/api/v1/khuyen-mai-theo-hoa-don` | Giáº£m giÃ¡ theo tá»•ng Ä‘Æ¡n hÃ ng          |
| 20  | KM theo Ä‘iá»ƒm           | `/api/v1/khuyen-mai-theo-diem`    | Äá»•i Ä‘iá»ƒm tÃ­ch lÅ©y láº¥y mÃ£ giáº£m    |
| 21  | ÄÃ¡nh giÃ¡ sáº£n pháº©m   | `/api/v1/danh-gia-san-pham`       | ÄÃ¡nh giÃ¡ & bÃ¬nh luáº­n SP                 |
| 22  | Storage (Public)          | `{secure_url_cloudinary}`         | Truy cáº­p áº£nh (khÃ´ng cáº§n auth)         |
| 23  | NhÃ¢n viÃªn               | `/api/v1/nhan-vien`               | Quáº£n lÃ½ thÃ´ng tin nhÃ¢n viÃªn            |
| 24  | Tráº£ hÃ ng               | `/api/v1/tra-hang`                | Phiáº¿u tráº£ hÃ ng & hoÃ n tiá»n            |

---

## 2. XÃ¡c thá»±c & PhÃ¢n quyá»n

### 2.1 CÆ¡ cháº¿ xÃ¡c thá»±c â€” JWT

- **Access Token**: gá»­i qua header `Authorization: Bearer <token>`, thá»i gian sá»‘ng ngáº¯n
- **Refresh Token**: lÆ°u trong cookie `HttpOnly`, dÃ¹ng Ä‘á»ƒ gia háº¡n access token
- Há»‡ thá»‘ng há»— trá»£ 2 loáº¡i user: **NhanVien** (nhÃ¢n viÃªn) vÃ  **KhachHang** (khÃ¡ch hÃ ng)

### 2.2 CÆ¡ cháº¿ phÃ¢n quyá»n â€” RBAC

- Má»—i user Ä‘Æ°á»£c gÃ¡n 1 **Role** (vai trÃ²)
- Má»—i Role chá»©a danh sÃ¡ch **Permission** (nhiá»u-nhiá»u)
- Má»—i Permission = 1 cáº·p `(apiPath, method)` â†’ tÆ°Æ¡ng á»©ng 1 endpoint cá»¥ thá»ƒ
- Khi gá»i API, **PermissionInterceptor** kiá»ƒm tra:
  1. Láº¥y email user tá»« JWT
  2. TÃ¬m user trong báº£ng `NhanVien` hoáº·c `KhachHang`
  3. Láº¥y Role â†’ danh sÃ¡ch Permission
  4. So khá»›p `(apiPath, method)` vá»›i request â†’ cho phÃ©p hoáº·c tráº£ vá» `403 Forbidden`

### 2.3 CÃ¡c endpoint Ä‘Äƒng nháº­p / Ä‘Äƒng kÃ½ (Whitelist â€” khÃ´ng cáº§n token)

| Endpoint                  | Method | MÃ´ táº£                                      |
| ------------------------- | ------ | --------------------------------------------- |
| `/api/v1/auth/login`      | POST   | ÄÄƒng nháº­p (email + password)               |
| `/api/v1/auth/register`   | POST   | ÄÄƒng kÃ½ tÃ i khoáº£n khÃ¡ch hÃ ng má»›i     |
| `/api/v1/auth/refresh`    | GET    | Gia háº¡n access token (dÃ¹ng refresh cookie) |
| `{secure_url_cloudinary}` | GET    | Xem áº£nh sáº£n pháº©m (public)               |
| `/v3/api-docs/**`         | GET    | Swagger API docs                              |
| `/swagger-ui/**`          | GET    | Swagger UI                                    |

---

## 3. CÃ¡c vai trÃ² trong há»‡ thá»‘ng

| ID  | TÃªn vai trÃ² | Loáº¡i user | MÃ´ táº£                                                                         |
| --- | ------------- | ----------- | -------------------------------------------------------------------------------- |
| 1   | `ADMIN`       | NhanVien    | Quáº£n trá»‹ viÃªn toÃ n quyá»n â€” truy cáº­p má»i chá»©c nÄƒng                 |
| 2   | `NHAN_VIEN`   | NhanVien    | NhÃ¢n viÃªn bÃ¡n hÃ ng â€” xem danh má»¥c, xá»­ lÃ½ Ä‘Æ¡n hÃ ng & phiáº¿u nháº­p |
| 3   | `KHACH_HANG`  | KhachHang   | KhÃ¡ch hÃ ng â€” mua sáº¯m, giá» hÃ ng, Ä‘áº·t hÃ ng, Ä‘Ã¡nh giÃ¡ sáº£n pháº©m   |

---

## 4. ADMIN â€” Quáº£n trá»‹ viÃªn toÃ n quyá»n

> **Pháº¡m vi:** ToÃ n bá»™ 113 permission â€” truy cáº­p má»i API endpoint.

### 4.1 Quáº£n lÃ½ sáº£n pháº©m & danh má»¥c

| Chá»©c nÄƒng                                    | Endpoint                                                                                                           | Method |
| ----------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ | ------ |
| Xem danh sÃ¡ch sáº£n pháº©m (lá»c, phÃ¢n trang) | `GET /api/v1/san-pham?tenSanPham=&kieuSanPhamId=&boSuuTapId=&thuongHieuId=&trangThai=&giaMin=&giaMax=&page=&size=` | GET    |
| Xem chi tiáº¿t sáº£n pháº©m                     | `GET /api/v1/san-pham/{id}`                                                                                        | GET    |
| Táº¡o sáº£n pháº©m (upload áº£nh)               | `POST /api/v1/san-pham` (multipart/form-data)                                                                      | POST   |
| Cáº­p nháº­t sáº£n pháº©m                       | `PUT /api/v1/san-pham`                                                                                             | PUT    |
| XÃ³a sáº£n pháº©m                               | `DELETE /api/v1/san-pham/{id}`                                                                                     | DELETE |
| CRUD Kiá»ƒu sáº£n pháº©m                        | `/api/v1/kieu-san-pham` + `/{id}`                                                                                  | CRUD   |
| CRUD Bá»™ sÆ°u táº­p                            | `/api/v1/bo-suu-tap` + `/{id}`                                                                                     | CRUD   |
| CRUD ThÆ°Æ¡ng hiá»‡u                            | `/api/v1/thuong-hieu` + `/{id}`                                                                                    | CRUD   |
| CRUD MÃ u sáº¯c                                 | `/api/v1/mau-sac` + `/{id}`                                                                                        | CRUD   |
| CRUD KÃ­ch thÆ°á»›c                             | `/api/v1/kich-thuoc` + `/{id}`                                                                                     | CRUD   |

### 4.2 Quáº£n lÃ½ chi tiáº¿t sáº£n pháº©m & hÃ¬nh áº£nh

| Chá»©c nÄƒng                                          | Endpoint                                                      | Method |
| ----------------------------------------------------- | ------------------------------------------------------------- | ------ |
| Xem táº¥t cáº£ biáº¿n thá»ƒ SP                        | `GET /api/v1/chi-tiet-san-pham`                               | GET    |
| Xem biáº¿n thá»ƒ theo ID                              | `GET /api/v1/chi-tiet-san-pham/{id}`                          | GET    |
| Xem biáº¿n thá»ƒ theo sáº£n pháº©m                    | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`          | GET    |
| Xem biáº¿n thá»ƒ theo cá»­a hÃ ng Ä‘ang Ä‘Äƒng nháº­p | `GET /api/v1/chi-tiet-san-pham/san-pham-tai-cua-hang`         | GET    |
| Táº¡o biáº¿n thá»ƒ SP (upload áº£nh)                  | `POST /api/v1/chi-tiet-san-pham` (multipart)                  | POST   |
| Cáº­p nháº­t biáº¿n thá»ƒ                             | `PUT /api/v1/chi-tiet-san-pham`                               | PUT    |
| XÃ³a biáº¿n thá»ƒ                                     | `DELETE /api/v1/chi-tiet-san-pham/{id}`                       | DELETE |
| Xem hÃ¬nh áº£nh theo CTSP                             | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}`   | GET    |
| Upload hÃ¬nh áº£nh cho CTSP                           | `POST /api/v1/hinh-anh/upload/{chiTietSanPhamId}` (multipart) | POST   |
| Táº¡o/Cáº­p nháº­t/XÃ³a hÃ¬nh áº£nh                   | `/api/v1/hinh-anh` + `/{id}`                                  | CRUD   |

### 4.3 Quáº£n lÃ½ Ä‘Æ¡n hÃ ng

| Chá»©c nÄƒng                                   | Endpoint                                                                                                  | Method |
| ---------------------------------------------- | --------------------------------------------------------------------------------------------------------- | ------ |
| Xem danh sÃ¡ch Ä‘Æ¡n hÃ ng (lá»c, phÃ¢n trang) | `GET /api/v1/don-hang?cuaHangId=&nhanVienId=&trangThai=&trangThaiThanhToan=&hinhThucDonHang=&page=&size=` | GET    |
| Xem chi tiáº¿t Ä‘Æ¡n hÃ ng                     | `GET /api/v1/don-hang/{id}`                                                                               | GET    |
| Táº¡o Ä‘Æ¡n hÃ ng online                       | `POST /api/v1/don-hang/online`                                                                            | POST   |
| Táº¡o Ä‘Æ¡n hÃ ng táº¡i quáº§y                 | `POST /api/v1/don-hang/tai-quay`                                                                          | POST   |
| Cáº­p nháº­t tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng         | `PUT /api/v1/don-hang`                                                                                    | PUT    |
| XÃ³a Ä‘Æ¡n hÃ ng                               | `DELETE /api/v1/don-hang/{id}`                                                                            | DELETE |
| Xem chi tiáº¿t dÃ²ng sáº£n pháº©m trong Ä‘Æ¡n  | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}`                                                      | GET    |
| CRUD chi tiáº¿t Ä‘Æ¡n hÃ ng                    | `/api/v1/chi-tiet-don-hang` + `/{id}`                                                                     | CRUD   |

**Tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng (trangThai):**

| GiÃ¡ trá»‹ | Ã nghÄ©a             | Ghi chÃº                                                     |
| ---------- | -------------------- | ------------------------------------------------------------ |
| 0          | Chá» xÃ¡c nháº­n     | Máº·c Ä‘á»‹nh khi táº¡o Ä‘Æ¡n online                         |
| 1          | ÄÃ£ xÃ¡c nháº­n      | NV/Admin xÃ¡c nháº­n                                         |
| 2          | Äang Ä‘Ã³ng gÃ³i     | Äang chuáº©n bá»‹ hÃ ng                                      |
| 3          | Äang giao hÃ ng      | ÄÃ£ gá»­i cho váº­n chuyá»ƒn                                 |
| 4          | ÄÃ£ há»§y            | Há»§y Ä‘Æ¡n hÃ ng                                            |
| 5          | ÄÃ£ nháº­n hÃ ng âœ… | Giao thÃ nh cÃ´ng â†’ tá»± Ä‘á»™ng cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y |

**Tráº¡ng thÃ¡i thanh toÃ¡n (trangThaiThanhToan):** `0` = ChÆ°a thanh toÃ¡n, `1` = ÄÃ£ thanh toÃ¡n

**HÃ¬nh thá»©c Ä‘Æ¡n hÃ ng (hinhThucDonHang):** `0` = Táº¡i quáº§y, `1` = Online

### 4.4 Quáº£n lÃ½ nháº­p hÃ ng

| Chá»©c nÄƒng                         | Endpoint                                                                                        | Method |
| ------------------------------------ | ----------------------------------------------------------------------------------------------- | ------ |
| Xem danh sÃ¡ch phiáº¿u nháº­p (lá»c) | `GET /api/v1/phieu-nhap?tenPhieuNhap=&trangThai=&cuaHang=&nhaCungCap=&ngayBatDau=&ngayKetThuc=` | GET    |
| Xem chi tiáº¿t phiáº¿u nháº­p        | `GET /api/v1/phieu-nhap/{id}`                                                                   | GET    |
| Táº¡o phiáº¿u nháº­p                 | `POST /api/v1/phieu-nhap`                                                                       | POST   |
| Cáº­p nháº­t phiáº¿u nháº­p          | `PUT /api/v1/phieu-nhap`                                                                        | PUT    |
| **Kiá»ƒm kÃª phiáº¿u nháº­p**        | `PUT /api/v1/phieu-nhap/kiem-ke/{id}`                                                           | PUT    |
| XÃ³a phiáº¿u nháº­p                  | `DELETE /api/v1/phieu-nhap/{id}`                                                                | DELETE |
| CRUD chi tiáº¿t phiáº¿u nháº­p       | `/api/v1/chi-tiet-phieu-nhap` + `/{id}` + `/phieu-nhap/{phieuNhapId}`                           | CRUD   |

**Tráº¡ng thÃ¡i phiáº¿u nháº­p:**

| GiÃ¡ trá»‹ | Ã nghÄ©a         |
| ---------- | ---------------- |
| 0          | ÄÃ£ Ä‘áº·t hÃ ng |
| 1          | ÄÃ£ nháº­n hÃ ng |
| 2          | Cháº­m giao      |
| 3          | ÄÃ£ há»§y        |
| 4          | Thiáº¿u hÃ ng    |
| 5          | HoÃ n thÃ nh âœ… |

### 4.5 Quáº£n lÃ½ khuyáº¿n mÃ£i

| Chá»©c nÄƒng                       | Endpoint                                    | Method |
| ---------------------------------- | ------------------------------------------- | ------ |
| CRUD Khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n | `/api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | CRUD   |
| CRUD Khuyáº¿n mÃ£i theo Ä‘iá»ƒm    | `/api/v1/khuyen-mai-theo-diem` + `/{id}`    | CRUD   |

### 4.6 Quáº£n lÃ½ cá»­a hÃ ng & nhÃ  cung cáº¥p

| Chá»©c nÄƒng         | Endpoint                         | Method |
| -------------------- | -------------------------------- | ------ |
| CRUD Cá»­a hÃ ng     | `/api/v1/cua-hang` + `/{id}`     | CRUD   |
| CRUD NhÃ  cung cáº¥p | `/api/v1/nha-cung-cap` + `/{id}` | CRUD   |
| CRUD NhÃ¢n viÃªn     | `/api/v1/nhan-vien` + `/{id}`    | CRUD   |

### 4.7 Quáº£n lÃ½ phÃ¢n quyá»n (RBAC)

| Chá»©c nÄƒng      | Endpoint                        | Method |
| ----------------- | ------------------------------- | ------ |
| CRUD Vai trÃ²     | `/api/v1/roles` + `/{id}`       | CRUD   |
| CRUD Quyá»n háº¡n | `/api/v1/permissions` + `/{id}` | CRUD   |

### 4.8 Quáº£n lÃ½ Ä‘Ã¡nh giÃ¡ sáº£n pháº©m

| Chá»©c nÄƒng                                | Endpoint                                                             | Method |
| ------------------------------------------- | -------------------------------------------------------------------- | ------ |
| Xem táº¥t cáº£ Ä‘Ã¡nh giÃ¡                  | `GET /api/v1/danh-gia-san-pham`                                      | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo ID                     | `GET /api/v1/danh-gia-san-pham/{id}`                                 | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo sáº£n pháº©m           | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`                 | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo chi tiáº¿t Ä‘Æ¡n hÃ ng | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` | GET    |
| **XÃ³a báº¥t ká»³ Ä‘Ã¡nh giÃ¡ nÃ o**        | `DELETE /api/v1/danh-gia-san-pham/{id}`                              | DELETE |

> **LÆ°u Ã½:** Admin KHÃ”NG táº¡o/sá»­a Ä‘Ã¡nh giÃ¡, chá»‰ cÃ³ thá»ƒ xÃ³a báº¥t ká»³ Ä‘Ã¡nh giÃ¡ nÃ o (kiá»ƒm duyá»‡t).

### 4.9 XÃ¡c thá»±c

| Chá»©c nÄƒng                | Endpoint                   | Method |
| --------------------------- | -------------------------- | ------ |
| ÄÄƒng nháº­p                | `POST /api/v1/auth/login`  | POST   |
| Xem thÃ´ng tin tÃ i khoáº£n | `GET /api/v1/auth/account` | GET    |
| Gia háº¡n token             | `GET /api/v1/auth/refresh` | GET    |
| ÄÄƒng xuáº¥t                | `POST /api/v1/auth/logout` | POST   |

---

## 5. NHAN_VIEN â€” NhÃ¢n viÃªn bÃ¡n hÃ ng

> **Pháº¡m vi:** Xem danh má»¥c & sáº£n pháº©m (chá»‰ GET), xá»­ lÃ½ Ä‘Æ¡n hÃ ng táº¡i quáº§y, quáº£n lÃ½ phiáº¿u nháº­p.
>
> **Tá»•ng:** 47 permission

### 5.1 Xem sáº£n pháº©m & danh má»¥c (CHá»ˆ XEM â€” khÃ´ng táº¡o/sá»­a/xÃ³a)

| Chá»©c nÄƒng                                          | Endpoint                                                         | Method |
| ----------------------------------------------------- | ---------------------------------------------------------------- | ------ |
| Xem danh sÃ¡ch sáº£n pháº©m                           | `GET /api/v1/san-pham`                                           | GET    |
| Xem chi tiáº¿t sáº£n pháº©m                           | `GET /api/v1/san-pham/{id}`                                      | GET    |
| Xem táº¥t cáº£ biáº¿n thá»ƒ SP                        | `GET /api/v1/chi-tiet-san-pham`                                  | GET    |
| Xem biáº¿n thá»ƒ theo ID                              | `GET /api/v1/chi-tiet-san-pham/{id}`                             | GET    |
| Xem biáº¿n thá»ƒ theo sáº£n pháº©m                    | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`             | GET    |
| Xem biáº¿n thá»ƒ theo cá»­a hÃ ng Ä‘ang Ä‘Äƒng nháº­p | `GET /api/v1/chi-tiet-san-pham/san-pham-tai-cua-hang`            | GET    |
| Xem danh sÃ¡ch mÃ u sáº¯c                             | `GET /api/v1/mau-sac` + `/{id}`                                  | GET    |
| Xem danh sÃ¡ch kÃ­ch thÆ°á»›c                         | `GET /api/v1/kich-thuoc` + `/{id}`                               | GET    |
| Xem danh sÃ¡ch kiá»ƒu sáº£n pháº©m                    | `GET /api/v1/kieu-san-pham` + `/{id}`                            | GET    |
| Xem danh sÃ¡ch bá»™ sÆ°u táº­p                        | `GET /api/v1/bo-suu-tap` + `/{id}`                               | GET    |
| Xem danh sÃ¡ch thÆ°Æ¡ng hiá»‡u                        | `GET /api/v1/thuong-hieu` + `/{id}`                              | GET    |
| Xem hÃ¬nh áº£nh SP                                    | `GET /api/v1/hinh-anh` + `/{id}` + `/chi-tiet-san-pham/{ctspId}` | GET    |
| Xem danh sÃ¡ch cá»­a hÃ ng                            | `GET /api/v1/cua-hang` + `/{id}`                                 | GET    |
| Xem danh sÃ¡ch nhÃ¢n viÃªn                            | `GET /api/v1/nhan-vien`                                          | GET    |

> Vá»›i endpoint `GET /api/v1/nhan-vien`: nhÃ¢n viÃªn chá»‰ xem danh sÃ¡ch nhÃ¢n viÃªn cÃ¹ng cá»­a hÃ ng vá»›i tÃ i khoáº£n Ä‘ang Ä‘Äƒng nháº­p.

### 5.2 Quáº£n lÃ½ Ä‘Æ¡n hÃ ng (XEM + Táº O Táº I QUáº¦Y + Cáº¬P NHáº¬T)

| Chá»©c nÄƒng                               | Endpoint                                           | Method |
| ------------------------------------------ | -------------------------------------------------- | ------ |
| Xem danh sÃ¡ch Ä‘Æ¡n hÃ ng                 | `GET /api/v1/don-hang`                             | GET    |
| Xem chi tiáº¿t Ä‘Æ¡n hÃ ng                 | `GET /api/v1/don-hang/{id}`                        | GET    |
| **Táº¡o Ä‘Æ¡n hÃ ng táº¡i quáº§y**         | `POST /api/v1/don-hang/tai-quay`                   | POST   |
| **Cáº­p nháº­t tráº¡ng thÃ¡i Ä‘Æ¡n hÃ ng** | `PUT /api/v1/don-hang`                             | PUT    |
| Xem chi tiáº¿t sáº£n pháº©m trong Ä‘Æ¡n    | `GET /api/v1/chi-tiet-don-hang` + `/don-hang/{id}` | GET    |
| Xem chi tiáº¿t Ä‘Æ¡n hÃ ng theo ID         | `GET /api/v1/chi-tiet-don-hang/{id}`               | GET    |

> **NhÃ¢n viÃªn KHÃ”NG thá»ƒ:** Táº¡o Ä‘Æ¡n hÃ ng online, xÃ³a Ä‘Æ¡n hÃ ng, táº¡o/sá»­a/xÃ³a chi tiáº¿t Ä‘Æ¡n hÃ ng.

### 5.3 Quáº£n lÃ½ nháº­p hÃ ng (PHIáº¾U NHáº¬P â€” gáº§n nhÆ° toÃ n quyá»n)

| Chá»©c nÄƒng                               | Endpoint                                                                  | Method |
| ------------------------------------------ | ------------------------------------------------------------------------- | ------ |
| Xem danh sÃ¡ch phiáº¿u nháº­p              | `GET /api/v1/phieu-nhap`                                                  | GET    |
| Xem chi tiáº¿t phiáº¿u nháº­p              | `GET /api/v1/phieu-nhap/{id}`                                             | GET    |
| **Táº¡o phiáº¿u nháº­p**                   | `POST /api/v1/phieu-nhap`                                                 | POST   |
| **Cáº­p nháº­t phiáº¿u nháº­p**            | `PUT /api/v1/phieu-nhap`                                                  | PUT    |
| **Kiá»ƒm kÃª phiáº¿u nháº­p**              | `PUT /api/v1/phieu-nhap/kiem-ke/{id}`                                     | PUT    |
| Xem chi tiáº¿t phiáº¿u nháº­p (dÃ²ng)      | `GET /api/v1/chi-tiet-phieu-nhap` + `/{id}` + `/phieu-nhap/{phieuNhapId}` | GET    |
| **Táº¡o chi tiáº¿t phiáº¿u nháº­p**        | `POST /api/v1/chi-tiet-phieu-nhap`                                        | POST   |
| **Cáº­p nháº­t chi tiáº¿t phiáº¿u nháº­p** | `PUT /api/v1/chi-tiet-phieu-nhap`                                         | PUT    |

> **NhÃ¢n viÃªn KHÃ”NG thá»ƒ:** XÃ³a phiáº¿u nháº­p, xÃ³a chi tiáº¿t phiáº¿u nháº­p.

### 5.4 Xem khuyáº¿n mÃ£i (CHá»ˆ XEM)

| Chá»©c nÄƒng                      | Endpoint                                        | Method |
| --------------------------------- | ----------------------------------------------- | ------ |
| Xem khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n | `GET /api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | GET    |
| Xem khuyáº¿n mÃ£i theo Ä‘iá»ƒm    | `GET /api/v1/khuyen-mai-theo-diem` + `/{id}`    | GET    |

### 5.5 Xem Ä‘Ã¡nh giÃ¡ sáº£n pháº©m (CHá»ˆ XEM)

| Chá»©c nÄƒng                                | Endpoint                                                             | Method |
| ------------------------------------------- | -------------------------------------------------------------------- | ------ |
| Xem táº¥t cáº£ Ä‘Ã¡nh giÃ¡                  | `GET /api/v1/danh-gia-san-pham`                                      | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo ID                     | `GET /api/v1/danh-gia-san-pham/{id}`                                 | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo sáº£n pháº©m           | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`                 | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo chi tiáº¿t Ä‘Æ¡n hÃ ng | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` | GET    |

### 5.6 NhÃ¢n viÃªn KHÃ”NG CÃ“ quyá»n

- âŒ Táº¡o/Sá»­a/XÃ³a sáº£n pháº©m, danh má»¥c (kiá»ƒu SP, BST, thÆ°Æ¡ng hiá»‡u, mÃ u sáº¯c, kÃ­ch thÆ°á»›c)
- âŒ Táº¡o/Sá»­a/XÃ³a hÃ¬nh áº£nh, cá»­a hÃ ng, nhÃ  cung cáº¥p
- âŒ Táº¡o/Sá»­a/XÃ³a nhÃ¢n viÃªn
- âŒ Quáº£n lÃ½ vai trÃ², quyá»n háº¡n
- âŒ Táº¡o/Sá»­a/XÃ³a khuyáº¿n mÃ£i
- âŒ Táº¡o Ä‘Æ¡n hÃ ng online
- âŒ XÃ³a Ä‘Æ¡n hÃ ng, phiáº¿u nháº­p
- âŒ Giá» hÃ ng (chá»‰ dÃ nh cho khÃ¡ch hÃ ng)
- âŒ Táº¡o/Sá»­a/XÃ³a Ä‘Ã¡nh giÃ¡ sáº£n pháº©m

---

## 6. KHACH_HANG â€” KhÃ¡ch hÃ ng

> **Pháº¡m vi:** Xem sáº£n pháº©m, giá» hÃ ng, Ä‘áº·t hÃ ng online, Ä‘Ã¡nh giÃ¡ sáº£n pháº©m, sá»­ dá»¥ng khuyáº¿n mÃ£i.
>
> **Tá»•ng:** 40+ permission

### 6.1 ÄÄƒng kÃ½ & ÄÄƒng nháº­p

| Chá»©c nÄƒng                | Endpoint                     | Method | Ghi chÃº                   |
| --------------------------- | ---------------------------- | ------ | -------------------------- |
| ÄÄƒng kÃ½ tÃ i khoáº£n      | `POST /api/v1/auth/register` | POST   | Public, khÃ´ng cáº§n token |
| ÄÄƒng nháº­p                | `POST /api/v1/auth/login`    | POST   | Email + Password           |
| Xem thÃ´ng tin tÃ i khoáº£n | `GET /api/v1/auth/account`   | GET    | Bao gá»“m `diemTichLuy`    |
| Gia háº¡n token             | `GET /api/v1/auth/refresh`   | GET    | DÃ¹ng refresh cookie       |
| ÄÄƒng xuáº¥t                | `POST /api/v1/auth/logout`   | POST   | XÃ³a refresh token         |

**Response Ä‘Äƒng nháº­p chá»©a:**

```json
{
  "access_token": "eyJ...",
  "user": {
    "id": 1,
    "email": "customer@example.com",
    "tenKhachHang": "Nguyá»…n VÄƒn A",
    "diemTichLuy": 150,
    "role": {
      "id": 4,
      "name": "KHACH_HANG"
    }
  }
}
```

### 6.2 Duyá»‡t sáº£n pháº©m (CHá»ˆ XEM)

| Chá»©c nÄƒng                                          | Endpoint                                                                                               | Method |
| ----------------------------------------------------- | ------------------------------------------------------------------------------------------------------ | ------ |
| Xem danh sÃ¡ch sáº£n pháº©m (lá»c, tÃ¬m kiáº¿m)       | `GET /api/v1/san-pham?tenSanPham=&kieuSanPhamId=&boSuuTapId=&thuongHieuId=&trangThai=&giaMin=&giaMax=` | GET    |
| Xem chi tiáº¿t sáº£n pháº©m                           | `GET /api/v1/san-pham/{id}`                                                                            | GET    |
| Xem biáº¿n thá»ƒ sáº£n pháº©m (mÃ u, size, tá»“n kho) | `GET /api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}`                                                   | GET    |
| Xem biáº¿n thá»ƒ theo ID                              | `GET /api/v1/chi-tiet-san-pham/{id}`                                                                   | GET    |
| Xem táº¥t cáº£ biáº¿n thá»ƒ                           | `GET /api/v1/chi-tiet-san-pham`                                                                        | GET    |
| Xem hÃ¬nh áº£nh sáº£n pháº©m                          | `GET /api/v1/hinh-anh/chi-tiet-san-pham/{ctspId}`                                                      | GET    |
| Xem danh sÃ¡ch danh má»¥c                             | `GET /api/v1/kieu-san-pham` + `/{id}`                                                                  | GET    |
| Xem danh sÃ¡ch bá»™ sÆ°u táº­p                        | `GET /api/v1/bo-suu-tap` + `/{id}`                                                                     | GET    |
| Xem danh sÃ¡ch thÆ°Æ¡ng hiá»‡u                        | `GET /api/v1/thuong-hieu` + `/{id}`                                                                    | GET    |
| Xem danh sÃ¡ch mÃ u sáº¯c                             | `GET /api/v1/mau-sac` + `/{id}`                                                                        | GET    |
| Xem danh sÃ¡ch kÃ­ch thÆ°á»›c                         | `GET /api/v1/kich-thuoc` + `/{id}`                                                                     | GET    |
| Xem danh sÃ¡ch cá»­a hÃ ng                            | `GET /api/v1/cua-hang` + `/{id}`                                                                       | GET    |
| Xem áº£nh sáº£n pháº©m (public)                       | `GET {secure_url_cloudinary}`                                                                          | GET    |

### 6.3 Giá» hÃ ng (CHá»ˆ KHÃCH HÃ€NG)

| Chá»©c nÄƒng                     | Endpoint                                              | Method |
| -------------------------------- | ----------------------------------------------------- | ------ |
| **ThÃªm sáº£n pháº©m vÃ o giá»** | `POST /api/v1/gio-hang/them-san-pham`                 | POST   |
| **Xem giá» hÃ ng cá»§a tÃ´i**    | `GET /api/v1/gio-hang/cua-toi`                        | GET    |
| **XÃ³a sáº£n pháº©m khá»i giá»** | `DELETE /api/v1/gio-hang/chi-tiet/{maChiTietGioHang}` | DELETE |
| **Xem khuyáº¿n mÃ£i há»£p lá»‡** | `GET /api/v1/gio-hang/khuyen-mai-hop-le`              | GET    |
| **Xem trÆ°á»›c giáº£m giÃ¡**     | `POST /api/v1/gio-hang/ap-dung-khuyen-mai`            | POST   |

**Request thÃªm SP vÃ o giá»:**

```json
{
  "maChiTietSanPham": 5,
  "soLuong": 2
}
```

**Logic Ä‘áº·c biá»‡t:** Náº¿u sáº£n pháº©m Ä‘Ã£ tá»“n táº¡i trong giá» â†’ tá»± Ä‘á»™ng cá»™ng thÃªm sá»‘ lÆ°á»£ng.

### 6.4 Äáº·t hÃ ng online

| Chá»©c nÄƒng                                                          | Endpoint                                             | Method |
| --------------------------------------------------------------------- | ---------------------------------------------------- | ------ |
| **Táº¡o Ä‘Æ¡n hÃ ng online**                                          | `POST /api/v1/don-hang/online`                       | POST   |
| Xem danh sÃ¡ch Ä‘Æ¡n hÃ ng cá»§a tÃ´i                                 | `GET /api/v1/don-hang`                               | GET    |
| Xem chi tiáº¿t Ä‘Æ¡n hÃ ng                                            | `GET /api/v1/don-hang/{id}`                          | GET    |
| **Cáº­p nháº­t Ä‘Æ¡n hÃ ng** (há»§y Ä‘Æ¡n, cáº­p nháº­t Ä‘á»‹a chá»‰) | `PUT /api/v1/don-hang`                               | PUT    |
| Xem sáº£n pháº©m trong Ä‘Æ¡n hÃ ng                                    | `GET /api/v1/chi-tiet-don-hang/don-hang/{donHangId}` | GET    |
| Xem chi tiáº¿t dÃ²ng sáº£n pháº©m                                     | `GET /api/v1/chi-tiet-don-hang/{id}`                 | GET    |
| Xem táº¥t cáº£ chi tiáº¿t Ä‘Æ¡n hÃ ng                                 | `GET /api/v1/chi-tiet-don-hang`                      | GET    |

**Request táº¡o Ä‘Æ¡n hÃ ng online:**

```json
{
  "diaChi": "123 Nguyá»…n TrÃ£i, Q.1, TP.HCM",
  "sdt": "0901234567",
  "cuaHangId": 1,
  "maKhuyenMaiHoaDon": 1,
  "maKhuyenMaiDiem": 2
}
```

**Luá»“ng Ä‘áº·t hÃ ng online:**

1. KH thÃªm SP vÃ o giá» hÃ ng â†’ `POST /api/v1/gio-hang/them-san-pham`
2. KH xem giá» hÃ ng â†’ `GET /api/v1/gio-hang/cua-toi`
3. KH xem khuyáº¿n mÃ£i há»£p lá»‡ â†’ `GET /api/v1/gio-hang/khuyen-mai-hop-le`
4. KH Ã¡p dá»¥ng khuyáº¿n mÃ£i (xem trÆ°á»›c giáº£m giÃ¡) â†’ `POST /api/v1/gio-hang/ap-dung-khuyen-mai`
5. KH Ä‘áº·t hÃ ng â†’ `POST /api/v1/don-hang/online`
6. Há»‡ thá»‘ng tá»± Ä‘á»™ng: táº¡o Ä‘Æ¡n + chi tiáº¿t Ä‘Æ¡n â†’ giáº£m tá»“n kho â†’ xÃ³a giá» hÃ ng
7. KH theo dÃµi Ä‘Æ¡n â†’ `GET /api/v1/don-hang/{id}`
8. Khi Ä‘Æ¡n hoÃ n thÃ nh (trangThai=5) â†’ há»‡ thá»‘ng tá»± cá»™ng Ä‘iá»ƒm tÃ­ch lÅ©y

**Quy táº¯c tÃ­ch Ä‘iá»ƒm:** Má»—i 100.000 VND â†’ 10 Ä‘iá»ƒm. VÃ­ dá»¥: Ä‘Æ¡n 350.000Ä‘ â†’ 30 Ä‘iá»ƒm.

### 6.5 Xem khuyáº¿n mÃ£i (CHá»ˆ XEM)

| Chá»©c nÄƒng                      | Endpoint                                        | Method |
| --------------------------------- | ----------------------------------------------- | ------ |
| Xem khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n | `GET /api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | GET    |
| Xem khuyáº¿n mÃ£i theo Ä‘iá»ƒm    | `GET /api/v1/khuyen-mai-theo-diem` + `/{id}`    | GET    |

### 6.6 ÄÃ¡nh giÃ¡ sáº£n pháº©m

| Chá»©c nÄƒng                                | Endpoint                                                             | Method |
| ------------------------------------------- | -------------------------------------------------------------------- | ------ |
| Xem táº¥t cáº£ Ä‘Ã¡nh giÃ¡                  | `GET /api/v1/danh-gia-san-pham`                                      | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo ID                     | `GET /api/v1/danh-gia-san-pham/{id}`                                 | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo sáº£n pháº©m           | `GET /api/v1/danh-gia-san-pham/san-pham/{sanPhamId}`                 | GET    |
| Xem Ä‘Ã¡nh giÃ¡ theo chi tiáº¿t Ä‘Æ¡n hÃ ng | `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{chiTietDonHangId}` | GET    |
| **Xem Ä‘Ã¡nh giÃ¡ cá»§a tÃ´i**              | `GET /api/v1/danh-gia-san-pham/cua-toi`                              | GET    |
| **Táº¡o Ä‘Ã¡nh giÃ¡** (multipart/form-data) | `POST /api/v1/danh-gia-san-pham`                                     | POST   |
| **Cáº­p nháº­t Ä‘Ã¡nh giÃ¡** (cá»§a mÃ¬nh)  | `PUT /api/v1/danh-gia-san-pham/{id}`                                 | PUT    |
| **XÃ³a Ä‘Ã¡nh giÃ¡** (cá»§a mÃ¬nh)          | `DELETE /api/v1/danh-gia-san-pham/{id}`                              | DELETE |

**Äiá»u kiá»‡n táº¡o Ä‘Ã¡nh giÃ¡:**

- âœ… ÄÃ£ Ä‘Äƒng nháº­p báº±ng tÃ i khoáº£n khÃ¡ch hÃ ng
- âœ… Chi tiáº¿t Ä‘Æ¡n hÃ ng thuá»™c Ä‘Æ¡n hÃ ng **cá»§a mÃ¬nh**
- âœ… ÄÆ¡n hÃ ng á»Ÿ tráº¡ng thÃ¡i **5 (ÄÃ£ nháº­n hÃ ng)**
- âœ… ChÆ°a Ä‘Ã¡nh giÃ¡ dÃ²ng sáº£n pháº©m nÃ y (má»—i dÃ²ng chá»‰ Ä‘Ã¡nh giÃ¡ 1 láº§n)

**Request táº¡o Ä‘Ã¡nh giÃ¡ (multipart/form-data):**

```
chiTietDonHangId = 7
soSao            = 5      (1-5 sao)
ghiTru           = "Sáº£n pháº©m ráº¥t Ä‘áº¹p!"
file             = (áº£nh Ä‘Ã¡nh giÃ¡, tÃ¹y chá»n)
```

### 6.7 KhÃ¡ch hÃ ng KHÃ”NG CÃ“ quyá»n

- âŒ Táº¡o/Sá»­a/XÃ³a sáº£n pháº©m, danh má»¥c, hÃ¬nh áº£nh
- âŒ Quáº£n lÃ½ cá»­a hÃ ng, nhÃ  cung cáº¥p
- âŒ Quáº£n lÃ½ vai trÃ², quyá»n háº¡n
- âŒ Quáº£n lÃ½ phiáº¿u nháº­p
- âŒ Táº¡o/Sá»­a/XÃ³a khuyáº¿n mÃ£i
- âŒ Táº¡o Ä‘Æ¡n hÃ ng táº¡i quáº§y
- âŒ XÃ³a Ä‘Æ¡n hÃ ng
- âŒ Táº¡o/Sá»­a/XÃ³a chi tiáº¿t Ä‘Æ¡n hÃ ng
- âŒ Xem nhÃ  cung cáº¥p
- âŒ ÄÃ¡nh giÃ¡ sáº£n pháº©m cá»§a ngÆ°á»i khÃ¡c

---

## 7. Endpoint cÃ´ng khai (khÃ´ng cáº§n Ä‘Äƒng nháº­p)

| Endpoint                      | Method | MÃ´ táº£                                       |
| ----------------------------- | ------ | ---------------------------------------------- |
| `POST /api/v1/auth/login`     | POST   | ÄÄƒng nháº­p                                   |
| `POST /api/v1/auth/register`  | POST   | ÄÄƒng kÃ½ tÃ i khoáº£n khÃ¡ch hÃ ng            |
| `GET /api/v1/auth/refresh`    | GET    | Gia háº¡n access token (cáº§n refresh cookie)  |
| `GET {secure_url_cloudinary}` | GET    | Xem áº£nh sáº£n pháº©m (public, cache 1 ngÃ y) |
| `GET /v3/api-docs/**`         | GET    | Swagger API documentation                      |
| `GET /swagger-ui/**`          | GET    | Swagger UI                                     |

---

## 8. Báº£ng ma tráº­n phÃ¢n quyá»n chi tiáº¿t

### 8.1 Sáº£n pháº©m & Danh má»¥c

| Module          | Endpoint                     | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| --------------- | ---------------------------- | ------ | ----- | --------- | ---------- |
| Sáº£n pháº©m    | `/api/v1/san-pham`           | GET    | âœ…   | âœ…       | âœ…        |
| Sáº£n pháº©m    | `/api/v1/san-pham/{id}`      | GET    | âœ…   | âœ…       | âœ…        |
| Sáº£n pháº©m    | `/api/v1/san-pham`           | POST   | âœ…   | âŒ        | âŒ         |
| Sáº£n pháº©m    | `/api/v1/san-pham`           | PUT    | âœ…   | âŒ        | âŒ         |
| Sáº£n pháº©m    | `/api/v1/san-pham/{id}`      | DELETE | âœ…   | âŒ        | âŒ         |
| Kiá»ƒu SP       | `/api/v1/kieu-san-pham`      | GET    | âœ…   | âœ…       | âœ…        |
| Kiá»ƒu SP       | `/api/v1/kieu-san-pham`      | POST   | âœ…   | âŒ        | âŒ         |
| Kiá»ƒu SP       | `/api/v1/kieu-san-pham`      | PUT    | âœ…   | âŒ        | âŒ         |
| Kiá»ƒu SP       | `/api/v1/kieu-san-pham/{id}` | DELETE | âœ…   | âŒ        | âŒ         |
| BST             | `/api/v1/bo-suu-tap`         | GET    | âœ…   | âœ…       | âœ…        |
| BST             | `/api/v1/bo-suu-tap`         | POST   | âœ…   | âŒ        | âŒ         |
| BST             | `/api/v1/bo-suu-tap`         | PUT    | âœ…   | âŒ        | âŒ         |
| BST             | `/api/v1/bo-suu-tap/{id}`    | DELETE | âœ…   | âŒ        | âŒ         |
| ThÆ°Æ¡ng hiá»‡u | `/api/v1/thuong-hieu`        | GET    | âœ…   | âœ…       | âœ…        |
| ThÆ°Æ¡ng hiá»‡u | `/api/v1/thuong-hieu`        | CRUD   | âœ…   | âŒ        | âŒ         |
| MÃ u sáº¯c      | `/api/v1/mau-sac`            | GET    | âœ…   | âœ…       | âœ…        |
| MÃ u sáº¯c      | `/api/v1/mau-sac`            | CRUD   | âœ…   | âŒ        | âŒ         |
| KÃ­ch thÆ°á»›c  | `/api/v1/kich-thuoc`         | GET    | âœ…   | âœ…       | âœ…        |
| KÃ­ch thÆ°á»›c  | `/api/v1/kich-thuoc`         | CRUD   | âœ…   | âŒ        | âŒ         |

### 8.2 Chi tiáº¿t SP & HÃ¬nh áº£nh

| Module      | Endpoint                                         | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ----------- | ------------------------------------------------ | ------ | ----- | --------- | ---------- |
| CTSP        | `/api/v1/chi-tiet-san-pham`                      | GET    | âœ…   | âœ…       | âœ…        |
| CTSP        | `/api/v1/chi-tiet-san-pham/{id}`                 | GET    | âœ…   | âœ…       | âœ…        |
| CTSP        | `/api/v1/chi-tiet-san-pham/san-pham/{spId}`      | GET    | âœ…   | âœ…       | âœ…        |
| CTSP        | `/api/v1/chi-tiet-san-pham`                      | POST   | âœ…   | âŒ        | âŒ         |
| CTSP        | `/api/v1/chi-tiet-san-pham`                      | PUT    | âœ…   | âŒ        | âŒ         |
| CTSP        | `/api/v1/chi-tiet-san-pham/{id}`                 | DELETE | âœ…   | âŒ        | âŒ         |
| HÃ¬nh áº£nh | `/api/v1/hinh-anh`, `/{id}`, `/chi-tiet-sp/{id}` | GET    | âœ…   | âœ…       | âœ…        |
| HÃ¬nh áº£nh | `/api/v1/hinh-anh` + upload                      | POST   | âœ…   | âŒ        | âŒ         |
| HÃ¬nh áº£nh | `/api/v1/hinh-anh`                               | PUT    | âœ…   | âŒ        | âŒ         |
| HÃ¬nh áº£nh | `/api/v1/hinh-anh/{id}`                          | DELETE | âœ…   | âŒ        | âŒ         |

### 8.3 Giá» hÃ ng

| Endpoint                                   | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ------------------------------------------ | ------ | ----- | --------- | ---------- |
| `POST /api/v1/gio-hang/them-san-pham`      | POST   | âœ…   | âŒ        | âœ…        |
| `GET /api/v1/gio-hang/cua-toi`             | GET    | âœ…   | âŒ        | âœ…        |
| `DELETE /api/v1/gio-hang/chi-tiet/{id}`    | DELETE | âœ…   | âŒ        | âœ…        |
| `GET /api/v1/gio-hang/khuyen-mai-hop-le`   | GET    | âœ…   | âŒ        | âœ…        |
| `POST /api/v1/gio-hang/ap-dung-khuyen-mai` | POST   | âœ…   | âŒ        | âœ…        |

### 8.4 ÄÆ¡n hÃ ng

| Endpoint                                      | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| --------------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/don-hang`                        | GET    | âœ…   | âœ…       | âœ…        |
| `GET /api/v1/don-hang/{id}`                   | GET    | âœ…   | âœ…       | âœ…        |
| `POST /api/v1/don-hang/online`                | POST   | âœ…   | âŒ        | âœ…        |
| `POST /api/v1/don-hang/tai-quay`              | POST   | âœ…   | âœ…       | âŒ         |
| `PUT /api/v1/don-hang`                        | PUT    | âœ…   | âœ…       | âœ…        |
| `DELETE /api/v1/don-hang/{id}`                | DELETE | âœ…   | âŒ        | âŒ         |
| `GET /api/v1/chi-tiet-don-hang`               | GET    | âœ…   | âœ…       | âœ…        |
| `GET /api/v1/chi-tiet-don-hang/don-hang/{id}` | GET    | âœ…   | âœ…       | âœ…        |
| `GET /api/v1/chi-tiet-don-hang/{id}`          | GET    | âœ…   | âœ…       | âœ…        |
| `POST /api/v1/chi-tiet-don-hang`              | POST   | âœ…   | âŒ        | âŒ         |
| `PUT /api/v1/chi-tiet-don-hang`               | PUT    | âœ…   | âŒ        | âŒ         |
| `DELETE /api/v1/chi-tiet-don-hang/{id}`       | DELETE | âœ…   | âŒ        | âŒ         |

### 8.5 Phiáº¿u nháº­p

| Endpoint                                                         | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ---------------------------------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/phieu-nhap`                                         | GET    | âœ…   | âœ…       | âŒ         |
| `GET /api/v1/phieu-nhap/{id}`                                    | GET    | âœ…   | âœ…       | âŒ         |
| `POST /api/v1/phieu-nhap`                                        | POST   | âœ…   | âœ…       | âŒ         |
| `PUT /api/v1/phieu-nhap`                                         | PUT    | âœ…   | âœ…       | âŒ         |
| `PUT /api/v1/phieu-nhap/kiem-ke/{id}`                            | PUT    | âœ…   | âœ…       | âŒ         |
| `DELETE /api/v1/phieu-nhap/{id}`                                 | DELETE | âœ…   | âŒ        | âŒ         |
| `GET /api/v1/chi-tiet-phieu-nhap` + `/{id}` + `/phieu-nhap/{id}` | GET    | âœ…   | âœ…       | âŒ         |
| `POST /api/v1/chi-tiet-phieu-nhap`                               | POST   | âœ…   | âœ…       | âŒ         |
| `PUT /api/v1/chi-tiet-phieu-nhap`                                | PUT    | âœ…   | âœ…       | âŒ         |
| `DELETE /api/v1/chi-tiet-phieu-nhap/{id}`                        | DELETE | âœ…   | âŒ        | âŒ         |

### 8.6 Cá»­a hÃ ng & NhÃ  cung cáº¥p

| Endpoint                               | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| -------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/cua-hang` + `/{id}`       | GET    | âœ…   | âœ…       | âœ…        |
| `POST/PUT/DELETE /api/v1/cua-hang`     | CRUD   | âœ…   | âŒ        | âŒ         |
| `GET /api/v1/nha-cung-cap` + `/{id}`   | GET    | âœ…   | âŒ        | âŒ         |
| `POST/PUT/DELETE /api/v1/nha-cung-cap` | CRUD   | âœ…   | âŒ        | âŒ         |

### 8.7 Khuyáº¿n mÃ£i

| Endpoint                                        | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ----------------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET /api/v1/khuyen-mai-theo-hoa-don` + `/{id}` | GET    | âœ…   | âœ…       | âœ…        |
| `POST/PUT/DELETE khuyen-mai-theo-hoa-don`       | CRUD   | âœ…   | âŒ        | âŒ         |
| `GET /api/v1/khuyen-mai-theo-diem` + `/{id}`    | GET    | âœ…   | âœ…       | âœ…        |
| `POST/PUT/DELETE khuyen-mai-theo-diem`          | CRUD   | âœ…   | âŒ        | âŒ         |

### 8.8 Vai trÃ² & Quyá»n háº¡n

| Endpoint                                  | Method | ADMIN | NHAN_VIEN | KHACH_HANG |
| ----------------------------------------- | ------ | ----- | --------- | ---------- |
| `GET/POST/PUT/DELETE /api/v1/roles`       | CRUD   | âœ…   | âŒ        | âŒ         |
| `GET/POST/PUT/DELETE /api/v1/permissions` | CRUD   | âœ…   | âŒ        | âŒ         |

### 8.9 ÄÃ¡nh giÃ¡ sáº£n pháº©m

| Endpoint                                               | Method | ADMIN            | NHAN_VIEN | KHACH_HANG        |
| ------------------------------------------------------ | ------ | ---------------- | --------- | ----------------- |
| `GET /api/v1/danh-gia-san-pham`                        | GET    | âœ…              | âœ…       | âœ…               |
| `GET /api/v1/danh-gia-san-pham/{id}`                   | GET    | âœ…              | âœ…       | âœ…               |
| `GET /api/v1/danh-gia-san-pham/san-pham/{spId}`        | GET    | âœ…              | âœ…       | âœ…               |
| `GET /api/v1/danh-gia-san-pham/chi-tiet-don-hang/{id}` | GET    | âœ…              | âœ…       | âœ…               |
| `GET /api/v1/danh-gia-san-pham/cua-toi`                | GET    | âŒ               | âŒ        | âœ…               |
| `POST /api/v1/danh-gia-san-pham`                       | POST   | âŒ               | âŒ        | âœ…               |
| `PUT /api/v1/danh-gia-san-pham/{id}`                   | PUT    | âŒ               | âŒ        | âœ… (cá»§a mÃ¬nh) |
| `DELETE /api/v1/danh-gia-san-pham/{id}`                | DELETE | âœ… (táº¥t cáº£) | âŒ        | âœ… (cá»§a mÃ¬nh) |

---

## 9. Gá»£i Ã½ xÃ¢y dá»±ng Frontend theo vai trÃ²

### 9.1 Trang ADMIN (Dashboard quáº£n trá»‹)

**Sidebar Menu gá»£i Ã½:**

```
ðŸ“Š Dashboard
ðŸ“¦ Quáº£n lÃ½ sáº£n pháº©m
   â”œâ”€â”€ Sáº£n pháº©m
   â”œâ”€â”€ Chi tiáº¿t sáº£n pháº©m (biáº¿n thá»ƒ)
   â”œâ”€â”€ HÃ¬nh áº£nh sáº£n pháº©m
   â”œâ”€â”€ Kiá»ƒu sáº£n pháº©m
   â”œâ”€â”€ Bá»™ sÆ°u táº­p
   â”œâ”€â”€ ThÆ°Æ¡ng hiá»‡u
   â”œâ”€â”€ MÃ u sáº¯c
   â””â”€â”€ KÃ­ch thÆ°á»›c
ðŸ›’ Quáº£n lÃ½ Ä‘Æ¡n hÃ ng
   â”œâ”€â”€ Danh sÃ¡ch Ä‘Æ¡n hÃ ng
   â”œâ”€â”€ Táº¡o Ä‘Æ¡n táº¡i quáº§y
   â””â”€â”€ Chi tiáº¿t Ä‘Æ¡n hÃ ng
ðŸ“¥ Quáº£n lÃ½ nháº­p hÃ ng
   â”œâ”€â”€ Phiáº¿u nháº­p
   â”œâ”€â”€ Chi tiáº¿t phiáº¿u nháº­p
   â””â”€â”€ Kiá»ƒm kÃª
ðŸª Há»‡ thá»‘ng
   â”œâ”€â”€ Cá»­a hÃ ng
   â”œâ”€â”€ NhÃ  cung cáº¥p
   â””â”€â”€ NhÃ¢n viÃªn (*)
ðŸŽ Khuyáº¿n mÃ£i
   â”œâ”€â”€ KM theo hÃ³a Ä‘Æ¡n
   â””â”€â”€ KM theo Ä‘iá»ƒm
â­ ÄÃ¡nh giÃ¡ sáº£n pháº©m
ðŸ” PhÃ¢n quyá»n
   â”œâ”€â”€ Vai trÃ²
   â””â”€â”€ Quyá»n háº¡n
ðŸ‘¤ TÃ i khoáº£n
```

### 9.2 Trang NHAN_VIEN (NhÃ¢n viÃªn bÃ¡n hÃ ng)

**Sidebar Menu gá»£i Ã½:**

```
ðŸ“Š Dashboard
ðŸ“¦ Sáº£n pháº©m (chá»‰ xem)
   â”œâ”€â”€ Danh sÃ¡ch sáº£n pháº©m
   â””â”€â”€ Chi tiáº¿t sáº£n pháº©m
ðŸ›’ ÄÆ¡n hÃ ng
   â”œâ”€â”€ Danh sÃ¡ch Ä‘Æ¡n hÃ ng
   â”œâ”€â”€ Táº¡o Ä‘Æ¡n táº¡i quáº§y
   â””â”€â”€ Cáº­p nháº­t tráº¡ng thÃ¡i
ðŸ“¥ Nháº­p hÃ ng
   â”œâ”€â”€ Phiáº¿u nháº­p (táº¡o/sá»­a)
   â”œâ”€â”€ Chi tiáº¿t phiáº¿u nháº­p (táº¡o/sá»­a)
   â””â”€â”€ Kiá»ƒm kÃª
ðŸŽ Khuyáº¿n mÃ£i (chá»‰ xem)
â­ ÄÃ¡nh giÃ¡ sáº£n pháº©m (chá»‰ xem)
ðŸ‘¤ TÃ i khoáº£n
```

### 9.3 Trang KHACH_HANG (Giao diá»‡n mua sáº¯m)

**Navigation gá»£i Ã½:**

```
ðŸ  Trang chá»§
ðŸ” TÃ¬m kiáº¿m / Lá»c sáº£n pháº©m
   â”œâ”€â”€ Theo danh má»¥c (kiá»ƒu SP)
   â”œâ”€â”€ Theo thÆ°Æ¡ng hiá»‡u
   â”œâ”€â”€ Theo bá»™ sÆ°u táº­p
   â”œâ”€â”€ Theo khoáº£ng giÃ¡
   â””â”€â”€ Theo mÃ u sáº¯c / kÃ­ch thÆ°á»›c
ðŸ“„ Chi tiáº¿t sáº£n pháº©m
   â”œâ”€â”€ Chá»n mÃ u sáº¯c / kÃ­ch thÆ°á»›c
   â”œâ”€â”€ Xem Ä‘Ã¡nh giÃ¡
   â””â”€â”€ ThÃªm vÃ o giá»
ðŸ›’ Giá» hÃ ng
   â”œâ”€â”€ Xem giá» hÃ ng
   â”œâ”€â”€ Cáº­p nháº­t sá»‘ lÆ°á»£ng
   â””â”€â”€ XÃ³a sáº£n pháº©m
ðŸ’³ Äáº·t hÃ ng (checkout)
   â”œâ”€â”€ Chá»n khuyáº¿n mÃ£i hÃ³a Ä‘Æ¡n
   â”œâ”€â”€ Äá»•i Ä‘iá»ƒm láº¥y KM
   â””â”€â”€ XÃ¡c nháº­n Ä‘Æ¡n hÃ ng
ðŸ“‹ ÄÆ¡n hÃ ng cá»§a tÃ´i
   â”œâ”€â”€ Theo dÃµi tráº¡ng thÃ¡i
   â”œâ”€â”€ Há»§y Ä‘Æ¡n (khi chá» xÃ¡c nháº­n)
   â””â”€â”€ ÄÃ¡nh giÃ¡ sáº£n pháº©m (khi Ä‘Ã£ nháº­n)
â­ ÄÃ¡nh giÃ¡ cá»§a tÃ´i
   â”œâ”€â”€ Xem danh sÃ¡ch Ä‘Ã¡nh giÃ¡
   â”œâ”€â”€ Sá»­a Ä‘Ã¡nh giÃ¡
   â””â”€â”€ XÃ³a Ä‘Ã¡nh giÃ¡
ðŸ‘¤ TÃ i khoáº£n
   â”œâ”€â”€ ThÃ´ng tin cÃ¡ nhÃ¢n
   â”œâ”€â”€ Äiá»ƒm tÃ­ch lÅ©y
   â””â”€â”€ ÄÄƒng xuáº¥t
```

### 9.4 CÃ¡ch xá»­ lÃ½ phÃ¢n quyá»n Ä‘á»™ng trÃªn FE

Khi Ä‘Äƒng nháº­p thÃ nh cÃ´ng, FE nháº­n Ä‘Æ°á»£c thÃ´ng tin user bao gá»“m `role`. Äá»ƒ render menu/chá»©c nÄƒng:

**CÃ¡ch 1: Hardcode theo tÃªn role**

```javascript
const role = user.role.name; // "ADMIN" | "NHAN_VIEN" | "KHACH_HANG"

if (role === "ADMIN") {
  // Hiá»‡n táº¥t cáº£ menu
} else if (role === "NHAN_VIEN") {
  // Hiá»‡n menu nhÃ¢n viÃªn
} else if (role === "KHACH_HANG") {
  // Hiá»‡n menu khÃ¡ch hÃ ng
}
```

**CÃ¡ch 2: Dá»±a trÃªn danh sÃ¡ch permissions (linh hoáº¡t hÆ¡n)**

```javascript
// Láº¥y thÃ´ng tin tÃ i khoáº£n + permissions
const account = await fetch("/api/v1/auth/account");
const permissions = account.role.permissions;

// Kiá»ƒm tra quyá»n trÆ°á»›c khi hiá»‡n nÃºt/menu
function hasPermission(apiPath, method) {
  return permissions.some((p) => p.apiPath === apiPath && p.method === method);
}

// VÃ­ dá»¥: chá»‰ hiá»‡n nÃºt "Táº¡o sáº£n pháº©m" náº¿u cÃ³ quyá»n
if (hasPermission("/api/v1/san-pham", "POST")) {
  showButton("Táº¡o sáº£n pháº©m");
}
```

### 9.5 Xá»­ lÃ½ lá»—i phÃ¢n quyá»n trÃªn FE

| HTTP Status | Ã nghÄ©a                                | HÃ nh Ä‘á»™ng FE                                              |
| ----------- | --------------------------------------- | ------------------------------------------------------------- |
| `401`       | ChÆ°a Ä‘Äƒng nháº­p / token háº¿t háº¡n | Redirect â†’ trang Ä‘Äƒng nháº­p hoáº·c tá»± refresh token    |
| `403`       | KhÃ´ng cÃ³ quyá»n                       | Hiá»ƒn thá»‹ thÃ´ng bÃ¡o "Báº¡n khÃ´ng cÃ³ quyá»n truy cáº­p" |
| `400`       | Lá»—i nghiá»‡p vá»¥                     | Hiá»ƒn thá»‹ message lá»—i tá»« response                      |
| `500`       | Lá»—i server                            | Hiá»ƒn thá»‹ thÃ´ng bÃ¡o lá»—i chung                          |

**Cáº¥u trÃºc response lá»—i:**

```json
{
  "statusCode": 400,
  "error": "Bad Request",
  "message": "KhÃ´ng tÃ¬m tháº¥y sáº£n pháº©m"
}
```

---

> **TÃ i liá»‡u chi tiáº¿t tá»«ng module:** Xem cÃ¡c file `01_Auth.md` â†’ `22_DanhGiaSanPham.md` trong thÆ° má»¥c `document/`.
