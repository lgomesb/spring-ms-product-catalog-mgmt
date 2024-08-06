package com.barbosa.ms.productmgmt.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@Table(name = "category")
@Entity
public class Category extends AbstractEntity {
    
    public Category(String name) {
        super();
        super.setName(name);
    }
    
    @Builder()
    public Category(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
