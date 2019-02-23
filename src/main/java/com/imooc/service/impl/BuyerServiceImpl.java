package com.imooc.service.impl;

import com.imooc.dto.OrderDto;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDto findOrderOne(String openId, String orderId) {
        OrderDto orderDto=orderService.findOneByOrderId(orderId);
        if(!orderDto.getBuyerOpenid().equals(openId)){
            log.error("【查询订单】订单的openId不一致.openId={}",openId);
            throw new SellException(ResultEnum.ORDER_OWEN_ERROR);
        }
        return orderDto;
    }

    @Override
    public OrderDto cancelOrder(String openId, String orderId) {
        OrderDto orderDto=orderService.findOneByOrderId(orderId);
        if(!orderDto.getBuyerOpenid().equals(openId)){
            log.error("【查询订单】订单的openId不一致.openId={}",openId);
            throw new SellException(ResultEnum.ORDER_OWEN_ERROR);
        }
        return orderDto;
    }
}
