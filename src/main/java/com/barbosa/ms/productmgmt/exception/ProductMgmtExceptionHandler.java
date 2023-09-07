package com.barbosa.ms.productmgmt.exception;


import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.util.logging.Logger;

@ControllerAdvice
public class ProductMgmtExceptionHandler {

    Logger logger = Logger.getLogger(ProductMgmtExceptionHandler.class.getName());
    
    @ExceptionHandler( ConstraintViolationException.class )
	public ResponseEntity<StandardError> constraintViolationError( ConstraintViolationException e, HttpServletRequest request) { 
        StandardError error = StandardError.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .messege(e.getMessage())
            .path(request.getRequestURI())
            .build();
            logger.info("#".repeat(10) + "ERROR HANDLER");
            e.getConstraintViolations().forEach(c -> logger.info(c.getMessage()));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
    }

    @ExceptionHandler( MethodArgumentNotValidException.class )
	public ResponseEntity<StandardError> validationError( MethodArgumentNotValidException e, HttpServletRequest request) { 
        
            ValidationError error = new ValidationError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                null,
                request.getRequestURI()
                );
        
            e.getBindingResult()
                .getFieldErrors()
                .forEach(f -> error.addError(f.getField(), f.getDefaultMessage()));


            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error); 
        }
        
        @ExceptionHandler( ObjectNotFoundException.class )
        public ResponseEntity<StandardError> objectNotFound( ObjectNotFoundException e, HttpServletRequest request) { 
            StandardError err = StandardError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource is: " + HttpStatus.NOT_FOUND.getReasonPhrase())
                .messege(e.getMessage())
                .path(request.getRequestURI())
                .build();		
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err); 
        }

    }
    