package br.com.jth.auditoria.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditoriaMessageEvent(
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
) { }
