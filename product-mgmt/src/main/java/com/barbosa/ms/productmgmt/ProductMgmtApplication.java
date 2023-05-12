package com.barbosa.ms.productmgmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.barbosa.ms.productmgmt.domain.dto.CategoryResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.CreateCategoryDTO;
import com.barbosa.ms.productmgmt.domain.dto.CreateProductDTO;
import com.barbosa.ms.productmgmt.domain.dto.ProductResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.ResponseDTO;
import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.CategoriaService;
import com.barbosa.ms.productmgmt.services.ProductService;

@SpringBootApplication
public class ProductMgmtApplication implements CommandLineRunner {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoriaService categoriaService;

	public static void main(String[] args) {
		SpringApplication.run(ProductMgmtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		CreateCategoryDTO dto = new CreateCategoryDTO();
		dto.setName("Test01");

		CategoryResponseDTO categoryResponse = (CategoryResponseDTO) categoriaService.create(dto);

		CreateProductDTO productDTO = new CreateProductDTO();
		productDTO.setName("Product-Test01");
		productDTO.setIdCategory(categoryResponse.getId().toString());

		ProductResponseDTO productResponse = (ProductResponseDTO) productService.create(productDTO);

		System.out.println("CATEGORY");
		System.out.println(categoryResponse);
		System.out.println("PRODUCT");
		System.out.println(productResponse);


		// Category category = new Category();
		// category.setName("computing");
		// Product product = new Product();
		// product.setName("keyboard");
		// product.setCategory(category);

		// categoryRepository.save(category);
		// productRepository.save(product);
		
	}

}
