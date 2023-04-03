package com.jjzhong.mall.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 注册请求
 */
public class RegisterReq extends HandleUserReq {
    @NotBlank(message = "email不能为空")
    @Email(message = "email不合法")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
