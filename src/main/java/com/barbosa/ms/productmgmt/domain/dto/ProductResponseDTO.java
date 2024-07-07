package com.barbosa.ms.productmgmt.domain.dto;

import java.util.UUID;

import com.barbosa.ms.productmgmt.domain.records.ProductRecord;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductResponseDTO extends ResponseDTO {
    
    private CategoryResponseDTO category;

    @Builder
    public ProductResponseDTO(String id, String name, CategoryResponseDTO category) {
        super();
        super.setId(UUID.fromString(id));
        super.setName(name);
        this.category = category;
    }

    public static ProductResponseDTO fromRecord(ProductRecord productRecord) {
        return ProductResponseDTO.builder()
            .id(productRecord.id().toString())
            .name(productRecord.name())
            .category(CategoryResponseDTO.fromRecord(productRecord.category()))
            .build();
    }
}
