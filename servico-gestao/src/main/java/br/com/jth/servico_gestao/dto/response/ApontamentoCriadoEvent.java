package br.com.jth.servico_gestao.dto.response;

import java.math.BigDecimal;

public record ApontamentoCriadoEvent(
        Long id,
        Long itemId,
        Double horasLiquidas,
        BigDecimal valorHoraAplicado
) {}