package br.com.jth.servico_gestao.mensageria;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.service.ItemService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemConsumer {

    private final ItemService itemService;

    @RabbitListener(queues = "projeto.itens.query.queue")
    @SendTo
    public List<ItemResponseDTO> responderItensDoProjeto(Long projetoId) {
        return itemService.listarPorProjeto(projetoId);
    }
}