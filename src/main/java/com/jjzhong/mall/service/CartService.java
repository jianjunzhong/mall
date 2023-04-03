package com.jjzhong.mall.service;

import com.jjzhong.mall.model.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count);

    List<CartVO> updateCount(Integer userId, Integer productId, Integer count);

    List<CartVO> updateSelect(Integer userId, Integer productId, Integer selectStatus);

    List<CartVO> updateSelectAll(Integer userId, Integer selectStatus);
    List<CartVO> delete(Integer userId, Integer productId);
}
