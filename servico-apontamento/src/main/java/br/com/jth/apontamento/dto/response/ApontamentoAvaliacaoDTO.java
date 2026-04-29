package br.com.jth.apontamento.dto.response;

import br.com.jth.apontamento.enums.ApontamentoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApontamentoAvaliacaoDTO(
        @NotNull(message = "O status é obrigatório")
        ApontamentoStatus status,

        @Size(max = 500, message = "A justificativa deve ter no máximo 500 caracteres")
        String justificativaReprovacao
) {}