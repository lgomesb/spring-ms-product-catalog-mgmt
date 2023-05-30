package com.barbosa.ms.productmgmt.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    private List<Category> categories = new ArrayList<>();

    @ParameterizedTest
    @MethodSource("provideCategoryData")
    void categoryDataProvider( String name) {
        System.out.println(":::::::::::::: Category: " + name );
        assertTrue(true);
    }

    private static Stream<Arguments> provideCategoryData() {        
        return Stream.of(
          Arguments.of("Test-01"),
          Arguments.of("Test-02")
        );
    }

    @Test 
    @Order(0)
    public void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("provideCategoryData")
    public void shouldWhenCallCreate(String categoryName) {
        category = repository.save(new Category(categoryName));
        categories.add(category);
        assertNotNull(category, "Should return Category is not null");
        assertNotNull(category.getId());
        assertEquals(categoryName, category.getName());        
    }

    @Test
    @Order(2)
    public void shouldWhenCallFindById() {
        categories = repository.saveAll(categories);
        Optional<Category> oCategory = repository
            .findById(categories.stream()
                .findAny()
                .get()
                .getId());
        assertNotNull(oCategory.get(), "Should return Category is not null");
        assertNotNull(oCategory.get().getId(), "Should return Category ID is not null");
        assertNotNull(oCategory.get().getName(), "Should return Category NAME is not null");
    }

    @Test
    @Order(3)
    public void shouldWhenCallUpdate() {
        String categoryNameUpdate = "Test-Update-Category";
        categories = repository.saveAll(categories);
        Optional<Category> oCategory = repository
            .findById(categories.stream()
                .findAny()
                .get()
                .getId());
        Category newCategory = oCategory.get();
        newCategory.setName(categoryNameUpdate);
        newCategory = repository.save(newCategory);
        assertEquals(categoryNameUpdate, newCategory.getName());
    }

    @Test
    @Order(4)
    public void shouldWhenCallDelete() {
        categories = repository.saveAll(categories);
        Optional<Category> oCategory = repository.findById(categories.stream()
            .findAny()
            .get()
            .getId());
        repository.delete(oCategory.get());
        Optional<Category> findCategory = repository.findById(oCategory.get().getId());
        assertFalse(findCategory.isPresent());
    }
}
