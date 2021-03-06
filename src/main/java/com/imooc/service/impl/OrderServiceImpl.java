package com.imooc.service.impl;

import com.imooc.convert.OrderMaster2OrderDto;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDto;
import com.imooc.dto.OrderDto;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMasterRepository orderMasterRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductService productService;

    @Override
    @Transactional
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
        orderDto.setOrderId(orderId);
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.NOPAY.getCode());
        orderMasterRepository.save(orderMaster);
        //4.减库存
        List<CartDto> cartDtos=new ArrayList<>();
        cartDtos=orderDto.getOrderDetails().stream()
                .map(e -> new CartDto(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.delStock(cartDtos);
        return orderDto;
    }

    @Override
    public OrderDto findOneByOrderId(String orderId) {
        OrderDto orderDto=new OrderDto();
        OrderMaster orderMaster= orderMasterRepository.findOne(orderId);
        if(null==orderMaster){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        BeanUtils.copyProperties(orderMaster,orderDto);
        List <OrderDetail> orderDetails=orderDetailRepository.findByOrderId(orderId);
        if(orderDetails.isEmpty()){
            throw new SellException(ResultEnum.DETAIL_NOT_EXIST);
        }
        orderDto.setOrderDetails(orderDetails);
        return orderDto;
    }

    @Override
    public Page<OrderDto> findOrderList(String buyerOpenId, Pageable pageable) {
        Page <OrderMaster> orderMaster=orderMasterRepository.findByBuyerOpenid(buyerOpenId,pageable);
        List <OrderDto> orderDtoList=OrderMaster2OrderDto.convertList(orderMaster.getContent());
        Page<OrderDto> orderDtoPage=new PageImpl<OrderDto>(orderDtoList,pageable,orderMaster.getTotalElements());
        return orderDtoPage;
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(OrderDto orderDto) {

        //判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERR);
        }
        //修改订单状态
        OrderMaster orderMaster=orderMasterRepository.findOne(orderDto.getOrderId());
        if (null==orderMaster){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        orderMasterRepository.save(orderMaster);
        //返回库存
        if(CollectionUtils.isEmpty(orderDto.getOrderDetails())){
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDto> cartDtos=new ArrayList<>();
        cartDtos=orderDto.getOrderDetails().stream()
                .map(e -> new CartDto(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.addStock(cartDtos);
        //如果已支付需要退款
        if(orderDto.getPayStatus().equals(PayStatusEnum.HASPAY)){
            //TODO
        }
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finishOrder(OrderDto orderDto) {
        //判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERR);
        }
        //修改订单状态
        OrderMaster orderMaster=orderMasterRepository.findOne(orderDto.getOrderId());
        if (null==orderMaster){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        orderMasterRepository.save(orderMaster);
        orderDto.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto payOrder(OrderDto orderDto) {
        //判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERR);
        }
        //判断支付状态
        if(!orderDto.getPayStatus().equals(PayStatusEnum.NOPAY.getCode())){
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERR);
        }
        //修改支付状态
        orderDto.setPayStatus(PayStatusEnum.HASPAY.getCode());
        OrderMaster master=new OrderMaster();
        BeanUtils.copyProperties(orderDto,master);
        OrderMaster result=orderMasterRepository.save(master);
        return orderDto;
    }
}
