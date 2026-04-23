package br.com.jth.servico_gestao.exception;

import java.util.UUID;

public class RecursoNaoEncontradoException extends RuntimeException{
    public RecursoNaoEncontradoException(String entidade, UUID id){
        super(entidade + "com id" + id + "não encontrado");
    }
}
