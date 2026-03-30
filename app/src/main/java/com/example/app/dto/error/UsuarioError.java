package com.example.app.dto.error;

import lombok.Data;

@Data
public class UsuarioError {
    private int status;
    private String message;
    private String code;
    public UsuarioError(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}