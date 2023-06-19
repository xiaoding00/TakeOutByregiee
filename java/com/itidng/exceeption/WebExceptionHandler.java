package com.itidng.exceeption;


import com.itidng.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * 全局异常捕获类
 */


@RestControllerAdvice
@ResponseBody
@Slf4j
public class WebExceptionHandler {

    /**
     * 判断添加员工时，用户名是否重复
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public R<String> sqlException(SQLException exception) {
        String message = exception.getMessage();
        log.info(message);
        if (message.contains("Duplicate entry")) {
            return new R<>(0, null, "账号" + message.split(" ")[2] + "已存在");
        }
        return new R<>(0, null, "错误未知");
    }

    /**
     * 判断是否发送了验证码，code为空则发送验证码
     *
     * @param exception
     * @return
     */
//    java.lang.NullPointerException: Cannot invoke "String.equals(Object)" because "codeInfo" is null
    @ExceptionHandler(NullPointerException.class)
    public R<String> NullPointerException(NullPointerException exception) {
        String message = exception.getMessage();
        log.info(message);
        if (message.contains("\"codeInfo\" is null")) {
            return new R<>(0, null, "验证码错误");
        }
        log.info(message);
        return new R<>(0, null, "错误未知");
    }


}
