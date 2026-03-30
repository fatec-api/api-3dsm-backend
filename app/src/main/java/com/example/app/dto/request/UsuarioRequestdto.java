package com.example.app.dto.request;

import com.example.app.model.entity.UsuarioModel;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UsuarioRequestdto {

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    private String nomeUsuario;

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    @Email(message = "E-mail informado é inválido.")
    private String email;

    @NotBlank(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}|:<>?]).{8,}$",
            message = "Senha inválida, ela deve ter ao menos 8 caracteres, incluindo ao menos 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial."
    )
    private String senha;

    @NotNull(message = "Preencha todos os campos obrigatórios (nome, e-mail, senha, confirme senha ou valor/hora).")
    @DecimalMin(value = "0.01", message = "O valor/hora deve ser maior que zero.")
    private BigDecimal valorHora;

    @NotNull(message = "Cargo é obrigatório.")
    private UsuarioModel.Cargo cargo;

    private String nivelExperiencia;
}