package br.com.jth.auditoria.dto.request;

import java.util.Map;

public record AuditoriaMessageEvent(
                String correlationId,
                String servicoOrigem,
                String usuarioId,
                String tipoAcao,
                Map<String, Object> detalhes) {
}