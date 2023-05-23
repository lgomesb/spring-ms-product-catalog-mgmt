package com.barbosa.ms.productmgmt.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductRecord create(ProductRecord record) {        
        Category category = getCategoryById(record.idCategory());
            
        Product product = new Product();
        product.setCategory(category);
        product.setName(record.name());
        Product productSaved = repository.save(product);
        return new ProductRecord(productSaved.getId(),productSaved.getName(), category.getId());
    }

    
    @Override
    public ProductRecord findById(UUID id) {
        Product product = getProductById(id);
        return new ProductRecord(product.getId(), product.getName(), product.getCategory().getId());
    }

    
    @Override
    public void update(ProductRecord record) {
        Product product = getProductById(record.id());
        product.setName(record.name());
        product.setCategory(getCategoryById(record.idCategory()));
        product.setModifieldOn(LocalDateTime.now());
        product.setModifiedBy("999999");
        repository.save(product);
    }
    
    @Override
    public void delete(UUID id) {
        Product product = getProductById(id);
        repository.delete(product);
    }
    
    private Product getProductById(UUID id) {
        return repository
        .findById(id)
        .orElseThrow(()-> new RuntimeException("Product not found by id: " + id.toString()));
    }
    
    private Category getCategoryById(UUID uuid) {
        return categoryRepository
            .findById(uuid)
            .orElseThrow(() -> new RuntimeException("Entity Category not found."));
    }
    
}
