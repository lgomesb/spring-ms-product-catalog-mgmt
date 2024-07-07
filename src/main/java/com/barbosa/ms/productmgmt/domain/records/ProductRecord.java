package com.barbosa.ms.productmgmt.domain.records;

import com.barbosa.ms.productmgmt.domain.entities.Product;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;


@Builder
public record ProductRecord(UUID id, String name, CategoryRecord category) implements Serializable {

    public static ProductRecord fromEntity(Product product) {
        if(product != null) {
            return ProductRecord.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .category(CategoryRecord.fromEntity(product.getCategory()))
                    .build();
        }

        return null;
    }
    
}
