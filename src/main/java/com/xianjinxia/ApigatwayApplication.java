package com.xianjinxia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@EnableWebMvc
@ComponentScan(basePackages = "com.xianjinxia")
public class ApigatwayApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApigatwayApplication.class, args);
	}
}
