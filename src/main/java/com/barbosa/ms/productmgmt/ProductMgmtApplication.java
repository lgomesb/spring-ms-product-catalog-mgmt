package com.barbosa.ms.productmgmt;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;

import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.services.CategoryService;
import com.barbosa.ms.productmgmt.services.ProductService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class ProductMgmtApplication implements CommandLineRunner {

	@Autowired
	private final MessageSource messageSource;

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

	    @PostConstruct
    public void postConstruct() {
        System.out.println("Running Message Property Data");
        System.out.println(messageSource.getMessage("field.name.required", null, Locale.getDefault()));
        System.out.println("End Message Property Data");
    }

}
