package br.com.jth.servico_gestao.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    private String nomeUsuario;

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    @Email(message = "E-mail informado é inválido.")
    private String email;

    @NotNull(message = "Preencha todos os campos obrigatórios.")
    @DecimalMin(value = "0.01", message = "O valor/hora deve ser maior que zero.")
    private BigDecimal valorHora;

    // Cargo removido — é atribuído pelo Keycloak via RabbitMQ após criação
    private String nivelExperiencia;
}