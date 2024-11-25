package com.iteletric.iteletricapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.iteletric.iteletricapi.models")
public class IteletricApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IteletricApiApplication.class, args);
	}

}
