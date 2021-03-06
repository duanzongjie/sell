package com.imooc.service;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {

    ProductInfo findone(String productId);

    /**
     * 查询所有在架商品列表
     * @return
     */

    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    public void addStock(List<CartDto> cartDtos);

    //减库存
    public void delStock(List<CartDto> cartDtos);

}
