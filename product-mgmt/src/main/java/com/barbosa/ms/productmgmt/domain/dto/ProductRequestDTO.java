package com.barbosa.ms.productmgmt.domain.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductRequestDTO extends DataDTO {
    
    private String idCategory;

    @JsonIgnore
    public UUID getUUIDCategory() {
        return UUID.fromString(idCategory);
    }
}
