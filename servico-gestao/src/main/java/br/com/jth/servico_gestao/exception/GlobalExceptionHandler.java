package br.com.jth.servico_gestao.exception;

import br.com.jth.servico_gestao.dto.error.ApiError;
import br.com.jth.servico_gestao.dto.error.UsuarioError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex){

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                erros
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<UsuarioError> handleEmailJaCadastrado(EmailJaCadastradoException ex) {

        UsuarioError error = new UsuarioError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                "EMAIL_JA_EXISTE"
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ItemMessageError> handleDataIntegrityException(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ItemMessageError(
                        HttpStatus.BAD_REQUEST.value(),
                        "Violação de integridade de dados."
                ));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ItemMessageError> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ItemMessageError(
                        ex.getStatusCode().value(),
                        ex.getReason()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex){
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor",
                List.of(ex.getMessage())
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<ApiError> handlerRecursoNaoEncontrado(RecursoNaoEncontradoException ex){
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Usuário com Id pesquisado não existente",
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    }