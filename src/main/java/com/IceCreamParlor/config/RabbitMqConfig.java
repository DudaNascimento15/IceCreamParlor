package com.IceCreamParlor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

     // exchanges e filas
    public static final String EXCHANGE = "sorv.ex";

    public static final String Q_CAIXA = "q.caixa";
    public static final String Q_ESTOQUE = "q.estoque";
    public static final String Q_PRODUCAO = "q.producao";
    public static final String Q_ENTREGAS = "q.entregas";
    public static final String Q_CLIENTE = "q.cliente";
    public static final String Q_RELATORIO = "q.relatorio";
    public static final String Q_WORKFLOW = "q.workflow";

    // Dead Letter Queues
    public static final String DLQ_CAIXA = "q.caixa.dlq";
    public static final String DLQ_ESTOQUE = "q.estoque.dlq";
    public static final String DLQ_PRODUCAO = "q.producao.dlq";
    public static final String DLQ_ENTREGAS = "q.entregas.dlq";

    //  exchange principal
    @Bean
    public TopicExchange exchange() {
        return ExchangeBuilder
            .topicExchange(EXCHANGE)
            .durable(true)
            .build();
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());

        // Configurações importantes para CloudAMQP
        template.setMandatory(true);
        template.setChannelTransacted(false);

        return template;
    }

    @Bean
    public Queue queueCaixa() {
        return QueueBuilder.durable(Q_CAIXA)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", DLQ_CAIXA)
            .withArgument("x-queue-type", "classic") // Importante para CloudAMQP
            .build();
    }

    @Bean
    public Queue dlqCaixa() {
        return QueueBuilder.durable(DLQ_CAIXA).build();
    }

    @Bean
    public Binding bindingCaixa() {
        return BindingBuilder
            .bind(queueCaixa())
            .to(exchange())
            .with("caixa.#");
    }

    @Bean
    public Queue queueEstoque() {
        return QueueBuilder.durable(Q_ESTOQUE)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", DLQ_ESTOQUE)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqEstoque() {
        return QueueBuilder.durable(DLQ_ESTOQUE).build();
    }

    @Bean
    public Binding bindingEstoque() {
        return BindingBuilder
            .bind(queueEstoque())
            .to(exchange())
            .with("estoque.#");
    }

    @Bean
    public Queue queueProducao() {
        return QueueBuilder.durable(Q_PRODUCAO)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", DLQ_PRODUCAO)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqProducao() {
        return QueueBuilder.durable(DLQ_PRODUCAO).build();
    }

    @Bean
    public Binding bindingProducao() {
        return BindingBuilder
            .bind(queueProducao())
            .to(exchange())
            .with("producao.#");
    }

    @Bean
    public Queue queueEntregas() {
        return QueueBuilder.durable(Q_ENTREGAS)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", DLQ_ENTREGAS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqEntregas() {
        return QueueBuilder.durable(DLQ_ENTREGAS).build();
    }

    @Bean
    public Binding bindingEntregas() {
        return BindingBuilder
            .bind(queueEntregas())
            .to(exchange())
            .with("entregas.#");
    }

    @Bean
    public Queue queueCliente() {
        return QueueBuilder.durable(Q_CLIENTE)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Binding bindingCliente() {
        return BindingBuilder
            .bind(queueCliente())
            .to(exchange())
            .with("cliente.#");
    }

    @Bean
    public Queue queueRelatorio() {
        return QueueBuilder.durable(Q_RELATORIO)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Binding bindingRelatorio() {
        return BindingBuilder
            .bind(queueRelatorio())
            .to(exchange())
            .with("#"); // Escuta TODOS os eventos
    }

    @Bean
    public Queue queueWorkflow() {
        return QueueBuilder.durable(Q_WORKFLOW)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Binding bindingWorkflowPagamento() {
        return BindingBuilder
            .bind(queueWorkflow())
            .to(exchange())
            .with("caixa.pagamento.aprovado");
    }

    @Bean
    public Binding bindingWorkflowEstoque() {
        return BindingBuilder
            .bind(queueWorkflow())
            .to(exchange())
            .with("estoque.reserva.confirmada");
    }
}