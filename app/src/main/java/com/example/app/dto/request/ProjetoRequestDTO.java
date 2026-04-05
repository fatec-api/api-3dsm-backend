package com.example.app.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.app.model.entity.ProjetoModel.StatusProjeto;
import com.example.app.model.entity.ProjetoModel.TipoProjeto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ProjetoRequestDTO {

    @NotBlank(message = "O nome do projeto é obrigatório")
    @Pattern(
            regexp = "^[A-Z]{3}\\d{4}$",
            message = "O nome do projeto deve conter 3 letras maiúsculas seguidas de 4 números (ex: GSW1234)"
    )
    private String nomeProjeto;

    @NotNull(message = "O tipo do projeto é obrigatório")
    private TipoProjeto tipoProjeto;

    @NotNull(message = "O valor do orçamento é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "O valor deve ser maior ou igual a 0")
    private BigDecimal valorOrcamento;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    private LocalDate dataFim;

    @NotNull(message = "O status do projeto é obrigatório")
    private StatusProjeto status;

    @NotNull(message = "O gestor responsável é obrigatório")
    private UUID gestorId;

    private Long clienteId;

    private List<UUID> profissionaisIds;
}
