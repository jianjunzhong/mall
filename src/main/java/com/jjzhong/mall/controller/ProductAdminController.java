package com.jjzhong.mall.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.model.pojo.Product;
import com.jjzhong.mall.model.request.AddProductReq;
import com.jjzhong.mall.model.request.UpdateProductReq;
import com.jjzhong.mall.service.ProductService;
import com.jjzhong.mall.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Tag(name = "管理员商品接口")
@RestController
@RequestMapping("/admin")
@Validated
public class ProductAdminController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UploadService uploadService;

    @PostMapping("/product/add")
    @Operation(description = "增加商品")
    public CommonResponse add(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return CommonResponse.success();
    }

    @PostMapping("/upload/image")
    @Operation(description = "上传图片")
    public CommonResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        return CommonResponse.success(uploadService.uploadImageReturnUrl(file, Constant.FILE_UPLOAD_IMAGE_CONTEXT));
    }

    @PostMapping("/product/update")
    @Operation(description = "后台更新商品")
    public CommonResponse update(@Valid @RequestBody UpdateProductReq updateProductReq) {
        productService.update(updateProductReq);
        return CommonResponse.success();
    }

    @PostMapping("/product/delete")
    @Operation(description = "后台删除商品")
    public CommonResponse delete(@RequestParam Integer id) {
        productService.delete(id);
        return CommonResponse.success();
    }

    @PostMapping("/product/batchUpdateSellStatus")
    @Operation(description = "后台批量上下架商品")
    public CommonResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return CommonResponse.success();
    }

    @GetMapping("/product/list")
    @Operation(description = "后台商品列表")
    public CommonResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo<Product> productPageInfo = productService.listForAdmin(pageNum, pageSize);
        return CommonResponse.success(productPageInfo);
    }

    @PostMapping("/upload/product")
    @Operation(description = "后台批量上传商品")
    public CommonResponse uploadProduct(@RequestParam("file") MultipartFile file) throws IOException {
        File newFile = uploadService.uploadFile(file, Constant.FILE_UPLOAD_FILE_CONTEXT);
        productService.addProductByExcel(newFile);
        return CommonResponse.success();
    }

}
