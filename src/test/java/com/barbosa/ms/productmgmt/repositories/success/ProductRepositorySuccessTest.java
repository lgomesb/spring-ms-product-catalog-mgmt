package com.barbosa.ms.productmgmt.repositories.success;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.barbosa.ms.productmgmt.ProductMgmtApplicationTests;
import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;

@ActiveProfiles(value = "test")
@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductRepositorySuccessTest {
    
    @Autowired
    private ProductRepository repository;

    private Product product;
    

    @Test
    @Order(0)
    void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Order(1)
    @ParameterizedTest
    @CsvSource({"Test-Create-Product,Category-Test"})
    void shouldWhenCallCreate(String productName, String categoryName) {
        product = repository.save(new Product(new Category(categoryName), productName, UUID.randomUUID()));
        assertNotNull(product, "Should return Product is not null");
        assertNotNull(product.getId());
        assertNotNull(product.getName());
        assertTrue(!product.getName().isBlank());
        assertEquals(productName, product.getName());
    }

    @Test
    @Order(2)
    void shouldWhenCallFindById() {
        product = repository.save(product);
        Optional<Product> oProduct = repository.findById(product.getId());
        assertNotNull(oProduct.get(), "Should return Product is not null");
        assertNotNull(oProduct.get().getId(), "Should return Product ID is not null");
        assertNotNull(oProduct.get().getName(), "Should return Product NAME is not null");
    }

    @Test
    @Order(3)
    void shouldWhenCallUpdate() {
        String productNameUpdate = "Test-Update-Product";
        product = repository.save(product);
        Optional<Product> oProduct = repository.findById(product.getId());
        Product newProduct = oProduct.get();
        newProduct.setName(productNameUpdate);
        newProduct = repository.save(newProduct);
        assertEquals(productNameUpdate, newProduct.getName());
    }

    @Test
    @Order(4)
    void shouldWhenCallDelete() {
        product = repository.save(product);
        Optional<Product> oProduct = repository.findById(product.getId());
        repository.delete(oProduct.get());
        Optional<Product> opProduct = repository.findById(product.getId());
        assertFalse(opProduct.isPresent());
    }
}
