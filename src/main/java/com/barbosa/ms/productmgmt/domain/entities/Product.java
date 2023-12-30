package com.barbosa.ms.productmgmt.domain.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "product")
@Getter
@Setter
@Entity
public class Product extends AbstractEntity {    

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Product(Category category, String name, UUID id) {
        super();
        super.setId(id);
        super.setName(name);
        this.category = category;
    }

}
