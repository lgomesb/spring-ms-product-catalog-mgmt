package com.barbosa.ms.productmgmt.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "product")
@Getter
@Setter
@Entity
public class Product extends AbstractEntity {    

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
