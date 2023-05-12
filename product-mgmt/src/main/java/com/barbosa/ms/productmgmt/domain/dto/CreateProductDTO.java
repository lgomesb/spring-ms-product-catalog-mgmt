package com.barbosa.ms.productmgmt.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateProductDTO extends DataDTO {
    
    private String idCategory;
}
