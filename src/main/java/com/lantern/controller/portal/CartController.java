package com.lantern.controller.portal;

import com.lantern.common.Const;
import com.lantern.common.ResponseCode;
import com.lantern.common.ServerResponse;
import com.lantern.pojo.User;
import com.lantern.service.ICartService;
import com.lantern.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by cat on 17-5-30.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    public ServerResponse<CartVO> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录, 请登录");
        }
        return iCartService.add(user.getUsername(), productId, count);
    }
}
