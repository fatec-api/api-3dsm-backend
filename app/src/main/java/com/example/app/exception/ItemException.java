package com.example.app.exception;

import com.example.app.dto.error.ItemError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ItemException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ItemError> handleValidationException(MethodArgumentNotValidException ex) {
        ItemError itemError = new ItemError(400, "Dados não preenchidos.");
        return new  ResponseEntity<>(itemError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ItemError> handleDataIntegrityException(DataIntegrityViolationException ex) {
        ItemError itemError = new ItemError(400,  HttpStatus.BAD_REQUEST.toString());
        return new  ResponseEntity<>(itemError, HttpStatus.BAD_REQUEST);
    }
}
