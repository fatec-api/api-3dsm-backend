package br.com.jth.servico_gestao.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GESTAO_EXCHANGE = "gestao.exchange";

    public static final String USUARIO_CRIADO_KEY     = "usuario.criado";
    public static final String USUARIO_ATUALIZADO_KEY = "usuario.atualizado";
    public static final String USUARIO_DELETADO_KEY   = "usuario.deletado";

    public static final String PROJETO_CRIADO_KEY  = "projeto.criado";
    public static final String PROJETO_DELETADO_KEY = "projeto.deletado";

    public static final String ITEM_CRIADO_KEY  = "item.criado";
    public static final String ITEM_DELETADO_KEY = "item.deletado";

    @Bean
    public TopicExchange gestaoExchange() {
        return new TopicExchange(GESTAO_EXCHANGE, true, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}