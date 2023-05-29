package com.barbosa.ms.productmgmt.domain.dto;

import java.util.UUID;

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
}
