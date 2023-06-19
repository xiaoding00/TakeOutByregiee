package com.itidng.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类别对象
 */


@Data
public class Category implements Serializable {
    //类别ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //类别类型 1菜品分类 2套餐分类
    private Integer type;
    //类别名称
    private String name;
    //类别排序
    private Integer sort;
    //类别创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //类别修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //类别创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //类别修改者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
