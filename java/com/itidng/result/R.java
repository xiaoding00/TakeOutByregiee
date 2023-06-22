package com.itidng.result;



import lombok.Data;
import java.util.HashMap;
import java.util.Map;


//与前端规定好的返回结果
@Data
public class R <T>{
    private Integer code;//编码为1 成功 编码为0 失败
    private T data; //数据
    private String msg; //消息

    private Map map=new HashMap();

    public R(Integer i, T employee, String msg) {
        this.code=i;
        this.data=employee;
        this.msg=msg;
    }
    public R(Integer i, T employee, String msg, Map map){
        this.code=i;
        this.data=employee;
        this.msg=msg;
        this.map=map;
    }


}
