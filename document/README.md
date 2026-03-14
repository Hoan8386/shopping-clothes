# Shopping API Documentation

> TÃ i liá»‡u mÃ´ táº£ táº¥t cáº£ cÃ¡c API endpoints cá»§a há»‡ thá»‘ng Shopping.  
> Base URL: `/api/v1`

---

## Má»¥c lá»¥c

| #   | Controller                    | File tÃ i liá»‡u                                          | MÃ´ táº£                                                          |
| --- | ----------------------------- | --------------------------------------------------------- | ----------------------------------------------------------------- |
| 1   | AuthController                | [01_Auth.md](01_Auth.md)                                  | XÃ¡c thá»±c: ÄÄƒng nháº­p, ÄÄƒng kÃ½, Refresh Token, ÄÄƒng xuáº¥t |
| 2   | SanPhamController             | [02_SanPham.md](02_SanPham.md)                            | Quáº£n lÃ½ sáº£n pháº©m                                           |
| 3   | ChiTietSanPhamController      | [03_ChiTietSanPham.md](03_ChiTietSanPham.md)              | Quáº£n lÃ½ chi tiáº¿t sáº£n pháº©m (biáº¿n thá»ƒ)                 |
| 4   | DonHangController             | [04_DonHang.md](04_DonHang.md)                            | Quáº£n lÃ½ Ä‘Æ¡n hÃ ng                                            |
| 5   | ChiTietDonHangController      | [05_ChiTietDonHang.md](05_ChiTietDonHang.md)              | Quáº£n lÃ½ chi tiáº¿t Ä‘Æ¡n hÃ ng                                 |
| 6   | GioHangController             | [06_GioHang.md](06_GioHang.md)                            | Quáº£n lÃ½ giá» hÃ ng                                             |
| 7   | PhieuNhapController           | [07_PhieuNhap.md](07_PhieuNhap.md)                        | Quáº£n lÃ½ phiáº¿u nháº­p hÃ ng                                   |
| 8   | ChiTietPhieuNhapController    | [08_ChiTietPhieuNhap.md](08_ChiTietPhieuNhap.md)          | Quáº£n lÃ½ chi tiáº¿t phiáº¿u nháº­p                              |
| 9   | HinhAnhController             | [09_HinhAnh.md](09_HinhAnh.md)                            | Quáº£n lÃ½ hÃ¬nh áº£nh sáº£n pháº©m                               |
| 10  | BoSuuTapController            | [10_BoSuuTap.md](10_BoSuuTap.md)                          | Quáº£n lÃ½ bá»™ sÆ°u táº­p                                        |
| 11  | KieuSanPhamController         | [11_KieuSanPham.md](11_KieuSanPham.md)                    | Quáº£n lÃ½ kiá»ƒu sáº£n pháº©m                                    |
| 12  | ThuongHieuController          | [12_ThuongHieu.md](12_ThuongHieu.md)                      | Quáº£n lÃ½ thÆ°Æ¡ng hiá»‡u                                        |
| 13  | MauSacController              | [13_MauSac.md](13_MauSac.md)                              | Quáº£n lÃ½ mÃ u sáº¯c                                             |
| 14  | KichThuocController           | [14_KichThuoc.md](14_KichThuoc.md)                        | Quáº£n lÃ½ kÃ­ch thÆ°á»›c                                         |
| 15  | CuaHangController             | [15_CuaHang.md](15_CuaHang.md)                            | Quáº£n lÃ½ cá»­a hÃ ng                                            |
| 16  | NhaCungCapController          | [16_NhaCungCap.md](16_NhaCungCap.md)                      | Quáº£n lÃ½ nhÃ  cung cáº¥p                                        |
| 17  | RoleController                | [17_Role.md](17_Role.md)                                  | Quáº£n lÃ½ vai trÃ²                                               |
| 18  | PermissionController          | [18_Permission.md](18_Permission.md)                      | Quáº£n lÃ½ quyá»n háº¡n                                           |
| 20  | KhuyenMaiTheoHoaDonController | [Khuyáº¿n MÃ£i Theo HÃ³a ÄÆ¡n](20_KhuyenMaiTheoHoaDon.md) | Quáº£n lÃ½ khuyáº¿n mÃ£i theo hÃ³a Ä‘Æ¡n                          |
| 21  | KhuyenMaiTheoDiemController   | [Khuyáº¿n MÃ£i Theo Äiá»ƒm](21_KhuyenMaiTheoDiem.md)      | Quáº£n lÃ½ khuyáº¿n mÃ£i theo Ä‘iá»ƒm tÃ­ch lÅ©y                  |
| 22  | DanhGiaSanPhamController      | [22_DanhGiaSanPham.md](22_DanhGiaSanPham.md)              | Quáº£n lÃ½ Ä‘Ã¡nh giÃ¡ sáº£n pháº©m                               |
| 23  | NhanVienController            | [23_NhanVien.md](23_NhanVien.md)                          | Quáº£n lÃ½ nhÃ¢n viÃªn                                            |

---

## XÃ¡c thá»±c

Há»‡ thá»‘ng sá»­ dá»¥ng **JWT (JSON Web Token)** Ä‘á»ƒ xÃ¡c thá»±c:

1. Gá»i `POST /api/v1/auth/login` Ä‘á»ƒ láº¥y `accessToken`
2. ÄÃ­nh kÃ¨m token vÃ o header: `Authorization: Bearer <accessToken>`
3. Khi token háº¿t háº¡n, gá»i `GET /api/v1/auth/refresh` (sá»­ dá»¥ng cookie `refresh_token`)
4. ÄÄƒng xuáº¥t báº±ng `POST /api/v1/auth/logout`

**Endpoints khÃ´ng yÃªu cáº§u xÃ¡c thá»±c:**

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `GET /api/v1/auth/refresh`
- `GET {secure_url_cloudinary}`

---

## MÃ£ lá»—i chung

| HTTP Status        | MÃ´ táº£                                  |
| ------------------ | ----------------------------------------- |
| `200 OK`           | ThÃ nh cÃ´ng                              |
| `201 Created`      | Táº¡o má»›i thÃ nh cÃ´ng                  |
| `204 No Content`   | XÃ³a thÃ nh cÃ´ng                         |
| `400 Bad Request`  | Dá»¯ liá»‡u Ä‘áº§u vÃ o khÃ´ng há»£p lá»‡ |
| `401 Unauthorized` | ChÆ°a xÃ¡c thá»±c / Token háº¿t háº¡n     |
| `403 Forbidden`    | KhÃ´ng cÃ³ quyá»n truy cáº­p              |
| `404 Not Found`    | KhÃ´ng tÃ¬m tháº¥y tÃ i nguyÃªn           |
