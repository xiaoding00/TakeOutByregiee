package com.itidng.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐表
 */
@Data
public class Setmeal {
    //套餐Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //套餐分类Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;
    //套餐名称
    private String name;
    //套餐价格
    private BigDecimal price;
    //套餐状态 0停售，1起售
    private Integer status;
    //套餐代码
    private Long code;
    //套餐描述
    private String description;
    //套餐图片
    private String image;
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
    //套餐是否已被删除，0为否，1为是
    private Integer isDeleted;
}
