package com.example.app.exception;

import com.example.app.dto.error.UsuarioError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsuarioException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UsuarioError> handleValidationException(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Preencha todos os campos obrigatórios.");

        UsuarioError usuarioError = new UsuarioError(400, mensagem);
        return new ResponseEntity<>(usuarioError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<UsuarioError> handleEmailJaCadastrado(EmailJaCadastradoException e) {
        UsuarioError usuarioError = new UsuarioError(409, e.getMessage());
        return new ResponseEntity<>(usuarioError, HttpStatus.CONFLICT);
    }
}