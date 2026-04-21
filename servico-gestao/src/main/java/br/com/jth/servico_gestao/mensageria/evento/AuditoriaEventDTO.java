package br.com.jth.servico_gestao.mensageria.evento;

import java.time.LocalDateTime;
import java.util.Map;

public record AuditoriaEventDTO(
        String correlationId,
        String servicoOrigem,
        String tipoAcao,
        String usuarioId,
        LocalDateTime timestamp,
        Map<String, Object> detalhes
) {}