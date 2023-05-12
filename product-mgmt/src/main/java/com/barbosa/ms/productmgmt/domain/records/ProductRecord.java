package com.barbosa.ms.productmgmt.domain.records;

import java.util.UUID;

public record ProductRecord(UUID id, String name, UUID idCategory) {
    
}
