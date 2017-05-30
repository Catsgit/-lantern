package com.lantern.service;

import com.lantern.common.ServerResponse;
import com.lantern.pojo.User;
import com.mysql.fabric.Server;

import javax.servlet.http.HttpSession;

/**
 * Created by cat on 17-5-28.
 */
public interface IUserService {

    //portal
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user, String registerToken);

    ServerResponse<String> registerGetVerify(String username);

    ServerResponse<String> checkUsernameValid(String username);

    ServerResponse<String> getVerify(String username);

    ServerResponse<String> checkVerify(String username, String verify);

    ServerResponse<String> forgetGetVerify(String username);

    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

    ServerResponse<String> resetGetVerify(String username);

    ServerResponse<String> resetPassword(User user, String newPassword, String resetToken);

    ServerResponse<User> updateInformation(User user);

    //ServerResponse<User> getInformation(String username);

    //backend
    ServerResponse checkAdminRole(User user);
}
