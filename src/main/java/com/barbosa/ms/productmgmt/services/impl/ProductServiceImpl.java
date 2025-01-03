package com.barbosa.ms.productmgmt.services.impl;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.ProductService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductRecord create(ProductRecord recordObject) {
        Category category = getCategoryById(recordObject.category().id());

        Product product = new Product();
        product.setCategory(category);
        product.setName(recordObject.name());
        Product productSaved = repository.save(product);
        return ProductRecord.fromEntity(productSaved);
    }

    @Override
    @CacheEvict(cacheNames = "productRecords", allEntries = true)
    public ProductRecord findById(UUID id) {
        Product product = getProductById(id);
        return ProductRecord.fromEntity(product);
    }

    @Override
    public void update(ProductRecord recordObject) {
        Product product = getProductById(recordObject.id());
        product.setName(recordObject.name());
        product.setCategory(getCategoryById(recordObject.category().id()));
        repository.save(product);
    }

    @Override
    @CacheEvict(cacheNames = "productRecords", allEntries = true)
    public void delete(UUID id) {
        Product product = getProductById(id);
        repository.delete(product);
    }

    @Override
    public List<ProductRecord> listAll() {
        return repository.findAll()
                .stream()
                .map(ProductRecord::fromEntity)
                .toList();
    }


    @Cacheable(cacheNames = "productRecords", // cacheManager = "cacheManager",
            key = "{#name, #pageRequest.pageSize, #pageRequest.pageNumber, #pageRequest.sort}")
    @Override
    public Page<ProductRecord> search(String name, PageRequest pageRequest) {
        Page<Product> products = repository.findDistinctByNameContaining(name, pageRequest);
        pageRequest.getPageSize();
        pageRequest.getPageNumber();
        return products.map(ProductRecord::fromEntity);

    }

    @Cacheable(cacheNames = "productRecords", key = "{#idCategory, #pageRequest.pageSize, #pageRequest.pageNumber}")
    @Override
    public Page<ProductRecord> findByCategory(UUID idCategory, PageRequest pageRequest) {
        Category category = this.getCategoryById(idCategory);
        Page<Product> products = repository.findDistinctByCategory(category, pageRequest);
        return products.map(ProductRecord::fromEntity);
    }

    private Product getProductById(UUID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product", id));
    }

    private Category getCategoryById(UUID id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category", id));
    }

}
