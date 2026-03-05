package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChiTietDonHangDTO {
    private Long id;
    private Long donHangId;
    private Long chiTietSanPhamId;
    private Double giaSanPham;
    private Double giamGia;
    private Double giaGiam;
    private Integer soLuong;
    private Double thanhTien;
}
