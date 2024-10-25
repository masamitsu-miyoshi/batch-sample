package com.example.batch_sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.batch_sample")
public class BatchSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchSampleApplication.class, args);
	}

}
