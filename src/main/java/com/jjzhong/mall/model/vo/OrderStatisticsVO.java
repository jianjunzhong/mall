package com.jjzhong.mall.model.vo;

import java.util.Date;

/**
 * 订单量数据统计 VO
 */
public class OrderStatisticsVO {
    private Date days;
    private Integer amount;

    public Date getDays() {
        return days;
    }

    public void setDays(Date days) {
        this.days = days;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
