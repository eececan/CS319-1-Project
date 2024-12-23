package com.project.btoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BtoprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtoprojectApplication.class, args);
	}

}
