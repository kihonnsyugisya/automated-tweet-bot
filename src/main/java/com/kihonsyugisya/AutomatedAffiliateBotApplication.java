package com.kihonsyugisya;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kihonsyugisya.repository")
public class AutomatedAffiliateBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatedAffiliateBotApplication.class, args);
	}

}
