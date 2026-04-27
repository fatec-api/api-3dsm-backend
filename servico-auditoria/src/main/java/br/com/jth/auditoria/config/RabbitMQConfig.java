package br.com.jth.auditoria.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "auditoria_queue";
    public static final String DLQ = "auditoria_queue_dlq";
    public static final String EXCHANGE = "auditoria.exchange";

    @Bean
    public Queue auditoriaQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public Queue auditoriaDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public DirectExchange auditoriaExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding auditoriaBinding(Queue auditoriaQueue, DirectExchange auditoriaExchange) {
        return BindingBuilder.bind(auditoriaQueue).to(auditoriaExchange).with(QUEUE);
    }
}