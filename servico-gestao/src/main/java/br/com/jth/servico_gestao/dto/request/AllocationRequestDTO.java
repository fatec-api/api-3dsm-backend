package br.com.jth.servico_gestao.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class AllocationRequestDTO {
     @NotNull(message = "O ID do projeto é obrigatório")
    private Long projectId;

    @NotNull(message = "O ID do item é obrigatório")
    private Long itemId;

    @NotEmpty(message = "É necessário selecionar pelo menos um profissional")
    private List<UUID> professionalIds;
}
