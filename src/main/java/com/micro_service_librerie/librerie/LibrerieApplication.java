package com.micro_service_librerie.librerie;



import com.micro_service_librerie.librerie.Model.Book;
import com.micro_service_librerie.librerie.Model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.fasterxml.classmate.TypeResolver;

@SpringBootApplication
@EnableSwagger2

@Configuration
public class LibrerieApplication {


	@Autowired
	private TypeResolver typeResolver;

	public static void main(String[] args) {
		SpringApplication.run(LibrerieApplication.class, args);
	}
 


	@Bean
	public Docket librerieApi(){
		return new Docket (DocumentationType.SWAGGER_2)
				 .additionalModels(
					typeResolver.resolve(Book.class),
					typeResolver.resolve(User.class))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.micro_service_librerie.librerie.Controller"))
				.build();
	}


	
}












