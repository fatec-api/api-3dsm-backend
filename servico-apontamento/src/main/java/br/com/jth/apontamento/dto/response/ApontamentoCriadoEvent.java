package br.com.jth.apontamento.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoCriadoEvent(
        Long id,
        UUID usuarioId,
        Long itemId,
        LocalDateTime dataApontamento,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        String observacao,
        Double horasLiquidas
) {}