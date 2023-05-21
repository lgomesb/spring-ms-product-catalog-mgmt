package com.barbosa.ms.productmgmt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.services.impl.CategoryServiceImpl;


public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl service;

    @Mock
    private CategoryRepository repository;
    
    private Category category;
    private CategoryRecord categoryRecord;
    private Given given = new Given();
    private When when = new When();
    private Then then = new Then();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSuccessWhenCreate() {
        given.categoryInicietedForSuccessfulReturn();
        given.categoryRecordInicietedForSuccessfulReturn();
        when.saveCategoryEntity();
        CategoryRecord record = when.callCreateInCategorySerivce();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    public void shouldSuccessWhenFindById() {
        given.categoryInicietedForSuccessfulReturn();
        when.findCategoryById();
        CategoryRecord record = when.callCategoryServiceFindById();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    public void shouldSuccessWhenUpdate() {
        given.categoryInicietedForSuccessfulReturn();
        given.categoryRecordInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.callCategoryServiceFindById();
        when.saveCategoryEntity();
        when.callCreateInCategorySerivce();
        then.shouldBeSuccessfulArgumentValidationByUpdate();        
    }

    @Test
    public void delete() {
        given.categoryInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.deleteCategoryEntity();
        when.callDelteInCategorySerivce();    
        then.shouldBeSuccessfulArgumentValidationByDelete();    
    }

    class Given {

        public UUID creationIdOfCategory() {
            return UUID.randomUUID();
        }

        public void categoryInicietedForSuccessfulReturn() {
           category = Category.builder()
                        .id(creationIdOfCategory())
                        .name("Category-Test-Success")
                        .build();
        }

        public void categoryRecordInicietedForSuccessfulReturn () {
            categoryRecord = new CategoryRecord(category.getId(), category.getName());
        }
    }

    class When {

        public void saveCategoryEntity() {
            when(repository.save(any(Category.class)))
            .thenReturn(category);
        }

        public void callDelteInCategorySerivce() {
            service.delete(given.creationIdOfCategory());
        }

        public void deleteCategoryEntity() {
            doNothing().when(repository).delete(any(Category.class));
        }

        public CategoryRecord callCategoryServiceFindById() {
            return service.findById(given.creationIdOfCategory());
        }

        public void findCategoryById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        }

        public CategoryRecord callCreateInCategorySerivce() {
            return service.create(categoryRecord);
        }

    }
    
    class Then {

        public void shouldBeSuccessfulValidationRules(CategoryRecord record) {
            assertNotNull(record);
            assertNotNull(record.name());
            assertEquals(record.name(), category.getName());
            assertNotNull(record.id());
            assertEquals(record.id(), category.getId());
        }

        public void shouldBeSuccessfulArgumentValidationByDelete() {
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(repository).delete(categoryCaptor.capture());
            assertNotNull(categoryCaptor.getValue());
            assertEquals(categoryCaptor.getValue().getName(),category.getName());
        }

        public void shouldBeSuccessfulArgumentValidationByUpdate() {
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(repository).save(categoryCaptor.capture());
            assertNotNull(categoryCaptor.getValue());
            assertEquals(categoryCaptor.getValue().getName(),category.getName());
        }

    }
}
