package com.barbosa.ms.productmgmt.domain.dto;

import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryResponseDTO extends ResponseDTO {

    public CategoryResponseDTO(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
