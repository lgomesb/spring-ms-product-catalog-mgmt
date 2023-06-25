package com.barbosa.ms.productmgmt.domain.dto;

import java.util.UUID;

import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryResponseDTO extends ResponseDTO {

    public static CategoryResponseDTO create(CategoryRecord record) {
       return CategoryResponseDTO.builder()
                .id(record.id())
                .name(record.name())
                .build();
    }

    @Builder
    public CategoryResponseDTO(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
