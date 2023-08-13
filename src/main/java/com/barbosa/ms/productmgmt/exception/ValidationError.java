package com.barbosa.ms.productmgmt.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ValidationError extends StandardError {

    private List<FieldMessage> messages = new ArrayList<>();
    
    
    public ValidationError(Integer status, String error, String messege, String path) {
        super(status, error, messege, path);
    }

    public void addError(String fieldName, String message) { 		
		messages.add(new FieldMessage(fieldName, message));
	}
    
    
}
