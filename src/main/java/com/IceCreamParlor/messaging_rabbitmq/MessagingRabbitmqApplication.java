package com.IceCreamParlor.messaging_rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
@EnableRetry
public class MessagingRabbitmqApplication {
    private static RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "sorv.ex";

    @Autowired
    public MessagingRabbitmqApplication(RabbitTemplate rabbitTemplate) {
        MessagingRabbitmqApplication.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(MessagingRabbitmqApplication.class, args);
    }

    public static <T> void publish(String routingKey, T payload, String correlationId, String usuario) {
        var env = new Envelope<>(UUID.randomUUID().toString(), correlationId, usuario, Instant.now(), payload);
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, env, m -> {
            m.getMessageProperties().setCorrelationId(correlationId);
            return m;
        });
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue caixaQueue() {
        return QueueBuilder.durable("q.caixa")
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "q.caixa.dlq")
                .build();
    }

    @Bean
    public Queue caixaDlq() {
        return QueueBuilder.durable("q.caixa.dlq").build();
    }

    @Bean
    public Binding caixaBinding(TopicExchange exchange) {
        return BindingBuilder.bind(caixaQueue()).to(exchange).with("caixa.#");
    }

    @Bean
    public Queue entregasQueue() {
        return QueueBuilder.durable("q.entregas")
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "q.entregas.dlq")
                .build();
    }

    @Bean
    public Queue entregasDlq() {
        return QueueBuilder.durable("q.entregas.dlq").build();
    }

    @Bean
    public Binding entregasBinding(TopicExchange exchange) {
        return BindingBuilder.bind(entregasQueue()).to(exchange).with("entregas.#");
    }

    @Bean
    public Queue producaoQueue() {
        return QueueBuilder.durable("q.producao")
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "q.producao.dlq")
                .build();
    }

    @Bean
    public Queue producaoDlq() {
        return QueueBuilder.durable("q.producao.dlq").build();
    }

    @Bean
    public Binding producaoBinding(TopicExchange exchange) {
        return BindingBuilder.bind(producaoQueue()).to(exchange).with("pedidos.#");
    }

    @Bean
    public Queue relatorioQueue() {
        return QueueBuilder.durable("q.relatorio")
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "q.relatorio.dlq")
                .build();
    }

    @Bean
    public Queue relatorioDlq() {
        return QueueBuilder.durable("q.relatorio.dlq").build();
    }

    @Bean
    public Binding relatorioBinding(TopicExchange exchange) {
        return BindingBuilder.bind(relatorioQueue()).to(exchange).with("relatorio.#");
    }

    @Bean
    public Queue estoqueQueue() {
        return QueueBuilder.durable("q.estoque")
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "q.estoque.dlq")
                .build();
    }

    @Bean
    public Queue estoqueDlq() {
        return QueueBuilder.durable("q.estoque.dlq").build();
    }

    @Bean
    public Binding estoqueBinding(TopicExchange exchange) {
        return BindingBuilder.bind(estoqueQueue()).to(exchange).with("estoque.#");
    }
}