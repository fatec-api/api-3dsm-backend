package com.example.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProjetoUsuarioDTO {

    @NotNull
    private Long projetoId;

    @NotNull
    private List<UUID> usuarioId;
}
