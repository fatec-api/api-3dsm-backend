package br.com.jth.servico_gestao.mensageria;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.mensageria.evento.AuditoriaEventDTO;
import br.com.jth.servico_gestao.mensageria.evento.ProjetoEventDTO;
import br.com.jth.servico_gestao.model.ProjetoModel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class ProjetoEventProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void publicarProjetoCriado(ProjetoModel model) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.PROJETO_CRIADO_KEY,
                toEventDTO(model)
        );
        publicarAuditoria("PROJETO_CRIADO", model.getId(),
                Map.of("id", model.getId(), "nome", model.getNomeProjeto(), "status", model.getStatus())
        );
    }

    public void publicarProjetoDeletado(Long id) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.PROJETO_DELETADO_KEY,
                id.toString()
        );
        publicarAuditoria("PROJETO_DELETADO", id, Map.of("id", id));
    }

    private void publicarAuditoria(String tipoAcao, Object id, Map<String, Object> detalhes) {
        AuditoriaEventDTO auditoria = new AuditoriaEventDTO(
                UUID.randomUUID().toString(),
                "servico-gestao",
                tipoAcao,
                id.toString(),
                LocalDateTime.now(),
                detalhes
        );
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                "auditoria." + tipoAcao.toLowerCase(),
                auditoria
        );
    }

    private ProjetoEventDTO toEventDTO(ProjetoModel m) {
        return new ProjetoEventDTO(
                m.getId(), m.getNomeProjeto(), m.getTipoProjeto(),
                m.getValorOrcamento(), m.getDataInicio(), m.getDataFim(),
                m.getStatus(),
                m.getGestor() != null ? m.getGestor().getNomeUsuario() : null,
                m.getCliente() != null ? m.getCliente().getNomeEmpresa() : null
        );
    }
}