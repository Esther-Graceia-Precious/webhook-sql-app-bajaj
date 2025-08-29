package com.example.webhooksqlapp; // must match the package of AppStartupRunner or be a parent package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebhookSqlAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebhookSqlAppApplication.class, args);
	}
}
