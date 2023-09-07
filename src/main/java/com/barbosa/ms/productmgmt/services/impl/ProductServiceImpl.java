package com.barbosa.ms.productmgmt.services.impl;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.ProductService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductRecord create(ProductRecord recordObject) {
        Category category = getCategoryById(recordObject.idCategory());

        Product product = new Product();
        product.setCategory(category);
        product.setName(recordObject.name());
        Product productSaved = repository.save(product);
        return new ProductRecord(productSaved.getId(), productSaved.getName(), category.getId());
    }

    @Override
    public ProductRecord findById(UUID id) {
        Product product = getProductById(id);
        return new ProductRecord(product.getId(), product.getName(), product.getCategory().getId());
    }

    @Override
    public void update(ProductRecord recordObject) {
        Product product = getProductById(recordObject.id());
        product.setName(recordObject.name());
        product.setCategory(getCategoryById(recordObject.idCategory()));
        product.setModifieldOn(LocalDateTime.now());
        product.setModifiedBy("999999");
        repository.save(product);
    }

    @Override
    public void delete(UUID id) {
        Product product = getProductById(id);
        repository.delete(product);
    }

    @Override
    public List<ProductRecord> listAll() {
        return repository.findAll()
                .stream()
                .map(entity -> ProductRecord.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .idCategory(entity.getId())
                        .build())
                .toList();
    }


    @Override
    public Page<ProductRecord> search(String name, PageRequest pageRequest) {

        Page<Product> products = repository.findDistinctByNameContaining(name, pageRequest);

        return products.map(entity -> ProductRecord.builder()
                .id(entity.getId())
                .name(entity.getName())
                .idCategory(entity.getCategory().getId())
                .build());

    }

    @Override
    public Page<ProductRecord> findByCategory(UUID idCategory, PageRequest pageRequest) {
        Category category = this.getCategoryById(idCategory);
        Page<Product> products = repository.findDistinctByCategory(category, pageRequest);
        return products.map(entity -> ProductRecord.builder()
                .id(entity.getId())
                .name(entity.getName())
                .idCategory(entity.getCategory().getId())
                .build());
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
