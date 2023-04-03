package com.jjzhong.mall.controller;

import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.model.request.QueryProductReq;
import com.jjzhong.mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商品接口")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    @Operation(description = "查询商品列表")
    public CommonResponse list(QueryProductReq queryProductReq) {
        return CommonResponse.success(productService.list(queryProductReq));
    }

    @GetMapping("/detail")
    @Operation(description = "商品详情")
    public CommonResponse detail(@RequestParam Integer id) {
        return CommonResponse.success(productService.detail(id));
    }
}
