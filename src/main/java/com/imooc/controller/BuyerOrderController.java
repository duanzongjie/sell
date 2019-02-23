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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/buyer/order/")
@RestController
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    //创建订单
    @PostMapping("/create")
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
    @GetMapping("/list")
    public ResultVO<List<OrderDto>> list(@RequestParam("openId") String openId,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size){
        if (null==openId){
            log.error("【查询订单列表】openId为空");
            throw  new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest request=new PageRequest(page,size);
        Page<OrderDto> orderDtoPage=orderService.findOrderList(openId,request);
        return ResultVOUtil.success(orderDtoPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDto> detail(@RequestParam("openId") String openId,
                                        @RequestParam("orderId") String orderId){
    OrderDto orderDto=orderService.findOneByOrderId(orderId);
    return ResultVOUtil.success(orderDto);
    }
    //取消订单
    @PostMapping("/cancel")
    public ResultVO cancel (@RequestParam("openId") String openId,
                            @RequestParam("orderId") String orderId){
        //TODO 不安全的做法，改进
        OrderDto orderDto=orderService.findOneByOrderId(orderId);
        orderService.cancelOrder(orderDto);
        return ResultVOUtil.success();
    }
}
