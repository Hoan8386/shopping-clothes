package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoaiKiemKeDTO {
    private Long id;
    private String tenLoaiKiemKe;
    private String moTa;
    private Integer trangThai;
}
