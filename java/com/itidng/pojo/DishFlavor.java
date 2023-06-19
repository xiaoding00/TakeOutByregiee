package com.itidng.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 口味对象
 */
@Data
public class DishFlavor {
    //口味ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //对应的菜品ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dishId;
    //口味类型名称：辣度，甜度......
    private String name;
    //选择的口味值
    private String value;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //修改者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    //口味是否被删除
    private Integer isDeleted;
}
