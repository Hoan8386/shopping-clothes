package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResKhachHangLookupDTO {
    private Long id;
    private String tenKhachHang;
    private String sdt;
    private String email;
    private Integer diemTichLuy;
}
