package com.example.app.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioResponseDTO {
    private UUID id;
    private String nomeUsuario;
    private String email;
    private String cargo;
    private String nivelExperiencia;
}