package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqDanhGiaSanPhamDTO {
    private Long chiTietDonHangId;
    private Integer soSao;
    private String ghiTru;
}
