package br.com.jth.servico_gestao.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String nomeEmpresa;
    private String nomeResponsavel;
    private String email;
    private String cnpj;
    private String telefoneEmpresa;
    private LocalDate dataCadastro;
    private boolean ativo;
}
