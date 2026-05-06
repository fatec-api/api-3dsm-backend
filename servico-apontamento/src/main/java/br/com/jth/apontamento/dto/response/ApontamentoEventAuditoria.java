package br.com.jth.apontamento.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoEventAuditoria(
        Long id,
        LocalDateTime criadoEm,
        Long itemId,
        UUID usuarioId,
        LocalDateTime dataApontamento,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        Double horasLiquidas,
        String observacao,
        String justificativa,
        String status
) {
}
