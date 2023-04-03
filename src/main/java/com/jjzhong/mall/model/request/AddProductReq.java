package com.jjzhong.mall.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

/**
 * 增加商品请求
 */
public class AddProductReq {

    @NotNull(message = "name不能为空")
    private String name;

    @NotNull(message = "请上传图片")
    private String image;

    @NotNull(message = "detail不能为空")
    @Size(min = 5, message = "描述不能少于5字符")
    private String detail;

    @NotNull(message = "categoryId不能为空")
    private Integer categoryId;

    @NotNull(message = "price不能为空")
    @Min(value = 1000, message = "价格不能小于1000分")
    private Integer price;

    @NotNull(message = "stock不能为空")
    @Max(10000)
    private Integer stock;

    @Override
    public String toString() {
        return "AddProductReq{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", detail='" + detail + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
