package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResGioHangDTO {

    private Long maGioHang;
    private int tongSoLuong;
    private BigDecimal tongTien;
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
        private String sku;
        private BigDecimal giaBan;
        private int soLuong;
        private BigDecimal thanhTien; // giaBan * soLuong
    }
}
