package com.jjzhong.mall.service.impl;

import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.dao.CartMapper;
import com.jjzhong.mall.model.dao.ProductMapper;
import com.jjzhong.mall.model.pojo.Cart;
import com.jjzhong.mall.model.pojo.Product;
import com.jjzhong.mall.model.vo.CartVO;
import com.jjzhong.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 用户购物车列表
     * @param userId 用户 id
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOs = cartMapper.selectListByUserId(userId);
        for (CartVO cartVO : cartVOs) {
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOs;
    }

    /**
     * 用户往购物车添加商品
     * @param userId 用户 id
     * @param productId 商品 id
     * @param count 商品数量
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validateProduct(productId, count);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setSelected(Constant.CartSelectStatus.SELECTED);
            cartMapper.insertSelective(newCart);
        } else {
            updateCount(cart, cart.getQuantity() + count);
        }
        return this.list(userId);
    }

    /**
     * 更新商品数量
     * @param userId 用户 id
     * @param productId 商品 id
     * @param count 商品数量
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> updateCount(Integer userId, Integer productId, Integer count) {
        validateProduct(productId, count);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            updateCount(cart, count);
        }
        return this.list(userId);
    }

    /**
     * 更新购物车选择状态
     * @param userId 用户 id
     * @param productId 商品 id
     * @param selectStatus 商品选择状态
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> updateSelect(Integer userId, Integer productId, Integer selectStatus) {
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            if (selectStatus != Constant.CartSelectStatus.SELECTED && selectStatus != Constant.CartSelectStatus.UN_SELECTED)
                selectStatus = Constant.CartSelectStatus.SELECTED;
            cartMapper.updateSelectStatus(userId, productId, selectStatus);
        }
        return this.list(userId);
    }

    /**
     * 更新购物车中所有商品的选择状态
     * @param userId 用户 id
     * @param selectStatus 商品选择状态
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> updateSelectAll(Integer userId, Integer selectStatus) {
        if (selectStatus != Constant.CartSelectStatus.SELECTED && selectStatus != Constant.CartSelectStatus.UN_SELECTED)
            selectStatus = Constant.CartSelectStatus.SELECTED;
        cartMapper.updateSelectStatus(userId, null, selectStatus);
        return this.list(userId);
    }

    /**
     * 删除购物车中的商品
     * @param userId 用户 id
     * @param productId 商品选择状态
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        int cnt = cartMapper.deleteByUserIdAndProductId(userId, productId);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        return this.list(userId);
    }

    /**
     * 校验购物车中的商品（是否上架，库存是否充足）
     * @param productId 商品选择状态
     * @param count 商品数量
     */
    public void validateProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus().equals(Constant.ProductSellStatus.NOT_SELL))
            throw new MallException(MallExceptionEnum.NOT_SELL);
        else {
            if (product.getStock() < count)
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
        }
    }

    /**
     * 更新购物车中的商品数量
     * @param cartOld 原购物车中的商品
     * @param count 更新的数量
     */
    private void updateCount(Cart cartOld, Integer count) {
        validateProduct(cartOld.getProductId(), count);
        Cart newCart = new Cart();
        newCart.setQuantity(count);
        newCart.setId(cartOld.getId());
        newCart.setSelected(Constant.CartSelectStatus.SELECTED);
        newCart.setProductId(cartOld.getProductId());
        newCart.setUserId(cartOld.getUserId());
        cartMapper.updateByPrimaryKeySelective(newCart);
    }

}
