package com.jjzhong.mall.controller;

import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.filter.UserFilter;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车接口")
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    @Operation(description = "获取用户购物车列表")
    public CommonResponse list() {
        User user = UserFilter.userThreadLocal.get();
        return CommonResponse.success(cartService.list(user.getId()));
    }

    @PostMapping("/add")
    @Operation(description = "购物车添加商品")
    public CommonResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        User user = UserFilter.userThreadLocal.get();
        return CommonResponse.success(cartService.add(user.getId(), productId, count));
    }

    @PostMapping("/update")
    @Operation(description = "更新购物车商品数量")
    public CommonResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        User user = UserFilter.userThreadLocal.get();
        return CommonResponse.success(cartService.updateCount(user.getId(), productId, count));
    }

    @PostMapping("/delete")
    @Operation(description = "删除购物车中商品")
    public CommonResponse delete(@RequestParam Integer productId) {
        User user = UserFilter.userThreadLocal.get();
        return CommonResponse.success(cartService.delete(user.getId(), productId));
    }

    @PostMapping("/select")
    @Operation(description = "选择购物车中商品")
    public CommonResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        User user = UserFilter.userThreadLocal.get();
        return CommonResponse.success(cartService.updateSelect(user.getId(), productId, selected));
    }

    @PostMapping("/selectAll")
    @Operation(description = "选择购物车中所有商品")
    public CommonResponse selectAll(@RequestParam Integer selected) {
        User user = UserFilter.userThreadLocal.get();
        return CommonResponse.success(cartService.updateSelectAll(user.getId(), selected));
    }
}
