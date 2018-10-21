package com.nilotpal.api.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = { "com.nilotpal.api.userapi.entity" })
public class UserApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}
}
