package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    OrderServiceImpl orderService;

    @Test
    public void createOrder() {
        OrderDto orderDto=new OrderDto();
        orderDto.setBuyerName("段先生");
        orderDto.setBuyerAddress("红柿苑");
        orderDto.setBuyerPhone("123456789");
        orderDto.setBuyerOpenid("d382602061");

        //购物车
        List<OrderDetail> orderDetails=new ArrayList<>();
        OrderDetail orderDetail=new OrderDetail();
        orderDetail.setProductId("12323");//产品Id
        orderDetail.setProductQuantity(4);//产品数量
        orderDetails.add(orderDetail);
        orderDto.setOrderDetails(orderDetails);
        OrderDto result= orderService.createOrder(orderDto);
        log.info("[创建订单] result="+result);

    }

    @Test
    public void findOneByOrderId() {
    }

    @Test
    public void findOrderList() {
    }

    @Test
    public void cancelOrder() {
    }

    @Test
    public void finishOrder() {
    }

    @Test
    public void payOrder() {
    }
}