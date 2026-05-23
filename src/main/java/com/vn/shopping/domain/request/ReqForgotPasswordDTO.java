package com.vn.shopping.domain.request;

import jakarta.validation.constraints.NotBlank;

public class ReqForgotPasswordDTO {
    @NotBlank(message = "email không được để trống")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}