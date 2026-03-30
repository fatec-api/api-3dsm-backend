package com.example.app.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ApontamentoRequestDTO(
        @NotNull(message = "O item é obrigatório")
        Long itemId,

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "A data do apontamento é obrigatória")
        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime dataApontamento,

        @NotNull(message = "A hora de início é obrigatória")
        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime horaInicio,

        @NotNull(message = "A hora de fim é obrigatória")
        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime horaFim,

        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime pausaInicio,

        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime pausaFim,

        @Size(max = 300, message = "A observação deve ter no máximo 300 caracteres")
        String observacao
) {

}
