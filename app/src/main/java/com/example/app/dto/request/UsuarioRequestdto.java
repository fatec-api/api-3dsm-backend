package com.example.app.dto.request;

import com.example.app.model.entity.UsuarioModel;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class UsuarioRequestdto {
    @NotBlank(message = "O nome não pode estar vazio.")
    private String nomeUsuario;
    @NotBlank(message = "O email não pode estar vazio.")
    @Email(message = "Email Inválido.")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$",
            message = "Senha deve conter maiúscula, minúscula, número e caractere especial"
    )
    private String senha;

    @NotNull(message = "Valor hora é obrigatório.")
    @DecimalMin(value = "0.01", message = "Valor hora mairo que zero.")
    private BigDecimal valorHora;

    @NotNull
    private UsuarioModel.Cargo cargo;
    @NotNull
    private boolean ativo;
    @NotNull
    private Timestamp criado_em;
}
