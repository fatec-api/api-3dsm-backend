package br.com.jth.auditoria.dto.request;

import java.time.LocalDateTime;

public record AuditoriaMessageEvent(
                Long id,
                LocalDateTime criadoEm,
                Long itemId,
                String usuarioId,
                LocalDateTime dataApontamento,
                LocalDateTime horaInicio,
                LocalDateTime horaFim,
                Double horasLiquidas,
                String observacao) {
}