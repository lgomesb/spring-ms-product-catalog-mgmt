package com.barbosa.ms.productmgmt.domain.records;

import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;

@Builder
public record ProductRecord(UUID id, String name, UUID idCategory) implements Serializable {
    
}
