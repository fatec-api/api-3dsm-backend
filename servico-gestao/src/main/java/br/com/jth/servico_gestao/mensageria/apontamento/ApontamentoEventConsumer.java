package br.com.jth.servico_gestao.mensageria.apontamento;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.dto.response.ApontamentoAvaliadoEvent;
import br.com.jth.servico_gestao.dto.response.ApontamentoCriadoEvent;
import br.com.jth.servico_gestao.enums.apontamento.ApontamentoStatus;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.repository.ItemRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApontamentoEventConsumer {

    private final ProjetoRepository projetoRepository;
    private final ItemRepository itemRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_APONTAMENTO_CRIADO)
    public void consumirApontamentoCriado(ApontamentoCriadoEvent event) {
        Long projetoId = itemRepository.findProjetoIdByItemId(event.itemId())
                .orElseThrow(() -> new RuntimeException(
                        "Item não encontrado ao processar evento: " + event.itemId()));

        ProjetoModel projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException(
                        "Projeto não encontrado ao processar evento: " + projetoId));

        Double horasLiquidas = event.horasLiquidas() != null ? event.horasLiquidas() : 0.0;
        Double pendentes = projeto.getHorasPendentesTotal() != null
                ? projeto.getHorasPendentesTotal() : 0.0;

        projeto.setHorasPendentesTotal(pendentes + horasLiquidas);
        projetoRepository.save(projeto);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_APONTAMENTO)
    public void consumirApontamentoAvaliado(ApontamentoAvaliadoEvent event) {
        Long projetoId = itemRepository.findProjetoIdByItemId(event.itemId())
                .orElseThrow(() -> new RuntimeException(
                        "Item não encontrado ao processar evento: " + event.itemId()));

        ProjetoModel projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException(
                        "Projeto não encontrado ao processar evento: " + projetoId));

        Double horasLiquidas = event.horasLiquidas() != null ? event.horasLiquidas() : 0.0;
        Double pendentes = projeto.getHorasPendentesTotal() != null
                ? projeto.getHorasPendentesTotal() : 0.0;

        // aprovado ou reprovado: sempre sai das pendentes
        projeto.setHorasPendentesTotal(Math.max(0.0, pendentes - horasLiquidas));

        if (event.status() == ApontamentoStatus.APROVADO) {
            Double realizadas = projeto.getHorasRealizadasTotal() != null
                    ? projeto.getHorasRealizadasTotal() : 0.0;
            projeto.setHorasRealizadasTotal(realizadas + horasLiquidas);
        }

        projetoRepository.save(projeto);
    }
}