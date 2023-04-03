package com.jjzhong.mall.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 增加目录请求
 */
public class AddCategoryReq {
    @NotNull(message = "name不能为空")
    @Size(min = 2, max = 5, message = "name长度需要在2到5之间")
    private String name;
    @NotNull(message = "type不能为空")
    @Max(value = 3, message = "type最大值为3")
    private Integer type;
    @NotNull(message = "parentId不能为空")
    private Integer parentId;
    @NotNull(message = "orderNum不能为空")
    private Integer orderNum;

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
