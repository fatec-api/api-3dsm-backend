package br.com.jth.servico_gestao.exception;

public class EmailJaCadastradoException extends RuntimeException{
    public EmailJaCadastradoException(String message){
        super(message);
    }
}
//restcontroleradvice