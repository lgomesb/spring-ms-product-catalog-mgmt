package com.barbosa.ms.productmgmt.domain.records;

import java.io.Serializable;
import java.util.UUID;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import lombok.Builder;

@Builder
public record CategoryRecord(UUID id, String name) implements Serializable {

    public static CategoryRecord fromEntity(Category category) {
        if(category != null)
            return new CategoryRecord(category.getId(), category.getName());

        return null;
    }

}
