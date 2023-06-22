package com.itidng.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebInterceptor webInterceptor() {
        return new WebInterceptor();
    }

    //静态资源映射
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("front/**").addResourceLocations("classpath:/front/");
        log.info("资源映射");
    }


    //拦截资源
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**", "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"

        };
        registry.addInterceptor(webInterceptor()).addPathPatterns("/**").excludePathPatterns(urls);
    }


}
