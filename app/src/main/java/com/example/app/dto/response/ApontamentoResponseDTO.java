package com.example.app.dto.response;

import java.time.LocalDateTime;

public record ApontamentoResponseDTO(
        Long id,
        Long itemId,
        String itemDescricao,
        Long usuarioId,
        String nomeUsuario,
        LocalDateTime dataApontamento,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        Double horasLiquidas,
        String observacao

) {
}
