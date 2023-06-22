package com.itidng.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车表
 */
@Data
public class ShoppingCart {
    //购物车ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //购物车名称
    private String name;
    //图片
    private String image;
    //下单用户ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    //菜品Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dishId;
    //套餐Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long setmealId;
    //口味
    private String dishFlavor;
    //数量
    private Integer number;
    //金额
    private BigDecimal amount;
    //创建时间
    private LocalDateTime createTime;
}
