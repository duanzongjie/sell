package com.imooc.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class OrderForm {

    /**
     *买家姓名
     *
     */
    @NotEmpty(message = "姓名必填")
    private String name;

    /**
     * 手机号码
     *
     */
    @NotEmpty(message = "手机号码必填")
    private String phone;

    /**
     * 地址
     *
     */
    @NotEmpty(message = "地址必填")
    private String adress;

    /**
     * 买家微信
     */
    @NotEmpty(message = "买家微信")
    private String openid;

    /**
     * 购物车不能为空
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;

}
