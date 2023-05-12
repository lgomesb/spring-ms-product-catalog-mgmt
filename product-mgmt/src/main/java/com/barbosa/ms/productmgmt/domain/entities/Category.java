package com.barbosa.ms.productmgmt.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "category")
@Entity
public class Category extends AbstractEntity {
    
    public Category(String name) {
        super();
        super.setName(name);
    }
}
