package br.com.jth.servico_gestao.mensageria.evento;

import br.com.jth.servico_gestao.enums.item.NivelAtividade;
import java.time.LocalDate;

public record ItemEventDTO(
        Long id,
        String codigo,
        String descricao,
        LocalDate dataAtribuicao,
        Integer previsaoHoras,
        NivelAtividade nivelAtividade,
        Long projetoId,
        String projetoNome,
        String usuarioNome
) {}