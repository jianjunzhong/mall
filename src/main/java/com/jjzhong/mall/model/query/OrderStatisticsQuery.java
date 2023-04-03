package com.jjzhong.mall.model.query;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商品订单量统计查询
 */
public class OrderStatisticsQuery {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
