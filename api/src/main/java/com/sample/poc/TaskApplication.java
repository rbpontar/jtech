package com.sample.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.sample.poc.domain.model"})
@ComponentScan(basePackages = {
		"com.sample.poc.controller",
		"com.sample.poc.infrastructure",
		"com.sample.poc.application",
		"com.sample.poc.domain",
		"com.sample.poc.poc",
		"com.sample.poc.presentation",
		"com.sample.poc.configuration",
		"com.sample.poc.util" }) // Add all relevant packages
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
		System.out.println("Rodando");
	}
}
