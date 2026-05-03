package br.com.jth.auditoria.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "auditoria_queue";
    public static final String DLQ = "auditoria_queue_dlq";

    public static final String GESTAO_EXCHANGE = "gestao.exchange";
    public static final String APONTAMENTO_AUDITORIA_KEY = "apontamento.auditoria";

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
    public TopicExchange gestaoExchange() {
        return new TopicExchange(GESTAO_EXCHANGE, true, false);
    }

    @Bean
    public Binding auditoriaBinding(Queue auditoriaQueue, TopicExchange gestaoExchange) {
        return BindingBuilder
                .bind(auditoriaQueue)
                .to(gestaoExchange)
                .with(APONTAMENTO_AUDITORIA_KEY);
    }

    // escuta tudo que vem do gestao.exchange com routing key apontamento.*
    @Bean
    public Binding bindingApontamentos(Queue auditoriaQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(auditoriaQueue).to(gestaoExchange).with("apontamento.*");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());

        return factory;
    }
}