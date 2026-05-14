package br.com.jth.servico_gestao.dto.request;

import br.com.jth.servico_gestao.enums.projeto.StatusProjeto;
import br.com.jth.servico_gestao.enums.projeto.TipoProjeto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ProjetoUpdateRequestDTO {

    @Pattern(
            regexp = "^[A-Z]{3}\\d{4}$",
            message = "O nome do projeto deve conter 3 letras maiúsculas seguidas de 4 números (ex: GSW1234)"
    )
    private String nomeProjeto;

    private TipoProjeto tipoProjeto;

    @DecimalMin(value = "0.0", inclusive = true, message = "O valor deve ser maior ou igual a 0")
    private BigDecimal valorOrcamento;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private StatusProjeto status;

    private UUID gestorId;

    private Long clienteId;

    private List<UUID> profissionalAlocadoIds;
}