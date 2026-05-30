package br.com.jth.apontamento.mensageria.item;

import br.com.jth.apontamento.config.RabbitMQConfig;
import br.com.jth.apontamento.dto.request.BuscarItemPorIdRequestDTO;
import br.com.jth.apontamento.dto.response.ItemResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemQueryProducer {

    private final RabbitTemplate rabbitTemplate;

    public ItemResponseDTO buscarItemPorId(Long itemId) {
        log.info(">>> MENSAGERIA: Solicitando dados do item ID: {}", itemId);
        BuscarItemPorIdRequestDTO request = new BuscarItemPorIdRequestDTO(itemId);

        return rabbitTemplate.convertSendAndReceiveAsType(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.ITEM_POR_ID_QUERY_KEY,
                request,
                new ParameterizedTypeReference<ItemResponseDTO>() {}
        );
    }
}
