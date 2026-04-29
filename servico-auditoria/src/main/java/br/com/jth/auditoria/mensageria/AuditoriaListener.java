package br.com.jth.auditoria.mensageria;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
        logAuditoria.setUsuarioId(event.usuarioId());
        logAuditoria.setTimestamp(LocalDateTime.now());

        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("usuarioId", event.observacao());
        detalhes.put("itemId", event.itemId());
        detalhes.put("dataApontamento", event.dataApontamento());
        detalhes.put("horaInicio", event.horaInicio());
        detalhes.put("horaFim", event.horaFim());
        detalhes.put("observacao", event.observacao());

        logAuditoria.setDetalhes(detalhes);

        AuditoriaLog salvo = auditoriaRepository.save(logAuditoria);
        log.info("Log salvo com sucesso: {}", salvo.getId());
    }
}