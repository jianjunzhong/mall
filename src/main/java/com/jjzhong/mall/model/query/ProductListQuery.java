package com.jjzhong.mall.model.query;

import java.util.List;

/**
 * 数据库中商品查询
 */
public class ProductListQuery {
    /** 商品类别 id */
    private List<Integer> categoryIds;
    /** 关键词 */
    private String keyword;

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
