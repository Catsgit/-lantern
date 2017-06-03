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

    public interface Cart{
        boolean CHECKED = true;    //购物车中选中状态
        boolean UN_CHECKED = false;     //购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT NUM FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT NUM SUCCESS";
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

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "交易完成"),
        ORDER_CLOSE(60, "交易关闭")
        ;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for(OrderStatusEnum orderStatusEnum : values()) {
                if(orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的订单状态");
        }
    }
    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");
        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
        public static PaymentTypeEnum codeOf(int code) {
            for(PaymentTypeEnum paymentTypeEnum : values()) {
                if(paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的支付类型");
        }
    }

}
