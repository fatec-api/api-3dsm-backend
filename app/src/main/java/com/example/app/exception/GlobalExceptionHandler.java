package com.example.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RecursoNaoEncontradoExcecao.class)
        public ResponseEntity<MensagemErro> handleRecursoNaoEncontrado(RecursoNaoEncontradoExcecao ex,  WebRequest request) {
            MensagemErro error = new MensagemErro(
                    HttpStatus.NOT_FOUND.value(),
                    "Recurso não encontrado",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<MensagemErro> handleGlobalException(Exception ex, WebRequest request) {
            MensagemErro error = new MensagemErro(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Ocorreu um erro inesperado no servidor.",
                    request.getDescription(false)
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

