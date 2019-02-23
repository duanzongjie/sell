package com.imooc.convert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.OrderDto;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDto {

    public static OrderDto convert(OrderForm orderForm){
        Gson gson=new Gson();
        OrderDto orderDto=new OrderDto();
         orderDto.setBuyerOpenid(orderForm.getOpenid());
         orderDto.setBuyerPhone(orderForm.getPhone());
         orderDto.setBuyerAddress(orderForm.getAdress());
         orderDto.setBuyerName(orderForm.getName());
        List<OrderDetail> orderDetailList=new ArrayList<>();
         try{
             orderDetailList= gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());

         }catch (Exception e){
             log.error("【对象转换异常】string={}",orderForm.getItems());
             throw new SellException(ResultEnum.PARAM_ERROR);
         }
         orderDto.setOrderDetails(orderDetailList);
         return orderDto;
    }
}
