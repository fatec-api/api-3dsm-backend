package com.example.app.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponseDTO {
    private UUID id;
    private String nomeUsuario;
    private String email;
    private String cargo;
    private String nivelExperiencia;
    private BigDecimal valorHora;
}