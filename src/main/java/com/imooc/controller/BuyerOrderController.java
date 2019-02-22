package com.imooc.controller;

import com.imooc.convert.OrderForm2OrderDto;
import com.imooc.dto.OrderDto;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.service.OrderService;
import com.imooc.utils.ResultVOUtil;
import com.imooc.viewobject.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/buyer/order/")
@RestController
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    //创建订单
    public ResultVO <Map<String,String>> createOrder(@Valid OrderForm orderForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确，orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDto orderDto= OrderForm2OrderDto.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDto.getOrderDetails())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_IS_EMPTY);
        }

        OrderDto result=orderService.createOrder(orderDto);

        Map<String,String> map=new HashMap<>();
        map.put("orderId",result.getOrderId());
        return ResultVOUtil.success(map);

    }


    //订单列表

    //订单详情

    //取消订单
}
