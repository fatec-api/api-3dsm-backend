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
        regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
        message = "CNPJ deve estar obrigatoriamente no formato 00.000.000/0000-00"
    )
    private String cnpj;

    @NotBlank(message = "Telefone da empresa é obrigatório")
    private String telefoneEmpresa;

    @NotBlank(message = "Telefone do responsável é obrigatório")
    private String telefoneResponsavel;
}