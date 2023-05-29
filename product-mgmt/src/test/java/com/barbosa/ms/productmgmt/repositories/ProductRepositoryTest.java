package com.barbosa.ms.productmgmt.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.barbosa.ms.productmgmt.ProductMgmtApplicationTests;
import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;

@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository repository;

    private Product product;
    

    @Test
    @Order(0)
    public void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Test
    @Order(1)
    public void shouldWhenCallCreate() {
        String productName = "Test-Create-Product";
        product = repository.save(new Product(new Category("Category-Test"), productName, UUID.randomUUID()));
        assertNotNull(product, "Should return Product is not null");
        assertNotNull(product.getId());
        assertEquals(productName, product.getName());
    }

    @Test
    @Order(2)
    public void shouldWhenCallFindById() {
        product = repository.save(product);
        Optional<Product> oProduct = repository.findById(product.getId());
        assertNotNull(oProduct.get(), "Should return Product is not null");
        assertNotNull(oProduct.get().getId(), "Should return Product ID is not null");
        assertNotNull(oProduct.get().getName(), "Should return Product NAME is not null");
    }

    @Test
    @Order(3)
    public void shouldWhenCallUpdate() {
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
    public void shouldWhenCallDelete() {
        product = repository.save(product);
        Optional<Product> oProduct = repository.findById(product.getId());
        repository.delete(oProduct.get());
        Optional<Product> opProduct = repository.findById(product.getId());
        assertFalse(opProduct.isPresent());
    }
}
