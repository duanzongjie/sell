package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.dataobject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.utils.serializer.Date2LongSerilizer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {

    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;


    /** 买家微信号*/
    private String buyerOpenid;

    /** 订单金额*/
    private BigDecimal orderAmount;

    /** 订单状态, 默认为新下单*/
    private Integer orderStatus;

    /** 支付状态, 默认未支付,0*/
    private Integer payStatus;

    /** 创建时间*/
    @JsonSerialize(using = Date2LongSerilizer.class)
    private Date createTime;
    /** 更新时间*/
    @JsonSerialize(using = Date2LongSerilizer.class)
    private  Date updateTime;

    private List<OrderDetail> orderDetails;
}
