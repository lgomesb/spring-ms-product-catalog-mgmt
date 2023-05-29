package com.barbosa.ms.productmgmt.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Optional;

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

@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryRepositoryTest {
    
    @Autowired
    private CategoryRepository repository;

    private Category category;
    

    @Test
    @Order(0)
    public void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Test
    @Order(1)
    public void shouldWhenCallCreate() {
        String categoryName = "Test-Create-Category";
        category = repository.save(new Category(categoryName));
        assertNotNull(category, "Should return Category is not null");
        assertNotNull(category.getId());
        assertEquals(categoryName, category.getName());
    }

    @Test
    @Order(2)
    public void shouldWhenCallFindById() {
        category = repository.save(category);
        Optional<Category> oCategory = repository.findById(category.getId());
        assertNotNull(oCategory.get(), "Should return Category is not null");
        assertNotNull(oCategory.get().getId(), "Should return Category ID is not null");
        assertNotNull(oCategory.get().getName(), "Should return Category NAME is not null");
    }

    @Test
    @Order(3)
    public void shouldWhenCallUpdate() {
        String categoryNameUpdate = "Test-Update-Category";
        category = repository.save(category);
        Optional<Category> oCategory = repository.findById(category.getId());
        Category newCategory = oCategory.get();
        newCategory.setName(categoryNameUpdate);
        newCategory = repository.save(newCategory);
        assertEquals(categoryNameUpdate, newCategory.getName());
    }

    @Test
    @Order(4)
    public void shouldWhenCallDelete() {
        category = repository.save(category);
        Optional<Category> oCategory = repository.findById(category.getId());
        repository.delete(oCategory.get());
        Optional<Category> opCategory = repository.findById(category.getId());
        assertFalse(opCategory.isPresent());
    }
}
