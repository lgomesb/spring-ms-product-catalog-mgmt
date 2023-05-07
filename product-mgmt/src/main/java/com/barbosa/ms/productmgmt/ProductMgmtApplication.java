package com.barbosa.ms.productmgmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;

@SpringBootApplication
public class ProductMgmtApplication implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductMgmtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Category category = new Category();
		category.setName("computing");
		Product product = new Product();
		product.setName("keyboard");
		product.setCategory(category);

		categoryRepository.save(category);
		productRepository.save(product);
		
	}

}
