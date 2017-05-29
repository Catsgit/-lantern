package com.lantern.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lantern.common.ServerResponse;
import com.lantern.dao.CategoryMapper;
import com.lantern.pojo.Category;
import com.lantern.service.ICategoryService;
import com.lantern.vo.CategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by cat on 17-5-29.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName) {
        if(StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setStatus(true);   //这个分类是可用的

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<CategoryVO>> selectCategoryList() {
        List<Category> categoryList = categoryMapper.selectCategoryList();
        if(categoryList.isEmpty()) {
            return ServerResponse.createByErrorMessage("当前暂无分类信息");
        }
        List<CategoryVO> categoryVOList = Lists.newArrayList();
        for(Category categoryItem : categoryList) {
            CategoryVO categoryVO = assembleCategoryVO(categoryItem);
            categoryVOList.add(categoryVO);
        }
        return ServerResponse.createBySuccess(categoryVOList);
    }

    private CategoryVO assembleCategoryVO(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(category.getId());
        categoryVO.setName(category.getName());

        return categoryVO;
    }

    @Override
    public ServerResponse<List<CategoryVO>> selectValidCategoryList() {
        List<Category> categoryList = categoryMapper.selectValidCategoryList();
        if(categoryList.isEmpty()) {
            return ServerResponse.createByErrorMessage("当前暂无分类信息");
        }
        List<CategoryVO> categoryVOList = Lists.newArrayList();
        for(Category categoryItem : categoryList) {
            CategoryVO categoryVO = assembleCategoryVO(categoryItem);
            categoryVOList.add(categoryVO);
        }
        return ServerResponse.createBySuccess(categoryVOList);
    }
}
