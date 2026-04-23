package br.com.jth.servico_gestao.dto.request;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
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

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}|:<>?]).{8,}$",
            message = "Senha inválida..."
    )
    private String senha;

    @NotNull(message = "Preencha todos os campos obrigatórios.")
    @DecimalMin(value = "0.01", message = "O valor/hora deve ser maior que zero.")
    private BigDecimal valorHora;

    @NotNull(message = "Cargo é obrigatório.")
    private Cargo cargo;

    private String nivelExperiencia;
}