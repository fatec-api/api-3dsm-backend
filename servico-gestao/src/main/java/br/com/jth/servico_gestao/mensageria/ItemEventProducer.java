package br.com.jth.servico_gestao.mensageria;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.mensageria.evento.AuditoriaEventDTO;
import br.com.jth.servico_gestao.mensageria.evento.ItemEventDTO;
import br.com.jth.servico_gestao.model.ItemModel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ItemEventProducer {

    private final AmqpTemplate amqpTemplate;

    public void publicarItemCriado(ItemModel model) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.ITEM_CRIADO_KEY,
                toEventDTO(model)
        );
        publicarAuditoria("ITEM_CRIADO", model.getId(),
                Map.of("id", model.getId(), "codigo", model.getCodigo(),
                        "descricao", model.getDescricao())
        );
    }

    public void publicarItemDeletado(Long id) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.ITEM_DELETADO_KEY,
                id.toString()
        );
        publicarAuditoria("ITEM_DELETADO", id, Map.of("id", id));
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

    private ItemEventDTO toEventDTO(ItemModel m) {
        return new ItemEventDTO(
                m.getId(),
                m.getCodigo(),
                m.getDescricao(),
                m.getDataAtribuicao(),
                m.getPrevisaoHoras(),
                m.getNivelAtividade(),
                m.getProjetoModel() != null ? m.getProjetoModel().getId() : null,
                m.getProjetoModel() != null ? m.getProjetoModel().getNomeProjeto() : null,
                m.getUsuarioModel() != null ? m.getUsuarioModel().getNomeUsuario() : null
        );
    }
}