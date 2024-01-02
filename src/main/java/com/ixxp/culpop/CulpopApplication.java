package com.ixxp.culpop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class CulpopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CulpopApplication.class, args);
	}

	@GetMapping("/")
	public String test() {
		return "배포확인!!!";
	}
}
