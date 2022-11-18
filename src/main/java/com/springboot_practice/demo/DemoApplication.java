package com.springboot_practice.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling	// 啟用排程功能
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		System.out.println("Run Success !");
	}

	// API 性質
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "Danece") String name) {
		return String.format("Hello %s !", name);
	}

	@GetMapping("/hello/{id}")
	public String hello_2(@PathVariable String id) {
		return String.format("Hello %s !", id);
	}

}
