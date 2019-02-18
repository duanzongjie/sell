package com.imooc.service;

import com.imooc.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    //创建订单
    OrderDto createOrder(OrderDto orderDto);

    //查询单个订单
    OrderDto findOneByOrderId(String orderId);

    //查询订单列表
    Page<OrderDto> findOrderList(String buyerOpenId, Pageable pageable);

    //取消订单
    OrderDto cancelOrder(OrderDto orderDto);

    //完结订单
    OrderDto finishOrder(OrderDto orderDto);

    //支付订单
    OrderDto payOrder(OrderDto orderDto);

}
