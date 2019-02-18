package com.imooc.dto;

import lombok.Data;

@Data
public class CartDto {

    //商品id
    private String productId;

    //数量
    private Integer productCount;

    public CartDto(String productId, Integer productCount) {
        this.productId = productId;
        this.productCount = productCount;
    }
}
