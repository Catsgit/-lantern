package com.lantern.service;

import com.github.pagehelper.PageInfo;
import com.lantern.common.ServerResponse;
import com.lantern.pojo.Shipping;

/**
 * Created by cat on 17-5-31.
 */
public interface IShippingService {

    ServerResponse add(String userId, Shipping shipping);

    ServerResponse delete(String userId, Integer shippingId);

    ServerResponse update(String userId, Shipping shipping);

    ServerResponse select(String userId, Integer shippingId);

    ServerResponse<PageInfo> list(String userId, int pageNum, int pageSize);
}
