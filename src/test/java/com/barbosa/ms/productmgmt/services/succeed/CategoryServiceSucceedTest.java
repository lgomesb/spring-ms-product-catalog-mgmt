package com.barbosa.ms.productmgmt.services.succeed;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceSucceedTest {

    @InjectMocks
    private CategoryServiceImpl service;

    @Mock
    private CategoryRepository repository;
    
    private Category category;
    private CategoryRecord categoryRecord;
    private final Given given = new Given();
    private final When when = new When();
    private final Then then = new Then();


    @Test
    @DisplayName("Create a new Category")
    void shouldSuccessWhenCreate() {
        given.categoryInitiatedForSuccessfulReturn();
        given.categoryRecordInitiatedForSuccessfulReturn();
        when.saveCategoryEntity();
        CategoryRecord successCategoryRecord = when.callCreateInCategoryService();
        then.shouldBeSuccessfulValidationRules(successCategoryRecord);
    }

    @Test
    @DisplayName("Find a Category by id")
    void shouldSuccessWhenFindById() {
        given.categoryInitiatedForSuccessfulReturn();
        when.findCategoryById();
        CategoryRecord successCategoryRecord = when.callCategoryServiceFindById();
        then.shouldBeSuccessfulValidationRules(successCategoryRecord);
    }

    @Test
    @DisplayName("Update a Category")
    void shouldSuccessWhenUpdate() {
        given.categoryInitiatedForSuccessfulReturn();
        given.categoryRecordInitiatedForSuccessfulReturn();
        when.findCategoryById();
        when.callCategoryServiceFindById();
        when.saveCategoryEntity();
        when.callCategoryServiceUpdate();
        then.shouldBeSuccessfulArgumentValidationByUpdate();        
    }

    @Test
    @DisplayName("Delete a Category")
    void shouldSuccessWhenDelete() {
        given.categoryInitiatedForSuccessfulReturn();
        when.findCategoryById();
        when.deleteCategoryEntity();
        when.callDeleteInCategoryService();    
        then.shouldBeSuccessfulArgumentValidationByDelete();    
    }

    @Test
    @DisplayName("Get all Categories")
    void shouldSuccessWhenListAll() {
        given.categoryInitiatedForSuccessfulReturn();
        when.findAllCategories();
        List<CategoryRecord> categoryRecords = when.callListAllInCategoryService();
        then.shouldBeSuccessfulArgumentValidationByListAll(categoryRecords);
    }

    @Test
    @DisplayName("Get pageable list Categories")
    void shouldSuccessWhenSearch() {
        given.categoryInitiatedForSuccessfulReturn();
        given.findDistinctByNameContaining();
        Page<CategoryRecord> categoryRecords = when.callSearchInCategoryService();
        then.shouldBeSuccessfulArgumentValidationBySearchCategory(categoryRecords);
    }

    class Given {

        public UUID creationIdOfCategory() {
            return UUID.randomUUID();
        }

        void categoryInitiatedForSuccessfulReturn() {
           category = Category.builder()
                        .id(creationIdOfCategory())
                        .name("Category-Test-Success")
                        .build();
        }

        void categoryRecordInitiatedForSuccessfulReturn () {
            categoryRecord = new CategoryRecord(category.getId(), category.getName());
        }

        public void findDistinctByNameContaining() {
            doReturn(new PageImpl<>(Collections.singletonList(category)))
                    .when(repository)
                    .findDistinctByNameContaining(anyString(), any(PageRequest.class));
        }
    }

    class When {

        void saveCategoryEntity() {
            when(repository.save(any(Category.class)))
            .thenReturn(category);
        }

        void callCategoryServiceUpdate() {
            service.update(categoryRecord);
        }

        void callDeleteInCategoryService() {
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
            when(repository.findAll()).thenReturn(Collections.singletonList(category));
        }

        public List<CategoryRecord> callListAllInCategoryService() {
            return service.listAll();
        }

        public Page<CategoryRecord> callSearchInCategoryService() {
            return service.search(category.getName(), PageRequest.of(1, 10));
        }
    }
    
    class Then {

        void shouldBeSuccessfulValidationRules(CategoryRecord categoryRecord) {
            assertNotNull(categoryRecord);
            assertNotNull(categoryRecord.name());
            assertEquals(categoryRecord.name(), category.getName());
            assertNotNull(categoryRecord.id());
            assertEquals(categoryRecord.id(), category.getId());
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

        void shouldBeSuccessfulArgumentValidationByListAll(List<CategoryRecord> categoryRecords) {
            assertNotNull(categoryRecords);
            assertFalse(categoryRecords.isEmpty());
        }

        public void shouldBeSuccessfulArgumentValidationBySearchCategory(Page<CategoryRecord> categoryRecords) {
            assertNotNull(categoryRecords);
            assertFalse(categoryRecords.isEmpty());
        }
    }
}
