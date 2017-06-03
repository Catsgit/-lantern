package com.lantern.controller.backend;

import com.github.pagehelper.PageInfo;
import com.lantern.common.Const;
import com.lantern.common.ResponseCode;
import com.lantern.common.ServerResponse;
import com.lantern.pojo.User;
import com.lantern.service.IOrderService;
import com.lantern.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by cat on 17-6-3.
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录, 请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.getManageOrderList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse orderDetail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录, 请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.getManageOrderDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse searchOrder(HttpSession session, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录, 请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.searchOrder(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse sendOrderGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录, 请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.sendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

}
