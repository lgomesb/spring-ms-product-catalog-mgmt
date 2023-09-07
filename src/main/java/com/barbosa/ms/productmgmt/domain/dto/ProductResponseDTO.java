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

    public static ProductResponseDTO create(ProductRecord productRecord) {
        return ProductResponseDTO.builder()
            .id(productRecord.id().toString())
            .name(productRecord.name())
            .idCategory(productRecord.idCategory().toString())
            .build();
    }
}
