package com.barbosa.ms.productmgmt.exception;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrors {

    private List<String> errors;

    public ApiErrors(String error) {
        this.errors = Arrays.asList(error);
    }
    
}
