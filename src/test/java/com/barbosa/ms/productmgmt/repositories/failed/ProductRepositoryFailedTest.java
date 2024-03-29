package com.barbosa.ms.productmgmt.repositories.failed;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.barbosa.ms.productmgmt.ProductMgmtApplicationTests;
import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;

import jakarta.validation.ConstraintViolationException;

@ActiveProfiles(value = "test")
@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductRepositoryFailedTest {
    
    private Product product;

    private Category category;
    
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    

    @BeforeEach
    void setup() {
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
    void shouldFailWhenCallCreate(String productName) {
        Product entity = new Product(category, productName, UUID.randomUUID());
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(entity);
        }, "Product name cannot be null, empty or blank");
    }

    @Test
    @Order(1)
    void shouldFailWhenCallFindById() {
        final Optional<Product> oProduct = repository.findById(UUID.randomUUID());
        assertThrows( ObjectNotFoundException.class, () -> {
            oProduct.orElseThrow(() ->
                 new ObjectNotFoundException("Product", UUID.randomUUID()));
        });
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {"Product-Update-Test"})
    void shouldFailWhenCallUpdate(String productName) {
        String productNameUpdate = "";
        product = repository.save(new Product(category, productName, UUID.randomUUID()));
        Optional<Product> oProduct = repository.findById(product.getId());
        Product newProduct = oProduct.get();
        newProduct.setName(productNameUpdate);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(newProduct);
        }, "Product name cannot be null, empty or blank");
    }

    @Order(3)
    @ParameterizedTest
    @ValueSource(strings = {"Product-Delete-Test"})
    void shouldFailWhenCallDelete(String productName) {
        product = repository.save(new Product(category, productName, UUID.randomUUID()));
        Optional<Product> opProduct = repository.findById(UUID.randomUUID());
        Product entity = opProduct.orElse(null);
        assertThrows( InvalidDataAccessApiUsageException.class, () -> {
            repository.delete(entity);
        }, "Should return Error when Product not blank or empty");
    }
}
