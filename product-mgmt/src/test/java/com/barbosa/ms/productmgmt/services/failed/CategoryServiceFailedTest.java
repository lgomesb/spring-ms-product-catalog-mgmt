package com.barbosa.ms.productmgmt.services.failed;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.services.impl.CategoryServiceImpl;


public class CategoryServiceFailedTest {

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
    public void shouldFailWhenCreate() {
        given.categoryInicietedForFailueReturn();
        given.categoryRecordInicietedForFailueReturn();
        when.saveCategoryEntity();
        then.shouldBeFailueWhenCreateCategory(IllegalArgumentException.class);
    }

    @Test
    public void shouldFailWhenFindById() {
        given.categoryInicietedForFailueReturn();
        when.findCategoryByIdWithFail();        
        then.shouldBeFailueWhenFindCategoryById(ObjectNotFoundException.class);
    }

    @Test
    public void shouldFailWhenUpdateWithIdNonExistent() {
        given.categoryInicietedForFailueReturn();
        given.categoryRecordInicietedForFailueReturn();
        when.findCategoryByIdWithFail();        
        then.shouldBeFailueWhenFindCategoryById(ObjectNotFoundException.class);    
    }

    @Test
    public void shouldFailWhenUpdateWithInvalidArgument() {
        given.categoryInicietedForFailueReturn();
        given.categoryRecordInicietedForFailueReturn();
        when.findCategoryById();
        when.saveCategoryEntity();
        then.shouldBeFailueWhenUpdateCategory(IllegalArgumentException.class);        
    }

    @Test
    public void shouldFailWhenDelete() {
        given.categoryInicietedForFailueReturn();
        when.findCategoryByIdWithFail();
        then.shouldBeFailueWhenDeleteCategory(ObjectNotFoundException.class);     
    }

    class Given {

        public UUID creationIdOfCategory() {
            return UUID.randomUUID();
        }

        public void categoryInicietedForFailueReturn() {
           category = Category.builder()
                        .id(creationIdOfCategory())
                        .name(null)
                        .build();
        }

        public void categoryRecordInicietedForFailueReturn () {
            categoryRecord = new CategoryRecord(category.getId(), null);
        }
    }

    class When {

        public void saveCategoryEntity() {            
            doThrow(new IllegalArgumentException("Error in insert category"))
                .when(repository)
                .save(any(Category.class));
        }

        public void callCategorySerivceUpdate() {
            service.update(categoryRecord);
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

        public void findCategoryByIdWithFail() {
            doThrow(new ObjectNotFoundException("Category", given.creationIdOfCategory()))
                .when(repository).findById(any(UUID.class));
                
        }

        public void findCategoryById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        }

        public CategoryRecord callCreateInCategorySerivce() {
            return service.create(categoryRecord);
        }

    }
    
    class Then {

        public <T extends Throwable> void shouldBeFailueWhenCreateCategory(Class<T> clazz) {
           assertThrows(clazz, () -> {
                when.callCreateInCategorySerivce();
           });
        }

        
        public <T extends Throwable> void shouldBeFailueWhenFindCategoryById(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callCategoryServiceFindById();
            });
        }
        
        public <T extends Throwable> void shouldBeFailueWhenUpdateCategory(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callCategorySerivceUpdate();
            });
        }

        public <T extends Throwable> void shouldBeFailueWhenDeleteCategory(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callDelteInCategorySerivce();
            });
        }
    }
}
