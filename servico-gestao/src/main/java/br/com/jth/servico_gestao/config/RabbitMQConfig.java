package br.com.jth.servico_gestao.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GESTAO_EXCHANGE = "gestao.exchange";

    public static final String QUEUE_USUARIO = "usuario.queue";
    public static final String QUEUE_PROJETO = "projeto.queue";
    public static final String QUEUE_ITEM    = "item.queue";

    public static final String USUARIO_CRIADO_KEY     = "usuario.criado";
    public static final String USUARIO_ATUALIZADO_KEY = "usuario.atualizado";
    public static final String USUARIO_DELETADO_KEY   = "usuario.deletado";

    public static final String PROJETO_CRIADO_KEY     = "projeto.criado";
    public static final String PROJETO_ATUALIZADO_KEY = "projeto.atualizado";
    public static final String PROJETO_DELETADO_KEY   = "projeto.deletado";

    public static final String ITEM_CRIADO_KEY     = "item.criado";
    public static final String ITEM_ATUALIZADO_KEY = "item.atualizado";
    public static final String ITEM_DELETADO_KEY   = "item.deletado";

    // comunicacao com servico-apontamento
    public static final String QUEUE_APONTAMENTO        = "apontamento.queue";
    public static final String APONTAMENTO_AVALIADO_KEY = "apontamento.avaliado";
    public static final String QUEUE_APONTAMENTO_CRIADO        = "apontamento.criado.queue";
    public static final String APONTAMENTO_CRIADO_KEY          = "apontamento.criado";




    public static final String QUEUE_PROJETO_ITENS_QUERY = "projeto.itens.query.queue";

    public static final String PROJETO_QUERY_KEY = "projeto.query.request";

    @Bean
    public Binding projetoItensQueryBinding(Queue projetoItensQueryQueue, TopicExchange gestaoExchange) {
        return BindingBuilder
                .bind(projetoItensQueryQueue)
                .to(gestaoExchange)
            .with(PROJETO_QUERY_KEY); 
}

    @Bean
    public Queue projetoItensQueryQueue() {
        return QueueBuilder.durable(QUEUE_PROJETO_ITENS_QUERY).build();
    }
        
    @Bean
    public Queue apontamentoCriadoQueue() {
        return QueueBuilder.durable(QUEUE_APONTAMENTO_CRIADO).build();
    }

    @Bean
    public Binding apontamentoCriadoBinding(Queue apontamentoCriadoQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(apontamentoCriadoQueue).to(gestaoExchange).with(APONTAMENTO_CRIADO_KEY);
    }

    @Bean
    public Queue apontamentoQueue() {
        return QueueBuilder.durable(QUEUE_APONTAMENTO).build();
    }

    @Bean
    public Binding apontamentoAvaliadoBinding(Queue apontamentoQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(apontamentoQueue).to(gestaoExchange).with(APONTAMENTO_AVALIADO_KEY);
    }
    //

    @Bean
    public TopicExchange gestaoExchange() {
        return new TopicExchange(GESTAO_EXCHANGE, true, false);
    }

    @Bean public Queue usuarioQueue() { return QueueBuilder.durable(QUEUE_USUARIO).build(); }
    @Bean public Queue projetoQueue() { return QueueBuilder.durable(QUEUE_PROJETO).build(); }
    @Bean public Queue itemQueue()    { return QueueBuilder.durable(QUEUE_ITEM).build(); }

    // Usuario bindings
    @Bean
    public Binding usuarioCriadoBinding(Queue usuarioQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(usuarioQueue).to(gestaoExchange).with(USUARIO_CRIADO_KEY);
    }
    @Bean
    public Binding usuarioAtualizadoBinding(Queue usuarioQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(usuarioQueue).to(gestaoExchange).with(USUARIO_ATUALIZADO_KEY);
    }
    @Bean
    public Binding usuarioDeletadoBinding(Queue usuarioQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(usuarioQueue).to(gestaoExchange).with(USUARIO_DELETADO_KEY);
    }

    // Projeto bindings
    @Bean
    public Binding projetoCriadoBinding(Queue projetoQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(projetoQueue).to(gestaoExchange).with(PROJETO_CRIADO_KEY);
    }
    @Bean
    public Binding projetoAtualizadoBinding(Queue projetoQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(projetoQueue).to(gestaoExchange).with(PROJETO_ATUALIZADO_KEY);
    }
    @Bean
    public Binding projetoDeletadoBinding(Queue projetoQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(projetoQueue).to(gestaoExchange).with(PROJETO_DELETADO_KEY);
    }

    // Item bindings
    @Bean
    public Binding itemCriadoBinding(Queue itemQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(itemQueue).to(gestaoExchange).with(ITEM_CRIADO_KEY);
    }
    @Bean
    public Binding itemAtualizadoBinding(Queue itemQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(itemQueue).to(gestaoExchange).with(ITEM_ATUALIZADO_KEY);
    }
    @Bean
    public Binding itemDeletadoBinding(Queue itemQueue, TopicExchange gestaoExchange) {
        return BindingBuilder.bind(itemQueue).to(gestaoExchange).with(ITEM_DELETADO_KEY);
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


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
    }