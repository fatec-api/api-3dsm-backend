package br.com.jth.servico_gestao.mensageria;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.dto.request.BuscarItemPorIdRequestDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.mapper.ItemMapper;
import br.com.jth.servico_gestao.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemPorIdConsumer {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ITEM_POR_ID_QUERY)
    @SendTo
    public ItemResponseDTO responderItemPorId(BuscarItemPorIdRequestDTO request) {
        log.info(">>> GESTÃO: Recebida solicitação de item por ID: {}", request.getItemId());
        return itemRepository.findById(request.getItemId())
                .map(itemMapper::toResponse)
                .orElse(null);
    }
}
