# Kiểm Kê Hàng Hóa Controller

> Base Path: `/api/v1/kiem-ke-hang-hoa`
> File: `KiemKeHangHoaController.java`
> Nhân viên tạo/nhập phiếu kiểm kê, admin duyệt hoặc yêu cầu kiểm kê lại.

---

## Cấu trúc dữ liệu phiếu kiểm kê

| Trường                | Kiểu          | Mô tả                             |
| --------------------- | ------------- | --------------------------------- |
| `id`                  | Long          | Mã phiếu kiểm kê (auto-increment) |
| `tenPhieuKiemKe`      | String(255)   | Tên phiếu                         |
| `loaiKiemKe`          | Object        | Loại kiểm kê                      |
| `cuaHang`             | Object        | Cửa hàng kiểm kê                  |
| `nhanVienTao`         | Object        | Nhân viên tạo phiếu               |
| `nhanVienDuyet`       | Object        | Admin duyệt                       |
| `trangThai`           | Integer       | Trạng thái phiếu                  |
| `ghiChu`              | String        | Ghi chú chung                     |
| `lyDoYeuCauKiemKeLai` | String        | Lý do admin yêu cầu kiểm kê lại   |
| `ngayKiemKe`          | LocalDateTime | Ngày kiểm kê                      |
| `ngayXacNhan`         | LocalDateTime | Ngày admin xác nhận               |
| `chiTietKiemKes`      | List          | Danh sách chi tiết kiểm kê        |

## Mã trạng thái phiếu

| Giá trị | Ý nghĩa             |
| ------- | ------------------- |
| `0`     | Nháp                |
| `1`     | Chờ duyệt           |
| `2`     | Yêu cầu kiểm kê lại |
| `3`     | Đã xác nhận         |

## Luồng nghiệp vụ

1. Nhân viên tạo phiếu (`trangThai = 0`).
2. Nhân viên điền số lượng thực tế từng dòng và gửi duyệt (`trangThai = 1`).
3. Admin duyệt:

- `YEU_CAU_KIEM_KE_LAI` -> phiếu về `trangThai = 2`.
- `XAC_NHAN` -> phiếu sang `trangThai = 3`, cập nhật tồn kho theo số lượng thực tế.

4. Khi xác nhận: tồn kho của `ChiTietSanPham` tại cửa hàng phiếu sẽ được ghi đè theo `soLuongThucTe`, đồng thời tổng tồn `SanPham.soLuong` được tính lại.

---

## 1. Lấy danh sách phiếu kiểm kê

- URL: `GET /api/v1/kiem-ke-hang-hoa`
- Quyền:
- `ADMIN`: xem tất cả
- `NHAN_VIEN`: xem phiếu của cửa hàng mình
- Response: `200 OK` + danh sách `ResKiemKeHangHoaDTO[]`

## 2. Lấy phiếu theo id

- URL: `GET /api/v1/kiem-ke-hang-hoa/{id}`
- Quyền: `ADMIN`, `NHAN_VIEN`
- Response: `200 OK`
- Lỗi: `400` nếu không tìm thấy id

## 3. Nhân viên tạo phiếu kiểm kê

- URL: `POST /api/v1/kiem-ke-hang-hoa`
- Quyền: `NHAN_VIEN` (ADMIN cũng có thể tạo)
- Body:

```json
{
  "tenPhieuKiemKe": "Kiểm kê kho cuối tháng 3",
  "loaiKiemKeId": 1,
  "cuaHangId": 1,
  "ghiChu": "Kiểm tra tồn kho thường kỳ",
  "ngayKiemKe": "2026-03-15T08:00:00",
  "chiTietKiemKes": [
    {
      "chiTietSanPhamId": 1,
      "soLuongThucTe": 120,
      "ghiChu": "Đầy đủ"
    },
    {
      "chiTietSanPhamId": 2,
      "soLuongThucTe": 95,
      "ghiChu": "Thiếu do hư hỏng"
    }
  ]
}
```

- Response: `201 Created`

## 4. Nhân viên cập nhật/nhập phiếu

- URL: `PUT /api/v1/kiem-ke-hang-hoa`
- Quyền: `NHAN_VIEN` tạo phiếu (`ADMIN` được phép)
- Chỉ cho phép khi phiếu đang ở `Nháp` hoặc `Yêu cầu kiểm kê lại`.
- Body giống tạo, bắt buộc có `id`.

## 5. Nhân viên gửi duyệt

- URL: `PUT /api/v1/kiem-ke-hang-hoa/{id}/gui-duyet`
- Quyền: `NHAN_VIEN` tạo phiếu (`ADMIN` được phép)
- Điều kiện:
- Phiếu phải có chi tiết
- Tất cả chi tiết đã có `soLuongThucTe`
- Response: `200 OK`, phiếu sang `Chờ duyệt`

## 6. Admin duyệt / yêu cầu kiểm kê lại

- URL: `PUT /api/v1/kiem-ke-hang-hoa/{id}/duyet`
- Quyền: `ADMIN`
- Body:

```json
{
  "hanhDong": "XAC_NHAN",
  "lyDo": ""
}
```

Hoặc:

```json
{
  "hanhDong": "YEU_CAU_KIEM_KE_LAI",
  "lyDo": "Vui lòng kiểm lại khu vực A"
}
```

- Response: `200 OK`
- Lỗi:
- `400` nếu hành động không hợp lệ
- `400` nếu yêu cầu kiểm kê lại mà không có lý do
- `400` nếu không phải `ADMIN`
