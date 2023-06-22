package com.itidng.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 */
@Data
public class Orders {
    //订单id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //订单号
    private String number;
    //订单状态 1待付款 2待派送 3已派送 4已完成 5已取消
    private Integer status;
    //下单用户Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    //下单地址id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addressBookId;
    //下单时间
    private LocalDateTime orderTime;
    //结账时间
    private LocalDateTime checkoutTime;
    private Integer payMethod;
    //支付方式 1微信 2支付宝
    private BigDecimal amount;
    //备注
    private String remark;
    //手机号
    private String phone;
    //地址
    private String address;
    //用户名
    private String userName;
    //收货人
    private String consignee;
}
