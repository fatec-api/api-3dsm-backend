package br.com.jth.servico_gestao.mensageria;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.mensageria.evento.AuditoriaEventDTO;
import br.com.jth.servico_gestao.mensageria.evento.UsuarioEventDTO;
import br.com.jth.servico_gestao.model.UsuarioModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioEventProducer {

    private final AmqpTemplate amqpTemplate;

    public void publicarUsuarioCriado(UsuarioModel model) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.USUARIO_CRIADO_KEY,
                toEventDTO(model)
        );
        publicarAuditoria("USUARIO_CRIADO", model.getId().toString(),
                Map.of("id", model.getId(), "email", model.getEmail(), "cargo", model.getCargo())
        );
    }

    public void publicarUsuarioAtualizado(UsuarioModel model) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.USUARIO_ATUALIZADO_KEY,
                toEventDTO(model)
        );
        publicarAuditoria("USUARIO_ATUALIZADO", model.getId().toString(),
                Map.of("id", model.getId(), "email", model.getEmail())
        );
    }

    public void publicarUsuarioDeletado(UUID id) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.USUARIO_DELETADO_KEY,
                id.toString()
        );
        publicarAuditoria("USUARIO_DELETADO", id.toString(), Map.of("id", id));
    }

    private void publicarAuditoria(String tipoAcao, String usuarioId, Map<String, Object> detalhes) {
        AuditoriaEventDTO auditoria = new AuditoriaEventDTO(
                UUID.randomUUID().toString(),
                "servico-gestao",
                tipoAcao,
                usuarioId,
                LocalDateTime.now(),
                detalhes
        );
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                "auditoria." + tipoAcao.toLowerCase(),
                auditoria
        );
    }

    private UsuarioEventDTO toEventDTO(UsuarioModel m) {
        return new UsuarioEventDTO(
                m.getId(), m.getNomeUsuario(), m.getEmail(),
                m.getCargo(), m.getValorHora(), m.isAtivo()
        );
    }
}