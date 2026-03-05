package com.vn.shopping.domain.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResChiTietDonHangDTO {
    private Long id;
    private Double giaSanPham;
    private Double giamGia;
    private Double giaGiam;
    private Integer soLuong;
    private Double thanhTien;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    private DonHangDTO donHang;
    private ChiTietSanPhamDTO chiTietSanPham;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonHangDTO {
        private Long id;
        private String diaChi;
        private String sdt;
        private Integer trangThai;
        private Integer tongTienTra;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietSanPhamDTO {
        private Long id;
        private String tenSanPham;
        private String hinhAnhChinh;
        private String tenMauSac;
        private String tenKichThuoc;
        private Integer soLuong;
    }
}
