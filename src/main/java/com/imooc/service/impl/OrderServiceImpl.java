package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import com.imooc.dto.OrderDto;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMasterRepository orderMasterRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductService productService;

    public OrderDto createOrder(OrderDto orderDto) {

        String orderId=KeyUtil.getUniqueKey();
        BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);
//        List<CartDto> cartDtos=new ArrayList<>();

        //1.查询商品（数量，价格）
        for(OrderDetail orderDetail:orderDto.getOrderDetails()){
            ProductInfo productInfo=productService.findone(orderDetail.getProductId());
            if (null==productInfo){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算总价  商品价格需要从数据库当中取
            orderAmount=productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //订单详情入库(orderDetail)
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);

//            CartDto cartDto=new CartDto(orderDetail.getProductId(),orderDetail.getProductQuantity());
//            cartDtos.add(cartDto);
        }

        //3.写入订单数据库（orderMaster）
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMasterRepository.save(orderMaster);
        //4.减库存
        List<CartDto> cartDtos=new ArrayList<>();
        orderDto.getOrderDetails().stream()
                .map(e -> new CartDto(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.delStock(cartDtos);
        return orderDto;
    }

    @Override
    public OrderDto findOneByOrderId(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDto> findOrderList(String buyerOpenId, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDto cancelOrder(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto finishOrder(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto payOrder(OrderDto orderDto) {
        return null;
    }
}
