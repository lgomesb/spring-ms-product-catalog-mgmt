package com.barbosa.ms.productmgmt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles(value = "test")
public class ProductMgmtApplicationTests implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProductMgmtApplicationTests.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("::::::::::::::: TESTE ::::::::::::::::::::");
	}

	@Test
	void assertTrue() {
		Assertions.assertTrue(Boolean.TRUE);
	}

}
