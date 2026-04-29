package br.com.jth.apontamento.mensageria.projeto;

import br.com.jth.apontamento.config.RabbitMQConfig;
import br.com.jth.apontamento.dto.request.BuscarItensPorProjetoRequestDTO;
import br.com.jth.apontamento.dto.response.ItemResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ProjetoQueryProducer {

    private final RabbitTemplate rabbitTemplate;

    public List<ItemResponseDTO> buscarItensParaApontamento(Long projetoId) {
        System.out.println(">>> MENSAGERIA: Solicitando itens do Projeto ID: " + projetoId);
        BuscarItensPorProjetoRequestDTO request = new BuscarItensPorProjetoRequestDTO(projetoId);

        // Utilizamos o convertSendAndReceive para esperar a resposta do outro microserviço
        // O ParameterizedTypeReference é essencial para que o Jackson saiba converter o JSON de volta para uma List de DTOs
        List<ItemResponseDTO> response = rabbitTemplate.convertSendAndReceiveAsType(
                RabbitMQConfig.GESTAO_EXCHANGE,
                RabbitMQConfig.PROJETO_QUERY_KEY,
                request,
                new ParameterizedTypeReference<List<ItemResponseDTO>>() {}
        );

        return response != null ? response : new ArrayList<>();
    }
}