package br.com.jth.servico_gestao.mensageria;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import br.com.jth.servico_gestao.dto.request.BuscarItensPorProjetoRequestDTO;
import br.com.jth.servico_gestao.dto.response.ItemResponseDTO;
import br.com.jth.servico_gestao.service.ItemService;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;

@Component
@RequiredArgsConstructor
public class ItemConsumer {

    private final ItemService itemService;

    @RabbitListener(queues = "projeto.itens.query.queue")
    @SendTo
    public List<ItemResponseDTO> responderItensDoProjeto(BuscarItensPorProjetoRequestDTO request) {
        System.out.println(">>> GESTÃO: Recebida solicitação para o projeto ID: " + request.getProjetoId());
        return itemService.listarPorProjeto(request.getProjetoId());
    }
}