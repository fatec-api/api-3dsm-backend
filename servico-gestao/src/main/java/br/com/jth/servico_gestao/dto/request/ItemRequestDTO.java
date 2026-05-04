package br.com.jth.servico_gestao.dto.request;

import br.com.jth.servico_gestao.enums.item.NivelAtividade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ItemRequestDTO {

    @NotBlank(message = "O campo de titulo não pode estar vazio")
    private String titulo;

    @NotBlank(message = "O campo de descrição não pode estar vazio.")
    @Size(max = 300, message = "Descrição deve ter no máximo 300 caracteres.")
    private String descricao;

    private LocalDate dataAtribuicao;

    @Min(value = 0, message = "A previsão de horas não pode ser negativa.")
    private Integer previsaoHoras;

    private NivelAtividade nivelAtividade;

    private List<UUID> usuarioIds;

    @NotNull(message = "O projeto é obrigatório.")
    private Long projetoId;
}
