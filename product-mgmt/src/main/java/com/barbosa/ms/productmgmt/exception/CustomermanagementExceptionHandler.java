package com.barbosa.ms.productmgmt.exception;


import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomermanagementExceptionHandler {

    
    
    @ExceptionHandler( ConstraintViolationException.class )
	public ResponseEntity<CustomException> constraintViolationError( ConstraintViolationException e, HttpServletRequest request) { 
        CustomException error = CustomException.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .messege(e.getMessage())
            .path(request.getRequestURI())
            .build();

            System.out.println("#".repeat(10) + "ERROR HANDLER");
            e.getConstraintViolations().forEach(c -> System.out.println(c.getMessage()));


            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
    }

    @ExceptionHandler( MethodArgumentNotValidException.class )
	public ResponseEntity<CustomException> validationError( MethodArgumentNotValidException e, HttpServletRequest request) { 
        
        BindingResult bindingResult = e.getBindingResult();
        List<String> errors = bindingResult.getAllErrors()
             .stream()
             .map(err -> err.getDefaultMessage())
             .collect(Collectors.toList());
        
        ApiErrors apiErrors = new ApiErrors(errors);
        
        CustomException error = CustomException.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .messege(apiErrors.getErrors().toString())
            .path(request.getRequestURI())
            .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
        }
        
        @ExceptionHandler( ObjectNotFoundException.class )
        public ResponseEntity<CustomException> objectNotFound( ObjectNotFoundException e, HttpServletRequest request) { 
            CustomException err = CustomException.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("Resource is: " + HttpStatus.NOT_FOUND.getReasonPhrase())
            .messege(e.getMessage())
            .path(request.getRequestURI())
            .build();		
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err); 
        }
        
        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<CustomException> genericError(Exception e, HttpServletRequest request) {
        //     CustomException error = CustomException.builder()
        //             .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        //             .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        //             .messege(e.getMessage())
        //             .path(request.getRequestURI())
        //             .build();
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        // }
        
    }
    