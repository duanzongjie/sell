package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Test
    public void  save(){
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId("12312");
        productInfo.setProductName("烤全羊");
        productInfo.setProductPrice(new BigDecimal(1300.00));
        productInfo.setProductDescription("很好吃的羊子");
        productInfo.setProductIcon("http://xxxx.com");
        productInfo.setProductStock(20);
        productInfo.setCategoryType(2);
        productInfo.setProductStatus(0);
        productInfoRepository.save(productInfo);
    }

    @Test
    public void findByProductStatus() {
       List <ProductInfo> productInfoList=productInfoRepository.findByProductStatus(0);
        Assert.assertNotEquals(productInfoList.size(),0);
    }
}