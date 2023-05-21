package com.barbosa.ms.productmgmt.domain.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
