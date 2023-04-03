package com.jjzhong.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.model.request.CreateOrderReq;
import com.jjzhong.mall.model.vo.OrderStatisticsVO;
import com.jjzhong.mall.model.vo.OrderVO;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    String create(Integer userId, CreateOrderReq createOrderReq);

    OrderVO detail(Integer userId, String orderNo);

    PageInfo<OrderVO> list4Customer(Integer userId, Integer pageNum, Integer pageSize);

    PageInfo<OrderVO> list4Admin(Integer pageNum, Integer pageSize);

    void cancel(Integer userId, String orderNo);

    String qrCode(String orderNo) throws IOException, WriterException;

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(User user, String orderNo);

    List<OrderStatisticsVO> statistics(OrderStatisticsQuery orderStatisticsQuery);
}
