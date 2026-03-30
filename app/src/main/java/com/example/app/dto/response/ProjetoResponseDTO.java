package com.example.app.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.app.model.entity.ProjetoModel.StatusProjeto;
import com.example.app.model.entity.ProjetoModel.TipoProjeto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@JsonInclude(Include.NON_NULL)
@Data
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
