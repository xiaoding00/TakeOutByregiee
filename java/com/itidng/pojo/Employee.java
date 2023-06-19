package com.itidng.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 员工对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee implements Serializable {
    //员工ID
    //问题：前端接收到的数据的ID末尾两到三位数字都变成了0。
    // 雪花ID的长度是19位数字，系统在bean中的ID用的是Long类型，数据库建表用的是bigint，
    // 接收雪花ID自然没有问题，但是前端的number类型只能接收16位数字，
    // 准确的说是：2的53次方减1，即为9007199254740991，所以回传的ID不对是数字精度丢失的原因造成的。
    //JsonSerialize注解可以帮我们实现字段值的序列化和反序列话，
    // @JsonSerialize(using = ToStringSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //员工姓名
    private String name;
    //员工账号名
    private String username;
    //员工账号密码
    private String password;
    //员工手机号
    private String phone;
    //员工性别
    private String sex;
    //员工编号
    private String idNumber;
    //员工状态
    private Integer status;
    //员工注册时间
    //插入时自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //员工修改时间
    //插入、修改时自动填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //员工创建者
    //插入时自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //员工修改者
    //插入、修改时自动填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
