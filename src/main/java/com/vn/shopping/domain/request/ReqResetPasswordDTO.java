package com.vn.shopping.domain.request;

import jakarta.validation.constraints.NotBlank;

public class ReqResetPasswordDTO {
    @NotBlank(message = "token không được để trống")
    private String token;

    @NotBlank(message = "newPassword không được để trống")
    private String newPassword;

    @NotBlank(message = "confirmPassword không được để trống")
    private String confirmPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}