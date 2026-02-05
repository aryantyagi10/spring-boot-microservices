package com.ecommerce.product_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice                    // 1. Tells Spring: "Watch all Controllers for exceptions"
public class GlobalExceptionHandler {

    // 2. This method runs ONLY when a validation error occurs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){

        // 1. Create an empty bucket to hold our simplified errors
        Map<String, String> errors = new HashMap<>();

        // 3. Loop through all the errors found by @Valid
        ex.getBindingResult().getAllErrors().forEach((error) -> {  //ex.getBindingResult().getAllErrors() returns a List of errors found during validation.
            String fieldName = ((FieldError) error).getField();               //Here we are casting error {object of type ObjectError} to FieldError because .getField() is not present in ObjectError class but its child class i.e. FieldError
            String errorMessage = error.getDefaultMessage();                   //.getDefaultMessage() is in ObjectError class.
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
        //return errors;
    }
}
