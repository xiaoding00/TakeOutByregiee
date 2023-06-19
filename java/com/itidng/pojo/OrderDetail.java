package com.itidng.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细表
 */
@Data
public class OrderDetail {
    //订单明细id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //订单明细名称
    private String name;
    //订单明细图片
    private String image;
    //订单id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    //菜品id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dishId;
    //套餐id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long setmealId;
    //口味
    private String dishFlavor;
    //数量
    private Integer number;
    //金额
    private BigDecimal amount;
}
