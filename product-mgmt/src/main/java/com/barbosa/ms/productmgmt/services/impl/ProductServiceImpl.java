package com.barbosa.ms.productmgmt.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbosa.ms.productmgmt.domain.dto.CreateProductDTO;
import com.barbosa.ms.productmgmt.domain.dto.DataDTO;
import com.barbosa.ms.productmgmt.domain.dto.ProductResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.ResponseDTO;
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
        Category category = categoryRepository
            .findById(record.idCategory())
            .orElseThrow(() -> new RuntimeException("Entity Category not found."));
            
        Product product = new Product();
        product.setCategory(category);
        product.setName(record.name());
        Product productSaved = repository.save(product);
        return new ProductRecord(productSaved.getId(),productSaved.getName(), category.getId());
    }

    @Override
    public ProductRecord findById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void update(ProductRecord record) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    

}
