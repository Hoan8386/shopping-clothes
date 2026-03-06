package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResChiTietPhieuNhapDTO {
    private Long id;
    private Long phieuNhapId;
    private String tenPhieuNhap;
    private Integer soLuong;
    private Integer soLuongThieu;
    private String ghiTru;
    private String ghiTruKiemHang;
    private Integer trangThai;
    private String trangThaiText;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    private ChiTietSanPhamDTO chiTietSanPham;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietSanPhamDTO {
        private Long id;
        private String tenSanPham;
        private String tenMauSac;
        private String tenKichThuoc;
        private Integer soLuong;
    }
}
