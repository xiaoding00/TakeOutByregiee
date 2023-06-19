package com.itidng.pojo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地址管理对象
 */
@Data
public class AddressBook {
    //地址管理id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    //收货人
    private String consignee;
    //性别 0 女 1 男
    private Integer sex;
    //手机号
    private String phone;
    //省级编号
    private String provinceCode;
    //省级名称
    private String provinceName;
    //市级编号
    private String cityCode;
    //市级名称
    private String cityName;
    //区级编号
    private String districtCode;
    //区级名称
    private String districtName;
    //详细地址
    private String detail;
    //标签
    private String label;
    //默认地址 0否 1是
    private Integer isDefault;
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
    //是否删除
    private Integer isDeleted;

}
