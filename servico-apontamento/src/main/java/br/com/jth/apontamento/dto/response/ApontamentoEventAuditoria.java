package br.com.jth.apontamento.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoEventAuditoria(
        Long id,
//        LocalDateTime timestamp,
        Long itemId,
        UUID usuarioId,
        LocalDateTime dataApontamento,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        Double horasLiquidas,
        String observacao
) {
}
