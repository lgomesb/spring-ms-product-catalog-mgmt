package com.barbosa.ms.productmgmt.repositories.failed;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;

import com.barbosa.ms.productmgmt.ProductMgmtApplicationTests;
import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductRepositoryFailedTest {
    
    private Product product;

    private Category category;
    
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    

    @BeforeEach
    public void setup() {
        assertNotNull(categoryRepository);
        assertNotNull(repository);
        category = categoryRepository.saveAndFlush(new Category("Category-Test"));
        System.out.println("#".repeat(80));
        System.out.println("BeforeEach");
        System.out.println("#".repeat(80));
    }

    @Order(0)
    @DisplayName("Should throw error when to try to salve product name null")
    @ParameterizedTest
    @CsvSource({"''"})
    public void shouldFailWhenCallCreate(String productName) {
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(new Product(category, productName, UUID.randomUUID()));
        }, "Product name cannot be null, empty or blank");
    }

    @Test
    @Order(1)
    public void shouldFailWhenCallFindById() {
        final Optional<Product> oProduct = repository.findById(UUID.randomUUID());
        assertThrows( ObjectNotFoundException.class, () -> {
            oProduct.orElseThrow(() ->
                 new ObjectNotFoundException("Product", UUID.randomUUID()));
        });
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {"Product-Update-Test"})
    public void shouldFailWhenCallUpdate(String productName) {
        String productNameUpdate = "";
        product = repository.save(new Product(category, productName, UUID.randomUUID()));
        Optional<Product> oProduct = repository.findById(product.getId());
        assertThrows(ConstraintViolationException.class, () -> {
            Product newProduct = oProduct.get();
            newProduct.setName(productNameUpdate);
            repository.saveAndFlush(newProduct);
        }, "Product name cannot be null, empty or blank");
    }

    @Order(3)
    @ParameterizedTest
    @ValueSource(strings = {"Product-Delete-Test"})
    public void shouldFailWhenCallDelete(String productName) {
        product = repository.save(new Product(category, productName, UUID.randomUUID()));
        Optional<Product> opProduct = repository.findById(UUID.randomUUID());
        assertThrows( InvalidDataAccessApiUsageException.class, () -> {
            repository.delete(opProduct.orElse(null));
        }, "Should return Error when Product not blank or empty");
    }
}
