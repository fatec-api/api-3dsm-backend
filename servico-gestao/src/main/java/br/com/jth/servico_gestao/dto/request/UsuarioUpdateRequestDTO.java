package br.com.jth.servico_gestao.dto.request;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

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

    private String senha;
    private String confirmaSenha;
}