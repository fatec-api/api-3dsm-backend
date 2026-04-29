package br.com.jth.servico_gestao.dto.response;


import br.com.jth.servico_gestao.enums.projeto.StatusProjeto;
import br.com.jth.servico_gestao.enums.projeto.TipoProjeto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjetoResponseDTO {

    private Long id;
    private String nomeProjeto;
    private TipoProjeto tipoProjeto;
    private BigDecimal valorOrcamento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusProjeto status;
    private String nomeGestor;
    private String nomeCliente;
}