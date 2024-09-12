package com.asat.tra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TraApplication {
	public static void main(String[] args) {
		AppInitializer.initialize();
		SpringApplication.run(TraApplication.class, args);
	}
}