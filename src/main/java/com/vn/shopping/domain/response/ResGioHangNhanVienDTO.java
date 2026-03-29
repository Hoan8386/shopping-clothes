package com.vn.shopping.domain.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResGioHangNhanVienDTO {
    private Long id;
    private Integer trangThai;

    private String tenNguoiMua;
    private String sdt;

    private Long khachHangId;
    private String tenKhachHang;
    private String emailKhachHang;
    private Integer diemTichLuy;

    private Long maKhuyenMaiHoaDon;
    private Long maKhuyenMaiDiem;

    private Integer tongSoLuong;
    private Integer tongTienGoc;
    private Integer tienGiamHoaDon;
    private Integer tienGiamDiem;
    private Integer tongTienGiam;
    private Integer tongTienThanhToan;

    private List<ChiTietDTO> chiTietGioHangs;
    private List<KhuyenMaiHoaDonDTO> khuyenMaiHoaDonHopLe;
    private List<KhuyenMaiDiemDTO> khuyenMaiDiemHopLe;

    @Getter
    @Setter
    public static class ChiTietDTO {
        private Long id;
        private Long chiTietSanPhamId;
        private String maVach;
        private Long sanPhamId;
        private String tenSanPham;
        private String tenMauSac;
        private String tenKichThuoc;
        private Integer soLuong;
        private Integer tonKho;
        private Double giaBan;
        private Double thanhTien;
    }

    @Getter
    @Setter
    public static class KhuyenMaiHoaDonDTO {
        private Long id;
        private String tenKhuyenMai;
        private Double phanTramGiam;
        private Integer giamToiDa;
        private Integer hoaDonToiThieu;
    }

    @Getter
    @Setter
    public static class KhuyenMaiDiemDTO {
        private Long id;
        private String tenKhuyenMai;
        private Double phanTramGiam;
        private Integer giamToiDa;
        private Integer hoaDonToiThieu;
        private Integer diemToiThieu;
    }
}
