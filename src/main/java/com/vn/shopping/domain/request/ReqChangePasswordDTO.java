package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChangePasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
