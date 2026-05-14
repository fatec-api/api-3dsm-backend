package br.com.jth.apontamento.mensageria.usuario;

import br.com.jth.apontamento.config.RabbitMQConfig;
import br.com.jth.apontamento.dto.request.BuscarValorHoraAtualRequestDTO; // Importe o DTO aqui
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioQueryProducer {

    private final RabbitTemplate rabbitTemplate;

    public BigDecimal buscarValorHoraAtual(UUID usuarioId) {
        log.info(">>> MENSAGERIA: Solicitando valor hora para o Usuário: {}", usuarioId);

        try {
            BuscarValorHoraAtualRequestDTO request = new BuscarValorHoraAtualRequestDTO(usuarioId);

            BigDecimal valorHora = rabbitTemplate.convertSendAndReceiveAsType(
                    RabbitMQConfig.GESTAO_EXCHANGE,
                    RabbitMQConfig.USUARIO_VALOR_QUERY_KEY,
                    request, 
                    new ParameterizedTypeReference<BigDecimal>() {}
            );

            if (valorHora == null) {
                log.warn("Serviço de Gestão retornou valor nulo para o usuário {}. Usando 0.00", usuarioId);
                return BigDecimal.ZERO;
            }

            return valorHora;
            
        } catch (Exception e) {
            log.error("Falha ao consultar valor hora via mensageria para o usuário {}", usuarioId, e);
            return BigDecimal.ZERO; 
        }
    }
}