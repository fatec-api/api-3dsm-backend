package br.com.jth.servico_gestao.dto.response;


import br.com.jth.servico_gestao.enums.projeto.StatusOrcamento;
import br.com.jth.servico_gestao.enums.projeto.StatusProjeto;
import br.com.jth.servico_gestao.enums.projeto.TipoProjeto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    private BigInteger horasPrevistasTotal;
    private Double horasRealizadasTotal;
    private Double horasPendentesTotal;
    private Double custoRealTotal;
    private StatusOrcamento statusOrcamento;
    private Double progressoProjeto;
    private String nomeGestor;
    private String nomeCliente;
}