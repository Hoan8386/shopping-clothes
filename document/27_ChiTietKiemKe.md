# ChiTietKiemKe (Thuộc phiếu kiểm kê)

> Chi tiết kiểm kê được quản lý bên trong endpoint phiếu kiểm kê, không có controller riêng.
> Liên kết: `KiemKeHangHoa (1) - (n) ChiTietKiemKe`, và `ChiTietKiemKe (n) - (1) ChiTietSanPham`.

---

## Cấu trúc dữ liệu

| Trường           | Kiểu           | Mô tả                                             |
| ---------------- | -------------- | ------------------------------------------------- |
| `id`             | Long           | Mã chi tiết kiểm kê (auto increment)              |
| `kiemKeHangHoa`  | KiemKeHangHoa  | Phiếu kiểm kê cha                                 |
| `chiTietSanPham` | ChiTietSanPham | Biến thể sản phẩm được kiểm                       |
| `soLuongHeThong` | Integer        | Tồn kho hệ thống tại thời điểm tạo/cập nhật phiếu |
| `soLuongThucTe`  | Integer        | Số lượng đếm thực tế                              |
| `chenhLech`      | Integer        | `soLuongThucTe - soLuongHeThong`                  |
| `ghiChu`         | String(255)    | Ghi chú từng dòng                                 |
| `ngayTao`        | LocalDateTime  | Tự động sinh                                      |
| `ngayCapNhat`    | LocalDateTime  | Tự động cập nhật                                  |

## Quy tắc nghiệp vụ

1. Mỗi dòng chi tiết phải tham chiếu đến một `MaChiTietSanPham` hợp lệ.
2. Chi tiết sản phẩm phải thuộc đúng cửa hàng của phiếu kiểm kê.
3. Khi tạo/cập nhật phiếu:

- `soLuongHeThong` lấy theo tồn hiện tại của `ChiTietSanPham.soLuong`.
- `chenhLech` được tính tự động.

4. Khi admin `XAC_NHAN` phiếu:

- `ChiTietSanPham.soLuong = soLuongThucTe` cho từng dòng.
- Tổng số lượng của sản phẩm cha được tính lại.

## Ví dụ item trong request

```json
{
  "chiTietSanPhamId": 1,
  "soLuongThucTe": 120,
  "ghiChu": "Kệ trưng bày"
}
```

## Ví dụ item trong response

```json
{
  "id": 10,
  "chiTietSanPhamId": 1,
  "tenSanPham": "Áo Oxford",
  "tenMauSac": "Trắng",
  "tenKichThuoc": "M",
  "soLuongHeThong": 115,
  "soLuongThucTe": 120,
  "chenhLech": 5,
  "ghiChu": "Kệ trưng bày"
}
```
