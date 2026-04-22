package br.com.jth.apontamento.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoRequestDTO(
        @NotNull(message = "O item é obrigatório")
        Long itemId,

        @NotNull(message = "O usuário é obrigatório")
        UUID usuarioId,

        @NotNull(message = "A data do apontamento é obrigatória")
        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime dataApontamento,

        @NotNull(message = "A hora de início é obrigatória")
        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime horaInicio,

        @NotNull(message = "A hora de fim é obrigatória")
        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime horaFim,

        @Size(max = 300, message = "A observação deve ter no máximo 300 caracteres")
        String observacao
) {

}
