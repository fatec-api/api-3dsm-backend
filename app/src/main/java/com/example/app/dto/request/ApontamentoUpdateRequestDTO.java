package com.example.app.dto.request;


import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoUpdateRequestDTO(
        Long itemId,

        UUID usuarioId,

        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime dataApontamento,

        @PastOrPresent(message = "Você não pode apontar horas para o futuro")
        LocalDateTime horaInicio,

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
