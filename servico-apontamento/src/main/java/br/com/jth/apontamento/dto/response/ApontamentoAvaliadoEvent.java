package br.com.jth.apontamento.dto.response;

import br.com.jth.apontamento.enums.ApontamentoStatus;
import java.math.BigDecimal;

// dto para mensageria
public record ApontamentoAvaliadoEvent(
        Long apontamentoId,
        Long itemId,
        Double horasLiquidas,
        ApontamentoStatus status,
        BigDecimal valorHoraAplicado,
        String nivelAtividade
) {}