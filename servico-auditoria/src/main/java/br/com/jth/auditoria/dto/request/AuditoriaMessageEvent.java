package br.com.jth.auditoria.dto.request;

import java.time.LocalDateTime;

public record AuditoriaMessageEvent(
        String usuarioId,
        Long itemId,
        LocalDateTime dataApontamento,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        String observacao) {
}