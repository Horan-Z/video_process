package com.vp.vpbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // 启用异步方法支持
@MapperScan("com.vp.vpbackend.mapper")
public class VpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VpBackendApplication.class, args);
    }

}
