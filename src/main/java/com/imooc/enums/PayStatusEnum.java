package com.imooc.enums;


import lombok.Getter;

@Getter
public enum PayStatusEnum {

    HASPAY(1,"支付成功"),
    NOPAY(0,"未支付");

    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
