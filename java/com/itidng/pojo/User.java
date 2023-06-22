package com.itidng.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用户表
 */
@Data
public class User {
    //用户Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //用户姓名
    private String name;
    //用户手机号,本次用邮件号替代。
    private String phone;
    //用户性别
    private String sex;
    //用户身份证号
    private String idNumber;
    //用户头像
    private String avatar;
    //用户当前状态
    private Integer status;
}
