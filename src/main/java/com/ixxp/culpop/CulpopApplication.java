package com.ixxp.culpop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CulpopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CulpopApplication.class, args);
		System.out.println("hello world");
	}

}
