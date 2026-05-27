package br.com.jth.servico_gestao.dto.response;

import java.math.BigDecimal;

public record ApontamentoCriadoEvent(
        Long apontamentoId,
        Long itemId,
        Double horasLiquidas,
        BigDecimal valorHoraAplicado
) {}