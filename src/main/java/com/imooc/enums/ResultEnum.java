package com.imooc.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    PARAM_ERROR(1,"参数不正确"),

    PRODUCT_NOT_EXIST(10,"商品不存在"),

    PRODUCT_NOT_ENOUGH(11,"商品库存不足"),

    ORDER_NOT_EXIST(20,"订单不存在"),

    DETAIL_NOT_EXIST(21,"详单不存在"),

    ORDER_STATUS_ERR(22,"订单状态不正确"),

    ORDER_DETAIL_EMPTY(23,"订单中无商品"),

    ORDER_PAY_STATUS_ERR(24,"订单支付状态不正确"),

    CART_IS_EMPTY(25,"购物车为空");



    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
