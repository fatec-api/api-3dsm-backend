package com.example.app.dto.error;

import lombok.Data;

@Data
public class UsuarioError {
    private int status;
    private String message;

    public UsuarioError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}