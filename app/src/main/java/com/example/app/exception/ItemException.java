package com.example.app.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@ControllerAdvice
public class ItemException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ItemError> handleValidationException(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new ItemError(400, "Preencha todos os campos obrigatórios para prosseguir: " + mensagens));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ItemError> handleDataIntegrityException(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest()
                .body(new ItemError(400, "Violação de integridade de dados."));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ItemError> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ItemError(ex.getStatusCode().value(), ex.getReason()));
    }
}