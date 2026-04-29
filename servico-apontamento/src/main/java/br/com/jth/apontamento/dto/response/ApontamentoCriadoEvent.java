package br.com.jth.apontamento.dto.response;

public record ApontamentoCriadoEvent(
        Long apontamentoId,
        Long itemId,
        Double horasLiquidas
) {}