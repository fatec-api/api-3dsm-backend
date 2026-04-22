package br.com.jth.auditoria.mensageria;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.jth.auditoria.config.RabbitMQConfig.QUEUE;
import br.com.jth.auditoria.dto.request.AuditoriaMessageEvent;
import br.com.jth.auditoria.model.AuditoriaLog;
import br.com.jth.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditoriaListener {

    private final AuditoriaRepository auditoriaRepository;

    @RabbitListener(queues = QUEUE)
    public void consumir(AuditoriaMessageEvent event) {
        log.info("Evento recebido: {}", event);

        AuditoriaLog logAuditoria = new AuditoriaLog();
        logAuditoria.setCorrelationId(event.correlationId());
        logAuditoria.setTimestamp(LocalDateTime.now());
        logAuditoria.setServicoOrigem(event.servicoOrigem());
        logAuditoria.setUsuarioId(event.usuarioId());
        logAuditoria.setTipoAcao(event.tipoAcao());
        logAuditoria.setDetalhes(event.detalhes());

        auditoriaRepository.save(logAuditoria);
        log.info("Log salvo com sucesso: {}", logAuditoria.getId());
    }
}