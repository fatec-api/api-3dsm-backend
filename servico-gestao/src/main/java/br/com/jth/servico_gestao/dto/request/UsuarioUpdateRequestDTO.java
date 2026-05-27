package br.com.jth.servico_gestao.dto.request;

import java.math.BigDecimal;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioUpdateRequestDTO {

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, cargo ou valor/hora).")
    private String nomeUsuario;

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, cargo ou valor/hora).")
    @Email(message = "E-mail informado é inválido.")
    private String email;

    @NotNull(message = "Cargo é obrigatório.")
    private Cargo cargo;

    @NotNull(message = "Preencha todos os campos obrigatórios.")
    @DecimalMin(value = "0.01", message = "O valor/hora deve ser maior que zero.")
    private BigDecimal valorHora;

    private String nivelExperiencia;

}