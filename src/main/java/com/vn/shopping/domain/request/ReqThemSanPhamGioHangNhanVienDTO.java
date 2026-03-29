package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqThemSanPhamGioHangNhanVienDTO {
    private Long chiTietSanPhamId;
    private String maVach;
    private Integer soLuong;
}
