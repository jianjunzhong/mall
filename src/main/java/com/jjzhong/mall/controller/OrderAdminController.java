package com.jjzhong.mall.controller;

import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员后台订单接口")
@RestController
@Validated
public class OrderAdminController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/order/list")
    @Operation(description = "后台订单列表")
    public CommonResponse listForAdmin(@RequestParam Integer pageNum, Integer pageSize) {
        return CommonResponse.success(orderService.list4Admin(pageNum, pageSize));
    }

    @PostMapping("/admin/order/delivered")
    @Operation(description = "支付")
    public CommonResponse deliver(@RequestParam String orderNo) {
        orderService.deliver(orderNo);
        return CommonResponse.success();
    }

    @GetMapping("/admin/order/statistics")
    @Operation(description = "每日订单量统计")
    public CommonResponse statistics(@Valid OrderStatisticsQuery orderStatisticsQuery) {
        return CommonResponse.success(orderService.statistics(orderStatisticsQuery));
    }
}
