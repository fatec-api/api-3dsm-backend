package com.example.app.dto.error;

import lombok.Data;

@Data
public class UsuarioError {
    int Status;
    String Message;
    public UsuarioError(int Status, String Message) {
        this.Status = Status;
        this.Message = Message;
    }
}
