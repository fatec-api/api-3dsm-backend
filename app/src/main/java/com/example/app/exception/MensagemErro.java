package com.example.app.exception;

import java.time.LocalDateTime;

public class MensagemErro {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String mensagem;
        private String path;

        public MensagemErro(int status, String error, String message, String path) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.mensagem = message;
            this.path = path;
        }
    }

