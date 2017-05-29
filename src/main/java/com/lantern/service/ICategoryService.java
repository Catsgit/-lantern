package com.lantern.service;

import com.lantern.common.ServerResponse;
import com.lantern.pojo.Category;
import com.lantern.vo.CategoryVO;

import java.util.List;
import java.util.Set;

/**
 * Created by cat on 17-5-29.
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

//    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
//
//    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
    ServerResponse<List<CategoryVO>> selectCategoryList();

    ServerResponse<List<CategoryVO>> selectValidCategoryList();
}
