package com.jjzhong.mall.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 更新目录请求
 */
public class UpdateCategoryReq {
    @NotNull(message = "id不能为空")
    private Integer id;
    @Size(min = 2, max = 5, message = "name长度需要在2到5之间")
    private String name;
    @Max(value = 3, message = "type最大值为3")
    private Integer type;
    private Integer parentId;
    private Integer orderNum;

    @Override
    public String toString() {
        return "UpdateCategoryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
