package com.barbosa.ms.productmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class ProductMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMgmtApplication.class, args);
	}	

}
