package br.com.jth.apontamento.dto.request;

import br.com.jth.apontamento.enums.Status_Apontamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApontamentoAprovarRequestDTO {
        @NotBlank(message = "Obrigatório Aprovar ou Reprovar o apontamento")
        Status_Apontamento status;

        @Size(max = 300, message = "A justificativa deve ter no máximo 300 caracteres")
        String justificativaReprovacao;
        
}
