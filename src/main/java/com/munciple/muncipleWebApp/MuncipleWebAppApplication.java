package com.munciple.muncipleWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MuncipleWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuncipleWebAppApplication.class, args);
		System.out.println("It Working...!");
	}

}
