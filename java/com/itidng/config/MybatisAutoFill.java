package com.itidng.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itidng.result.CurrentId;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;


//公共字段自动填充
@Slf4j
@Configuration
public class MybatisAutoFill implements MetaObjectHandler {

    //插入时执行该方法
    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentId = CurrentId.getCurrentId();
        metaObject.setValue("createUser", currentId);
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentId);
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    //更新时是执行该方法
    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentId = CurrentId.getCurrentId();
        long id = Thread.currentThread().getId();
        log.info("当前数据ID为：{}", currentId + "当前数据线程为：" + id);
        metaObject.setValue("updateUser", currentId);
        metaObject.setValue("updateTime", LocalDateTime.now());

    }
}
