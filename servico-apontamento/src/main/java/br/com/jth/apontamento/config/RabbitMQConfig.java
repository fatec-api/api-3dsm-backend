package br.com.jth.apontamento.config;

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

    public static final String QUEUE_APONTAMENTOS       = "apontamentos.queue";
    public static final String EXCHANGE_APONTAMENTOS    = "apontamentos.exchange";
    public static final String ROUTING_KEY_APONTAMENTOS = "apontamentos.routing-key";

    // Fila durável (sobrevive a restart do broker)
    @Bean
    public Queue queueApontamentos() {
        return QueueBuilder.durable(QUEUE_APONTAMENTOS).build();
    }

    // Exchange do tipo Direct
    @Bean
    public DirectExchange exchangeApontamentos() {
        return new DirectExchange(EXCHANGE_APONTAMENTOS);
    }

    // Binding: exchange + routing-key → fila
    @Bean
    public Binding bindingApontamentos(Queue queueApontamentos,
                                       DirectExchange exchangeApontamentos) {
        return BindingBuilder
                .bind(queueApontamentos)
                .to(exchangeApontamentos)
                .with(ROUTING_KEY_APONTAMENTOS);
    }

    // Serialização/deserialização automática como JSON
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
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
