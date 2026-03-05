package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResDonHangDTO {
    private Long id;
    private String diaChi;
    private String sdt;
    private Integer tongTien;
    private Integer tienGiam;
    private Integer tongTienGiam;
    private Integer tongTienTra;
    private String trangThai;
    private String trangThaiThanhToan;
    private String hinhThucDonHang;
    private KhuyenMaiHoaDonDTO khuyenMaiHoaDon;
    private KhuyenMaiDiemDTO khuyenMaiDiem;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    private CuaHangDTO cuaHang;
    private KhachHangDTO khachHang;
    private NhanVienDTO nhanVien;
    private List<ChiTietDonHangDTO> chiTietDonHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CuaHangDTO {
        private Long id;
        private String tenCuaHang;
        private String diaChi;
        private String soDienThoai;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KhachHangDTO {
        private Long id;
        private String tenKhachHang;
        private String sdt;
        private String email;
        private Integer diemTichLuy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NhanVienDTO {
        private Long id;
        private String tenNhanVien;
        private String email;
        private String soDienThoai;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietDonHangDTO {
        private Long id;
        private Long chiTietSanPhamId;
        private String tenSanPham;
        private String hinhAnhChinh;
        private String tenMauSac;
        private String tenKichThuoc;
        private Double giaSanPham;
        private Double giamGia;
        private Double giaGiam;
        private Integer soLuong;
        private Double thanhTien;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KhuyenMaiHoaDonDTO {
        private Long id;
        private String tenKhuyenMai;
        private Double phanTramGiam;
        private Integer giamToiDa;
        private Integer hoaDonToiDa;
        private Integer tienDaGiam;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KhuyenMaiDiemDTO {
        private Long id;
        private String tenKhuyenMai;
        private Double phanTramGiam;
        private Integer giamToiDa;
        private Integer hoaDonToiDa;
        private Integer diemToiThieu;
        private Integer tienDaGiam;
    }
}
