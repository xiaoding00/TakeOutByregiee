package com.itidng.config;

import com.alibaba.fastjson.JSON;
import com.itidng.pojo.User;
import com.itidng.result.CurrentId;
import com.itidng.result.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截网页，判断是否登录。
 */
@Slf4j
public class WebInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断员工是否登录
        if (request.getSession().getAttribute("employee") != null) {
            log.info("员工已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            //设置当前线程的用户ID
            CurrentId.setCurrentId(empId);
            long id = Thread.currentThread().getId();
            log.info("当前数据ID为：{}",empId+"当前数据线程为："+id);
            return true;
        }

        //判断用户是否登录
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("user"));
           User user = (User) request.getSession().getAttribute("user");
            Long userId = user.getId();
            CurrentId.setCurrentId(userId);
            long id = Thread.currentThread().getId();
            return true;
        }

        // 如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(new R<>(0, null, "NOTLOGIN")));
        return false;
    }
}
