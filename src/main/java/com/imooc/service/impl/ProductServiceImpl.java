package com.imooc.service.impl;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository repository;

    @Override
    public ProductInfo findone(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void addStock(List<CartDto> cartDtos) {
        for(CartDto cartDto:cartDtos){
            ProductInfo productInfo= repository.findOne(cartDto.getProductId());
            if(null==productInfo){
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            productInfo.setProductStock(productInfo.getProductStock()+cartDto.getProductQuantity());
            repository.save(productInfo);
        }

    }

    @Override
    @Transactional
    public void delStock(List<CartDto> cartDtos) {
        for(CartDto cartDto:cartDtos){
           ProductInfo productInfo= repository.findOne(cartDto.getProductId());
           if(null==productInfo){
               throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
           }
           if(productInfo.getProductStock()<cartDto.getProductQuantity()){
               throw  new SellException(ResultEnum.PRODUCT_NOT_ENOUGH);
           }
            productInfo.setProductStock(productInfo.getProductStock()-cartDto.getProductQuantity());
            repository.save(productInfo);
        }
    }
}
