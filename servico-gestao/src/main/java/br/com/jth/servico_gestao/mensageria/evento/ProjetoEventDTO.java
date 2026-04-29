package br.com.jth.servico_gestao.mensageria.evento;

import br.com.jth.servico_gestao.enums.projeto.StatusProjeto;
import br.com.jth.servico_gestao.enums.projeto.TipoProjeto;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjetoEventDTO(
        Long id,
        String nomeProjeto,
        TipoProjeto tipoProjeto,
        BigDecimal valorOrcamento,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusProjeto status,
        String nomeGestor,
        String nomeCliente
) {}