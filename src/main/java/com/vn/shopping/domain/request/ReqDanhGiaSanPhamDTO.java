package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqDanhGiaSanPhamDTO {
    private Long sanPhamId;
    private Long donHangId;
    private Integer soSao;
    private String ghiChu;
}
