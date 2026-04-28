package br.com.jth.servico_gestao.dto.response;

import br.com.jth.servico_gestao.enums.item.NivelAtividade;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HorasPorAtividadeDTO {
    private NivelAtividade nivelAtividade;
    private Integer horasPrevistas;
    private Integer horasRealizadas;    // sempre 0 por enquanto, até implementar apontamentos
    private Double percentual;          // (realizadas / previstas) * 100
}