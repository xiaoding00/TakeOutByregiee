package com.itidng.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Dish {
    //菜品id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //菜品名称
    private String name;
    //菜品类别id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;
    //菜品价格
    private BigDecimal price;
    //菜品代号
    private Long code;
    //菜品图示
    private String image;
    //菜品描述
    private String description;
    //菜品状态
    private Integer status;
    //菜品分类
    private Integer sort;
    //菜品创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //菜品修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //菜品创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //菜品修改者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    //菜品是否被删除
    private Long isDeleted;
}
