package com.barbosa.ms.productmgmt.services.impl;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.services.CategoryService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryRecord create(CategoryRecord recordObject) {
        Category category = repository.save(new Category(recordObject.name()) );
        return new CategoryRecord(category.getId(), category.getName());
    }

    @Override
    public CategoryRecord findById(UUID id) {
        Category category = this.getCategoryById(id);
        return new CategoryRecord(category.getId(), category.getName());
    }

    
    @Override
    public void update(CategoryRecord recordObject) {
        Category category = this.getCategoryById(recordObject.id());
        category.setName(recordObject.name());
        category.setModifiedOn(LocalDateTime.now());
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
                    .id(entity.getId())
                    .name(entity.getName())
                    .build())
            .toList();
    }
    
    
}
