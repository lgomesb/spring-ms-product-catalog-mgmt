package com.barbosa.ms.productmgmt.domain.dto;

import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import lombok.Builder;
import java.util.UUID;

public class CategoryResponseDTO extends ResponseDTO {

    public static CategoryResponseDTO create(CategoryRecord categoryRecord) {
       return CategoryResponseDTO.builder()
                .id(categoryRecord.id())
                .name(categoryRecord.name())
                .build();
    }

    @Builder
    public CategoryResponseDTO(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
