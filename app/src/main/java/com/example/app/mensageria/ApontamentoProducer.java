package com.example.app.mensageria;

import com.example.app.config.RabbitMQConfig;
import com.example.app.dto.request.ApontamentoRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApontamentoProducer {

    private final AmqpTemplate amqpTemplate;

    public void publicar(ApontamentoRequestDTO dto) {
        log.info("Publicando apontamento na fila: usuarioId={}", dto.usuarioId());
        amqpTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_APONTAMENTOS,
                RabbitMQConfig.ROUTING_KEY_APONTAMENTOS,
                dto
        );
    }
}