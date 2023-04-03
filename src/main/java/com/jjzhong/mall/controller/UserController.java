package com.jjzhong.mall.controller;

import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.filter.UserFilter;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.model.request.HandleUserReq;
import com.jjzhong.mall.model.request.RegisterReq;
import com.jjzhong.mall.service.EmailService;
import com.jjzhong.mall.service.UserService;
import com.jjzhong.mall.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "用户接口")
@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    @Operation(description = "用户注册")
    public CommonResponse register(@Valid RegisterReq registerReq) {
        userService.register(registerReq);
        return CommonResponse.success();
    }

    @GetMapping("/login")
    @Operation(description = "用户登陆")
    public CommonResponse login(@Valid HandleUserReq handleUserReq, HttpSession session) {
        User user = userService.login(handleUserReq.getUserName(), handleUserReq.getPassword());
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER, user);
        return CommonResponse.success(user);
    }

    @PostMapping("/user/update")
    @Operation(description = "更新用户签名")
    public CommonResponse updateUserInfo(@RequestParam String signature, HttpSession session) {
        User currentUser = UserFilter.userThreadLocal.get();
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return CommonResponse.success();
    }

    @PostMapping("/user/logout")
    @Operation(description = "注销用户")
    public CommonResponse logout(HttpSession session) {
        session.removeAttribute(Constant.MALL_USER);
        return CommonResponse.success();
    }

    @GetMapping("/adminLogin")
    @Operation(description = "登陆管理员")
    public CommonResponse adminLogin(@Valid HandleUserReq handleUserReq, HttpSession session) {
        User user = userService.adminLogin(handleUserReq.getUserName(), handleUserReq.getPassword());
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER, user);
        return CommonResponse.success(user);
    }

    @PostMapping("/user/sendEmail")
    public CommonResponse sendEmail(@RequestParam @NotBlank @Email String email) {
        emailService.sendVerifyCodeEmail(email);
        return CommonResponse.success();
    }

    @GetMapping("/loginWithJwt")
    public CommonResponse loginWithJwt(@Valid HandleUserReq handleUserReq) {
        User user = userService.login(handleUserReq.getUserName(), handleUserReq.getPassword());
        user.setPassword(null);
        String token = JwtUtils.generateJwtToken(user);
        return CommonResponse.success(token);
    }
}
