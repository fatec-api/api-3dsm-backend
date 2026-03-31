package com.example.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoResponseDTO(
        Long id,
        LocalDateTime criadoEm,
        Long itemId,
        String itemDescricao,
        UUID usuarioId,
        String nomeUsuario,
        LocalDateTime dataApontamento,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        Double horasLiquidas,
        String observacao

) {
}
