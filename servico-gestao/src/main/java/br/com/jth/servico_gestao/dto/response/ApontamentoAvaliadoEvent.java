package br.com.jth.servico_gestao.dto.response;

import br.com.jth.servico_gestao.enums.apontamento.ApontamentoStatus;

// espelho do evento de servico-apontamento
public record ApontamentoAvaliadoEvent(
        Long apontamentoId,
        Long itemId,
        Double horasLiquidas,
        ApontamentoStatus status
) {}