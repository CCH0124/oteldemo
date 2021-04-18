package com.otel.example.oteldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OteldemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OteldemoApplication.class, args);
	}

}
