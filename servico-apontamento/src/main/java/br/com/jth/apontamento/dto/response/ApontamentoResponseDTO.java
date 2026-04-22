package br.com.jth.apontamento.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoResponseDTO(
        Long id,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime criadoEm,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime atualizadoEm,

        Long itemId,

        String itemDescricao,

        UUID usuarioId,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataApontamento,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime horaInicio,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime horaFim,

        Double horasLiquidas,

        String observacao

) {
}
