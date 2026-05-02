package br.com.jth.servico_gestao.mensageria.evento;

import br.com.jth.servico_gestao.enums.item.NivelAtividade;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ItemEventDTO(
        Long id,
        String codigo,
        String descricao,
        LocalDate dataAtribuicao,
        Integer previsaoHoras,
        NivelAtividade nivelAtividade,
        Long projetoId,
        String projetoNome,
        List<String> usuarioNomes
) {}