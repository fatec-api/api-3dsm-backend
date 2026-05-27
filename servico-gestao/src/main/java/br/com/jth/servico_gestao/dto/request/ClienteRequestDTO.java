package br.com.jth.servico_gestao.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank(message = "Nome da empresa é obrigatório")
    private String nomeEmpresa;

    @NotBlank(message = "Nome do responsável é obrigatório")
    private String nomeResponsavel;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(
        regexp = "\\d{14}",
        message = "CNPJ deve conter exatamente 14 números"
    )
    private String cnpj;

    @NotBlank(message = "Telefone da empresa é obrigatório")
    private String telefoneEmpresa;

    @NotBlank(message = "Telefone do responsável é obrigatório")
    private String telefoneResponsavel;

    private Boolean ativo;
}