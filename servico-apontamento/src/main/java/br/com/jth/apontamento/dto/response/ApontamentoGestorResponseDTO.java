package br.com.jth.apontamento.dto.response;

import br.com.jth.apontamento.enums.ApontamentoStatus;
import br.com.jth.apontamento.enums.NivelAtividade;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoGestorResponseDTO(
        Long id,
        UUID usuarioId,
        String usuarioNome,
        Long itemId,
        String itemDescricao,
        NivelAtividade nivelAtividade,
        Long projetoId,
        String projetoNome,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataApontamento,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime horaInicio,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime horaFim,

        Double horasLiquidas,
        String observacao,
        ApontamentoStatus status,
        String justificativaReprovacao,
        BigDecimal valorHoraAplicado
) {}
