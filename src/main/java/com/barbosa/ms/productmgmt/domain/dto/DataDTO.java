package com.barbosa.ms.productmgmt.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class DataDTO {
    
    @NotNull(message = "{field.name.required}")
    @NotBlank(message = "{field.name.not-blank}")
    @NotEmpty(message = "{field.name.required}")
    private String name;
    
}
