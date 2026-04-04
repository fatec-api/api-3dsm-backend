package com.example.app.exception;

public class ItemError {
    int Status;
    String Mensagem;
    public ItemError(int Status, String Mensagem) {
        this.Status = Status;
        this.Mensagem = Mensagem;
    }
}
