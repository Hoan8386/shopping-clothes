package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChiTietKiemKeDTO {
    private Long id;
    private Long chiTietSanPhamId;
    private Integer soLuongThucTe;
    private String ghiChu;
}
