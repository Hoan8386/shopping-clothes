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
public class ResPhieuNhapDTO {
    private Long id;
    private String tenPhieuNhap;
    private Integer trangThai;
    private String trangThaiText;
    private LocalDateTime ngayDatHang;
    private LocalDateTime ngayNhanHang;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    private CuaHangDTO cuaHang;
    private NhaCungCapDTO nhaCungCap;
    private List<ChiTietPhieuNhapDTO> chiTietPhieuNhaps;

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
    public static class NhaCungCapDTO {
        private Long id;
        private String tenNhaCungCap;
        private String soDienThoai;
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietPhieuNhapDTO {
        private Long id;
        private Long chiTietSanPhamId;
        private String tenSanPham;
        private String tenMauSac;
        private String tenKichThuoc;
        private Integer soLuong;
        private String ghiTru;
        private String ghiTruKiemHang;
        private Integer trangThai;
    }
}
