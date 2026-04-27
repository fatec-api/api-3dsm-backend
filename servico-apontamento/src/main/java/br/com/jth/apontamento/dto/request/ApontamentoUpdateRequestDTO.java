package br.com.jth.apontamento.dto.request;

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

        @Size(max = 300, message = "A observação deve ter no máximo 300 caracteres")
        String observacao
) {

}
