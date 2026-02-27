package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResGioHangDTO {

    private Long maGioHang;
    private int tongSoLuong;
    private Double tongTien;
    private List<ChiTietGioHangDTO> chiTietGioHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietGioHangDTO {
        private Long maChiTietGioHang;
        private Long maChiTietSanPham;
        private String tenSanPham;
        private String kichThuoc;
        private String mauSac;
        private Double giaBan;
        private int soLuong;
        private Double thanhTien; // giaBan * soLuong
    }
}
