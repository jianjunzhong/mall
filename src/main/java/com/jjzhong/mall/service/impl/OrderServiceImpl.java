package com.jjzhong.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.dao.CartMapper;
import com.jjzhong.mall.model.dao.OrderItemMapper;
import com.jjzhong.mall.model.dao.OrderMapper;
import com.jjzhong.mall.model.dao.ProductMapper;
import com.jjzhong.mall.model.pojo.Order;
import com.jjzhong.mall.model.pojo.OrderItem;
import com.jjzhong.mall.model.pojo.Product;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.model.request.CreateOrderReq;
import com.jjzhong.mall.model.vo.CartVO;
import com.jjzhong.mall.model.vo.OrderItemVO;
import com.jjzhong.mall.model.vo.OrderStatisticsVO;
import com.jjzhong.mall.model.vo.OrderVO;
import com.jjzhong.mall.service.CartService;
import com.jjzhong.mall.service.OrderService;
import com.jjzhong.mall.service.UploadService;
import com.jjzhong.mall.service.UserService;
import com.jjzhong.mall.util.FileUtils;
import com.jjzhong.mall.util.OrderNoFactory;
import com.jjzhong.mall.util.QRCodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UploadService uploadService;


    /**
     * 创建订单
     * @param createOrderReq 创建订单请求
     * @return 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(Integer userId, CreateOrderReq createOrderReq) {

        // 获取购物车中已选择的商品并清除
        List<CartVO> cartVOs = cartService.list(userId);
        List<CartVO> cartVOSelected = cartVOs.stream()
                .filter(item -> {
                    if (item.getSelected().equals(Constant.CartSelectStatus.SELECTED)) {
                        cartMapper.deleteByPrimaryKey(item.getId());
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
        // 如果没有已选择的商品
        if (CollectionUtils.isEmpty(cartVOSelected))
            throw new MallException(MallExceptionEnum.CART_EMPTY_OR_NOT_SELECTED);
        // 判断是否上架或者库存充足
        validateOrderItems(cartVOSelected);
        // 将CartVO转换为OrderItem
        List<OrderItem> orderItems = cartVO2OrderItem(cartVOSelected);
        // 扣库存
        for (OrderItem orderItem : orderItems) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            productMapper.updateStock(orderItem.getProductId(), product.getStock() - orderItem.getQuantity());
        }
        // 创建订单
        String orderNo = OrderNoFactory.getOrderNo();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(calculateTotalPrice(cartVOSelected));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        orderMapper.insertSelective(order);
        // 将订单中的所有商品写入order_item表中
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderNo(orderNo);
            orderItemMapper.insertSelective(orderItem);
        }
        return orderNo;
    }

    /**
     * 查询订单
     * @param orderNo 订单号
     * @return 订单 VO
     */
    @Override
    public OrderVO detail(Integer userId, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        validateOrder(userId, order);
        return order2OrderVO(order);
    }

    /**
     * 查询普通用户订单列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的订单 VO 列表
     */
    @Override
    public PageInfo<OrderVO> list4Customer(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectByUserId(userId);
        List<OrderVO> orderVOs = new ArrayList<>();
        for (Order order : orders) {
            orderVOs.add(order2OrderVO(order));
        }
        return new PageInfo<>(orderVOs);
    }

    /**
     * 查询管理员订单列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的订单 VO 列表
     */
    @Override
    public PageInfo<OrderVO> list4Admin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectList();
        List<OrderVO> orderVOs = new ArrayList<>();
        for (Order order : orders) {
            orderVOs.add(order2OrderVO(order));
        }
        return new PageInfo<>(orderVOs);
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     */
    @Override
    public void cancel(Integer userId, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        validateOrder(userId, order);
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAY.getCode())) {
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setEndTime(new Date());
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
            // 恢复商品库存
            List<OrderItem> orderItems = orderItemMapper.selectListByOrderNo(orderNo);
            for (OrderItem orderItem : orderItems) {
                Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
                Integer stock = product.getStock() + orderItem.getQuantity();
                productMapper.updateStock(orderItem.getProductId(), stock);
            }

        } else {
            throw new MallException(MallExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 生成订单号二维码
     * @param orderNo 二维码
     * @return 二维码图片地址
     * @throws IOException IO 异常
     * @throws WriterException 写异常
     */
    @Override
    public String qrCode(String orderNo) throws IOException, WriterException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String payUrl = "http://" + Constant.ORDER_PAY_HOST + "/pay?orderNo=" + orderNo;
        String dstDir = Constant.FILE_UPLOAD_DIR + Constant.FILE_UPLOAD_IMAGE_CONTEXT + "/qrcode/";
        FileUtils.makeDirsIfNotExists(dstDir);
        QRCodeGenerator.generateQRCodeImage(payUrl,
                350,
                350,
                dstDir + orderNo + ".png");
        URI uri = uploadService.getURI(Constant.FILE_UPLOAD_IMAGE_CONTEXT);
        String pngAddr = uri + "/qrcode/" + orderNo + ".png";
        return pngAddr;
    }

    /**
     * 支付订单
     * @param orderNo 订单号
     */
    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAY.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new MallException(MallExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 订单发货
     * @param orderNo 订单号
     */
    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        validateOrder(order);
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new MallException(MallExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 完结订单
     * @param orderNo 订单号
     */
    @Override
    public void finish(User user, String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (userService.checkAdmin(user)) validateOrder(order);
        else validateOrder(user.getId(), order);
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERED.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new MallException(MallExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 查询订单量数据
     * @param orderStatisticsQuery 订单数据查询
     * @return 订单数据列表
     */
    @Override
    public List<OrderStatisticsVO> statistics(OrderStatisticsQuery orderStatisticsQuery) {
        return orderMapper.selectOrderStatistics(orderStatisticsQuery);
    }

    /**
     * 验证订单是否有效
     * @param order 订单实体
     */
    private void validateOrder(Order order) {
        if (order == null) {
            throw new MallException(MallExceptionEnum.ORDER_NOT_FOUND);
        }
    }

    /**
     * 验证订单是否有效或匹配
     * @param userId 用户 id
     * @param order 订单实体
     */
    private void validateOrder(Integer userId, Order order) {
        validateOrder(order);
        if (!order.getUserId().equals(userId)) {
            throw new MallException(MallExceptionEnum.ORDER_NOT_MATCH);
        }
    }

    /**
     * Order 转换为 OrderVO
     * @param order 要转换的 Order
     * @return OrderVO
     */
    private OrderVO order2OrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(order.getOrderStatus()).getName());
        // 获取OrderItemsVOList
        List<OrderItem> orderItems = orderItemMapper.selectListByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    /**
     * 用于下单时将购物车中的商品转换成 OrderItem
     * @param cartVOSelected 购物车中已选择的商品
     * @return OrderItem 列表
     */
    private List<OrderItem> cartVO2OrderItem(List<CartVO> cartVOSelected) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartVO cartVO : cartVOSelected) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    /**
     * 计算购物车中已选择的商品的总价
     * @param cartVOSelected 购物车中已选择的商品
     * @return 购物车中已选择的商品的总价
     */
    private Integer calculateTotalPrice(List<CartVO> cartVOSelected) {
        Integer totalPrice = 0;
        for (CartVO cartVO : cartVOSelected) {
            totalPrice += cartVO.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * 校验商品是否上架或者库存是否充足
     * @param cartVOs 购物车 VO 列表
     */
    private void validateOrderItems(List<CartVO> cartVOs) {
        for (CartVO cartVO : cartVOs) {
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            if (product == null || product.getStatus().equals(Constant.ProductSellStatus.NOT_SELL))
                throw new MallException(MallExceptionEnum.NOT_SELL);
            else {
                if (product.getStock() < cartVO.getQuantity())
                    throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
        }
    }
}
