package com.jjzhong.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.vo.CategoryVO;
import com.jjzhong.mall.model.dao.CategoryMapper;
import com.jjzhong.mall.model.pojo.Category;
import com.jjzhong.mall.model.request.AddCategoryReq;
import com.jjzhong.mall.model.request.UpdateCategoryReq;
import com.jjzhong.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 增加商品分类
     * @param addCategoryReq 增加商品分类请求
     */
    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category);
        int cnt = categoryMapper.insertSelective(category);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.ADD_FAILED);
        }
    }

    /**
     * 更新商品分类
     * @param updateCategoryReq 更新商品分类请求
     */
    @Override
    public void update(UpdateCategoryReq updateCategoryReq) {
        Category categoryOld1 = categoryMapper.selectByPrimaryKey(updateCategoryReq.getId());
        if (categoryOld1 == null) {
            throw new MallException(MallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Category categoryOld2 = categoryMapper.selectByName(updateCategoryReq.getName());
        if (categoryOld2 != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        int cnt = categoryMapper.updateByPrimaryKeySelective(category);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 删除商品分类
     * @param categoryId 商品分类 id
     */
    @Override
    public void delete(Integer categoryId) {
        Category categoryOld = categoryMapper.selectByPrimaryKey(categoryId);
        if (categoryOld == null) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int cnt = categoryMapper.deleteByPrimaryKey(categoryId);
        if (cnt == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    /**
     * 管理员分类列表查询
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页后的商品分类列表
     */
    @Override
    public PageInfo<Category> listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "type, order_num");
        List<Category> categoryList = categoryMapper.selectList();
        return new PageInfo<>(categoryList);
    }

    /**
     * 普通用户商品分类列表
     * @return 商品分类 VO
     */
    @Override
    @Cacheable(value = "listForCustomer")
    public List<CategoryVO> listForCustomer() {
        return recursivelyGetCustomerList(0);
    }

    /**
     * 获取子分类的 id
     * @param parentId 父分类 id
     * @param ids 用于保存分类的列表
     */
    @Override
    public void getChildCategoryIds(Integer parentId, List<Integer> ids) {
        List<Category> categories = categoryMapper.selectByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)) {
            for (Category category : categories) {
                ids.add(category.getId());
                getChildCategoryIds(category.getId(), ids);
            }
        }
    }

    /**
     * 迭代获取商品分类列表
     * @param parentId 父 id
     * @return 商品分类 VO
     */
    private List<CategoryVO> recursivelyGetCustomerList(Integer parentId) {
        List<Category> categories = categoryMapper.selectByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)) {
            List<CategoryVO> categoryVOs = new ArrayList<>();
            for (Category category : categories) {
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVO.setChildCategory(recursivelyGetCustomerList(category.getId()));
                categoryVOs.add(categoryVO);
            }
            return categoryVOs;
        }
        return new ArrayList<>();
    }
}
