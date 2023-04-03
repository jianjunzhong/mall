package com.jjzhong.mall.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.model.pojo.Category;
import com.jjzhong.mall.model.request.AddCategoryReq;
import com.jjzhong.mall.model.request.UpdateCategoryReq;
import com.jjzhong.mall.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品目录接口")
@RestController
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/category/add")
    @Operation(description = "增加目录")
    public CommonResponse addCategory(@Valid @RequestBody AddCategoryReq addCategoryReq) {
        categoryService.add(addCategoryReq);
        return CommonResponse.success();
    }

    @PostMapping("/admin/category/update")
    @Operation(description = "更新目录")
    public CommonResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq) {
        categoryService.update(updateCategoryReq);
        return CommonResponse.success();
    }

    @PostMapping("/admin/category/delete")
    @Operation(description = "删除目录")
    public CommonResponse deleteCategory(Integer id) {
        categoryService.delete(id);
        return CommonResponse.success();
    }

    @GetMapping("/admin/category/list")
    @Operation(description = "返回目录列表（管理员）")
    public CommonResponse categoryList4Admin(Integer pageNum, Integer pageSize) {
        PageInfo<Category> pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return CommonResponse.success(pageInfo);
    }

    @GetMapping("/category/list")
    @Operation(description = "返回目录列表（普通用户）")
    public CommonResponse categoryList4Customer() {
        return CommonResponse.success(categoryService.listForCustomer());
    }
}
