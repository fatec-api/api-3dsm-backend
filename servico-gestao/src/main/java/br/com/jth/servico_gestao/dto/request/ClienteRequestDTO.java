package br.com.jth.servico_gestao.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank
    private String nomeEmpresa;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String cnpj;
}