package com.vn.shopping.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private String maKhachHang;
    private String tenKhachHang;
    private String email;
    private String sdt;
}