package br.com.jth.servico_gestao.exception;

public class ItemMessageError {

    private int status;
    private String mensagem;

    public ItemMessageError(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    public int getStatus() { return status; }
    public String getMensagem() { return mensagem; }
}