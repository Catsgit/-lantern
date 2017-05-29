package com.lantern.service.impl;

import com.lantern.common.Const;
import com.lantern.common.ServerResponse;
import com.lantern.common.TokenCache;
import com.lantern.common.VerifyCache;
import com.lantern.dao.UserMapper;
import com.lantern.pojo.User;
import com.lantern.service.IUserService;
import com.lantern.util.MD5Util;
import com.lantern.util.VerifyUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by cat on 17-5-28.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("帐号未注册");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user, String registerToken) {
        if(StringUtils.isBlank(registerToken)) {
            return ServerResponse.createByErrorMessage("参数错误, token需要传递");
        }
        ServerResponse validResponse = this.checkUsernameValid(user.getUsername());
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + user.getUsername());
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }

        if(StringUtils.equals(registerToken, token)) {
            user.setRole(Const.Role.ROLE_CUSTOMER);
            user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
            int resultCount = userMapper.insert(user);
            if(resultCount == 0) {
                return ServerResponse.createByErrorMessage("注册失败");
            }
            return ServerResponse.createBySuccessMessage("注册成功");
        }
        return ServerResponse.createByErrorMessage("token错误,请重新获取");

    }

    @Override
    public ServerResponse<String> registerGetVerify(String username) {
        ServerResponse validResponse = this.checkUsernameValid(username);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        return getVerify(username);
    }

    @Override
    public ServerResponse<String> getVerify(String username) {
//        String code = VerifyUtil.sendVerify(username);
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        if(code == null) {
            return ServerResponse.createByErrorMessage("验证码生成失败");
        }
        code = MD5Util.MD5EncodeUtf8(code);
        VerifyCache.setKey(VerifyCache.VERIFY_PREFIX + username, code);
        return ServerResponse.createBySuccessMessage("验证码发送成功");
    }

    @Override
    public ServerResponse<String> checkVerify(String username, String verify) {
        if(StringUtils.isBlank(verify)) {
            return ServerResponse.createByErrorMessage("参数错误, 验证码需要传递");
        }

        String v = VerifyCache.getKey(VerifyCache.VERIFY_PREFIX + username);
        if(StringUtils.isBlank(v)) {
            return ServerResponse.createByErrorMessage("验证码无效或过期");
        }
        if(StringUtils.equals(MD5Util.MD5EncodeUtf8(verify), v)) {
            String verifyToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, verifyToken);
            return ServerResponse.createBySuccessMessage(verifyToken);
        }
        return ServerResponse.createByErrorMessage("验证码错误");
    }

    @Override
    public ServerResponse<String> checkUsernameValid(String username) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount > 0) {
            return ServerResponse.createByErrorMessage("帐号已存在");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> forgetGetVerify(String username) {
        ServerResponse validResponse = this.checkUsernameValid(username);
        if(validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("帐号不存在");
        }
        return getVerify(username);
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数数据, token需要传递");
        }
        ServerResponse validResponse = this.checkUsernameValid(username);
        if(validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("帐号不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(resultCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            } else {
                return ServerResponse.createByErrorMessage("修改密码失败");
            }
        }
        return ServerResponse.createByErrorMessage("token错误, 请重新获取");
    }

    @Override
    public ServerResponse<String> resetGetVerify(String username) {
        return getVerify(username);
    }

    @Override
    public ServerResponse<String> resetPassword(User user, String newPassword, String resetToken) {
        if(StringUtils.isBlank(resetToken)) {
            return ServerResponse.createByErrorMessage("参数错误, token需要传递");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + user.getUsername());
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(resetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            user.setPassword(md5Password);
            int updateCount = userMapper.updateByPrimaryKeySelective(user);
            if(updateCount == 0) {
                return ServerResponse.createByErrorMessage("密码更新失败");
            }
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("token错误, 请重新获取");
    }
}
