package br.com.jth.apontamento.mensageria;

import br.com.jth.apontamento.config.RabbitMQConfig;
import br.com.jth.apontamento.dto.request.ApontamentoRequestDTO;
import br.com.jth.apontamento.exception.NegocioException;
import br.com.jth.apontamento.service.ApontamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApontamentoConsumer {

    private final ApontamentoService apontamentoService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_APONTAMENTOS)
    public void receber(ApontamentoRequestDTO dto) {
        log.info("Apontamento recebido via fila: usuarioId={}, itemId={}", dto.usuarioId(), dto.itemId());
        try {
            apontamentoService.salvarApontamento(dto);
        } catch (NegocioException e) {
            log.error("Mensagem rejeitada por regra de negocio: {}", e.getMessage(), e);
            // Aqui você pode lançar AmqpRejectAndDontRequeueException
            // se não quiser reprocessar em caso de erro de negócio
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }
}