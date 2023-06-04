package com.barbosa.ms.productmgmt.repositories.failed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.hibernate.ObjectNotFoundException;
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
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryRepositoryFailedTest {

    @Autowired
    private CategoryRepository repository;

    private static Stream<Arguments> provideCategoryData() {        
        return Stream.of(
          Arguments.of("Category-Test-01"),
          Arguments.of("Category-Test-02")
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
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(new Category(null));
        }, "Should return Error when Category not null");
    }


    @Order(2)
    @ParameterizedTest
    @MethodSource("provideCategoryData")
    public void shouldWhenCallFindById(String categoryName) {
        repository.save(new Category(categoryName));
        Optional<Category> oCategory = repository.findById(UUID.randomUUID());
        assertThrows( ObjectNotFoundException.class, () -> {
            oCategory.orElseThrow(() ->
                 new ObjectNotFoundException("Category", UUID.randomUUID()));
        });
    }

  
    @Order(3)
    @ParameterizedTest
    @MethodSource("provideCategoryData")
    public void shouldWhenCallUpdate(String categoryName) {
        String categoryNameUpdate = "Test-Update-Category";
        Category category = repository.save(new Category(categoryName));
        Optional<Category> oCategory = repository.findById(category.getId());
        Category newCategory = oCategory.get();
        newCategory.setName(categoryNameUpdate);
        newCategory = repository.save(newCategory);
        assertEquals(categoryNameUpdate, newCategory.getName());
    }
  
    @Order(4)
    @ParameterizedTest
    @MethodSource("provideCategoryData")
    public void shouldWhenCallDelete(String categoryName) {
        Category category = repository.save(new Category(categoryName));
        Optional<Category> oCategory = repository.findById(category.getId());
        repository.delete(oCategory.get());
        Optional<Category> findCategory = repository.findById(oCategory.get().getId());
        assertFalse(findCategory.isPresent());
    }
}
