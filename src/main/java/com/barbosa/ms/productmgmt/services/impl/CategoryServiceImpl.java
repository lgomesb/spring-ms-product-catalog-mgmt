package com.barbosa.ms.productmgmt.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.ObjectNotFoundException;
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
    public void update(CategoryRecord categoryRecord) {
        Category category = this.getCategoryById(categoryRecord.id());
        category.setName(categoryRecord.name());
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
                  .orElseThrow(()-> new ObjectNotFoundException("Category", id));
    }

    @Override
    public List<CategoryRecord> listAll() {
        return repository.findAll()
            .stream()
            .map(entity -> CategoryRecord.builder()
            .        id(entity.getId())
                    .name(entity.getName())
                    .build())
            .collect(Collectors.toList());
    }
    
    
}
