package com.lantern.common;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Created by cat on 17-5-28.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Role{
        int ROLE_SYSTEM_ADMIN = 0;      //系统管理员
        int ROLE_ADMIN = 1;     //管理员
        int ROLE_CUSTOMER = 2;      //普通用户
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在售");
        private int code;
        private String value;
        ProductStatusEnum(int code, String value) {
             this.code = code;
             this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

    }
}
