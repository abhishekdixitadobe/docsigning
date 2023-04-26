package com.hr.docsigning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DocsigningApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocsigningApplication.class, args);
	}
}
