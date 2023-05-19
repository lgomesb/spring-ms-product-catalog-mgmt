package com.barbosa.ms.productmgmt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSuccessWhenCreate() {
        String categoryName = "Category-Test-Success";
        Category category = new Category(categoryName);
        category.setId(UUID.randomUUID());
        
        when(repository.save(any(Category.class)))
        .thenReturn(category);
        
        CategoryRecord record = service.create(new CategoryRecord(null, categoryName));
        assertNotNull(record);
        assertNotNull(record.name());
        assertEquals(record.name(), category.getName());
        assertNotNull(record.id());
        assertEquals(record.id(), category.getId());

    }

    @Test
    public void shouldSuccessWhenFindById() {
        String nameCategory = "Category-Test-FindById-Success";
        Category category = new Category(nameCategory);
        UUID idCategory = UUID.randomUUID();
        category.setId(idCategory);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        CategoryRecord record = service.findById(idCategory);
        assertNotNull(record);
        assertNotNull(record.name());
        assertEquals(record.name(), category.getName());
        assertNotNull(record.id());
        assertEquals(record.id(), category.getId());
    }

    @Test
    public void shouldSuccessWhenUpdate() {
        String categoryName = "Category-Test-Success";
        UUID idCategory = UUID.randomUUID();
        Category category = new Category(categoryName);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        when(repository.save(any(Category.class))).thenReturn(category);
        service.update(new CategoryRecord(idCategory, categoryName));
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(repository).save(categoryCaptor.capture());
        assertNotNull(categoryCaptor.getValue());
        assertEquals(categoryCaptor.getValue().getName(),categoryName);
        
    }

    @Test
    public void delete() {
        String categoryName = "Category-Test-Success";
        UUID idCategory = UUID.randomUUID();
        Category category = new Category(categoryName);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        doNothing().when(repository).delete(any(Category.class));
        service.delete(idCategory);
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(repository).delete(categoryCaptor.capture());
        assertNotNull(categoryCaptor.getValue());
        assertEquals(categoryCaptor.getValue().getName(),categoryName);
        
    }
    
}
