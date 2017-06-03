package com.lantern.service;

import com.github.pagehelper.PageInfo;
import com.lantern.common.ServerResponse;
import com.lantern.vo.OrderVO;

import java.util.Map;

/**
 * Created by cat on 17-6-1.
 */
public interface IOrderService {
    ServerResponse pay(String userId, Long orderNo, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(String userId, Long orderNo);

    ServerResponse createOrder(String userId, Integer shippingId);

    ServerResponse cancelOrder(String userId, Long orderNo);

    ServerResponse getOrderCartProduct(String userId);

    ServerResponse getOrderDetail(String userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(String userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> getManageOrderList(int pageNum, int pageSize);

    ServerResponse<OrderVO> getManageOrderDetail(Long orderNo);

    ServerResponse<PageInfo> searchOrder(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> sendGoods(Long orderNo);
}
