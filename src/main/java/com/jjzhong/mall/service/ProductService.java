package com.jjzhong.mall.service;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.model.pojo.Product;
import com.jjzhong.mall.model.request.AddProductReq;
import com.jjzhong.mall.model.request.QueryProductReq;
import com.jjzhong.mall.model.request.UpdateProductReq;

import java.io.File;

public interface ProductService {
    void add(AddProductReq addProductReq);

    void update(UpdateProductReq updateProductReq);

    void delete(Integer productId);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize);

    PageInfo<Product> list(QueryProductReq queryProductReq);

    Product detail(Integer id);


    void addProductByExcel(File file);
}
