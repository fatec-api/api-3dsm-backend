package br.com.jth.auditoria.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AuditoriaLogResponseDTO(
        String id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime criadoEm,
        Long itemId,
        UUID usuarioId,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataApontamento,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime horaInicio,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime horaFim,
        Double horasLiquidas,
        String observacao) {
}