package com.example.app.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MensagemErro(
        String mensagem,
        int status,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime timestamp) {
}
