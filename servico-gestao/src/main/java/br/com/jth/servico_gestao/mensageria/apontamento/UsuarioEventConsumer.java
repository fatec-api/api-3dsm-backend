package br.com.jth.servico_gestao.mensageria.apontamento;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component
public class UsuarioEventConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_USUARIO)
    public void onAtualizarUsuario(Map<String, Object> event){
        String keycloakId = event.get("id").toString();
        log.info("ID recebido para atualização no Keycloak: {}", keycloakId);
    }
}
