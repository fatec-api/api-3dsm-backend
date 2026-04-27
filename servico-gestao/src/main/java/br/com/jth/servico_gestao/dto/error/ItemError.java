package br.com.jth.servico_gestao.dto.error;

import lombok.Data;

@Data
public class ItemError {
    int Status;
    String Mensagem;
    public ItemError(int Status, String Mensagem){
        this.Status = Status;
        this.Mensagem = Mensagem;
    }
}
