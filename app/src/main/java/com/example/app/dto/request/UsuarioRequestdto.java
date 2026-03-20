package com.example.app.dto.request;

import com.example.app.model.entity.UsuarioModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class UsuarioRequestdto {
    @NotBlank
    private String nomeUsuario;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotNull
    private BigDecimal valorHora;
    @NotNull
    private UsuarioModel.NivelExperiencia nivelExperiencia;
    @NotNull
    private UsuarioModel.Cargo cargo;

    private boolean ativo;
    @NotNull
    private Timestamp criado_em;
}
