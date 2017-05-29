package com.lantern.controller.portal;

import com.lantern.common.Const;
import com.lantern.common.ServerResponse;
import com.lantern.pojo.User;
import com.lantern.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by cat on 17-5-28.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;


    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }


    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register_get_verify.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> registerGetVerify(String username) {
        return iUserService.registerGetVerify(username);
    }

    @RequestMapping(value = "check_verify.do", method = RequestMethod.POST)
    public ServerResponse<String> checkVerify(String username, String verify) {
        return iUserService.checkVerify(username, verify);
    }


    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user, String registerToken) {
        return iUserService.register(user, registerToken);
    }

    @RequestMapping(value = "check_username_valid.do", method = RequestMethod.GET)
    @ResponseBody
    public  ServerResponse<String> checkUsernameValid(String username) {
        return iUserService.checkUsernameValid(username);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    @RequestMapping(value = "forget_get_verify.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetVerify(String username) {
        return iUserService.forgetGetVerify(username);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return iUserService.forgetResetPassword(username, newPassword, forgetToken);
    }

    @RequestMapping(value = "reset_get_verify.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetGetVerify(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetGetVerify(user.getUsername());
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String newPassword, String resetToken) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(user, newPassword, resetToken);
    }



}
