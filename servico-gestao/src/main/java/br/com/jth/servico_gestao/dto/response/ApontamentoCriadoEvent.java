package br.com.jth.servico_gestao.dto.response;

public record ApontamentoCriadoEvent(
        Long apontamentoId,
        Long itemId,
        Double horasLiquidas
) {}