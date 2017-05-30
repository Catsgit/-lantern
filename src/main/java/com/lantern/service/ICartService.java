package com.lantern.service;

import com.lantern.common.ServerResponse;
import com.lantern.vo.CartVO;

/**
 * Created by cat on 17-5-30.
 */
public interface ICartService {

    ServerResponse<CartVO> add(String userId, Integer productId, Integer count);
}
