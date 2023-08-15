package com.barbosa.ms.productmgmt.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Transactional(readOnly = true)
    Page<Product> findDistinctByNameContaining(String name, PageRequest pageRequest);

    @Transactional(readOnly = true)
    Page<Product> findDistinctByCategory(Category category, PageRequest pageRequest);
    
}
