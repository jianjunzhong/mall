package com.jjzhong.mall.service;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.model.pojo.Category;
import com.jjzhong.mall.model.request.AddCategoryReq;
import com.jjzhong.mall.model.request.UpdateCategoryReq;
import com.jjzhong.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    void add(AddCategoryReq addCategoryReq);

    void update(UpdateCategoryReq updateCategoryReq);

    void delete(Integer categoryId);

    PageInfo<Category> listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listForCustomer();

    void getChildCategoryIds(Integer parentId, List<Integer> ids);
}
