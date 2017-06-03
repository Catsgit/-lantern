package com.lantern.dao;

import com.lantern.pojo.OrderMaster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMasterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderMaster record);

    int insertSelective(OrderMaster record);

    OrderMaster selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderMaster record);

    int updateByPrimaryKey(OrderMaster record);

    OrderMaster selectByUserIdAndOrderNo(@Param(value = "userId") String userId, @Param(value = "orderNo") Long orderNo);

    OrderMaster selectByOrderNo(Long orderNo);

    List<OrderMaster> selectByUserId(String userId);

    List<OrderMaster> selectAllOrder();
}