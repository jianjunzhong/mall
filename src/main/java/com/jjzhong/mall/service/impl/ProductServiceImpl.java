package com.jjzhong.mall.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.listener.ProductDataListener;
import com.jjzhong.mall.model.dao.ProductMapper;
import com.jjzhong.mall.model.pojo.Product;
import com.jjzhong.mall.model.query.ProductListQuery;
import com.jjzhong.mall.model.request.AddProductReq;
import com.jjzhong.mall.model.request.QueryProductReq;
import com.jjzhong.mall.model.request.UpdateProductReq;
import com.jjzhong.mall.model.upload.ProductData;
import com.jjzhong.mall.service.CategoryService;
import com.jjzhong.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品服务
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;

    /**
     * 增加商品
     * @param addProductReq 增加商品请求
     */
    @Override
    public void add(AddProductReq addProductReq) {
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        int cnt = productMapper.insertSelective(product);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.ADD_FAILED);
        }
    }

    /**
     * 管理员后台更新商品，并将更新后的商品缓存到 redis 中
     * @param updateProductReq 更新商品请求
     */
    @Override
    public void update(UpdateProductReq updateProductReq) {
        Product productOld = productMapper.selectByName(updateProductReq.getName());
        if (productOld != null && !productOld.getId().equals(updateProductReq.getId())) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        int cnt = productMapper.updateByPrimaryKeySelective(product);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 删除商品
     * @param productId 商品 id
     */
    @Override
    public void delete(Integer productId) {
        int cnt = productMapper.deleteByPrimaryKey(productId);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    /**
     * 批量更新商品上架状态
     * @param ids 商品 id 数组
     * @param saleStatus 上架状态
     */
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    /**
     * 查询商品列表返回给管理员后台
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的商品
     */
    @Override
    public PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectListForAdmin();
        return new PageInfo<>(list);
    }

    /**
     * 获取商品请求
     * @param queryProductReq 查询商品请求
     * @return 分页商品列表
     */
    @Override
    public PageInfo<Product> list(QueryProductReq queryProductReq) {
        ProductListQuery productListQuery = new ProductListQuery();
        if (queryProductReq.getCategoryId() != null) {
            List<Integer> idList = new ArrayList<>();
            idList.add(queryProductReq.getCategoryId());
            categoryService.getChildCategoryIds(queryProductReq.getCategoryId(), idList);
            productListQuery.setCategoryIds(idList);
        }
        if (StringUtils.hasText(queryProductReq.getKeyword())) {
            String keyword = "%" + queryProductReq.getKeyword() + "%";
            productListQuery.setKeyword(keyword);
        }
        if (queryProductReq.getOrderBy() != null) {
            String orderBy = queryProductReq.getOrderBy();
            if (Constant.ProductListOrderBy.PRICE_ORDER_ENUM.contains(orderBy))
                PageHelper.startPage(queryProductReq.getPageNum(), queryProductReq.getPageSize(), orderBy);
            else PageHelper.startPage(queryProductReq.getPageNum(), queryProductReq.getPageSize());
        }
        List<Product> list = productMapper.selectList(productListQuery);
        return new PageInfo<>(list);
    }

    /**
     * 查询商品详情
     * @param id 商品 id
     * @return 商品详情
     */
    @Override
    public Product detail(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    /**
     * Excel 批量导入商品
     * @param file 传输的 Excel 文件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProductByExcel(File file) {
        EasyExcel.read(file, ProductData.class, new ProductDataListener(productMapper)).sheet().doRead();
    }
}
