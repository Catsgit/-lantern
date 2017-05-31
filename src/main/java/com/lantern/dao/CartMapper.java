package com.lantern.dao;

import com.lantern.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") String userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(String userId);

    int selectCartProductCheckedStatusByUserId(String UserId);

    int deleteByUserIdProductIds(@Param("userId") String userId, @Param("productIdList") List<String> productIdList);

    int checkedOrUnCheckedProduct(@Param("userId") String userId, @Param("checked") boolean checked, @Param("productId") Integer productId);

    int selectCartProductCount(String userId);
}