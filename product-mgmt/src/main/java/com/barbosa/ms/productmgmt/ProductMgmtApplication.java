package com.barbosa.ms.productmgmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.services.CategoryService;
import com.barbosa.ms.productmgmt.services.ProductService;

@SpringBootApplication
public class ProductMgmtApplication implements CommandLineRunner {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoriaService;

	public static void main(String[] args) {
		SpringApplication.run(ProductMgmtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("::::::::::::::: NORMAL ::::::::::::::::::::");
		try {
			CategoryRecord category = categoriaService.create(new CategoryRecord(null, "Test-Category-01"));
			ProductRecord product = productService.create(new ProductRecord(null, "Product-Test01", category.id()));
	
			System.out.println("CATEGORY");
			System.out.println(category);
			System.out.println("PRODUCT");
			System.out.println(product);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
