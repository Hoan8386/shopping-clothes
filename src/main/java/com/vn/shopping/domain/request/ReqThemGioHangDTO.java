package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqThemGioHangDTO {

    private Long maKhachHang;

    private Long maChiTietSanPham;

    private Integer soLuong;
}
