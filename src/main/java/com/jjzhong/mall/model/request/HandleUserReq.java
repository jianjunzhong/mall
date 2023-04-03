package com.jjzhong.mall.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 处理用户账户的请求
 */
public class HandleUserReq {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Size(min = 8, message = "密码最短为8位")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
