package com.barbosa.ms.productmgmt.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbosa.ms.productmgmt.domain.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
}
