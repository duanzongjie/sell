package com.imooc.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    PRODUCT_NOT_EXIST(10,"商品不存在"),

    PRODUCT_NOT_ENOUGH(20,"商品库存不足"),

    ORDER_NOT_EXIST(30,"订单不存在"),

    DETAIL_NOT_EXIST(40,"详单不存在");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
