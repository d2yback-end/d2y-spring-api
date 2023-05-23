package com.d2y.d2yspringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class D2ySpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(D2ySpringApiApplication.class, args);
	}

}
