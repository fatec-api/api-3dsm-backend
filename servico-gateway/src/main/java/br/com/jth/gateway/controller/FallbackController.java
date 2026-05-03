package br.com.jth.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/gestao")
    public Mono<ResponseEntity<Map<String, String>>> gestaoFallback(ServerWebExchange exchange) {
        return buildFallback(exchange, "servico-gestao");
    }

    @RequestMapping("/apontamento")
    public Mono<ResponseEntity<Map<String, String>>> apontamentoFallback(ServerWebExchange exchange) {
        return buildFallback(exchange, "servico-apontamento");
    }

    @RequestMapping("/auditoria")
    public Mono<ResponseEntity<Map<String, String>>> auditoriaFallback(ServerWebExchange exchange) {
        return buildFallback(exchange, "servico-auditoria");
    }

    private Mono<ResponseEntity<Map<String, String>>> buildFallback(
            ServerWebExchange exchange, String servico) {

        String correlationId = exchange.getRequest().getHeaders()
                .getFirst("X-Correlation-Id");

        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", "503");
        body.put("erro", "Serviço temporariamente indisponível.");
        body.put("servico", servico);
        body.put("correlationId", correlationId != null ? correlationId : "N/A");

        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body));
    }
}