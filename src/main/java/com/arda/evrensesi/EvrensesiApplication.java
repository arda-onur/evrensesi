package com.arda.evrensesi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EvrensesiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvrensesiApplication.class, args);
	}

}
