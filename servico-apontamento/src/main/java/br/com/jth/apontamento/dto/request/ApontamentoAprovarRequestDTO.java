package br.com.jth.apontamento.dto.request;

import br.com.jth.apontamento.enums.ApontamentoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApontamentoAprovarRequestDTO {
        @NotBlank(message = "Obrigatório Aprovar ou Reprovar o apontamento")
        ApontamentoStatus status;

        @Size(max = 300, message = "A justificativa deve ter no máximo 300 caracteres")
        String justificativaReprovacao;
        
}
