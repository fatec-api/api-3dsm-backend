package br.com.jth.apontamento.dto.response;


import br.com.jth.apontamento.enums.Status_Apontamento;

// dto para mensageria
public record ApontamentoAvaliadoEvent(
        Long apontamentoId,
        Long itemId,
        Double horasLiquidas,
        Status_Apontamento status
) {}