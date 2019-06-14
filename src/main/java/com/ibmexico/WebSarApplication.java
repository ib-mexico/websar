package com.ibmexico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebSarApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSarApplication.class, args);
	}
}
