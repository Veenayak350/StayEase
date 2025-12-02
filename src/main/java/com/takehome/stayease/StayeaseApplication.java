package com.takehome.stayease;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;


@SpringBootApplication
public class StayeaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(StayeaseApplication.class, args);
	}

	@Bean
	@Scope("prototype")
	public ModelMapper  modelMapper() {
		return new ModelMapper();
	} 


}
