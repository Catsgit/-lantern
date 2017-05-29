package com.lantern.common;

/**
 * Created by cat on 17-5-28.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public interface Role{
        int ROLE_SYSTEM_ADMIN = 0;      //系统管理员
        int ROLE_ADMIN = 1;     //管理员
        int ROLE_CUSTOMER = 2;      //普通用户
    }
}
