package com.vp.vpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpBackendApplication.class, args);
	}

}
