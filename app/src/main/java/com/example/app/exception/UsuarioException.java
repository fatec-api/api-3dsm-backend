package com.example.app.exception;

import com.example.app.dto.error.UsuarioError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsuarioException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UsuarioError>handleValidationException(MethodArgumentNotValidException e) {
        UsuarioError usuarioError = new UsuarioError(400, "Campos obrigatórios não preenchidos.");
        return new ResponseEntity<>(usuarioError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UsuarioError> handleDataIntegrityException(DataIntegrityViolationException e) {
        UsuarioError usuarioError = new UsuarioError(400, "Verifique os dados antes de enviar.");
        return new ResponseEntity<>(usuarioError, HttpStatus.BAD_REQUEST);
    }
}
