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

    // comunicação com servico-gestao
    public static final String GESTAO_EXCHANGE = "gestao.exchange";

    public static final String APONTAMENTO_CRIADO_KEY   = "apontamento.criado";
    public static final String APONTAMENTO_AVALIADO_KEY = "apontamento.avaliado";
    public static final String APONTAMENTO_DELETADO_KEY = "apontamento.deletado";
    public static final String APONTAMENTO_AUDITORIA_KEY   = "apontamento.auditoria";


    public static final String PROJETO_QUERY_KEY = "projeto.query.request";

    @Bean
    public TopicExchange gestaoExchange() {
        return new TopicExchange(GESTAO_EXCHANGE, true, false);
    }

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
}
