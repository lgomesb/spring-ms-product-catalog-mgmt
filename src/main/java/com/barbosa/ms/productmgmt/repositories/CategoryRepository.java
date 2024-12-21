package com.barbosa.ms.productmgmt.repositories;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Transactional(readOnly = true)
    Page<Category> findDistinctByNameContaining(String name, PageRequest pageRequest);
}
