package com.jjzhong.mall.controller;

import com.google.zxing.WriterException;
import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.filter.UserFilter;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.model.request.CreateOrderReq;
import com.jjzhong.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "普通用户订单接口")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    @Operation(description = "创建订单")
    public CommonResponse create(@Valid @RequestBody CreateOrderReq createOrderReq) {
        Integer userId = UserFilter.userThreadLocal.get().getId();
        return CommonResponse.success(orderService.create(userId, createOrderReq));
    }

    @GetMapping("/order/detail")
    @Operation(description = "前台订单详情")
    public CommonResponse detail(@RequestParam String orderNo) {
        Integer userId = UserFilter.userThreadLocal.get().getId();
        return CommonResponse.success(orderService.detail(userId, orderNo));
    }

    @GetMapping("/order/list")
    @Operation(description = "前台订单列表")
    public CommonResponse detail(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Integer userId = UserFilter.userThreadLocal.get().getId();
        return CommonResponse.success(orderService.list4Customer(userId, pageNum, pageSize));
    }

    @PostMapping("/order/cancel")
    @Operation(description = "前台取消订单")
    public CommonResponse cancel(@RequestParam String orderNo) {
        Integer userId = UserFilter.userThreadLocal.get().getId();
        orderService.cancel(userId, orderNo);
        return CommonResponse.success();
    }

    @GetMapping("/order/qrcode")
    @Operation(description = "生成支付二维码")
    public CommonResponse qrCode(@RequestParam String orderNo) throws IOException, WriterException {
        return CommonResponse.success(orderService.qrCode(orderNo));
    }

    @GetMapping("/pay")
    @Operation(description = "支付")
    public CommonResponse pay(@RequestParam String orderNo) {
        orderService.pay(orderNo);
        return CommonResponse.success();
    }

    @PostMapping("/order/finish")
    @Operation(description = "完结")
    public CommonResponse finish(@RequestParam String orderNo) {
        User currentUser = UserFilter.userThreadLocal.get();
        orderService.finish(currentUser, orderNo);
        return CommonResponse.success();
    }
}
