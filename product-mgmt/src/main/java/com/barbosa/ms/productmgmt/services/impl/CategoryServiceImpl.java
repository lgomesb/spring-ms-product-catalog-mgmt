package com.barbosa.ms.productmgmt.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public CategoryRecord create(CategoryRecord record) {
        Category category = repository.save(new Category(record.name()) );
        return new CategoryRecord(category.getId(), category.getName());
    }

    @Override
    public CategoryRecord findById(UUID id) {
        Category category = this.getCategoryById(id);
        return new CategoryRecord(category.getId(), category.getName());
    }

    
    @Override
    public void update(CategoryRecord record) {
        Category category = this.getCategoryById(record.id());
        category.setName(record.name());
        category.setModifieldOn(LocalDateTime.now());
        category.setModifiedBy("99999");
        repository.save(category);      
    }
    
    @Override
    public void delete(UUID id) {
        Category category = this.getCategoryById(id);
        repository.delete(category);

    }
    
    private Category getCategoryById(UUID id) {
        return repository.findById(id)
                  .orElseThrow(()-> new RuntimeException("Category not found by id: " + id.toString()));
    }
    
    
}
