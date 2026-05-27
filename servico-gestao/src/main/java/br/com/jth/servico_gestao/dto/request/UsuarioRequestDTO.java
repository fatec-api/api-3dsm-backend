package br.com.jth.servico_gestao.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, ou valor/hora).")
    private String nomeUsuario;

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, ou valor/hora).")
    @Email(message = "E-mail informado é inválido.")
    private String email;

    @NotNull(message = "Preencha todos os campos obrigatórios.")
    @DecimalMin(value = "0.01", message = "O valor/hora deve ser maior que zero.")
    private BigDecimal valorHora;

    // Cargo removido — é atribuído pelo Keycloak via RabbitMQ após criação
    private String nivelExperiencia;
}