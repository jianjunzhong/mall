package com.jjzhong.mall.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

/**
 * 查询商品列表的请求
 */
public class QueryProductReq {
    private String orderBy;
    private Integer categoryId;
    private String keyword;
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    @Override
    public String toString() {
        return "QueryProductReq{" +
                "orderBy='" + orderBy + '\'' +
                ", categoryId=" + categoryId +
                ", keyword='" + keyword + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryIds) {
        this.categoryId = categoryIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
