package br.com.jth.servico_gestao.mensageria;

import java.math.BigDecimal;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import br.com.jth.servico_gestao.dto.request.BuscarValorHoraAtualRequestDTO;
import br.com.jth.servico_gestao.service.UsuarioService;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UsuarioValorAtualConsumer {
    private final UsuarioService usuarioService;

    @RabbitListener(queues = "usuario.valor.query.queue")
    @SendTo
    public BigDecimal responderValorHoraAtual(BuscarValorHoraAtualRequestDTO request) {
        System.out.println(">>> GESTÃO: Recebida solicitação para o usuario ID: " + request.getUsuarioUuid());
        return (usuarioService.pegarUsuario(request.getUsuarioUuid())).valorHora();
    }

}