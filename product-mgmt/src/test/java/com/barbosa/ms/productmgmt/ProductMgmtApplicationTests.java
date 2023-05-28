package com.barbosa.ms.productmgmt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductMgmtApplicationTests implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProductMgmtApplicationTests.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("::::::::::::::: TESTE ::::::::::::::::::::");
	}

}
