package com.vp.vpbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vp.vpbackend.mapper")
public class VpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpBackendApplication.class, args);
	}

}
