package br.com.jth.apontamento.dto.response;

import br.com.jth.apontamento.enums.NivelAtividade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDTO {

    private Long id;
    private String codigo;
    private String descricao;
    private LocalDate dataAtribuicao;
    private Integer previsaoHoras;
    private NivelAtividade nivelAtividade;
    private Long projetoId;
    private String projetoNome;
    private String usuarioNome;
}
