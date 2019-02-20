package com.imooc.dataobject;


import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Id;


@Data
@Entity
@DynamicUpdate
public class OrderMaster {

    @Id
    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;

    /** 订单金额*/
    private BigDecimal orderAmount;

    /** 订单状态, 默认为新下单*/
    private Integer orderStatus=OrderStatusEnum.NEW.getCode();

    /** 支付状态, 默认未支付,0*/
    private Integer payStatus= PayStatusEnum.NOPAY.getCode();

    private Date createTime;

    private  Date updateTime;

}
