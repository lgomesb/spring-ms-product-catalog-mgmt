package com.barbosa.ms.productmgmt.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ValidationError extends StandardError {

    private final List<FieldMessage> messages = new ArrayList<>();
    
    
    public ValidationError(Integer status, String error, String messege, String path) {
        super(status, error, messege, path);
    }

    public void addError(String fieldName, String message) { 		
		messages.add(new FieldMessage(fieldName, message));
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages);
    }
}
