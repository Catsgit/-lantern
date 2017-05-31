package com.lantern.service;

import com.lantern.common.ServerResponse;
import com.lantern.vo.CartVO;

/**
 * Created by cat on 17-5-30.
 */
public interface ICartService {

    ServerResponse<CartVO> add(String userId, Integer productId, Integer count);

    ServerResponse<CartVO> update(String userId, Integer productId, Integer count);

    ServerResponse<CartVO> delete(String userId, String productIds);

    ServerResponse<CartVO> select(String userId);

    ServerResponse<CartVO> selectOrUnSelect(String userId, boolean checked, Integer productId);

    ServerResponse<Integer> getProductCount(String userId);
}
