package com.barbosa.ms.productmgmt.services.succeed;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CategoryServiceSucceedTest {

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
    void shouldSuccessWhenCreate() {
        given.categoryInicietedForSuccessfulReturn();
        given.categoryRecordInicietedForSuccessfulReturn();
        when.saveCategoryEntity();
        CategoryRecord record = when.callCreateInCategoryService();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenFindById() {
        given.categoryInicietedForSuccessfulReturn();
        when.findCategoryById();
        CategoryRecord record = when.callCategoryServiceFindById();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenUpdate() {
        given.categoryInicietedForSuccessfulReturn();
        given.categoryRecordInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.callCategoryServiceFindById();
        when.saveCategoryEntity();
        when.callCategorySerivceUpdate();
        then.shouldBeSuccessfulArgumentValidationByUpdate();        
    }

    @Test
    void shouldSuccessWhenDelete() {
        given.categoryInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.deleteCategoryEntity();
        when.callDelteInCategorySerivce();    
        then.shouldBeSuccessfulArgumentValidationByDelete();    
    }

    @Test
    void shouldSuccessWhenListAll() {
        given.categoryInicietedForSuccessfulReturn();
        when.findAllCategories();
        Page<CategoryRecord>  categoryRecords = when.callListAllInCategoryService();
        then.shouldBeSuccessfulArgumentValidationByListAll(categoryRecords);
    }

    class Given {

        public UUID creationIdOfCategory() {
            return UUID.randomUUID();
        }

        void categoryInicietedForSuccessfulReturn() {
           category = Category.builder()
                        .id(creationIdOfCategory())
                        .name("Category-Test-Success")
                        .build();
        }

        void categoryRecordInicietedForSuccessfulReturn () {
            categoryRecord = new CategoryRecord(category.getId(), category.getName());
        }
    }

    class When {

        void saveCategoryEntity() {
            when(repository.save(any(Category.class)))
            .thenReturn(category);
        }

        void callCategorySerivceUpdate() {
            service.update(categoryRecord);
        }

        void callDelteInCategorySerivce() {
            service.delete(given.creationIdOfCategory());
        }

        void deleteCategoryEntity() {
            doNothing().when(repository).delete(any(Category.class));
        }

        public CategoryRecord callCategoryServiceFindById() {
            return service.findById(given.creationIdOfCategory());
        }

        void findCategoryById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        }

        public CategoryRecord callCreateInCategoryService() {
            return service.create(categoryRecord);
        }

        void findAllCategories() {
            when(repository.findAll(any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(Collections.singletonList(category)));
        }

        public Page<CategoryRecord> callListAllInCategoryService() {
            return service.listAll(PageRequest.of(1, 10));
        }
    }
    
    class Then {

        void shouldBeSuccessfulValidationRules(CategoryRecord record) {
            assertNotNull(record);
            assertNotNull(record.name());
            assertEquals(record.name(), category.getName());
            assertNotNull(record.id());
            assertEquals(record.id(), category.getId());
        }

        void shouldBeSuccessfulArgumentValidationByDelete() {
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(repository).delete(categoryCaptor.capture());
            assertNotNull(categoryCaptor.getValue());
            assertEquals(categoryCaptor.getValue().getName(),category.getName());
        }

        void shouldBeSuccessfulArgumentValidationByUpdate() {
            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(repository).save(categoryCaptor.capture());
            assertNotNull(categoryCaptor.getValue());
            assertEquals(categoryCaptor.getValue().getName(),category.getName());
        }

        void shouldBeSuccessfulArgumentValidationByListAll(Page<CategoryRecord> categoryRecords) {
            assertNotNull(categoryRecords);
            assertFalse(categoryRecords.isEmpty());
        }
    }
}
