package com.example.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<MensagemErro> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex, WebRequest request) {
            MensagemErro error = new MensagemErro(
                    HttpStatus.NOT_FOUND.value(),
                    "Recurso não encontrado",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(NegocioException.class)
        public ResponseEntity<MensagemErro> handleNegocioException(NegocioException ex, WebRequest request) {
            MensagemErro error = new MensagemErro(
                    HttpStatus.UNPROCESSABLE_CONTENT.value(),
                    "Regra de negócio não atendida",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );

            return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
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

