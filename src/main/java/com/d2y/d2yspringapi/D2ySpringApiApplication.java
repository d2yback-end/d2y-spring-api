package com.d2y.d2yspringapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.d2y.d2yspringapi.dto.RegisterRequest;
import com.d2y.d2yspringapi.models.Role;
import com.d2y.d2yspringapi.services.AuthenticationService;

@SpringBootApplication
@EnableAsync
public class D2ySpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(D2ySpringApiApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner commandLineRunner(AuthenticationService service) {
	// return args -> {
	// var admin = RegisterRequest.builder()
	// .firstName("Admin")
	// .lastName("Admin")
	// .email("munawar.adi31@mail.com")
	// .password("password")
	// .role(Role.ADMIN)
	// .build();
	// System.out.println("Admin : " + service.registerUser(admin));

	// var manager = RegisterRequest.builder()
	// .firstName("Admin")
	// .lastName("Admin")
	// .email("manager@mail.com")
	// .password("password")
	// .role(Role.MANAGER)
	// .build();
	// System.out.println("Manager : " + service.registerUser(manager));

	// };
	// }

}
