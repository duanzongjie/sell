package com.imooc.service;

import com.imooc.dto.OrderDto;
import org.hibernate.criterion.Order;

public interface BuyerService {

    //查询一个订单
    OrderDto findOrderOne(String openId,String orderId);

    //取消一个订单
    OrderDto cancelOrder (String openId,String orderId);
}
