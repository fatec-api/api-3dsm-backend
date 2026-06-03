package br.com.jth.apontamento.mensageria;

import br.com.jth.apontamento.config.RabbitMQConfig;
import br.com.jth.apontamento.dto.response.ApontamentoAvaliadoEvent;
import br.com.jth.apontamento.dto.response.ApontamentoCriadoEvent;
import br.com.jth.apontamento.model.ApontamentoModel;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.jth.apontamento.dto.response.ApontamentoEventAuditoria;


@Component
@RequiredArgsConstructor
public class ApontamentoEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicarApontamentoCriado(ApontamentoModel apontamento) {
        ApontamentoCriadoEvent event = new ApontamentoCriadoEvent(
                apontamento.getId(),
                apontamento.getUsuarioId(),
                apontamento.getItemId(),
                apontamento.getDataApontamento(),
                apontamento.getHoraInicio(),
                apontamento.getHoraFim(),
                apontamento.getObservacao(),
                apontamento.getHorasLiquidas(),
                apontamento.getValorHoraAplicado()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.APONTAMENTO_CRIADO_KEY,
                event
        );
    }

    public void publicarApontamentoAvaliado(ApontamentoModel apontamento) {
        ApontamentoAvaliadoEvent event = new ApontamentoAvaliadoEvent(
                apontamento.getId(),
                apontamento.getItemId(),
                apontamento.getHorasLiquidas(),
                apontamento.getStatus(),
                apontamento.getValorHoraAplicado(),
                apontamento.getNivelAtividade() != null ? apontamento.getNivelAtividade().name() : null
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.APONTAMENTO_AVALIADO_KEY,
                event
        );
    }

    public void publicarApontamentoAuditoria(ApontamentoModel apontamento) {
        ApontamentoEventAuditoria event = new ApontamentoEventAuditoria(
                apontamento.getId(),
                apontamento.getCriadoEm(),
                apontamento.getItemId(),
                apontamento.getUsuarioId(),
                apontamento.getDataApontamento(),
                apontamento.getHoraInicio(),
                apontamento.getHoraFim(),
                apontamento.getHorasLiquidas(),
                apontamento.getObservacao(),
                apontamento.getJustificativaReprovacao(),
                apontamento.getStatus().name()
        );

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.GESTAO_EXCHANGE,
            RabbitMQConfig.APONTAMENTO_AUDITORIA_KEY,
            event
        );
    }

    
}

