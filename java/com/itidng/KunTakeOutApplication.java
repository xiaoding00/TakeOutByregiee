package com.itidng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class KunTakeOutApplication {


    public static void main(String[] args) {
        SpringApplication.run(KunTakeOutApplication.class, args);
        log.info("项目启动成功");
    }

}
