package com.itidng.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐关系表
 */

@Data
public class SetmealDish {
    //套餐关系id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //套餐id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long setmealId;
    //菜品id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dishId;
    //菜品名称
    private String name;
    //菜品原价
    private BigDecimal price;
    //套餐份数
    private Integer copies;
    //套餐显示排序
    private Integer sort;
    //套餐创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //套餐修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //套餐创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //套餐修改者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    //套餐是否被删除，0为否，1为是。
    private Integer isDeleted;
}
