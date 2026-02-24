package com.vn.shopping.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String tenKhachHang;
    private String email;
    private String sdt;
}