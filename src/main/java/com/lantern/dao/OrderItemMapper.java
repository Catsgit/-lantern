package com.lantern.dao;

import com.lantern.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.ast.Or;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByOrderNoAndUserId(@Param(value = "orderNo") Long orderNo, @Param(value = "userId") String userId);

    List<OrderItem> getByOrderNo(Long orderNo);

    void batchInsert(@Param(value = "orderItemList") List<OrderItem> orderItemList);
}