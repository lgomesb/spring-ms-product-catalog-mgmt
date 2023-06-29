package com.barbosa.ms.productmgmt.domain.dto;

import java.util.UUID;

import com.barbosa.ms.productmgmt.domain.records.ProductRecord;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductResponseDTO extends ResponseDTO {
    
    private String idCategory;

    @Builder
    public ProductResponseDTO(String id, String name, String idCategory) { 
        super();
        super.setId(UUID.fromString(id));
        super.setName(name);
        this.idCategory = idCategory;
    }

    public static ProductResponseDTO create(ProductRecord record) {
        return ProductResponseDTO.builder()
            .id(record.id().toString())
            .name(record.name())
            .idCategory(record.idCategory().toString())
            .build();
    }
}
